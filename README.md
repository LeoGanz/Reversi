# Reversi game

This project contains a implementation of the Reversi game. A detailed description on playing Reversi can be found on [Wikipedia](https://en.wikipedia.org/wiki/Reversi) as well as inside the game.

Our features:
- play against our AI
- hotseat modus
- multiplayer via TCP/IP
- server with multiple private and public lobbies 
- simple JSON protocol - (_feel free to create your own client ;)_)
- spectator view for multiplayer games 
- own coloured disks
- beautiful UI

## Our AI
Our AI got improved mutliple times by playing against itself. So after some rounds we came up with a simple and competitive AI.

## JSON for Multiplayer
Our Server uses simple `JSON` messages via TCP/IP. So feel free to create your own client. 


## Dependencies

The project requires Java 11.
It was only tested on Ubuntu 18.04 with openjdk 11.0.3 2019-04-16,
so no other guarantees can be made.

The project is built with `gradle`, version 5.6.4. The provided `gradlew` wrapper automatically downloads and uses
the correct gradle version.


## Building the Project

On Linux and Mac OS, run the following command from the project's root directory to compile the program,
run all checks and create an executable jar:

```
./gradlew build jar
```

On Windows, run the following command from the project's root directory to compile the program,
run all checks and create an executable jar:

```
./gradlew.bat build jar
```

If the command succeeds, the jar is found in `build/libs/reversi-fddlj.jar`.
This jar can be executed with `java -jar build/libs/reversi-fddlj.jar`


## Running the Program

To run the program during development without any checks, run `./gradlew run` .
