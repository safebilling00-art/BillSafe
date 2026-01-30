package com.billsafe.di

import android.content.Context
import androidx.room.Room
import com.billsafe.data.api.BillSafeApi
import com.billsafe.data.dao.BillDao
import com.billsafe.data.dao.SubscriptionDao
import com.billsafe.data.dao.UserDao
import com.billsafe.data.database.AppDatabase
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
object AppModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "billsafe_db"
        ).build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("http://10.0.2.2:5000/") // Android emulator localhost
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideBillSafeApi(retrofit: Retrofit): BillSafeApi {
        return retrofit.create(BillSafeApi::class.java)
    }

    @Singleton
    @Provides
    fun provideBillDao(database: AppDatabase): BillDao = database.billDao()

    @Singleton
    @Provides
    fun provideSubscriptionDao(database: AppDatabase): SubscriptionDao = database.subscriptionDao()

    @Singleton
    @Provides
    fun provideUserDao(database: AppDatabase): UserDao = database.userDao()
}
