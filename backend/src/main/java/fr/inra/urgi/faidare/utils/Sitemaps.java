package fr.inra.urgi.faidare.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Spliterators;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * A generator of site maps.
 * @author JB Nizet
 */
@Component
public class Sitemaps {
    public static final int BUCKET_COUNT = 11;

    public static <T> void generateSitemap(String sitemapPath,
                                           OutputStream out,
                                           Iterator<T> entryIterator,
                                           Predicate<T> entryPredicate,
                                           Function<T, String> entryToPath) {
        SanityChecker sanityChecker = new SanityChecker(sitemapPath);

        Writer writer = new BufferedWriter(new OutputStreamWriter(out, StandardCharsets.UTF_8));
        Stream<T> entries =
            StreamSupport.stream(Spliterators.spliteratorUnknownSize(entryIterator, 0), false);
        entries.filter(entryPredicate)
               .map(entryToPath)
               .map(entryPath -> Sitemaps.generateSitemapUrl(entryPath) + '\n')
               .forEach(entry -> {
                   try {
                       writer.write(entry);
                       sanityChecker.addEntry(entry);
                   }
                   catch (IOException e) {
                       throw new UncheckedIOException(e);
                   }
               });

        try {
            writer.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

        sanityChecker.check();
    }

    public static String generateSitemapUrl(String path) {
        return ServletUriComponentsBuilder
            .fromCurrentContextPath()
            .path(path)
            .toUriString();
    }

    private static class SanityChecker {
        private static final Logger LOGGER = LoggerFactory.getLogger(SanityChecker.class);

        private static final int MAX_ENTRY_COUNT = 50_000;
        private static final int MAX_BYTE_COUNT = 50 * 1024 * 1024;

        private static final int DANGER_ENTRY_COUNT = 40_000;
        private static final int DANGER_BYTE_COUNT = 40 * 1024 * 1024;

        private final String sitemapPath;
        private int entryCount = 0;
        private int byteCount = 0;

        public SanityChecker(String sitemapPath) {
            this.sitemapPath = sitemapPath;
        }

        public void addEntry(String entry) {
            entryCount++;
            byteCount += entry.length();
        }

        public void check() {
            if (entryCount > MAX_ENTRY_COUNT) {
                LOGGER.error("The generated sitemap at path "
                                 + sitemapPath +
                                 " has more than "
                                 + MAX_ENTRY_COUNT +
                                 " entries and will thus be rejected by search engines. Increase Sitemaps.BUCKET_COUNT for a better distribution of sitemap entries.");
            } else if (entryCount > DANGER_ENTRY_COUNT) {
                LOGGER.warn("The generated sitemap at path "
                                + sitemapPath
                                + " has more than "
                                + DANGER_ENTRY_COUNT
                                + " entries and is thus approaching the max of "
                                + MAX_ENTRY_COUNT
                                + ". Increase Sitemaps.BUCKET_COUNT for a better distribution of sitemap entries.");
            }

            if (byteCount > MAX_BYTE_COUNT) {
                LOGGER.error("The generated sitemap at path "
                                 + sitemapPath
                                 + " has more than "
                                 + MAX_BYTE_COUNT
                                 + " bytes and will thus be rejected by search engines. Increase Sitemaps.BUCKET_COUNT for a better distribution of sitemap entries.");
            } else if (entryCount > DANGER_ENTRY_COUNT) {
                LOGGER.warn("The generated sitemap at path "
                                + sitemapPath
                                + " has more than "
                                + DANGER_BYTE_COUNT
                                + " bytes and is thus approaching the max of "
                                + MAX_BYTE_COUNT
                                + ". Increase Sitemaps.BUCKET_COUNT for a better distribution of sitemap entries.");
            }
        }
    }
}
