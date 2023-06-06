You would typically use a Fake Database when you want to simulate the behavior of the real database
and control the test environment, especially when testing business logic or data manipulation code.

On the other hand, an in-memory Room database is more suitable when you want to test the integration
between your app's code and the Room database, including the database schema, SQL queries, and data
access logic.


* Unit UI tests using **ComposeContentTestRule**. It validates your Composable in full separation, 
  without access to Activity. Useful for testing stateless Composables.

* UI tests using **AndroidComposeTestRule**. It validates your Composable inside an Activity, preferably 
  an empty one, e.g. ComponentActivity. Useful when activity resources are needed, for example in Screen Composables.
  
* Integration UI tests using **AndroidComposeTestRule**. It validates your Composable inside your Activity,
  which in the case of single-activity architecture would be MainActivity. Useful for root Composables 
  that includes ViewModel as an argument. Therefore **Hilt** will be used here as well.


utilize **onNodeWithText** and **onNodeWithContentDescription**

Use **createAndroidComposeRule<ComponentActivity>()**:
* When you need to test a Composable that can be used in multiple activities or screens.
* When you want to test a Composable in isolation from the specific activity implementation.
* When the Composable doesn't rely on any specific behavior or dependencies of a particular activity.

Use **createAndroidComposeRule<MainActivity>():**
* When you need to test a Composable that is tightly coupled with the main activity.
* When the Composable relies on specific activity behaviors, such as **navigation**, **state management**, or other activity-specific features.
* When you want to test the integration between the Composable and the main activity.


Compose Testing
There are three main ways to interact with elements:
* Finders let you select one or multiple elements (or nodes in the Semantics tree) to make assertions or perform actions on them.
* Assertions are used to verify that the elements exist or have certain attributes.
* Actions inject simulated user events on the elements, such as clicks or other gestures.


using a real ViewModel is appropriate because you want to test the integration between your 
UI components and the actual ViewModel logic.
@HiltAndroidTest
class RocketsRouteTest { RocketsRoute(nviewModel = composeTestRule.getHiltTestViewModel())

On the other hand, there may be situations where **you want to isolate your UI tests from the actual 
ViewModel implementation and dependencies.** In such cases, you can use a fake ViewModel that
provides
predefined or controlled behavior.

https://developer.android.com/jetpack/compose/testing-cheatsheet

Add to project
https://bitrise.io/blog/post/getting-started-with-maestro-the-new-mobile-ui-testing-framework-from-mobile-dev

When writing tests with Hilt, **you may need to uninstall certain modules** to ensure proper test
isolation.
Here are some general guidelines to help you determine which modules to uninstall:

1. Application-level modules: If your Hilt modules include bindings at the application level
   (`@Singleton` scope), you may need to uninstall these modules to prevent conflicts with
   test-specific
   bindings. This is especially important if your application-level modules provide dependencies
   that
   have side effects or interact with external systems (e.g., network calls, database access).

2. Modules providing test-specific dependencies: If you have modules specifically designed to
   provide
   test-specific dependencies (e.g., fake implementations, mock objects), you should uninstall these
   modules to avoid conflicts with the actual module bindings.

3. Modules with dependencies on Android components: If your Hilt modules have dependencies on
   Android
   components (e.g., `Context`, `Application`, `Activity`), you may need to uninstall these modules
   in
   tests where the Android components are not available or needed. This helps avoid unnecessary
   dependencies and improves test isolation.

4. Modules providing third-party integrations: If your Hilt modules provide bindings for third-party
   integrations (e.g., Firebase, network clients), you may consider uninstalling these modules in
   tests
   to avoid unnecessary dependencies on external systems.

By uninstalling modules, you ensure that the test uses a clean and isolated dependency graph,
which is crucial for reliable and focused unit testing.

~~~