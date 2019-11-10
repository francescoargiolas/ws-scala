package repo

import akka.Done
import model.User

import scala.concurrent.ExecutionContext
import ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.util.Try

object UserRepo {

  var users: Array[User] = Array[User]()

  def getUser(username: String): Future[Option[User]] =
    Future{
      val uList = users.filter( _.username == username )
      if(uList.size > 0 ) Option(uList(0)) else Option.empty
    }


  def addUser(user: User) = Future{
    users :+= user
    Done
  }

}
