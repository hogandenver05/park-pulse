package com.denverhogan.parkpulse.util

import java.time.OffsetDateTime
import java.time.temporal.ChronoUnit

fun formatLastUpdated(lastUpdated: String): String {
    return try {
        val updatedTime = OffsetDateTime.parse(lastUpdated)
        val now = OffsetDateTime.now(updatedTime.offset)

        val minutes = ChronoUnit.MINUTES.between(updatedTime, now)
        if (minutes < 1) return "Just now"
        if (minutes < 2) return "1 minute ago"
        if (minutes < 60) return "$minutes minutes ago"

        val hours = ChronoUnit.HOURS.between(updatedTime, now)
        if (hours < 24) return "$hours hours ago"

        val days = ChronoUnit.DAYS.between(updatedTime, now)
        if (days == 1L) return "1 day ago"
        if (days < 7) return "$days days ago"

        val weeks = ChronoUnit.WEEKS.between(updatedTime, now)
        if (weeks == 1L) return "1 week ago"

        "$weeks weeks ago"
    } catch (_: Exception) {
        // Fallback for any parsing errors
        lastUpdated
    }
}
