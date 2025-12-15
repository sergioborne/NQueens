# NQueens app (WIP)♟️

## 📱 Features

* Configurable board size (from 4 to 20)
* Leaderboard

## 🛠 Tech Stack

* Kotlin 100%
* Jetpack Compose
* Compose Navigation 3
* Testing libraries (MockK, JUnit, Turbine)
* Dependency Injection: Hilt
* Concurrency: Coroutines & Flow
* Persistence: Room Database

## 🏗 Architecture

* Followed separation of concerns, ui, domain, data layers. But did not go too far in defining boundaries as the test is simple and that way dependencies handling is simpler
* In case of a bigger codebase, I would split each feature in it’s own module, probably with an api and impl modules, putting the things that the feature needs to expose to other features in the api module as interfaces, and adding the implementation on the impl module.


## 📱 UI Tests

* Located in :app/src/androidTest.

* Tests the Compose UI by mocking the ViewModel.

#### Scenarios covered:

* Game screen ()


## 🔮 Future improvements

Having in mind that there is limited time, here are some things that I would do different or improve in the future:

* Not use Columns + Rows + Box for the board and instead use a canvas with optional zoom so it can support bigger board sizes without compromising usability.
* Improve transition animation between states to feel less bumpy
* Hardcoded test tags: I will implement them in a better way given more time using some sealed class so they can be referenced from app code and test code in a simpler and safer way.

## 📝 Things that I have tried or considered

* Having the game timer in the ViewModel but was giving some issues with testing plus a lot of recompositions happening so I decided to move it directly to compose to simplify it.
* Decided to use Compose Navigation 3 even though I did not have experience using it as I think it will be the standard pretty soon and it was worth a try.
* Tried to avoid writing unit test for "proxy" classes that only call methods from a dependency as they don't give value for the assignement. In a production code I might add them as a safety layer in case changes are introduced in the future.

## 🚀 How to Run 

### The App

* Clone the repository.
* Open in Android Studio.
* Sync Gradle project.
* Run on an Emulator or Physical Device.

### The Tests

* Unit Tests: ``` ./gradlew testDebugUnitTest ```
* UI Tests: ``` ./gradlew connectedDebugAndroidTest ```

Note: Ensure animations are disabled on the device for reliable UI testing.
