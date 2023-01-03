package binar.finalproject.binair.buyer.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import binar.finalproject.binair.buyer.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class WishlistDaoTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var db : WishListDatabase
    private lateinit var dao : WishlistDAO
    private val sampleWishlist = DataDummy.sampleWishlist()

    @Before
    fun initDB(){
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WishListDatabase::class.java
        ).allowMainThreadQueries().build()
        dao = db.wishlistDAO()
    }

    @After
    fun closeDB() = db.close()

    @Test
    fun insertWishlistSuccess() = runBlockingTest {
        dao.insertWishList(sampleWishlist)
        val actualWishlist = dao.getWishList("1")
        Assert.assertEquals(sampleWishlist,actualWishlist[0])
    }

    @Test
    fun deleteWishlistSuccess() = runBlockingTest {
        dao.insertWishList(sampleWishlist)
        dao.delWishList(sampleWishlist)
        val actualWishlist = dao.getWishList("1")
        Assert.assertEquals(0,actualWishlist.size)
    }
}