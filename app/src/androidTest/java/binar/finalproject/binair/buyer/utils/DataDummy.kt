package binar.finalproject.binair.buyer.utils

import binar.finalproject.binair.buyer.data.model.DataWishList

object DataDummy {
    fun sampleWishlist() = DataWishList(
        idWishlist = 1,
        id = "31588def-1557-4a4c-8083-a2a3010790f5",
        from = "Surabaya",
        to = "Makassar",
        dateStart = "2023-01-12",
        dateEnd = null,
        departureTime = "12:00",
        arrivalTime = "13:00",
        airportFrom = "Juanda International Airport",
        airportTo = "Sultan Hasanuddin International Airport",
        adultPrice = 1420000,
        childPrice = 1200000,
        type = "roundtrip",
        user = "1"
    )
}