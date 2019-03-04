package com.umana.corso.catalogue.api

import akka.actor.ActorRef
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import com.umana.corso.catalogue.domain.model.Movie
import com.umana.corso.catalogue.domain.usecase.message.MovieMessages._

import scala.concurrent.duration.DurationDouble

/**
  * Definisce gli endpoint delle nostri api
  */
trait RestInterface extends JsonSupport {

  val userActor: ActorRef

  private implicit val timeout: Timeout = Timeout(5.seconds)

  lazy val userRoutes: Route = pathPrefix("catalog") {
    pathPrefix("movie") {
    concat(
        pathEnd {
          get {
            val response = userActor ? GetMovieList()
            onSuccess(response) {
              case GetMovieListResponse(movieList) => complete(StatusCodes.OK, movieList)
              case _ => complete(StatusCodes.InternalServerError)
            }
          }
        },
        path(Segment) {
          title =>
            // GET /catalog/$TITLE
            get {
              val response = userActor ? GetMovieListByTitle(title)
              onSuccess(response) {
                case GetMovieListByTitleResponse(movieList) => complete(StatusCodes.OK, movieList)
                case _ => complete(StatusCodes.InternalServerError)
              }
            }

      }
    )
  }
  }
  }


