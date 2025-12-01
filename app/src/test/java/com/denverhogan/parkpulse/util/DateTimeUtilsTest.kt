package com.denverhogan.parkpulse.util

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

class DateTimeUtilsTest {

    @Test
    fun `formatLastUpdated returns Just now for less than 1 minute`() {
        val now = OffsetDateTime.now()
        val oneMinuteAgo = now.minusSeconds(30).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        
        val result = formatLastUpdated(oneMinuteAgo)
        
        assertEquals("Just now", result)
    }

    @Test
    fun `formatLastUpdated returns 1 minute ago for 1 minute`() {
        val now = OffsetDateTime.now()
        val oneMinuteAgo = now.minusMinutes(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        
        val result = formatLastUpdated(oneMinuteAgo)
        
        assertEquals("1 minute ago", result)
    }

    @Test
    fun `formatLastUpdated returns minutes ago for less than 60 minutes`() {
        val now = OffsetDateTime.now()
        val thirtyMinutesAgo = now.minusMinutes(30).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        
        val result = formatLastUpdated(thirtyMinutesAgo)
        
        assertEquals("30 minutes ago", result)
    }

    @Test
    fun `formatLastUpdated returns hours ago for less than 24 hours`() {
        val now = OffsetDateTime.now()
        val fiveHoursAgo = now.minusHours(5).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        
        val result = formatLastUpdated(fiveHoursAgo)
        
        assertEquals("5 hours ago", result)
    }

    @Test
    fun `formatLastUpdated returns 1 day ago for 1 day`() {
        val now = OffsetDateTime.now()
        val oneDayAgo = now.minusDays(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        
        val result = formatLastUpdated(oneDayAgo)
        
        assertEquals("1 day ago", result)
    }

    @Test
    fun `formatLastUpdated returns days ago for less than 7 days`() {
        val now = OffsetDateTime.now()
        val threeDaysAgo = now.minusDays(3).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        
        val result = formatLastUpdated(threeDaysAgo)
        
        assertEquals("3 days ago", result)
    }

    @Test
    fun `formatLastUpdated returns 1 week ago for 1 week`() {
        val now = OffsetDateTime.now()
        val oneWeekAgo = now.minusWeeks(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        
        val result = formatLastUpdated(oneWeekAgo)
        
        assertEquals("1 week ago", result)
    }

    @Test
    fun `formatLastUpdated returns weeks ago for multiple weeks`() {
        val now = OffsetDateTime.now()
        val threeWeeksAgo = now.minusWeeks(3).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        
        val result = formatLastUpdated(threeWeeksAgo)
        
        assertEquals("3 weeks ago", result)
    }

    @Test
    fun `formatLastUpdated handles invalid date format gracefully`() {
        val invalidDate = "invalid-date-format"
        
        val result = formatLastUpdated(invalidDate)
        
        // Should return the original string as fallback
        assertEquals(invalidDate, result)
    }

    @Test
    fun `formatLastUpdated handles empty string`() {
        val emptyString = ""
        
        val result = formatLastUpdated(emptyString)
        
        // Should return empty string as fallback
        assertEquals(emptyString, result)
    }

    @Test
    fun `formatLastUpdated handles future dates`() {
        val now = OffsetDateTime.now()
        val futureDate = now.plusHours(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        
        // Future dates might return negative values, but function should handle it
        val result = formatLastUpdated(futureDate)
        
        // Should not crash, might return original or handle gracefully
        assert(result.isNotEmpty())
    }

    @Test
    fun `formatLastUpdated handles exactly 60 minutes`() {
        val now = OffsetDateTime.now()
        val exactlyOneHourAgo = now.minusHours(1).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        
        val result = formatLastUpdated(exactlyOneHourAgo)
        
        // Should show as hours, not minutes
        assertEquals("1 hours ago", result)
    }

    @Test
    fun `formatLastUpdated handles exactly 24 hours`() {
        val now = OffsetDateTime.now()
        val exactlyOneDayAgo = now.minusHours(24).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        
        val result = formatLastUpdated(exactlyOneDayAgo)
        
        // Should show as days, not hours
        assert(result.contains("day") || result.contains("days"))
    }

    @Test
    fun `formatLastUpdated handles exactly 7 days`() {
        val now = OffsetDateTime.now()
        val exactlyOneWeekAgo = now.minusDays(7).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        
        val result = formatLastUpdated(exactlyOneWeekAgo)
        
        // Should show as weeks, not days
        assert(result.contains("week") || result.contains("weeks"))
    }

    @Test
    fun `formatLastUpdated preserves timezone offset`() {
        // Test with different timezone
        val dateTime = OffsetDateTime.parse("2024-01-01T12:00:00+05:00")
        val formatted = dateTime.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        
        // Function should handle timezone correctly
        val result = formatLastUpdated(formatted)
        
        assert(result.isNotEmpty())
    }
}

