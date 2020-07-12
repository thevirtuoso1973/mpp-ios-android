[![official JetBrains project](https://jb.gg/badges/official.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)

# Kotlin Multiplatform projects: Sharing code between iOS and Android

The core repository for [this tutorial](https://play.kotlinlang.org/hands-on/Targeting%20iOS%20and%20Android%20with%20Kotlin%20Multiplatform/01_Introduction).

## Running

You need Xcode 10.3+ and Android Studio 3.4+ with Kotlin 1.3.41+ on macOS 10.14 to that tutorial project.

You will need to install an emulator on Android to run the app. For this you'll need to:
- Open the AVD manager (click the phone icon in the top right of your Android Studio window)
- Click on "Create Virtual Device..."
- Select any Phone from the list and click Next.
- Select system image. It's good practice to test your work on the minimum API level that you support so you might want to go with API level 23 (if you can't see it under the 'Recommended' tab, look under the 'Other images' tab). Note that you will need to install it before you can select it. If you're feeling particularly keen, you can install an emulator with the highest API level too and test your work on both devices.

To run your code on Android, simply press the 'play' button in Android Studio, with your virtual device selected.

To run your code on iOS, first run mpp-ios-android > SharedCode > Tasks > buils > packForXcode on the right hand side Gradle menu in Android Studio. After this is done, go to XCode and run your application with any simulator (there should be a bunch of them in the options so no need to install anything).

## App Structure

We are building a multiplatform project with Kotlin. This means that instead of building two separate native apps, one for Android and one for iOS, the project is structured in a way that all the code that does not need to be platform specific, is shared. The shared code and Android platform specific code are written in Kotlin. The iOS platform specific code is written in Swift.

The structure of our specific project is as follows:

- SharedCode folder has code that is shared between Android and iOS apps - it should contain all the business logic. We have defined an ApplicationContract interface, which outlines the methods we are expecting to be defined in shared code (see Presenter) and the methods we expect to exist in the native code (View).
- app folder contains code that is Android platform specific. All the Android UI elements live here as well as some platform specific functions (e.g. opening a link in a browser).
- native folder contains code that is iOS platform specific. All the iOS UI elements live here, as well as some platform specific functions (e.g. opening a link in a browser).

The way to think about this structure is that when we open the app, the platform specific code should tell the Presenter that a page has been loaded and pass a reference to itself into it. In Android, this is done when the MainActivity view is created (see presenter.onViewTaken being called in onCreate function) and on iOS this is done in the ViewController.swift file (see presenter.onViewTaken being called under viewDidLoad). Now whenever the user interacts with the UI, the native code should call a method on presenter to let know that this has happened and then the presenter can call methods on view, telling it what to change in the UI.

For example, let's say we want to add a button to our app. Clicking that button should do a complicated calculation and display the result in the UI. The first thing we should do is add methods to the contract that we think presenter and view should have. We should add:

- abstract fun onButtonTapped() method to the Presenter class. This way we promise the native code that this function will exist in the shared code. If the user clicks the button, the native code can just call presenter.onButtonTapped() and not worry about the business logic.
- fun setLabel(text: String) method to the View interface. Luckily, this already exists, so we don't need to modify the view contract. But maybe we'd like to display an error popup if something goes wrong with the calculation? In that case we could add showAlert(text: String) method to the View interface.

When the presenter.onButtonTapped() is called from the native code, the Presenter will run the complicated calculation. If everything goes well, it can call view.setLabel(result) which will update the UI. If anything goes wrong, it should call view.showAlert("Oops! Something went wrong"). This way, if something changes about the algorithm, we will only need to make the changes in one place (the shared code), rather than on two platforms.

Of course, besides only defining the methods in the contract, the methods should be added to the relevant components. We should add onButtonTapped function to the Presenter class, and showAlert(text: String) method to the native views. On Android, the function should be added to the MainActivity class and on iOS, the function should be added to the ViewController class.

Note that if there is a method in the contract that doesn't exist either on the presenter or the view, your project won't build. This ensures that whenever the presenter calls anything on the view or vice-versa, the methods will actually exist and the app won't crash.
