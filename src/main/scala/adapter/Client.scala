package adapter

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import utils.Common

import scala.concurrent.{Future, Promise}
import scala.util.{Failure, Success, Try}
import akka.http.scaladsl.unmarshalling._

case class UserCustom(userId: Int, id: Int, title: String, completed: Boolean)

import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import spray.json.DefaultJsonProtocol._

object Client extends Common{
  def execute = {

    //val promise = Promise[Option[ResponseEntity]]()
    /*val request = HttpRequest(
      method = HttpMethods.POST,
      uri = "https://userservice.example/users",
      entity = HttpEntity(ContentTypes.`text/plain(UTF-8)`, "data")
    )*/
    val responseFuture: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = "https://jsonplaceholder.typicode.com/todos/1"))

    implicit val superCustomFormat = jsonFormat4(UserCustom)

    responseFuture
      .onComplete {
        case Success(res) => val userCustom = Unmarshal(res.entity).to[UserCustom]; userCustom.map( println(_))
        case Failure(_)   => sys.error("something wrong")
      }
    /*
    println("executed http request")
    responseFuture
      .onComplete {
        case Success(value) => {
          println("response: ")
          promise.complete( Try( Option(value.entity) ) )
        }
        case Failure(_) => promise.complete(_)
      }

    promise.future*/
  }
}
