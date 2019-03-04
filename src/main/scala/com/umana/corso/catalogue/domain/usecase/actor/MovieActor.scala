package com.umana.corso.catalogue.domain.usecase.actor

import akka.actor.{Actor, Props}
import akka.pattern.pipe
import akka.util.Timeout
import com.umana.corso.catalogue.domain.exception.{CreateMovieException, DeleteMovieByIdException}
import com.umana.corso.catalogue.domain.repository.MovieRepository
import com.umana.corso.catalogue.domain.usecase.message.MovieMessages._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration.DurationInt

/**
  * Definisce gli use case riguardanti gli users:
  * - creazione di un nuovo (ask msg: @see CreateUser response: @see CreateUserResponse)
  * - ricerca di uno user per id (ask msg: @see GetUserById response: @see GetUserByIdResponse)
  * - ricerca di uno user date le credenziali (ask msg: @see GetUserByCredential response: @see GetUserByCredentialResponse)
  * - rimuove uno user per id (ask msg: @see DeleteUserById response: @see DeleteUserByIdResponse)
  * - ritorna la lista di tutti gli utenti (ask msg: @see GetUserList response: @see GetUserListResponse)
  *
  * @param userRepository
  */
class MovieActor(movieRepository: MovieRepository) extends Actor {

  private implicit val executionContext: ExecutionContext = context.system.dispatcher
  private implicit val timeout: Timeout = Timeout(5.seconds)

  override def receive: Receive = {



    case GetMovieList() =>
      movieRepository.getMovieList()
        .map(movieList => GetMovieListResponse(movieList))
        .pipeTo(sender())

    case GetMovieById(id) =>
      movieRepository.getMovieById(id)
        .map(movie => GetMovieByIdResponse(movie))
        .pipeTo(sender())

    case GetMovieListByTitle(title) =>
      movieRepository.getMovieListByTitle(title)
        .map(movieList => GetMovieListByTitleResponse(movieList))
        .pipeTo(sender())

    case GetMovieListByCategory(title) =>
      movieRepository.getMovieListByCategory(title)
        .map(movieList => GetMovieListByCategoryResponse(movieList))
        .pipeTo(sender())

    case GetMovieListByProducer(producer) =>
      movieRepository.getMovieListByProducer(producer)
        .map(movieList => GetMovieListByProducerResponse(movieList))
        .pipeTo(sender())

    case DeleteMovieById(id) =>
      movieRepository.deleteMovieById(id)
        .map(_ => DeleteMovieByIdResponse(Right(Unit)))
        .recover {
          case e: DeleteMovieByIdException => DeleteMovieByIdResponse(Left(e))
        }
        .pipeTo(sender())

    case CreateMovie(producer,title,duration,price,category) =>
      movieRepository
        .insertMovie(producer,title,duration,price,category)
        .map(_ => CreateMovieResponse(Right(Unit)))
        .recover {
          case e: CreateMovieException => CreateMovieResponse(Left(e))
        }
        .pipeTo(sender())

  }
}

object MovieActor {
  def props(userRepository: MovieRepository): Props = Props(classOf[MovieActor], userRepository)
}
