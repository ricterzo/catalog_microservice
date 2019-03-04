package com.umana.corso.catalogue.domain.repository

import com.umana.corso.catalogue.domain.model.Movie

import scala.concurrent.Future

trait MovieRepository {

  def getMovieList(): Future[Seq[Movie]]
  def getMovieById(id:String): Future[Option[Movie]]
  def getMovieListByTitle(title:String): Future[Seq[Movie]]
  def getMovieListByCategory(category:String): Future[Seq[Movie]]
  def getMovieListByProducer(producer:String): Future[Seq[Movie]]
  def deleteMovieById(id:String):Future[Unit]
  def insertMovie(producer:String,title:String,duration:Int,price:Double,category:String):Future[Unit]
}
