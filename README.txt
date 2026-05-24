Echoes Arena
Version 1.0.0

Team
Irgimbayev Adi, Koshkinbai Nurbek, Akzhigit Dias

Project Overview
Echoes Arena is a 2D fantasy arena survival game built with Java, libGDX, and the LWJGL3 desktop backend. The player chooses a hero class, enters an arena, survives enemy waves, collects health pickups, uses melee and ranged attacks, and clears staged battles against increasingly dangerous enemies and bosses.

Submission Files
- Echoes Arena-winX64.zip: Windows executable package with bundled Java runtime.
- Echoes Arena-1.0.0.jar: Runnable JAR build.
- EchoesArena_GDD.pdf: Game Design Document.
- EchoesArena_Diagrams.pdf: Game flow, class diagram, and level sketch.
- README.txt: This file.

How to Run
Recommended Windows build:
1. Unzip "Echoes Arena-winX64.zip".
2. Open "Echoes Arena.exe".
3. No Java installation is required for this version because the runtime is included.

Runnable JAR build:
1. Install Java 17 or newer.
2. Open a terminal in the folder containing the JAR.
3. Run:
   java -jar "Echoes Arena-1.0.0.jar"

Controls
- Move: WASD or Arrow Keys
- Dash: Space
- Ranged Homing Attack: Left Mouse Button
- Melee Strike: Right Mouse Button
- Ultimate Fireball: Q
- Ultimate Frost Rift: E
- Upgrade Tree: Tab
- Pause / Resume: Esc

Game Objective
Survive each arena stage, defeat enemy waves and bosses, collect health pickups, upgrade your character, and clear all levels without dying.

Core Gameplay Features
- Class-based player selection.
- Fast arena combat with dash, melee, ranged homing shots, and ultimate abilities.
- Multiple enemy behaviors, including melee, ranged, swarm, support, tank, and boss-style encounters.
- Stage and wave progression.
- Health pickups and kill tracking.
- Upgrade and progression systems.
- Pause screen that resumes the existing game state without resetting kills, pickups, or stage progress.
- Custom fantasy UI, arena visuals, and Echoes Arena application icon.

Build Information
Framework: libGDX
Language: Java
Desktop Backend: LWJGL3
Java Target: Java 17
Build Tool: Gradle

Build commands used:
   ./gradlew lwjgl3:dist
   ./gradlew lwjgl3:packageWinX64

On Windows, the same commands can be run as:
   .\gradlew.bat lwjgl3:dist
   .\gradlew.bat lwjgl3:packageWinX64

The JAR output is created at:
   lwjgl3/build/libs/Echoes Arena-1.0.0.jar

The Windows executable package is created at:
   lwjgl3/build/construo/dist/Echoes Arena-winX64.zip

Known Issues
- If Windows blocks the downloaded JAR or EXE, right-click the file, open Properties, and choose Unblock.
- The standalone JAR requires Java 17 or newer. The Windows EXE package does not require a separate Java install.
- On some machines, Windows may briefly show a generic icon until the desktop or folder is refreshed.

Credits
Echoes Arena was developed as a final project using Java and libGDX. Visual and audio assets are included in the project assets folder and are used for educational, non-commercial project submission purposes.
