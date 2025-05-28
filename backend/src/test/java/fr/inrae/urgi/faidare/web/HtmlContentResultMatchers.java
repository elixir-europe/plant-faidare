package fr.inrae.urgi.faidare.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Set;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.test.web.servlet.ResultMatcher;

/**
 * Result matchers useful to perform various checks on HTML content
 * @author JB Nizet
 */
public class HtmlContentResultMatchers {
    public ResultMatcher endsCorrectly() {
        return result -> {
            assertThat(result.getResponse().getContentAsString().trim()).endsWith("</html>");
        };
    }

    public ResultMatcher hasTitle(String title) {
        return result -> {
            Document document = Jsoup.parse(result.getResponse().getContentAsString());
            assertThat(document.title().trim()).isEqualTo(title);
        };
    }

    public ResultMatcher containsH2s(String... h2Texts) {
        return result -> {
            Document document = Jsoup.parse(result.getResponse().getContentAsString());
            Set<String> actualH2Texts = document.getElementsByTag("h2")
                                     .stream()
                                     .map(element -> element.text().trim())
                                     .collect(
                                         Collectors.toSet());
            for (String h2Text : h2Texts) {
                assertThat(actualH2Texts).contains(h2Text);
            }
        };
    }
}
