package fr.inra.urgi.faidare.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utilities for sites
 * @author JB Nizet
 */
public class Sites {
    public static String siteIdToLocationId(String siteId) {
        return Base64.getUrlEncoder().encodeToString(("urn:INRAE-URGI/location/" + siteId).getBytes(StandardCharsets.US_ASCII));
    }
}
