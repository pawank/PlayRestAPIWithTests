import java.util.Date

import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.{Mode, Application}
import play.api.cache.CacheApi
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.{JsString, Json, JsValue, JsObject}
import org.scalatestplus.play.{PlaySpec, OneAppPerSuite}
import play.api.test._
import play.api.test.Helpers._

import scala.concurrent.duration.Duration


/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class PersonControllerSpec extends Specification{
  sequential
  /*
  implicit override lazy val app: FakeApplication =
    FakeApplication(
      additionalConfiguration = Map("ehcacheplugin" -> "disabled")
    )
   */
  "Person Controller" should {

    "send 404 on a bad request" in new WithApplication() {
      val result = route(FakeRequest(GET, "/dummy"))
      status(result.get) must equalTo(NOT_FOUND)
    }

    "create a new record" in new WithApplication() {
      val obj = Json.obj()
      val input:JsValue = obj + ("firstName" -> JsString("pawan")) + ("lastName" -> JsString("kumar")) + ("fullName" -> JsString("pawan kumar")) + ("birthday" -> JsString(new Date().toString))
      //println(input.toString())
      val uri = controllers.routes.PersonController.create().url
      //println("Uri:" + uri)
      val fakeRequest = FakeRequest(method = Helpers.POST,uri = uri, headers = FakeHeaders(Seq("Content-type"->"application/json")), body = input)
      val result = route(fakeRequest)
      result must not be(None)
      val f = result.get
      //println("RESULT:" + contentAsString(f))
      status(f) must equalTo(CREATED)
      //println(f)
      //(contentAsJson(f) \\ "firstName") must contain("pawan")
      contentType(f) must beSome("application/json")
    }

    "get all records" in new WithApplication() {
      val result = route(FakeRequest(GET, "/api/v1/persons"))
      result must not be(None)
      val f = result.get
      status(f) must equalTo(OK)
      contentType(f) must beSome("application/json")
      val resultString = contentAsString(f)
      //println(resultString)
      resultString must contain ("test")
    }
  }
}
