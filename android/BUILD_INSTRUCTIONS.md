# Koi Chi - Android App Bundle Build Instructions

## Prerequisites
- Install Android Studio or Android command-line tools
- Install JDK 17 or later
- Set ANDROID_HOME environment variable

## Build Steps

### Option 1: Using Android Studio
1. Open Android Studio
2. Select "Open an existing project"
3. Navigate to and select the `android` folder
4. Wait for Gradle sync to complete
5. Build > Generate Signed Bundle / APK
6. Select "Android App Bundle"
7. Create or select a keystore
8. Build the release AAB

### Option 2: Using Command Line
```bash
cd android
./gradlew bundleRelease
```

The AAB file will be created at:
`android/app/build/outputs/bundle/release/app-release.aab`

## Background Prevention Features
The app includes the following to prevent background execution:

1. **AndroidManifest.xml**:
   - `android:stopWithTask="true"` - Stops app when removed from recent apps
   - `android:launchMode="singleTask"` - Single instance only

2. **MainActivity.java**:
   - `onPause()` - Pauses WebView and JavaScript timers
   - `onResume()` - Resumes only when app is visible
   - `onDestroy()` - Properly cleanup WebView

3. **HTML Game** (already has):
   - `document.visibilitychange` listener suspends animation and audio

## Testing
Test background behavior:
1. Install the app
2. Open the app and interact with koi pond
3. Press home button
4. Check battery usage - should show minimal/no background activity

## Uploading to Google Play
1. Go to Google Play Console
2. Create a new app or select existing
3. Production > Create new release
4. Upload the `app-release.aab` file
5. Complete store listing and content rating
6. Submit for review

## Notes
- Minimum Android version: 7.0 (API 24)
- Target Android version: 14 (API 34)
- App ID: com.pollitoproductions.koichi
- Version: 1.0 (versionCode 1)
