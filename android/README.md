# Koi Chi Android App

This directory contains the Android application wrapper for the Koi Chi koi pond game.

## Structure
```
android/
├── app/
│   ├── src/main/
│   │   ├── java/com/pollitoproductions/koichi/
│   │   │   └── MainActivity.java          # Main activity with WebView
│   │   ├── res/
│   │   │   ├── values/
│   │   │   │   ├── strings.xml           # App name and strings
│   │   │   │   ├── styles.xml            # App theme
│   │   │   │   └── colors.xml            # Colors
│   │   │   └── mipmap-hdpi/
│   │   │       ├── ic_launcher.xml       # Launcher icon
│   │   │       └── ic_launcher_foreground.xml
│   │   ├── assets/
│   │   │   └── index.html                # The koi pond game
│   │   └── AndroidManifest.xml           # App configuration
│   ├── build.gradle                      # App build configuration
│   └── proguard-rules.pro               # Code optimization rules
├── build.gradle                          # Project build configuration
├── settings.gradle                       # Project settings
└── BUILD_INSTRUCTIONS.md                # How to build the AAB

```

## Features
- ✅ Fullscreen immersive experience
- ✅ Hardware acceleration enabled
- ✅ Prevents background execution to save battery
- ✅ Proper lifecycle management (pause/resume/destroy)
- ✅ JavaScript and DOM storage enabled
- ✅ No action bar or title bar
- ✅ Handles back button navigation

## Build
See [BUILD_INSTRUCTIONS.md](BUILD_INSTRUCTIONS.md) for detailed build steps.
