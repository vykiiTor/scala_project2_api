To run the project

Go into the terminal and enter those commands :
- sbt
- project rest
- compile
- ~reStart

Go into an other terminal and enter the following command :
curl "http://localhost:8080/games/count"
The output shloud be : "223388 game(s) in historical data"

Game.scala defines the data structure for our API. Within this file, numerous opaque types are specified to represent distinct entities related to Games, including match dates, season years, home teams, away teams, home scores, away scores, Elo probabilities for home and away teams. An opaque type is an abstract type with a concealed internal representation, which means that its underlying value cannot be accessed directly but only through methods explicitly defined for that type.

Subsequently, this file contains the definition of a Game class that utilizes these opaque types to represent a match. Additionally, it incorporates JSON encoders and decoders to serialize/deserialize Game objects, along with a JdbcDecoder for mapping rows in the database to Game instances.

Main.scala implements an API for historical baseball (MLB) game data. It employs the ZIO framework to handle asynchronous effects and dependencies. The API offers several endpoints to access data, such as the list of games, the number of games, the last twenty games of a specific team, the prediction of a game's result, and the last game between two teams. Data is stored in an in-memory H2 database, and games are loaded from a CSV file during application startup. The API responds to HTTP requests by providing text or JSON responses, depending on the requested URL.

Here are some examples of endpoints you can use to test our API:

curl "http://localhost:8080/text"
curl "http://localhost:8080/json"
curl "http://localhost:8080/init"
curl "http://localhost:8080/games"
curl "http://localhost:8080/games/count"
curl "http://localhost:8080/games/ATL"
curl "http://localhost:8080/game/predict/ATL/NYM"
curl "http://localhost:8080/game/latest/ATL/NYM"