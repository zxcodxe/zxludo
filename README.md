# ASTA LUDO — Complete Offline Prank Source

A standalone Android/Kotlin Ludo-style game for local testing and harmless friend/family pranks.

## Features
- 1 VS 1
- 2 VS 2 TEAM
- 4 PLAYERS
- Offline gameplay; no account, server, or internet required
- `ASTA SELECT` for starting player
- `PRANK: SECRET WINNER` mode: privately selects a favored player; that player receives a stronger dice advantage, but the game is still playable normally
- Dice, token movement, captures, safe squares, extra turn on 6, and win state
- Team winner display in 2 VS 2
- Original board/UI; no Ludo King assets

## Build
Open the project in Android Studio, allow Gradle sync, then run:

`app > Tasks > build > assembleDebug`

The generated APK will normally be under:

`app/build/outputs/apk/debug/app-debug.apk`

## Project
- `settings.gradle`
- root `build.gradle`
- `app/build.gradle`
- `app/src/main/AndroidManifest.xml`
- `app/src/main/java/com/asta/ludoprank/MainActivity.kt`
- `app/src/main/res/values/styles.xml`

This package contains source code only; an APK is not included because a Gradle/Android build environment is not available in this session.
