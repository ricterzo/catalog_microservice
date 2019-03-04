package com.umana.corso.catalogue.domain.repository

import com.umana.corso.catalogue.domain.model.Movie

import scala.concurrent.Future

trait MovieRepository {



  def getMovieList(): Future[Seq[Movie]]

  def getMovieListByTitle(title:String): Future[Seq[Movie]]

}
