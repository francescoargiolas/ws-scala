package utils

import java.sql.DriverManager

import akka.actor.ActorSystem
import akka.event.Logging
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import model.{AuthorJson, Authors, BookJson, Books, User}
import org.squeryl.adapters.{H2Adapter, PostgreSqlAdapter}
import org.squeryl.{PrimitiveTypeMode, Session, SessionFactory}
import repo.{Author, Book, LibraryDb}
import spray.json.DefaultJsonProtocol._

import scala.concurrent.ExecutionContext

trait Common{

    implicit val system: ActorSystem = ActorSystem("helloworld")
    implicit val executionContext: ExecutionContext = system.dispatcher
    implicit val materializer: ActorMaterializer = ActorMaterializer()

    ///define unmarshaller
    implicit val itemFormat = jsonFormat2(User)
    implicit val authorFormat = jsonFormat4(Author)
    implicit val bookFormat = jsonFormat4(Book)
    implicit val bookJsonFormat = jsonFormat3(BookJson)
    implicit val booksFormat = jsonFormat1(Books)
    implicit val authorJsonFormat = jsonFormat3(AuthorJson)
    implicit val authorsFormat = jsonFormat1(Authors)

    val config = ConfigFactory.load()
    def logger = system.log // Logging(system, getClass)

    def initDatabaseConnection(): Unit ={
        Class.forName("org.h2.Driver");
        SessionFactory.concreteFactory = Some(() =>
            Session.create(
                DriverManager.getConnection("jdbc:h2:~/test", "sa", ""),
                new H2Adapter)
        )

    }

    initDatabaseConnection()

    /*
    akka {
  event-handlers = ["akka.event.slf4j.Slf4jEventHandler"]
  loglevel = DEBUG
}
per la configurazione con il log di akka
     */
    //def getLogger() = Logging(system, getClass)
}
