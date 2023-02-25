Android Architecture Starter Templates (beta) Example.

Features:
Reactive MVVM arch with Kotlin Flows
RoomDB
Hilt
Declarative with ComposeUI
Bill of Materials (BoM)
All Composable(s) have a preview.

We use Accompanist as it is maintained by the Google Android Compose Team.
Accompanist is a group of libraries that aim to supplement Jetpack Compose with features that are
commonly required by developers but not yet available.

https://github.com/google/accompanist/tree/main/sample/src/main/java/com/google/accompanist/sample/permissions

[Video Tutorial](https://www.youtube.com/watch?v=ME3LH2bib3g)
[code](https://github.com/philipplackner/GraphQlCountriesApp)

==================
[Android GraphQL Tutorial](https://medium.com/android-news/yelpql-learn-graphql-by-building-yelp-app-da2a71f16c77)
[Android GraphQL Example](https://medium.com/android-news/hello-apollo-writing-your-first-android-app-with-graphql-d8edabb35a2)

### GraphQL

[Yelp tutorial](https://graphql.org/learn/)

##### Yelp GraphQL

[Yelp Docs](https://docs.developer.yelp.com/reference/v3_business_search)
[Yelp GraphQL](https://docs.developer.yelp.com/docs/graphql-intro)
[Test Yelp GraphQL](https://www.yelp.com/developers/graphiq)

##### Postman

[Postman GraphQL](https://learning.postman.com/docs/sending-requests/graphql/graphql/#using-postmans-built-in-support-for-graphql)

### Arach

[app arch](https://developer.android.com/topic/architecture/intro)
[Video Series](https://www.youtube.com/watch?v=TPWmfJq16rA&list=PLWz5rJ2EKKc8GZWCbUm3tBXKeqIi3rcVX)

### Code

[Apollo GraphQL tutorial](https://www.apollographql.com/docs/kotlin/tutorial/00-introduction/)
[Apollo Compose Code](https://github.com/apollographql/apollo-kotlin-tutorial/tree/compose)
[Download Schema](https://www.apollographql.com/docs/kotlin/tutorial/02-add-the-graphql-schema/)

[Architecture Learning Journey](https://github.com/android/nowinandroid/blob/main/docs/ArchitectureLearningJourney.md)
[Modularization Learning Journey](https://github.com/android/nowinandroid/blob/main/docs/ModularizationLearningJourney.md)

[app](https://github.com/android/nowinandroid/tree/main/app)
[core datastore](https://github.com/android/nowinandroid/tree/main/core/datastore)

Thoughts:
[Now in Android](https://github.com/android/nowinandroid)
[img app](https://github.com/android/nowinandroid/tree/main/app)
[free one](https://proandroiddev.com/build-a-modular-android-app-architecture-25342d99de82)

Google Arch Guidelines:

1) Data Layer
2) UI Layer
3) Domain Layer

The [Data Layer](https://developer.android.com/topic/architecture/data-layer):
Data with business logic made of

Repositories -connect to -> Data Sources (Remote or local) only one source of data

Repositories: 1. Expose data, 2. Centralize changes, 3. Resolve conflicts, 4 Contain business
logic (domain layer)
Each to only one data source. (ie. data vs payments)

Single source of truth.
The repository should update/resolve local cache when connecting to the API

Data Immutability is done with Kotlin data class.
Use different data classes for various layers.

Errors: use Kotlin Flow catch operator.
Testing: Use Room in memory database for local data testing. Use fake data with DI. WireMock for
network test.

The UI Layer:
UI Elements / State Holders - App State -

1. App data -> UI data
2. UI data -> UI elements
3. User events -> UI changes

UiState: use Kotlin data class
ViewModel is a state holder (Unidirectional data flow)

1. ViewModel exposes UI state
2. UI notifies ViewModel of events
3. ViewModel updates state and is consumed by the UI

UI is updated by state exposure using Kotlin Flows / Channels (StateFlow)
Compose: Use mutableStateOf

UI state consumption is done by the terminal operator ".collect"
Need to track the lifecycle. ViewModels last longer then then UI. Only collect when the UI is
active.

UI Events.

1. User Events - Actions - onClick function ()->Unit
2. ViewModel Events - Exposes all the business logic need by the UI - State Update.
   Use uiState: StateFlow<UiState> = ...
   errorMessages: List<Int> - using scaffoldState.snackbarhostState.showSnackbar

- refresh<Data> --
- Use LaunchedEffect - Start a coroutine from a Composable.

MainUIRoute - has all the ViewModels.

Domain Layer:
only holds business logic. Has the UseCases-
Naming is {verb in present tense explainig what you are doing} {noun/what} {the world UseCase}
example: getLatestGymLocationUseCase

1. Simple
2. Lightweight
3. Immutable
   Use Suspend functions, Flows and Callbacks to message the UI Layer.
   Usecase use other Usecases.
   Lifecycle - scoped to caller / create a new instance when passing as a dependency
   Threading - Must be main-safe / Move job to data layer if result can be cached.

Common Tasks:

1. Encapsulate reusable business logic.
2. Combine data from multiple repositories

Use a usecase to combine data that is needed by the ViewModel.
Use the kotlin invoke function to make it callable: suspend operator fun invoke():
List<GymWithImage>

Usecases should be thread safe as they can be called noumerous times.
Everything must go though Domain Layer.

1. Reduce complexity of UI layer
2. Avoid duplication
3. Improve testability

---
Modularization
Divide your project into:

1. Feature modules
2. Library modules
3. App modules

Layers: App -only-> Feature -only-> Library

---
Enities -
Remote Entities - JSon Response - data class RemoteUser(val userid :String, val token:String) /
RemoteInventory
Database Entities - @Entity using Room
Domain Entities - business models - data call Inventory() -
UiState - UI State - @Parcelize data class Inventory():Parcelable
with mapper classes -
Remote and Database = DataLayer
UiState or Domain = UI Layer
----
data class RemoteInventory

Nice Alternative
[platform](https://github.com/igorwojda/android-showcase/tree/c522831b8b1ab7e3f909a205aa84510b15440b7e)






























 







