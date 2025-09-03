package com.denverhogan.themeparks.repository

import com.denverhogan.themeparks.model.DestinationListItem
import com.denverhogan.themeparks.model.GetAllDestinationsResult
import kotlinx.coroutines.delay

class InMemoryDestinationsListRepository() : DestinationsListRepository {
    override suspend fun getAllDestinations(): GetAllDestinationsResult {
        delay(3000)
        return GetAllDestinationsResult.Success(
            destinations = listOf(
                DestinationListItem(name = "Universal Studios", location = "Orlando, FL"),
                DestinationListItem(name = "King's Island", location = "Mason, OH"),
                DestinationListItem(name = "Holiday World", location = "Santa Claus, IN"),
                DestinationListItem(name = "Walt Disney World", location = "Orlando, FL"),
                DestinationListItem(name = "Carowinds", location = "Charlotte, NC"),
                DestinationListItem(name = "Kentucky Kingdom", location = "Louisville, KY"),
                DestinationListItem(name = "Disneyland", location = "Anaheim, CA")
            )
        )
    }
}