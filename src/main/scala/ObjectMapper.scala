import model.{AuthorJson, BookJson}
import repo.{Author, Book}

object ObjectMapper {

  def bookJsonToBook(in: BookJson): Book = {
    new Book( title = in.title, description = in.description, authorId = in.author)
  }

  def authorJsonToAuthor(in: AuthorJson): Author = {
    new Author( firstName = in.firstName, lastName = in.lastName, email = in.email)
  }


}
