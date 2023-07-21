package mlb

import munit._
import zio.http._
import zio.*

class MlbApiSpec extends munit.ZSuite {

  /**
   * Tests using enpoinds of val static are functionnal due to Http.collect[Request]
   * However tests using endpoinds from val endpoints are not successful. None value maybe because of Http.collectZIO[Request] miss used
   */

  val app: App[Any] = mlb.MlbApi.static

  testZ("GET / - 404") {
    val req = Request.get(URL(Root))
    assertZ(app.runZIO(req).isFailure)
  }

  testZ("GET /text - Hello MLB Fans!") {
    val req = Request.get(URL(Root / "text"))
    assertEqualsZ(app.runZIO(req), Response.text("Hello MLB Fans!").withStatus(Status.Ok))
  }

  testZ("GET /json - Hello MLB Fans!") {
    val req = Request.get(URL(Root / "json"))
    assertEqualsZ(app.runZIO(req), Response.json("""{"greetings": "Hello MLB Fans!"}""").withStatus(Status.Ok))
  }

  testZ("GET /init") {
    val req = Request.get(URL(Root / "init"))
    val shouldBe = Response.text("Not Implemented").withStatus(Status.Ok)
    for {
      response <- app.runZIO(req)
    } yield assertEquals(response, shouldBe)
  }


  // testZ("GET /help") {
  //   val req = Request.get(URL(Root / "help"))
  //   assertZ(app.runZIO(req).isSuccess)
  // }

  //   testZ("GET /games/count - Number of games in historical data") {
  //   val req = Request.get(URL(Root / "games" / "count"))
  //   val shouldBe = Response.text("223388 game(s) in historical data").withStatus(Status.Ok)
  //   for {
  //     response <- app.runZIO(req)
  //   } yield assertEquals(response, shouldBe)
  // }

  // // testZ("GET /games/count - Nomber of games in historical data") {
  // //   val req = Request.get(URL(Root / "games" / "count"))
  // //   assertEqualsZ(app.runZIO(req), Response.text("223388 game(s) in historical data").withStatus(Status.Ok))
  // // }

  // testZ("GET /games/history/BOS - Last ten games of a team") {
  //   val req = Request.get(URL(Root / "games" / "history" / "BOS"))
  //   assertZ(app.runZIO(req).isSuccess)
  // }

  // testZ("GET /game/latest/ATL/CHW - Latest game between two teams") {
  //   val req = Request.get(URL(Root / "game" / "latest" / "ATL" / "CHW"))
  //   assertZ(app.runZIO(req).isSuccess)
  // }

  // testZ("GET /game/latest/ATL/CHW - Latest game between two teams") {
  //   val req = Request.get(URL(Root / "game" / "latest" / "ATL" / "CHW"))
  //   assertEqualsZ(app.runZIO(req), Response.json("""{"date":"2019-09-01","season":2019,"homeTeam":"ATL","awayTeam":"CHW","eloPreHomeTeam":1545.84728710668,"eloPreAwayTeam":1454.37855293889,"eloProbHomeTeam":0.6603134722293296,"eloProbAwayTeam":0.33968652777067043,"pitcherHomeTeam":"Julio Teheran","pitcherAwayTeam":"Lucas Giolito"}""").withStatus(Status.Ok))
  // }
}
