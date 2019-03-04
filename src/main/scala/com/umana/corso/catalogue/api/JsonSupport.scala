package com.umana.corso.catalogue.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.umana.corso.catalogue.domain.model.Movie
import spray.json.DefaultJsonProtocol

/**
  * Definisce i json marshallers per ogni classe che viene esposta dalle api
  */
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {


  implicit val movieJsonFormat = jsonFormat6(Movie)

}
