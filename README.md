# Racetrack_v2
maturitní projekt na motivy hry "Racetrack"

## Basic rules

 - The goal of the game is to get to the finish in the least turns.
 - All cars start at the START tile.
 - Cars cannot collide or interfere with each other in any way.
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

Follow these steps to implement your own AI to the game:

1\) Create your own AI class that implements the java.model.DriverAI interface.

*Optional: Copy your AI into the resource/ai folder. 

2\) Start the game and add your AI to the game in the AI settings by choosing the .java file in the file explorer. (You can add the same AI more than once.)

3\) All added AIs will control their own cars as long as there are enough cars for them. The rest of the cars will be human-controlled.