package mlb

import zio.*
import zio.jdbc.*
import zio.http.*
import com.github.tototoshi.csv.*
import mlb.EloPreHomeTeams.EloPreHomeTeam
import mlb.EloProbHomeTeams.EloProbHomeTeam
import zio.stream.ZStream
import zio.schema.*
import zio.schema.syntax.*

import java.time.LocalDate

object MlbApi extends ZIOAppDefault {

  import DataService._
  import ApiService._
  import HomeTeams._
  import AwayTeams._

  val static: App[Any] = Http.collect[Request] {
    case Method.GET -> Root / "text" => Response.text("Hello MLB Fans!")
    case Method.GET -> Root / "json" => Response.json("""{"greetings": "Hello MLB Fans!"}""")
  }.withDefaultErrorResponse

  val endpoints: App[ZConnectionPool] = Http.collectZIO[Request] {
    case Method.GET -> Root / "init" =>
      ZIO.succeed(Response.text("Not Implemented").withStatus(Status.NotImplemented))

    // Endpoint for future help info
    case Method.GET -> Root / "help" =>
      ZIO.succeed(Response.json(
        """{
             "/help": "Endpoint for API Help",
             "/welcome": "Endpoint for API Welcome"
           }""").withStatus(Status.NotImplemented))

    // Endpoint for API Welcome
    case Method.GET -> Root / "welcome" =>
      ZIO.succeed(Response.text("Welcome on Major League Baseball API").withStatus(Status.NotImplemented))
    case Method.GET -> Root / "game" / "latest" / homeTeam / awayTeam => //tofix
      for {
        game: Option[Game] <- latest(HomeTeam(homeTeam), AwayTeam(awayTeam))
        res: Response = latestGameResponse(game)
      } yield res
    case Method.GET -> Root / "game" / "predict" / homeTeam / awayTeam =>
      for{
        probHomeTeam: Option[Double] <- getEloProbHome(HomeTeam(homeTeam), AwayTeam(awayTeam))
        probAwayTeam: Option[Double] <- getEloProbAway(HomeTeam(homeTeam), AwayTeam(awayTeam))
        res: Response = predictResponse(homeTeam, probHomeTeam, awayTeam, probAwayTeam)
      } yield res
    case Method.GET -> Root / "games" / "count" =>
      for {
        count: Option[Int] <- count
        res: Response = countResponse(count)
      } yield res
    case Method.GET -> Root / "games" / "history" / aTeam => //tofix
      for{
        games: List[Game] <- lastTenGames(HomeTeam(aTeam), AwayTeam(aTeam))
        res: Response = historyReponse(games)
      }yield res
    case _ =>
      ZIO.succeed(Response.text("Not Found").withStatus(Status.NotFound))
  }.withDefaultErrorResponse


  val appLogic: ZIO[ZConnectionPool & Server, Throwable, Unit] = for {
    conn <- create
    // Path to CSV file (to be adapted to your environment)
    source <- ZIO.succeed(CSVReader.open(("/Users/victortran/IdeaProjects/scala_project2_api/src/csvfiles/mlb_elo.csv")))
    //Stream = List()
    stream <- ZStream
      // Read CSV file as string sequence stream
      .fromIterator[Seq[String]](source.iterator)
      // Transform each string sequence into a Game object using opaque types
      .map { values =>
        val date = GameDates.GameDate(LocalDate.parse(values(0)))
        val season = SeasonYears.SeasonYear(values(1).toInt)
        val homeTeam = HomeTeams.HomeTeam(values(4))
        val awayTeam = AwayTeams.AwayTeam(values(5))
        val eloPreHome =  EloPreHomeTeams.EloPreHomeTeam(values(6).toDouble)
        val eloPreAway =  EloPreAwayTeams.EloPreAwayTeam(values(7).toDouble)
        val eloProbHome = EloProbHomeTeams.EloProbHomeTeam(values(8).toDouble)
        val eloProbAway = EloProbAwayTeams.EloProbAwayTeam(values(9).toDouble)
        val pitcherHome = PitcherHomeTeams.PitcherHomeTeam(values(14))
        val pitcherAway = PitcherAwayTeams.PitcherAwayTeam(values(15))

        Game(date, season, homeTeam, awayTeam, eloPreHome, eloPreAway, eloProbHome, eloProbAway, pitcherHome, pitcherAway)
      }
      // Group sets by batches of 1000 to insert them in the database
      .grouped(1000)
      // Convert groupings into lists and insert sets into the database
      .foreach(chunk => insertRows(chunk.toList))
    // Closing the CSV file
    _ <- ZIO.succeed(source.close())
    // Execute a select query in the database
    res <- select
    // Display query results in the console
    _ <- Console.printLine(res)
    // Launch HTTP server with defined endpoints
    _ <- Server.serve[ZConnectionPool](static ++ endpoints)
  } yield ()

  override def run: ZIO[Any, Throwable, Unit] =
    appLogic.provide(createZIOPoolConfig >>> connectionPool, Server.default)
}



object ApiService {

  import zio.json.EncoderOps
  import Game._

  def countResponse(count: Option[Int]): Response = {
    count match
      case Some(c) => Response.text(s"$c game(s) in historical data").withStatus(Status.Ok)
      case None => Response.text("No game in historical data").withStatus(Status.NotFound)
  }

  def latestGameResponse(game: Option[Game]): Response = {
    println(game)
    game match
      case Some(g) => Response.json(g.toJson).withStatus(Status.Ok)
      case None => Response.text("No game found in historical data").withStatus(Status.NotFound)
  }

  def predictResponse(homeTeam: String, predHome: Option[Double], awayTeam: String, predAway: Option[Double]): Response = {
    (predHome, predAway) match
      case (Some(d), Some(a)) => Response.text(s"Prediction for ${homeTeam} is $d \nPrediction for ${awayTeam} is $a").withStatus(Status.Ok)
      case (None, Some(a)) => Response.text(s"Prediction for ${homeTeam} not found \nPrediction for ${awayTeam} is $a").withStatus(Status.Ok)
      case (Some(d), None) => Response.text(s"Prediction for ${homeTeam} is $d \nPrediction for ${awayTeam} not found").withStatus(Status.Ok)
      case (None, None) => Response.text(s"Prediction for ${homeTeam} not found \nPrediction for ${awayTeam} not found").withStatus(Status.Ok)
  }
  def historyReponse(games:List[Game]): Response = {
    println(games)
    games match
      case Nil => Response.text("No games for this team").withStatus(Status.NotFound)
      case _ => Response.text(s"${games.mkString("\n")}").withStatus(Status.Ok)
  }
}

object DataService {

  val createZIOPoolConfig: ULayer[ZConnectionPoolConfig] =
    ZLayer.succeed(ZConnectionPoolConfig.default)

  val properties: Map[String, String] = Map(
    "user" -> "postgres",
    "password" -> "postgres"
  )

  val connectionPool: ZLayer[ZConnectionPoolConfig, Throwable, ZConnectionPool] =
    ZConnectionPool.h2mem(
      database = "mlb",
      props = properties
    )

  val create: ZIO[ZConnectionPool, Throwable, Unit] = transaction {
  execute(
    sql"""CREATE TABLE IF NOT EXISTS games(
          date DATE NOT NULL,
          season_year INT NOT NULL,
          home_team VARCHAR(3),
          away_team VARCHAR(3),
          elo_pre_home DOUBLE,
          elo_pre_away DOUBLE,
          elo_prob_home DOUBLE,
          elo_prob_away DOUBLE,
          pitcher1_home VARCHAR(255),
          pitcher1_away VARCHAR(255)
          )"""
  )
}

  import GameDates.*
  import SeasonYears.*
  import HomeTeams.*
  import AwayTeams.*

  
  // Select a game from the database
  val select: ZIO[ZConnectionPool, Throwable, Option[Game]] = transaction {
    selectOne(
      sql"SELECT * FROM games"
        .as[Game]
    )
  }

  // Insert a batch of games into the database
  def insertRows(games: List[Game]): ZIO[ZConnectionPool, Throwable, UpdateResult] = {
    val rows: List[Game.Row] = games.map(_.toRow)
    transaction {
      insert(
        sql"INSERT INTO games(date, season_year, home_team, away_team, elo_pre_home, elo_pre_away, elo_prob_home, elo_prob_away, pitcher1_home, pitcher1_away)".values[Game.Row](rows)
      )
    }
  }

  val count: ZIO[ZConnectionPool, Throwable, Option[Int]] = transaction {
    selectOne(
      sql"SELECT COUNT(*) FROM games".as[Int]
    )
  }

  def latest(homeTeam: HomeTeam, awayTeam: AwayTeam): ZIO[ZConnectionPool, Throwable, Option[Game]] = {
    transaction {
      selectOne(
        sql"SELECT date, season_year, home_team, away_team FROM games WHERE home_team = ${HomeTeam.unapply(homeTeam)} AND away_team = ${AwayTeam.unapply(awayTeam)} ORDER BY date DESC LIMIT 1".as[Game]
      )
    }
  }

  def getEloProbHome(homeTeam: HomeTeam, awayTeam: AwayTeam): ZIO[ZConnectionPool, Throwable, Option[Double]] = {
    transaction {
      selectOne(
        sql"SELECT elo_prob_home FROM games WHERE home_team = ${HomeTeam.unapply(homeTeam)} AND away_team = ${AwayTeam.unapply(awayTeam)} ORDER BY date DESC LIMIT 1".as[Double]
      )
    }
  }

  def getEloProbAway(homeTeam: HomeTeam, awayTeam: AwayTeam): ZIO[ZConnectionPool, Throwable, Option[Double]] = {
    transaction {
      selectOne(
        sql"SELECT elo_prob_away FROM games WHERE home_team = ${HomeTeam.unapply(homeTeam)} AND away_team = ${AwayTeam.unapply(awayTeam)} ORDER BY date DESC LIMIT 1".as[Double]
      )
    }
  }
  // Retrieve the last then games for a specific team in both home and away position
  def lastTenGames(homeTeam: HomeTeam, awayTeam: AwayTeam): ZIO[ZConnectionPool, Throwable, List[Game]] = {
    transaction {
      selectAll(
        sql"SELECT date, season, homeTeam, awayTeam, homeScore, awayScore, eloProbHome, eloProbAway FROM games WHERE homeTeam = ${HomeTeam.unapply(homeTeam)} OR awayTeam = ${AwayTeam.unapply(awayTeam)} ORDER BY date DESC LIMIT 20".as[Game]
      ).map(_.toList)
    }
  }
}
