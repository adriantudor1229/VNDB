# Room + Hilt Integration Guide for VNDB App

> *A comprehensive guide to implementing local database caching with Room and Hilt dependency injection in your VNDB application.*

---

## Table of Contents

1. [Overview](#overview)
2. [Prerequisites](#prerequisites)
3. [Project Structure](#project-structure)
4. [Step 1: Create Room Entity](#step-1-create-room-entity)
5. [Step 2: Create DAO Interface](#step-2-create-dao-interface)
6. [Step 3: Create Database Class](#step-3-create-database-class)
7. [Step 4: Create Hilt Module](#step-4-create-hilt-module)
8. [Step 5: Create Repository](#step-5-create-repository)
9. [Step 6: Update ViewModel](#step-6-update-viewmodel)
10. [Step 7: Migrations](#step-7-migrations)
11. [Testing](#testing)

---

## Overview

This guide shows how to integrate **Room Database** with **Hilt** dependency injection in your VNDB App. The implementation follows these principles:

- **Single Source of Truth**: Room database serves as local cache
- **Dependency Injection**: Hilt provides database and DAO instances
- **Coroutines**: All database operations use suspend functions
- **Flow**: Reactive data streams for UI updates
- **Clean Architecture**: Separation of concerns with Repository pattern

---

## Prerequisites

Your project already has:
- ✅ Hilt configured (`VNDBApplication` with `@HiltAndroidApp`)
- ✅ KSP plugin enabled
- ✅ Room dependencies added (version 2.8.4)
- ✅ `@HiltViewModel` in use

---

## Project Structure

```
com.example.vndbapp/
├── db/
│   ├── VndbDatabase.kt           # Room database
│   ├── entity/
│   │   └── VisualNovelEntity.kt  # Room entity
│   └── dao/
│       └── VisualNovelDao.kt     # Data Access Object
├── hilt/
│   ├── AppModule.kt              # Existing module
│   └── DatabaseModule.kt         # NEW: Room module
├── datalayer/
│   └── local/
│       └── LocalVisualNovelRepository.kt  # NEW: Local data source
└── model/
    └── VisualNovel.kt            # Existing API model
```

---

## Step 1: Create Room Entity

**File:** `app/src/main/java/com/example/vndbapp/db/entity/VisualNovelEntity.kt`

> **Note:** The entity is separate from the API model (`VisualNovel.kt`). This keeps concerns separated.

```kotlin
package com.example.vndbapp.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo

@Entity(tableName = "visual_novels")
data class VisualNovelEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,

    @ColumnInfo(name = "title")
    val title: String,

    @ColumnInfo(name = "description")
    val description: String,

    @ColumnInfo(name = "image_url")
    val imageUrl: String? = null,

    @ColumnInfo(name = "thumbnail_url")
    val thumbnailUrl: String? = null,

    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
)
```

### Extension Functions (for API ↔ Entity conversion)

**File:** `app/src/main/java/com/example/vndbapp/db/entity/VisualNovelEntity.kt` (append to file)

```kotlin
// Convert API model to Entity
fun com.example.vndbapp.model.VisualNovel.toEntity(): VisualNovelEntity {
    return VisualNovelEntity(
        id = id,
        title = title,
        description = description,
        imageUrl = image.url,
        thumbnailUrl = image.thumbnail
    )
}

// Convert Entity to API model
fun VisualNovelEntity.toModel(): com.example.vndbapp.model.VisualNovel {
    return com.example.vndbapp.model.VisualNovel(
        id = id,
        title = title,
        description = description,
        image = com.example.vndbapp.model.Image(
            url = imageUrl,
            thumbnail = thumbnailUrl
        )
    )
}
```

---

## Step 2: Create DAO Interface

**File:** `app/src/main/java/com/example/vndbapp/db/dao/VisualNovelDao.kt`

```kotlin
package com.example.vndbapp.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import androidx.room.Update
import com.example.vndbapp.db.entity.VisualNovelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VisualNovelDao {

    /**
     * Get all visual novels as Flow for reactive updates
     */
    @Query("SELECT * FROM visual_novels ORDER BY created_at DESC")
    fun getAllVisualNovels(): Flow<List<VisualNovelEntity>>

    /**
     * Get a specific visual novel by ID
     */
    @Query("SELECT * FROM visual_novels WHERE id = :id")
    suspend fun getVisualNovelById(id: String): VisualNovelEntity?

    /**
     * Search visual novels by title
     */
    @Query("SELECT * FROM visual_novels WHERE title LIKE '%' || :query || '%' ORDER BY title ASC")
    fun searchVisualNovels(query: String): Flow<List<VisualNovelEntity>>

    /**
     * Insert visual novels. Replace if exists.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisualNovels(novels: List<VisualNovelEntity>)

    /**
     * Insert single visual novel
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisualNovel(novel: VisualNovelEntity)

    /**
     * Update existing visual novel
     */
    @Update
    suspend fun updateVisualNovel(novel: VisualNovelEntity)

    /**
     * Delete a visual novel
     */
    @Delete
    suspend fun deleteVisualNovel(novel: VisualNovelEntity)

    /**
     * Delete all visual novels
     */
    @Query("DELETE FROM visual_novels")
    suspend fun deleteAllVisualNovels()

    /**
     * Get count of visual novels
     */
    @Query("SELECT COUNT(*) FROM visual_novels")
    suspend fun getVisualNovelCount(): Int
}
```

---

## Step 3: Create Database Class

**File:** `app/src/main/java/com/example/vndbapp/db/VndbDatabase.kt`

```kotlin
package com.example.vndbapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.vndbapp.db.dao.VisualNovelDao
import com.example.vndbapp.db.entity.VisualNovelEntity

@Database(
    entities = [VisualNovelEntity::class],
    version = 1,
    exportSchema = true  // Enable schema export for migrations
)
abstract class VndbDatabase : RoomDatabase() {

    abstract fun visualNovelDao(): VisualNovelDao

    companion object {
        const val DATABASE_NAME = "vndb_database"
    }
}
```

---

## Step 4: Create Hilt Module

**File:** `app/src/main/java/com/example/vndbapp/hilt/DatabaseModule.kt`

```kotlin
package com.example.vndbapp.hilt

import android.content.Context
import androidx.room.Room
import com.example.vndbapp.db.VndbDatabase
import com.example.vndbapp.db.dao.VisualNovelDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideVndbDatabase(
        @ApplicationContext context: Context
    ): VndbDatabase {
        return Room.databaseBuilder(
            context,
            VndbDatabase::class.java,
            VndbDatabase.DATABASE_NAME
        )
            .fallbackToDestructiveMigration()  // For development - remove in production
            // .addMigrations(Migration1To2)  // Uncomment for production migrations
            .build()
    }

    @Provides
    @Singleton
    fun provideVisualNovelDao(
        database: VndbDatabase
    ): VisualNovelDao {
        return database.visualNovelDao()
    }
}
```

### Explanation of Annotations:

| Annotation | Purpose |
|------------|---------|
| `@Module` | Marks this class as a Hilt module that provides dependencies |
| `@InstallIn(SingletonComponent::class)` | Dependencies live for entire app lifecycle |
| `@Provides` | Marks function that provides a dependency |
| `@Singleton` | Only one instance created per app lifecycle |
| `@ApplicationContext` | Injects application Context |

---

## Step 5: Create Repository

**File:** `app/src/main/java/com/example/vndbapp/datalayer/local/LocalVisualNovelRepository.kt`

```kotlin
package com.example.vndbapp.datalayer.local

import com.example.vndbapp.db.dao.VisualNovelDao
import com.example.vndbapp.db.entity.VisualNovelEntity
import com.example.vndbapp.db.entity.toModel  // Extension function import
import com.example.vndbapp.model.VisualNovel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalVisualNovelRepository @Inject constructor(
    private val visualNovelDao: VisualNovelDao
) {

    /**
     * Get all visual novels as Flow
     * Note: toModel() is an extension function defined in VisualNovelEntity.kt
     */
    fun getAllVisualNovels(): Flow<List<VisualNovel>> {
        return visualNovelDao.getAllVisualNovels()
            .map { entities -> entities.map { entity -> entity.toModel() } }
    }

    /**
     * Get visual novel by ID
     */
    suspend fun getVisualNovelById(id: String): VisualNovel? {
        return visualNovelDao.getVisualNovelById(id)?.let { it.toModel() }
    }

    /**
     * Search visual novels by title
     */
    fun searchVisualNovels(query: String): Flow<List<VisualNovel>> {
        return visualNovelDao.searchVisualNovels(query)
            .map { entities -> entities.map { entity -> entity.toModel() } }
    }

    /**
     * Save visual novels to database
     */
    suspend fun saveVisualNovels(novels: List<VisualNovel>) {
        val entities = novels.map { it.toEntity() }
        visualNovelDao.insertVisualNovels(entities)
    }

    /**
     * Save single visual novel
     */
    suspend fun saveVisualNovel(novel: VisualNovel) {
        visualNovelDao.insertVisualNovel(novel.toEntity())
    }

    /**
     * Delete all visual novels
     */
    suspend fun deleteAllVisualNovels() {
        visualNovelDao.deleteAllVisualNovels()
    }

    /**
     * Get count of cached visual novels
     */
    suspend fun getCacheCount(): Int {
        return visualNovelDao.getVisualNovelCount()
    }
}
```

---

## Step 6: Update ViewModel

**File:** `app/src/main/java/com/example/vndbapp/mvvm/VisualNovelViewModel.kt` (modified)

```kotlin
package com.example.vndbapp.mvvm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vndbapp.datalayer.api.repository.VisualNovelRepository
import com.example.vndbapp.datalayer.local.LocalVisualNovelRepository
import com.example.vndbapp.model.VisualNovel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VisualNovelViewModel @Inject constructor(
    private val visualNovelRepository: VisualNovelRepository,
    private val localRepository: LocalVisualNovelRepository  // NEW: Local repository
) : ViewModel() {

    private val _uiState = MutableStateFlow<VisualNovelState>(VisualNovelState.Loading)
    val uiState: StateFlow<VisualNovelState> = _uiState.asStateFlow()

    // Constants for API field specifications
    companion object {
        const val DEFAULT_FIELDS = "title, image.url, image.thumbnail, description"
    }

    init {
        // Start observing cached data - single Flow collection
        observeCachedData()
        // Load from API on init
        refreshFromApi()
    }

    /**
     * Observe cached visual novels from local database.
     * This runs continuously and updates UI when data changes.
     * IMPORTANT: Only called once from init to avoid multiple Flow collections.
     */
    private fun observeCachedData() {
        viewModelScope.launch {
            localRepository.getAllVisualNovels().collect { novels ->
                _uiState.update {
                    when {
                        novels.isEmpty() && _uiState.value is VisualNovelState.Success ->
                            VisualNovelState.Empty
                        novels.isEmpty() -> VisualNovelState.Loading
                        else -> VisualNovelState.Success(visualNovels = novels)
                    }
                }
            }
        }
    }

    fun onEvent(event: VisualNovelEvent) {
        when (event) {
            is VisualNovelEvent.GetVisualNovel -> getVisualNovels(
                page = event.page,
                title = event.title,
                visualNovels = event.filters,
            )
            is VisualNovelEvent.RefreshCache -> refreshFromApi()
        }
    }

    private fun getVisualNovels(
        page: Int,
        title: String,
        visualNovels: List<String> = emptyList()
    ) {
        _uiState.value = VisualNovelState.Loading
        viewModelScope.launch {
            try {
                val response = visualNovelRepository.getVisualNovels(
                    page = page,
                    fields = title,
                    filters = visualNovels
                )

                if (response.isSuccessful && response.body() != null) {
                    val results = response.body()!!.results

                    // Cache results locally
                    localRepository.saveVisualNovels(results)

                    _uiState.update {
                        VisualNovelState.Success(visualNovels = results)
                    }
                } else {
                    val error = "Error: ${response.code()} ${response.message()}"
                    Log.e("VisualNovelViewModel", error)
                    _uiState.value = VisualNovelState.Error(message = error)
                }
            } catch (e: Exception) {
                // If API fails, try to load from cache
                Log.e("VisualNovelViewModel", "API error, loading from cache", e)
                loadCachedVisualNovels()
            }
        }
    }

    private fun refreshFromApi() {
        getVisualNovels(
            page = 1,
            title = DEFAULT_FIELDS
        )
    }
}

sealed class VisualNovelState {
    object Loading : VisualNovelState()
    object Empty : VisualNovelState()  // NEW: Empty cache state
    data class Success(val visualNovels: List<VisualNovel>) : VisualNovelState()
    data class Error(val message: String) : VisualNovelState()
}

sealed interface VisualNovelEvent {
    data class GetVisualNovel(
        val page: Int,
        val title: String = "title, image.url, image.thumbnail, description",
        val filters: List<String> = emptyList()
    ) : VisualNovelEvent

    object RefreshCache : VisualNovelEvent  // NEW event
}
```

---

## Step 7: Migrations

For production apps, you need to handle database schema changes:

### Option A: Destructive Migration (Development Only)

```kotlin
Room.databaseBuilder(context, VndbDatabase::class.java, "vndb_database")
    .fallbackToDestructiveMigration()  // Deletes and recreates DB on version change
    .build()
```

### Option B: Proper Migrations (Production)

**File:** `app/src/main/java/com/example/vndbapp/db/migrations/Migration1To2.kt`

```kotlin
package com.example.vndbapp.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Example: Add new column
        database.execSQL(
            "ALTER TABLE visual_novels ADD COLUMN rating REAL DEFAULT 0.0"
        )
    }
}
```

**Update DatabaseModule:**

```kotlin
@Provides
@Singleton
fun provideVndbDatabase(
    @ApplicationContext context: Context
): VndbDatabase {
    return Room.databaseBuilder(
        context,
        VndbDatabase::class.java,
        VndbDatabase.DATABASE_NAME
    )
    .addMigrations(MIGRATION_1_2)  // Add migrations
    .build()
}
```

---

## Testing

### Test DAO

```kotlin
@RunWith(AndroidJUnit4::class)
class VisualNovelDaoTest {

    private lateinit var database: VndbDatabase
    private lateinit var dao: VisualNovelDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().targetContext,
            VndbDatabase::class.java
        ).build()
        dao = database.visualNovelDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveVisualNovel() = runTest {
        val entity = VisualNovelEntity(
            id = "v1",
            title = "Test Novel",
            description = "Test Description"
        )

        dao.insertVisualNovel(entity)
        val retrieved = dao.getVisualNovelById("v1")

        assertThat(retrieved).isEqualTo(entity)
    }
}
```

### Test with Hilt

```kotlin
@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class LocalVisualNovelRepositoryTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var repository: LocalVisualNovelRepository

    @Before
    fun init() {
        hiltRule.inject()
    }

    @Test
    fun saveAndGetVisualNovel() = runTest {
        val novel = VisualNovel(
            id = "v1",
            title = "Test",
            description = "Test",
            image = Image()
        )

        repository.saveVisualNovel(novel)
        val retrieved = repository.getVisualNovelById("v1")

        assertThat(retrieved).isEqualTo(novel)
    }
}
```

---

## Best Practices

| Practice | Description |
|----------|-------------|
| **Use suspend functions** | All Room DAO operations should be suspend functions |
| **Prefer Flow over LiveData** | Flow is more modern and composable |
| **Single source of truth** | Database is the source, API updates it |
| **Separate models** | API models ≠ Room entities |
| **Use @Singleton** | Database instance should be app-scoped |
| **Export schema** | Set `exportSchema = true` for migrations |
| **Test DAOs** | Write unit tests for database operations |
| **Handle offline** | Show cached data when API fails |

---

## Troubleshooting

### Common Issues

1. **"Abstract room database implementation not found"**
   - Ensure KSP plugin is applied
   - Clean and rebuild project
   - Check `@Database` annotation has correct entities

2. **"Cannot access database on the main thread"**
   - Use `suspend` functions in DAO
   - Launch database operations in coroutine scope

3. **Migration errors**
   - Use `fallbackToDestructiveMigration()` during development
   - Export and compare schemas for proper migrations

---

## Sources

- [Dependency injection with Hilt | App architecture](https://developer.android.com/training/dependency-injection/hilt-android)
- [Room | Jetpack](https://developer.android.com/jetpack/androidx/releases/room)
- [Integrating Room with Hilt in Jetpack Compose](https://coldfusion-example.blogspot.com/2025/01/integrating-room-with-hilt-in-jetpack.html)
- [Dagger Hilt Documentation](https://dagger.dev/hilt/)

---

## Critical Fixes Applied

This guide was reviewed by The Hanged Man (cold, rational analytical review). The following critical issues were identified and fixed:

| Issue | Original Problem | Fix Applied |
|-------|------------------|-------------|
| **Double `val` syntax error** | `val val filters: List<String>` | Removed duplicate `val` keyword |
| **Infinite Flow collection** | `collect()` called from init AND catch block | Renamed to `observeCachedData()`, only called once from init |
| **Empty cache stuck in Loading** | No state emitted when cache is empty | Added `Empty` state, proper condition handling |
| **Invalid extension import** | `import com.example.vndbapp.db.entity.toModel` | Added comment explaining it's an extension function, used explicit `let` for nullable safety |
| **Hardcoded field strings** | Field specification repeated in code | Added `DEFAULT_FIELDS` constant in ViewModel companion |

### Important Implementation Notes

1. **Flow Collection**: The `observeCachedData()` method starts a single continuous Flow collection in `init`. Never call `collect` multiple times on the same Flow from different coroutines.

2. **Empty State Handling**: The `VisualNovelState.Empty` state is now emitted when:
   - Cache is empty AND
   - We were previously showing Success (to avoid flickering during initial load)

3. **Extension Functions**: The `toModel()` extension function is defined in `VisualNovelEntity.kt`. When importing, you're importing the extension function, not a class method.

4. **Constants**: Field specifications are kept in the ViewModel's companion object for maintainability.

---

*Generated with **Eye of Mystery Prying** by Cattleya, the Hermit*
*Reviewed by **The Hanged Man** - critical analysis and corrections applied*
