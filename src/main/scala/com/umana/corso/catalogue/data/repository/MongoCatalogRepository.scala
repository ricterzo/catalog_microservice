package com.umana.corso.catalogue.data.repository

import java.util.UUID

import akka.actor.ActorSystem
import com.umana.corso.catalogue.domain.model.Movie
import com.umana.corso.catalogue.domain.repository.MovieRepository
import org.mongodb.scala.bson.collection.immutable.Document
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase, MongoWriteException}

import scala.concurrent.{ExecutionContext, Future}

class MongoCatalogRepository(system: ActorSystem, url: String) extends MovieRepository {

  private val KEY_ID = "_id"
  private val KEY_PRODUCER = "producer"
  private val KEY_TITLE = "title"
  private val KEY_DURATION = "duration"
  private val KEY_PRICE = "price"
  private val KEY_CATEGORY = "category"

  private implicit val executionContext: ExecutionContext = system.dispatchers.lookup("mongodb-dispatcher")

  private val mongoClient: MongoClient = MongoClient(url)
  private val database: MongoDatabase = mongoClient.getDatabase("corso")
  private val collection: MongoCollection[Document] = database.getCollection("movies")


  override def getMovieList(): Future[Seq[Movie]] = {
    collection.find()
      .toFuture()
      .map { documentList =>
        documentList.map(mapToMovie)
      }
  }

  private def mapToMovie(document: Document): Movie = Movie(
    document.getString(KEY_ID),
    document.getString(KEY_PRODUCER),
    document.getString(KEY_TITLE),
    document.getInteger(KEY_DURATION),
    document.getDouble(KEY_PRICE),
    document.getString(KEY_CATEGORY),
  )

}