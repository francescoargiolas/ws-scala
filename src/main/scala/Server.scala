import akka.Done

import scala.concurrent.{Future, Promise}
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import model.{Books, User}
import repo.{Book, LibraryDb, UserRepo}
import utils.Common
import akka.stream.scaladsl.Source
import akka.util.ByteString
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.util.{Failure, Random, Success, Try}
import scala.io.StdIn
import adapter.Client
import org.squeryl.PrimitiveTypeMode._
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.io.StdIn

object Server extends App with Common {

  val numbers = Source.fromIterator(() =>
    Iterator.continually(Random.nextInt()))

  val route = pathPrefix("ws") {
    concat(
      get {
        concat(
            path("static") {

              println("static")

              val p = Promise[Books]()
              inTransaction {

                val books = from(LibraryDb.books)(select(_))
                for (book <- books) {
                  println(book)
                }

                p.complete( Try {
                  Books(books.toSeq)
                })

              }

              complete( p.future )

            },
            path("client") {

              println("client ")
              //complete( Client.execute )

              Client.execute
              complete("Done")

            },
            path("random") {
              complete(
                HttpEntity(
                ContentTypes.`text/plain(UTF-8)`,
                // transform each number to a chunk of bytes
                //numbers.map(n => ByteString(s"$n\n"))
                 "jhgjhgjh"
              ))
            },
            pathPrefix( """\w+""".r ) { username =>
              // there might be no item for a given id
              val maybeItem: Future[Option[User]] = UserRepo.getUser(username)

              onSuccess(maybeItem) {
                case Some(item) => complete(item)
                case None       => complete("user "+username+" not found")
              }
            }
        )
      },
      post {
        concat(
          path( "user" ) {
            entity(as[User]) { user =>
              println("adding " + user)
              val saved: Future[Done] = UserRepo.addUser(user)
              onComplete(saved) { done =>
                complete("user created")
              }
            }
          }
        )
      }
    )
  }

  val bindingFuture = Http().bindAndHandle(route, config.getString("http.interface"), config.getInt("http.port"))

  logger.info("+++Server start ")
  logger.debug("+++Server start debug")
  //println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done

}
