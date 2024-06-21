# Viato Trip Planner App README

## Table of Contents
1. [Introduction](#introduction)
2. [Prerequisites](#prerequisites)
3. [Setup and Installation](#setup-and-installation)
4. [Coding Conventions](#coding-conventions)
5. [Android Studio](#android-studio)

## Introduction
This is the readme for the project group Viato Trip Planner. In it, there is the step by step guidance on how to run the project.

## Prerequisites
Before you begin, ensure you meet the following requirements:
- You have installed the latest version of [Android Studio](https://developer.android.com/studio).
- You have installed the latest version of [Visual Studio](https://code.visualstudio.com/download)

## Setup and Installation
1. **Clone the Repository Viato-trip-planner:**
   ```bash
   git clone git@github.com:michaelehiosu/Viato-trip-planner.git
   cd your-repo-name
This repository has all the logic for the app. 

2. **Clone the Repository Viato-trip-planner-api:**
   ```bash
   git clone git@github.com:michaelehiosu/Viato-trip-planner-api.git
   cd your-repo-name
This repository gets all the data from the skyscanner API and Travel Advisor API. You can run the project on Visual Studio. 

## Coding Conventions
We followed the [Kotlin coding convention](https://kotlinlang.org/docs/coding-conventions.html)

## Android Studio

Download Android Studio

- Open Android Studio and select `File->Open...` or from the Android Launcher select `Import project and navigate to the root directory of your project.
- Select the directory or drill in and select the file `build.gradle` in the cloned repo.
- Click 'OK' to open the the project in Android Studio.
- A Gradle sync should start, but you can force a sync and build the 'app' module as needed.

### Gradle (command line)

- Build the APK: `./gradle build`

## Using your phone as an Emulator

Connect an Android device to your development machine, turn on developer option and enable USB debugging.

### Run Android Studio

- Select `Run -> Run 'app'` (or `Debug 'app'`) from the menu bar
- Select the device you wish to run the app on and click 'OK'


