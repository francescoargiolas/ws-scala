package repo
import org.squeryl.annotations.Column
import java.util.Date
import java.sql.Timestamp
import java.util.logging.LogManager

import org.squeryl.{KeyedEntity, PrimitiveTypeMode, Schema, Session}
import org.squeryl.PrimitiveTypeMode._
import org.squeryl.dsl.ManyToOne

case class Author(override val id: Int, var firstName: String, var lastName: String, var email: Option[String]) extends LibraryDbObject {
  def this() = this(0,"","",Some(""))
  def this(firstName: String, lastName: String, email: Option[String]) = this(0,firstName,lastName,email)

}

class LibraryDbObject extends KeyedEntity[Int] {
  val id: Int = 0
  var timeOfLastUpdate = new Timestamp(System.currentTimeMillis)
}

case class Book(override val id: Int, var title: String, var description: String, var authorId: Int ) extends LibraryDbObject{
def this() = this(0, "","",0)
def this(title: String, description: String, authorId: Int) = this(0, title, description, authorId)

  //lazy val author: ManyToOne[Author] =  LibraryDb.authorsToBooks.right(this)
}

object LibraryDb extends Schema {
  val authors = table[Author]
  val books = table[Book]

  val authorsToBooks = oneToManyRelation(authors, books).via((a,b) => a.id === b.authorId)

/*
override def drop = {
  Session.cleanupResources
  super.drop
}*/

}



