# Cellularena (Winter Challenge 2024)

Source code for CodinGame's Winter Challenge 2024 event.

https://github.com/CGjupoulton/WinterChallenge2024-Cellularena

# How to use the CLI Referee

## Play a match between 2 bots

```shell
$ java  -jar /PATH/TO/winter-2024-cellularena-cli-referee-1.0-SNAPSHOT.jar -p1name "BOT1" -p1 "bot1.exe" -p2name "BOT2" -p2 "bot2.exe"
387
327
seed=1686036124564
```
Output explanation:
* line 1: player 1 score
* line 2: player 2 score
* line 4: game's seed

Options `-p1name` and `-p2name` are not mandatory. You can use then to give a friendly name to your bots. By default, the bots are named according to the value of options `-p1` and `-p2`.

To use bots written in Python for exemple, replace `-p1 "bot1.exe"` by `-p1 "python3 bot1.py"`

## Play a match between 2 bots on a specific league

```shell
$ java  -jar /PATH/TO/winter-2024-cellularena-cli-referee-1.0-SNAPSHOT.jar -p1name "BOT1" -p1 "bot1.exe" -p2name "BOT2" -p2 "bot2.exe" -lvl 4
387
327
seed=1686036124564
```
Leagues:
* 1 = Wood 4 : Your organism can only grow basic organs
* 2 = Wood 3 : Your organism can grow harvesters
* 3 = Wood 2 : Your organism can grow tentacles
* 4 = Wood 1 : Your organism can grow sporers
* 5 = Bronze/Silver/Gold/Legend : Real maps

## Play a match between 2 bots with a specific game's seed

```shell
$ java  -jar /PATH/TO/winter-2024-cellularena-cli-referee-1.0-SNAPSHOT.jar -p1name "BOT1" -p1 "bot1.exe" -p2name "BOT2" -p2 "bot2.exe" -d seed=1686036124564
387
327
seed=1686036124564
```

## Play a match between 2 bots and log game details

```shell
$ java  -jar /PATH/TO/winter-2024-cellularena-cli-referee-1.0-SNAPSHOT.jar -p1name "BOT1" -p1 "bot1.exe" -p2name "BOT2" -p2 "bot2.exe" -l game.json
158
142
seed=1686065027701
```

## Play a match between 2 bots and view it in your browser locally

```shell
$ java  -jar /PATH/TO/winter-2024-cellularena-cli-referee-1.0-SNAPSHOT.jar -p1name "BOT1" -p1 "bot1.exe" -p2name "BOT2" -p2 "bot2.exe" -s
http://localhost:8888/test.html
Exposed web server dir: /tmp/codingame
```
Open `http://localhost:8888/test.html` in your browser and enjoy !

## Replay locally in your browser a game from its game details

```shell
$ java  -jar /PATH/TO/winter-2024-cellularena-cli-referee-1.0-SNAPSHOT.jar -r game.json
http://localhost:8888/test.html
Exposed web server dir: /tmp/codingame
```
Open `http://localhost:8888/test.html` in your browser and enjoy !

## Use with CG Brutal Tester

CG Brutal Tester is here: https://github.com/dreignier/cg-brutaltester

Build it from source code or get a release.

Then you can run it:

```shell
java -jar /PATH/TO/cg-brutaltester-1.0.0.jar -r "java -jar /PATH/TO/winter-2024-cellularena-cli-referee-1.0-SNAPSHOT.jar" -p1 "bot1.exe" -p2 "bot2.exe" -t 4 -n 100
```

See CG Brutal Tester's README on how to interpret its output and what each option means.

# How to build the CLI Referee

Instructions for Linux (and Windows Subsystem for Linux).

For Windows, you only need to adapt the first part (setting up JDK and maven).

```shell
WORK_DIR="/PATH/TO/THE/DIR/WHERE/THIS/REPO/WILL/BE/CLONED"

# Install maven package from your distribution
# Install NodeJS and NPM from your distribution

#
# Check everything is OK
#

java -version
# Should output something like this:
# openjdk version "17.0.11" 2024-04-16
# OpenJDK Runtime Environment (build 17.0.11+9-Debian-1deb12u1)
# OpenJDK 64-Bit Server VM (build 17.0.11+9-Debian-1deb12u1, mixed mode, sharing)

mvn -v
# Should output something like this:
# Apache Maven 3.8.7
# Maven home: /usr/share/maven
# Java version: 17.0.11, vendor: Debian, runtime: /usr/lib/jvm/java-17-openjdk-amd64
# Default locale: fr_FR, platform encoding: UTF-8
# OS name: "linux", version: "6.1.0-21-amd64", arch: "amd64", family: "unix"

#
# Get source code
#

git clone https://github.com/CodinGame/SummerChallenge2024Olymbits.git "$WORK_DIR"

#
# Build
#

cd "$WORK_DIR"
mvn  clean
rm -f dependency-reduced-pom.xml
pushd src/main/resources
rm package-lock.json
npm install
npm start
rm -fr node_modules
popd

#
# Build jar
#

mvn package

# If everything goes well, the jar to launch is in the 'target' directory
ls target/winter-2024-cellularena-cli-referee-1.0-SNAPSHOT.jar
```
