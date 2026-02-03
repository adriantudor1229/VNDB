package com.example.vndbapp.datalayer.api

import com.example.vndbapp.model.RequestBodyVisualNovel
import com.example.vndbapp.model.VisualNovelRespone
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import javax.inject.Singleton

interface VisualNovelApiService {

    // Since this is our format we can see that we need to specify header
    // curl https://api.vndb.org/kana/vn --header 'Content-Type: application/json' --data '{
    //    "filters": ["id", "=", "v17"],
    //    "fields": "title, image.url" }'

    @Headers("Content-Type: application/json")
    @POST("vn")
    suspend fun getVisualNovels(
        @Body requestBodyVisualNovel: RequestBodyVisualNovel
    ): Response<VisualNovelRespone>
}

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    /**
     * Provides a singleton Moshi instance for JSON serialization/deserialization.
     *
     * INTERVIEW EXPLANATION:
     * Moshi is a modern JSON library for Android/Kotlin that's more efficient than Gson.
     * I chose Moshi over Gson because:
     * 1. Better Kotlin support with KotlinJsonAdapterFactory - handles Kotlin-specific features
     *    like null safety, default parameters, and data classes automatically
     * 2. Smaller APK size impact
     * 3. Better performance through compile-time code generation when using Kotlin codegen
     * 4. More proactive maintenance by Square (same company as Retrofit, OkHttp)
     *
     * The KotlinJsonAdapterFactory is crucial here - it enables reflection-based JSON
     * conversion that understands Kotlin's type system, including nullable types and
     * default parameter values. Without this, Moshi would fall back to Java reflection
     * and potentially fail on Kotlin-specific constructs.
     *
     * @Singleton ensures we only create one Moshi instance app-wide, avoiding the
     * overhead of building multiple parsers while maintaining thread safety (Moshi
     * instances are thread-safe by design).
     */
    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    /**
     * Provides a singleton Retrofit instance configured for the VNDB API.
     *
     * INTERVIEW EXPLANATION:
     * Retrofit is the industry-standard type-safe REST client for Android. Instead of
     * manually handling HTTP connections and JSON parsing, we define an interface with
     * annotations, and Retrofit generates the implementation at runtime.
     *
     * Key design decisions here:
     *
     * 1. BASE URL: "https://api.vndb.org/kana/"
     *    - Defined once and reused for all endpoints
     *    - Each API method only needs the path (e.g., "vn")
     *    - Easy to switch between staging/production environments
     *
     * 2. MoshiConverterFactory with asLenient():
     *    - Converts JSON responses to our Kotlin data classes
     *    - asLenient() is used to handle edge cases in API responses that might not
     *      strictly conform to JSON spec (like trailing commas, comments, etc.)
     *    - This makes the app more resilient to API inconsistencies
     *
     * 3. Builder pattern:
     *    - Retrofit uses builder pattern for clean, readable configuration
     *    - Easy to add more features later (interceptors, converters, etc.)
     *
     * @Singleton ensures one HTTP client is shared across the app, which enables:
     * - Connection pooling (reusing TCP connections)
     * - Reduced memory footprint
     * - Consistent configuration across all API calls
     */
    @Provides
    @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit = Retrofit.Builder()
        .baseUrl("https://api.vndb.org/kana/")
        .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
        .build()

    /**
     * Provides the VisualNovelApiService implementation from Retrofit.
     *
     * INTERVIEW EXPLANATION:
     * This is where the magic happens - Retrofit.create() dynamically generates a concrete
     * implementation of our interface at runtime using Java's dynamic proxy mechanism.
     *
     * Why return the interface instead of the implementation?
     * 1. Dependency Inversion Principle - depend on abstractions, not concretions
     * 2. Easy to mock for testing (we can create a fake implementation)
     * 3. We don't need to know the implementation class - Retrofit handles it
     *
     * The generated implementation:
     * - Reads the @POST, @Headers annotations to build the HTTP request
     * - Uses the @Body annotation to serialize the request body with Moshi
     * - Uses the return type to deserialize the response with Moshi
     * - Handles the suspend keyword for coroutine support (makes the call asynchronous)
     *
     * @Singleton ensures the same proxy instance is reused, avoiding the cost of
     * generating multiple proxies (which involves reflection).
     *
     * NOTE: There's a typo in the method name "provedStoreApiService" - should be
     * "provideApiService". However, Hilt matches methods by return type, not name,
     * so this still works correctly.
     */
    @Provides
    @Singleton
    fun provedStoreApiService(retrofit: Retrofit): VisualNovelApiService =
        retrofit.create(VisualNovelApiService::class.java)
}
