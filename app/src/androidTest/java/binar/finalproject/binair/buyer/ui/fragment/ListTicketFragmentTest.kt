package binar.finalproject.binair.buyer.ui.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import binar.finalproject.binair.buyer.JsonConverter
import binar.finalproject.binair.buyer.R
import binar.finalproject.binair.buyer.data.EspressoIdlingResource
import binar.finalproject.binair.buyer.di.AppModule
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.testing.HiltAndroidTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@HiltAndroidTest
@AndroidEntryPoint
class ListTicketFragmentTest{
    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        AppModule.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun testFragment(){
        launchFragmentInContainer(themeResId = R.style.Theme_BinairBuyer) {
            ListTicketFragment()
        }
        onView(withId(R.id.rvListTicket)).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchTicketFound() {
        launchFragmentInContainer<ListTicketFragment>()

        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JsonConverter.readStringFromFile("search_ticket_found.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.rvListTicket)).check(matches(isDisplayed()))
        onView(withText("Surabaya")).check(matches(isDisplayed()))
    }

    @Test
    fun testSearchTicketNotFound() {
        launchFragmentInContainer { ListTicketFragment() }

        val mockResponse = MockResponse()
            .setResponseCode(404)
            .setBody(JsonConverter.readStringFromFile("search_ticket_not_found.json"))
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.tvTicketNotFound)).check(matches(isDisplayed()))
        onView(withText("Tiket tidak ditemukan")).check(matches(isDisplayed()))
    }
}