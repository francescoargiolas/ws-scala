import akka.Done
import akka.actor.ActorSystem
import akka.http.javadsl.server.PathMatcher1
import akka.http.scaladsl.Http
import akka.http.scaladsl.coding.Deflate
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.http.scaladsl.server.Directives._

import scala.concurrent.{ExecutionContext, Future}
import scala.io.StdIn
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import model.User
import repo.UserRepo
import spray.json.DefaultJsonProtocol._

object Server extends App {

  implicit val system: ActorSystem = ActorSystem("helloworld")
  implicit val executionContext: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  implicit val itemFormat = jsonFormat2(User)

  /*
  val route = concat {
    get {
      pathPrefix("getUser" / """\w+""".r) { username =>
        // there might be no item for a given id
        println("++ Call: user/"+username)
        val maybeItem: Future[Option[User]] = UserRepo.getUser("sss")

        onSuccess(maybeItem) {
          case Some(item) => complete(item)
          case None       => complete(StatusCodes.NotFound)
        }
      }
    }
    post {
      path("user") {
        entity(as[User]) { user =>
          println("adding "+user)
          val saved: Future[Done] = UserRepo.addUser(user)
          onComplete(saved) { done =>
            complete("user created")
          }
        }
      }
    }
  }*/

  val route = path("user") {
    concat(
      get {
        complete("get")
      },
      post {
        complete("post")
      }
    )
  }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done

}
