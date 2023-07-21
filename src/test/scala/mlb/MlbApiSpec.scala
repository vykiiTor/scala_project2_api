package mlb

import munit._
import zio.http._
import zio.*

class MlbApiSpec extends munit.ZSuite {

  val app: App[Any] = mlb.MlbApi.static

  testZ("GET / - 404") {
    val req = Request.get(URL(Root))
    assertZ(app.runZIO(req).isFailure)
  }

  testZ("GET /text - Hello MLB Fans!") {
    val req = Request.get(URL(Root / "text"))
    assertEqualsZ(app.runZIO(req), Response.text("Hello MLB Fans!"))
  }

  
}
