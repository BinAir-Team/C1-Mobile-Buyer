package binar.finalproject.binair.buyer.di

import android.content.Context
import androidx.room.Room
import binar.finalproject.binair.buyer.BuildConfig
import binar.finalproject.binair.buyer.data.UserRepository
import binar.finalproject.binair.buyer.data.local.WishListDatabase
import binar.finalproject.binair.buyer.data.local.WishlistDAO
import binar.finalproject.binair.buyer.data.remote.APIService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun getFlightServie(): APIService {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://binair-backend-production.up.railway.app/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
        return retrofit.create(APIService::class.java)
    }

    @Singleton
    @Provides
    fun userRepo() = UserRepository(getFlightServie())

    @Provides
    fun provideWishlistDAO(db: WishListDatabase): WishlistDAO = db.wishlistDAO()

    @Singleton
    @Provides
    fun provideDB(@ApplicationContext context: Context): WishListDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            WishListDatabase::class.java,
            "wishlist.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}