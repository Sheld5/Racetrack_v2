# Racetrack

PC version of the classic "racetrack" game (usually played with pen and paper) with AI interface.

Web about the project: https://sheld5.github.io/Racetrack_v2/

## Installation

To run the game do one of the following:

- Download the jar file. (/out/artifacts/Racetrack_v2_jar/Racetrack_v2.jar)

- Download the source code and run Ant, which will compile the code and create the jar file as well.

## Basic rules

 - The goal of the game is to get to the finish in the least turns.
 - All cars start at the START tile.
 - Cars are controlled by changing their velocity vector each turn by -1,0 or 1 in each direction.
 - Each turn the position of the car changes by adding its velocity vector to it.
 - The car moves through all tiles along the way, so it cannot for example "jump" over walls etc.
 - Cars cannot collide or interfere with each other in any way.
 - Each turn the car travels from
 - The car has to pass all checkpoints before passing the finish.
 - Adjacent CHECKPOINT tiles count as a single checkpoint.
 - If there is more than one FINISH tile, it does not matter which one the car passes.

## Tile types

ROAD, GRASS, START, CHECKPOINT, FINISH - rideable

WALL - not rideable, 3-turn punishment for crashing into it

WATER - the car will sink and isn't going to be able to continue the race

SAND - the car stops and its velocity is set to {0,0} (this results in only being able to move 1 tile per turn on sand)

ICE - if the car finishes the turn on ice, it cannot be controlled next turn and instead moves in the same trajectory and with the same velocity; if the velocity of the car is 0 (it crashed into a wall on ice), it can be controlled next turn as usual

## AI implementation

You can download some AIs from the /ai directory or you can write your own AI.
If you want to add AI in the game, just click on the folder icon in the pre-game settings
and choose the java file of the AI you want.

If you want to write your own AI implement the "DriverAI.java" interface and then use your AI the same way.

## Adding custom maps

To add a custom map to the game follow these steps:

- Download the "Tiled" map editor and use the tile-set located in /maps/RacetrackTileSet.tsx to create your map.

- Add your map into the /maps directory and add the name of the file to the /META-INF/maps.txt file.

- Run the game and choose your map in the pre-game settings.