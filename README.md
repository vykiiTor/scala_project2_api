# Major League Baseball API 
This project is an API for fetching Major League Baseball (MLB) game data.

## Developped by:
- LISZEWSKI Benjamin
- MABECQUE Julien
- RAVAILHE Hugo
- TRAN Victor

## Project Structure
The code is organized into three main modules:

MlbApi: This is the main module where the application is launched. It also defines the set of endpoints for the HTTP server.

ApiService: This module defines the different HTTP responses sent by the API. It contains functions that transform the data fetched from the database into HTTP responses.

DataService: This module is responsible for all interactions with the database. It contains functions to create the database, insert data, and fetch data.

Game.scala : Our data is structured in a Game object, which represents a single game in the Major League Baseball (MLB). The Game object has the following attributes:

**date**: A GameDate representing the date when the game was played.

**season** : A SeasonYear representing the year of the MLB season.

**homeTeam and awayTeam**: HomeTeam and AwayTeam types representing the home and away teams.

**eloPreHomeTeam and eloPreAwayTeam**: EloPreHomeTeam and EloPreAwayTeam types representing the Elo ratings of the home and away teams before the game.

**eloProbHomeTeam and eloProbAwayTeam**: EloProbHomeTeam and EloProbAwayTeam types representing the predicted probability of winning for the home and away teams based on their Elo ratings.

**pitcherHomeTeam and pitcherAwayTeam**: PitcherHomeTeam and PitcherAwayTeam types representing the main pitchers for the home and away teams.

## Installation

Clone the Git repository or download the project files.
```console
git clone https://github.com/vykiiTor/scala_project2_api.git
```

Navigate to the project directory.
```console
cd scala_project2_api
```

## Run the project

Run the application with sbt.
```console
sbt update
```
```console
sbt compile
```
```console
sbt run
```


## How It Works ##
On application startup, the API reads a CSV file containing game data and inserts it into the database.

Once the data is inserted, the API starts an HTTP server that exposes several endpoints for interacting with the data. Here is a list of these endpoints:

* GET /init: Checks if the database has been initialized.
* GET /help: Provides information about the API and its endpoints.
* GET /welcome: Sends a welcome message.
* GET /game/latest/{homeTeam}/{awayTeam}: Returns information about the latest game between two teams.
* GET /game/predict/{homeTeam}/{awayTeam}: Returns a prediction for a game between two teams.
* GET /games/count: Returns the number of games in historical data.
* GET /games/history/{team}: Returns the last ten games of a team.

To see the result in a given format, you can launch an API tester like postman and use GET with "http://localhost:8080/" + endpoint.

Another methode is to use a second terminal with the curl command : curl "http://localhost:8080/" + endpoint 

## Dependencies ##
This project uses several Scala libraries. Here is a list of these libraries and why they are used:

ZIO: A library for asynchronous and concurrent processing. Used to handle asynchronous execution of the code and to handle errors.
zio-jdbc: A library for interacting with the database. Used to create the database, insert data, and fetch data.
zio-http: A library for setting up an HTTP server. Used to define the endpoints for the HTTP server.
tototoshi-csv: A library for reading CSV files. Used to read the CSV file containing the game data.