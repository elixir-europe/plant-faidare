package fr.inra.urgi.faidare.web.thymeleaf;

import java.util.Collections;
import java.util.List;

/**
 * A navbar link
 * @author JB Nizet
 */
public final class NavbarEntry {
    private final String label;
    private final String url;
    private final List<NavbarEntry> subMenu;

    private NavbarEntry(String label, String url, List<NavbarEntry> subMenu) {
        this.label = label;
        this.url = url;
        this.subMenu = subMenu;
    }

    public static NavbarEntry link(String label, String url) {
        return new NavbarEntry(label, url, Collections.emptyList());
    }

    public static NavbarEntry menu(String label, List<NavbarEntry> subMenu) {
        return new NavbarEntry(label, null, subMenu);
    }

    public String getLabel() {
        return label;
    }

    public String getUrl() {
        return url;
    }

    public List<NavbarEntry> getSubMenu() {
        return subMenu;
    }
}
