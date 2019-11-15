import java.sql.DriverManager

import org.squeryl.adapters.{H2Adapter, PostgreSqlAdapter}
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.{KeyedEntity, Schema, Session, SessionFactory}
import repo.Book

object DbTest extends App {

  def initDatabaseConnection(): Unit ={

    /*
    Class.forName("org.postgresql.Driver");
    SessionFactory.concreteFactory = Some(() =>
      Session.create(
        DriverManager.getConnection("jdbc:postgresql://localhost:5432/library", "cdst", "cdst"),
        new PostgreSqlAdapter)
    )*/


        Class.forName("org.h2.Driver");
        SessionFactory.concreteFactory = Some(() =>
          Session.create(
            DriverManager.getConnection("jdbc:h2:~/test", "sa", ""),
            new H2Adapter)
        )

    /*
    Class.forName("org.h2.Driver");
    val session = Session.create(
      java.sql.DriverManager.getConnection("jdbc:h2:~/test", "sa", ""),
      new H2Adapter
    )*/

  }

  import repo.LibraryDb

  initDatabaseConnection();

  try {


    transaction{
      println("insert")
      LibraryDb.printDdl
      //LibraryDb.drop
      //LibraryDb.create
      //LibraryDb.books.insert( Book("title", "description", "author") )

    }

    /*
    transaction{
      println("select")
      val books = from(LibraryDb.books)(select(_))
      println(books.size)
      for (book <- books) {
        println(book)
      }
    }*/

  }
  catch {
    case e:Exception => {
      println(e)
    }
  }

}
