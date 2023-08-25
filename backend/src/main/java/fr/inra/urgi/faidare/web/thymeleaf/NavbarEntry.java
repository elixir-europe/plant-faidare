package fr.inra.urgi.faidare.web.thymeleaf;

import java.util.Collections;
import java.util.List;

/**
 * A navbar link
 * @author JB Nizet
 */
public final class NavbarEntry {
    private final String labelKey;
    private final String url;
    private final List<NavbarEntry> subMenu;

    private NavbarEntry(String labelKey, String url, List<NavbarEntry> subMenu) {
        this.labelKey = labelKey;
        this.url = url;
        this.subMenu = subMenu;
    }

    public static NavbarEntry link(String labelKey, String url) {
        return new NavbarEntry(labelKey, url, Collections.emptyList());
    }

    public static NavbarEntry menu(String labelKey, List<NavbarEntry> subMenu) {
        return new NavbarEntry(labelKey, null, subMenu);
    }

    public String getLabelKey() {
        return labelKey;
    }

    public String getUrl() {
        return url;
    }

    public List<NavbarEntry> getSubMenu() {
        return subMenu;
    }
}
