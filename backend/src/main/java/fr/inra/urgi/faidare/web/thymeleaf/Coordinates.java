package fr.inra.urgi.faidare.web.thymeleaf;

import java.text.DecimalFormat;
import java.util.Locale;

/**
 * The actual object offering Coordinates helper methods to thymeleaf
 * @author JB Nizet
 */
public class Coordinates {
    private final Locale locale;

    public Coordinates(Locale locale) {
        this.locale = locale;
    }

    public String format(Double value) {
        if (value == null) {
            return "";
        }
        return DecimalFormat.getInstance(locale).format(value);
    }

    public String formatLatitude(Double value) {
        if (value == null) {
            return "";
        }
        return this.format(value) + " — " + this.toLatitudeDegrees(value);
    }

    public String formatLongitude(Double value) {
        if (value == null) {
            return "";
        }
        return this.format(value) + " — " + this.toLongitudeDegrees(value);
    }

    public String toLatitudeDegrees(Double latitude) {
        if (latitude == null) {
            return "";
        }

        return toDegrees(latitude) + " " + ((latitude < 0) ? "S" : "N");
    }

    public String toLongitudeDegrees(Double longitude) {
        if (longitude == null) {
            return "";
        }

        return toDegrees(longitude) + " " + ((longitude < 0) ? "W" : "E");
    }

    private String toDegrees(double value) {
        double absoluteDegrees = Math.abs(value);
        int fullDegrees = (int) absoluteDegrees;
        double remainingMinutes = (absoluteDegrees - fullDegrees) * 60;
        int minutes = (int) remainingMinutes;
        double remainingSeconds = (remainingMinutes - minutes) * 60;
        int seconds = (int) remainingSeconds;
        if (seconds == 60) {
            minutes += 1;
            seconds = 0;
        }
        if (minutes == 60) {
            fullDegrees += 1;
            minutes = 0;
        }
        return fullDegrees + "°" + minutes + "'" + seconds;
    }
}
