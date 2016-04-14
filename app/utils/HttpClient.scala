package utils

import com.google.inject.{Inject, Singleton}
import play.api.Logger
import play.api.libs.json.Json
import play.api.libs.ws.{WSClient, WSRequest, WSResponse}

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

/**
  * Class for making HTTP requests
  * https://playframework.com/documentation/2.5.x/ScalaWS
  */

@Singleton
class HttpClient @Inject() (implicit ec: ExecutionContext, ws: WSClient) {

  def demo = {
    val request: WSRequest = ws.url("https://github.com/")
        .withRequestTimeout(10000.millis)
        .withHeaders("Accept" -> "application/json")
        .withQueryString("param_X" -> "value_Y")

    val data1 = Json.obj("key1" -> "value1", "key2" -> "value2")
    val data2 = Map("key1" -> Seq("value1"), "key2" -> Seq("value2"))

    Logger.debug("Request: " + request.uri + " --- \n" + request)

    val futureResponse: Future[WSResponse] = request.post(data2)

    /* Make it synchronous (wait for the result) */
    val result:WSResponse = Await.result(futureResponse, 10.seconds)
    result.body
  }
}
