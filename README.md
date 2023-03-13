# NewsApp

* A News Application uses [NEWS API](https://newsapi.org/) based on Kotlin and MVVM architecture.
* A single-activity pattern, using the Navigation component to manage fragment operations.
* Handles background tasks using coroutines + Flow.

## Paging 3
* The interesting thing  about this repository is that it implements the [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) Library that has so many features that simplified complicated process creating RecyclerView with paging.
    - Loading small chunk of data that reduces usage of network bandwidth and system resources.
    - Built-in support for error handling, including refresh and retry capabilities.
    - Built-in separator, header, and footer support.
    - Automatically requests the correct page when the user has scrolled to the end of the list.
    - Ensures that multiple requests are not triggered at the same time.


## Libraries
- 100% Kotlin + [Coroutines](https://github.com/Kotlin/kotlinx.coroutines) + [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/)
- MVVM Architecture
- Architecture Components (Lifecycle, Paging, ViewModel, DataBinding, Navigation, Room)
- [NEWS API](https://newsapi.org/)
- [Dagger2 Hilt](https://dagger.dev/hilt/) for dependency injection
- [Retrofit2 & Gson](https://github.com/square/retrofit) for REST API
- [leakCanary](https://github.com/square/leakcanary) Memory leak detection library for Android.
- [Coil](https://github.com/coil-kt/coil) for loading images
