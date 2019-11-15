package repo

import akka.Done
import model.User
import utils.Common
import scala.concurrent.Future

object UserRepo extends Common{

  var users: Array[User] = Array[User]()

  def getUser(username: String): Future[Option[User]] =
    Future{
      logger.info("search user "+username)
      val uList = users.filter( _.username == username )
      if(uList.size > 0 ) Option(uList(0)) else Option.empty
    }


  def addUser(user: User) = Future{
    users :+= user
    Done
  }

}
