  import adapter.Client
  import akka.Done
  import akka.http.scaladsl.Http
  import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
  import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
  import akka.http.scaladsl.server.Directives._
  import akka.stream.scaladsl.Source
  import model.{AuthorJson, Authors, BookJson, Books, User}
  import org.squeryl.PrimitiveTypeMode._
  import repo.{Author, Book, LibraryDb, UserRepo}
  import utils.Common

  import scala.concurrent.{Future, Promise}
  import scala.io.StdIn
  import scala.util.{Failure, Random, Success, Try}

  object LibraryServer extends App with Common {

    val route = pathPrefix("library") {
      concat(
        get {
          concat(
            pathPrefix("books") {
              concat(
                  path("") {
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

                  onSuccess(p.future){
                      case value : Books => complete( value )
                      case _ => complete( Books(Seq[Book]()) )
                  }

              },
                //path( """\d+""".r ) { id =>
                path( IntNumber ) { id =>

                  val p = Promise[Book]()
                  inTransaction {

                    val books = from(LibraryDb.books)(s =>
                          where(s.id === id)
                          select(s)
                    )

                    p.complete( Try {
                      books.toSeq(0)
                    })

                  }

                  onComplete(p.future){
                    case Success(value) => complete( value )
                    case Failure(exception) => complete( StatusCodes.NotFound)
                  }

                }
              )
            },
            pathPrefix("authors") {
              concat(
                path("") {
                  val p = Promise[Authors]()
                  inTransaction {

                    p.complete( Try {
                      Authors(from(LibraryDb.authors)(select(_)).toSeq)
                    })

                  }

                  onSuccess(p.future){
                    case value : Authors => complete( value )
                    case _ => complete( Authors(Seq[Author]()) )
                  }

                },
                path( IntNumber ) { id =>

                  val p = Promise[Author]()
                  inTransaction {

                    p.complete( Try {
                      LibraryDb.authors.get(id)
                    })

                  }

                  onComplete(p.future){
                    case Success(value) => complete( value )
                    case Failure(exception) => complete( StatusCodes.NotFound)
                  }

                }
              )
            }
          )
        },
        post {
          concat(
            path( "books" ) {
              entity(as[BookJson]) { book =>

                inTransaction {
                  LibraryDb.books.insert( ObjectMapper.bookJsonToBook(book))
                }

                complete(Done)
              }
            },
            path( "authors" ) {
              entity(as[AuthorJson]) { author =>

                inTransaction {
                  LibraryDb.authors.insert( ObjectMapper.authorJsonToAuthor(author))
                }

                complete(Done)
              }
            }
          )
        },
        delete {
          concat(
            path( "books" / IntNumber ) { id =>

              inTransaction {
                LibraryDb.books.delete(id)
              }

              complete(Done)
            }
          )
        },
        put {
          concat(
            path( "books" / IntNumber ) { id =>

              entity(as[BookJson]) { bookJson =>

                inTransaction {
                  val book = LibraryDb.books.get(id)
                  book.title = bookJson.title
                  book.description = bookJson.description
                  book.authorId = bookJson.author
                  //book.author = LibraryDb.authors.get(bookJson.author)
                  //val newBook = Book(id, book.title, book.description, book.author)
                  //println(book)
                  LibraryDb.books.insertOrUpdate( book )
                }

                complete(Done)
              }

            }
          )
        }
      )
    }

    val bindingFuture = Http().bindAndHandle(route, config.getString("http.interface"), config.getInt("http.port"))

    logger.info("+++Library Server start ")
    //println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done

  }
