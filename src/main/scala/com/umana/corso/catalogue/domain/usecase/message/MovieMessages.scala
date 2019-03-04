package com.umana.corso.catalogue.domain.usecase.message

import com.umana.corso.catalogue.domain.exception.{CreateMovieException, DeleteMovieByIdException}
import com.umana.corso.catalogue.domain.model.Movie

/**
  * Classe contenente i messaggi che sa gestire lo UserActor
  */
object MovieMessages {

  case class CreateMovie(producer:String,title:String,duration:Int,price:Double,category:String)

  case class CreateMovieResponse(result:Either[CreateMovieException,Unit])

  case class GetMovieList()

  case class GetMovieListResponse(result: Seq[Movie])

  case class GetMovieById(title:String)

  case class GetMovieByIdResponse(result: Option[Movie])

  case class GetMovieListByTitle(title:String)

  case class GetMovieListByTitleResponse(result: Seq[Movie])

  case class GetMovieListByCategory(category:String)

  case class GetMovieListByCategoryResponse(result: Seq[Movie])

  case class GetMovieListByProducer(producer:String)

  case class GetMovieListByProducerResponse(result: Seq[Movie])

  case class DeleteMovieById(id: String)

  case class DeleteMovieByIdResponse(result: Either[DeleteMovieByIdException, Unit])
  //endregion

}
