# Crasher
A sample app used to test Fabric integration. Includes a simple UI used to trigger native or non-native crashes, either on the main thread or on a background thread.

## Setup

1. If you do not already have a Fabric account, follow the [Fabric Getting Started Guide](https://docs.fabric.io/android/fabric/overview.html).
1. Set your Fabric API key in `app/src/main/AndroidManifest.xml`. Instructions for finding your key can be found [here](https://docs.fabric.io/android/fabric/settings/api-keys.html).
1. If you are not set up to build native code, follow the instructions for downloading the NDK and tools [here](https://developer.android.com/ndk/guides/index.html#download-ndk).
1. Add the app to your Fabric account. Do this by opening the app in Android Studio, installing the Fabric plugin, and following the instructions. More info [here](https://docs.fabric.io/android/fabric/settings/app.html). Note that the plugin may suggest edits to the code. Sometimes these edits do not compile. Simply accept them and revert anything that doesn't make sense.
1. Run `./gradlew assembleDebug crashlyticsUploadSymbolsRelease`.

## Triggering a Crash

1. Install the app on a device.
1. Open the app.
1. Tap the enable Crashlytics button.
1. Tap one of the crash buttons.
1. Optionally, start some background threads to see the impact on time it takes to crash.
1. If you triggered a native crash, reopen the app so that the report willl be uploaded.
1. View the crash at [https://www.fabric.io]().

## Current Issues

### Native Crash on a Background Thread

Triggering a native crash from the main thread will cause the app to crash immediately. Triggering one from a background thread will cause the app to hang for several seconds before crashing. This time increases with the total number of background threads. For instance, with 60 threads, the app might take up to 50 seconds to crash.

You can repro this in the sample app by enabling Crashlytics, starting 50 to 100 background threads then tapping "FORCE NATIVE CRASH ON BACKGROUND THREAD". The stopwatch will run until the app crashes.
