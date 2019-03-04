package com.umana.corso.catalogue.api

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.umana.corso.catalogue.api.model.{ErrorCode, NewMovie}
import com.umana.corso.catalogue.domain.exception.{MissingIdException, MovieAlreadyExistsException}
import com.umana.corso.catalogue.domain.model.Movie
import com.umana.corso.catalogue.domain.usecase.message.MovieMessages._

import scala.concurrent.duration.DurationDouble

/**
  * Definisce gli endpoint delle nostri api
  */
trait RestInterface extends JsonSupport {

  val movieActor: ActorRef

  private implicit val timeout: Timeout = Timeout(5.seconds)

  lazy val userRoutes: Route = pathPrefix("catalog") {
    pathPrefix("movie") {
      concat(
        pathEnd {
          concat(
            post {
              entity(as[NewMovie]) { movie =>
                val response = movieActor ? CreateMovie(movie.producer, movie.title, movie.duration,movie.price,movie.category)
                onSuccess(response) {
                  case CreateMovieResponse(Right(())) => complete(StatusCodes.Created)
                  case CreateMovieResponse(Left(_: MovieAlreadyExistsException)) => complete(StatusCodes.BadRequest, ErrorCode("MovieAlreadyExists"))
                  case _ => complete(StatusCodes.InternalServerError)
                }
              }
            },
            get {
              concat(
                parameters("title") { (title) =>
                  val response = movieActor ? GetMovieListByTitle(title)
                  onSuccess(response) {
                    case GetMovieListByTitleResponse(movieList) => complete(StatusCodes.OK, movieList)
                    case _ => complete(StatusCodes.InternalServerError)
                  }
                },
                parameters("category") { (category) =>
                  val response = movieActor ? GetMovieListByCategory(category)
                  onSuccess(response) {
                    case GetMovieListByCategoryResponse(movieList) => complete(StatusCodes.OK, movieList)
                    case _ => complete(StatusCodes.InternalServerError)
                  }
                },
                parameters("producer") { (producer) =>
                  val response = movieActor ? GetMovieListByProducer(producer)
                  onSuccess(response) {
                    case GetMovieListByProducerResponse(movieList) => complete(StatusCodes.OK, movieList)
                    case _ => complete(StatusCodes.InternalServerError)
                  }

                },

                {
                  val response = movieActor ? GetMovieList()
                  onSuccess(response) {
                    case GetMovieListResponse(movieList) => complete(StatusCodes.OK, movieList)
                    case _ => complete(StatusCodes.InternalServerError)
                  }
                }
              )
            }
          )
        }
        , path(Segment) {
          ID =>
            concat(
              // GET /catalog/movie/$ID
              get {
                val response = movieActor ? GetMovieById(ID)
                onSuccess(response) {
                  case GetMovieByIdResponse(movie) => complete(StatusCodes.OK, movie)
                  case _ => complete(StatusCodes.InternalServerError)
                }
              },
              delete {
                val response = movieActor ? DeleteMovieById(ID)
                onSuccess(response) {
                  case DeleteMovieByIdResponse(Right(())) => complete(StatusCodes.OK)
                  case DeleteMovieByIdResponse(Left(_: MissingIdException)) => complete(StatusCodes.NotFound, ErrorCode("InvalidIdException"))
                  case _ => complete(StatusCodes.InternalServerError)
                }
              }
            )
        }
      )
    }
  }
}


