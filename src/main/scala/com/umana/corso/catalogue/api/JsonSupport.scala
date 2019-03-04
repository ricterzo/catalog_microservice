package com.umana.corso.catalogue.api
import com.umana.corso.catalogue.api.model.ErrorCode
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.umana.corso.catalogue.domain.model.Movie
import spray.json.DefaultJsonProtocol
import com.umana.corso.catalogue.api.model.NewMovie
/**
  * Definisce i json marshallers per ogni classe che viene esposta dalle api
  */
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val newMovieJsonFormat = jsonFormat5(NewMovie)
  implicit val movieJsonFormat = jsonFormat6(Movie)
  implicit val errorCodeJsonFormat = jsonFormat1(ErrorCode)
}
