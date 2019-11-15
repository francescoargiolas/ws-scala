package model

import repo.{Author, Book}

case class User(username: String, password: String)

case class BookJson(val title: String, val description: String, val author: Int)
case class AuthorJson(val firstName: String, val lastName: String, val email: Option[String])
case class Books(val books: Seq[Book])
case class Authors(val authors: Seq[Author])