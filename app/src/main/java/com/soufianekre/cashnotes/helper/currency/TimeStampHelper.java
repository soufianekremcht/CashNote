package com.soufianekre.cashnotes.helper.currency;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.sql.Timestamp;

/**
 * A utility class to deal with {@link Timestamp} operations in a centralized way.
 */
public final class TimeStampHelper {

    public static final Timestamp EPOCH_ZERO_TIMESTAMP = new Timestamp(0);

    /**
     * Should be not instantiated.
     */
    private TimeStampHelper() {}

    /**
     * We are using Joda Time classes because they are thread-safe.
     */
    private static final DateTimeZone UTC_TIME_ZONE = DateTimeZone.forID("UTC");
    private static final DateTimeFormatter UTC_DATE_FORMAT =
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter UTC_DATE_WITH_MILLISECONDS_FORMAT =
            DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * Get a {@link String} representing the {@link Timestamp}
     * in UTC time zone and 'yyyy-MM-dd HH:mm:ss.SSS' format.
     *
     * @param timestamp The {@link Timestamp} to format.
     * @return The formatted {@link String}.
     */
    public static String getUtcStringFromTimestamp(Timestamp timestamp) {
        return UTC_DATE_WITH_MILLISECONDS_FORMAT.withZone(UTC_TIME_ZONE).print(timestamp.getTime());
    }

    /**
     * @return A {@link Timestamp} with time in milliseconds equals to zero.
     */
    public static Timestamp getTimestampFromEpochZero() {
        return EPOCH_ZERO_TIMESTAMP;
    }

    /**
     * Get the {@link Timestamp} with the value of given UTC {@link String}.
     * The {@link String} should be a representation in UTC time zone with the following format
     * 'yyyy-MM-dd HH:mm:ss.SSS' OR 'yyyy-MM-dd HH:mm:ss' otherwise an IllegalArgumentException
     * will be throw.
     *
     * @param utcString A {@link String} in UTC.
     * @return A {@link Timestamp} for given utcString.
     */
    public static Timestamp getTimestampFromUtcString(String utcString) {
        DateTime dateTime;
        try {

            dateTime = UTC_DATE_WITH_MILLISECONDS_FORMAT.withZone(UTC_TIME_ZONE).parseDateTime(utcString);
            return new Timestamp(dateTime.getMillis());

        } catch (IllegalArgumentException firstException) {
            try {
                // In case of parsing of string without milliseconds.
                dateTime = UTC_DATE_FORMAT.withZone(UTC_TIME_ZONE).parseDateTime(utcString);
                return new Timestamp(dateTime.getMillis());

            } catch (IllegalArgumentException secondException) {
                // If we are here:
                // - The utcString has an invalid format OR
                // - We are missing some relevant pattern.
                throw new IllegalArgumentException("Unknown utcString = '" + utcString + "'.", secondException);
            }
        }
    }

    /**
     * @return A {@link Timestamp} initialized with the system current time.
     */
    public static Timestamp getTimestampFromNow() {
        return new Timestamp(System.currentTimeMillis());
    }
}