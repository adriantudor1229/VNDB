## Preparing for interviews

### Technologies used for this 
- Hilt for DI
- ROOM for database
- Kotlin Coroutines
- Retrofit
- Moshi

### Design Pattern

- MVVM

### UI

- Jetpack Compose

# Most of this file would be about Android and Kotlin.
## I will start with Kotlin first, since the aspects of Kotlin are more important from my point of view. 

## Kotlin
### In this section I want to talk about what is the core for Kotlin and how to prepare it for interview.
#### If my speculation is right, most of the interviewers will ask you about generic things about Kotlin, so going in depth is too much.

## Extension function 
### Enables you to add new functionality to existing classes without modifying their code or inheriting from them. One example that I used and I think it was quite good it's mapping.
#### You want to map from entity to model and vice-versa. 
#### `fun Entity.toDto()  fun Dto.toEntity()`
#### Quite simple but very efficient

## Lazy vs lateinit
### Lateinit: We may not want to initialize our values at declaration time, but instead want to initialize and use them at any later time in our application. However before we use our value, we need to initialize it before it can be used.
- only var can be used
- no primitives (Int, Boolean, Long)
- it's not thread-safe
- avoid Nullability: it allows you to define non-nullable type (String instead of String?)
- often used with DI where dependencies are injected after the constructor completes
- lifecycle-dependent: essential for UI components, initialized in onCreate or onViewCreated, rather than the constructor
- run time error

### Lazy initialization: is a design pattern. With lazy initialization, we can create objects only the first time we access them. It ensures that objects that are expensive to create are initialized only where they are to be used, not on the app startup.
- if you don't need to initialize, you don't.
- improves performance and memory usage
- it is used for caching, UI element loading
- is thread-safe: offer a built-in synchronization mechanism to ensure that initialization occurs only once, even in multi-threaded scenarios.
- compile time error