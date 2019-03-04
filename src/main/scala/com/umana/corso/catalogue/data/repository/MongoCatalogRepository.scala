package com.umana.corso.catalogue.data.repository
import org.mongodb.scala.model.Filters._
import java.util.UUID

import com.umana.corso.catalogue.domain.exception.{DeleteMovieByIdException, MissingIdException, MovieAlreadyExistsException}
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
  override def getMovieById(id:String): Future[Option[Movie]] = {
    collection.find(Document(KEY_ID->id))
      .headOption()
      .map {
        _.map(mapToMovie)
      }
  }

  override def getMovieListByTitle(titleToFind:String): Future[Seq[Movie]] = {
    collection.find(regex(KEY_TITLE, "^"+titleToFind))
      .toFuture()
      .map { documentList =>
        documentList.map(mapToMovie)
      }
  }

  override def getMovieListByCategory(categoryToFind: String): Future[Seq[Movie]] = {
    collection.find(regex(KEY_CATEGORY, "^"+categoryToFind))
      .toFuture()
      .map { documentList =>
        documentList.map(mapToMovie)
      }
  }
  override def getMovieListByProducer(producerToFind: String): Future[Seq[Movie]] = {
    collection.find(regex(KEY_PRODUCER, "^"+producerToFind))
      .toFuture()
      .map { documentList =>
        documentList.map(mapToMovie)
      }
  }

  override def deleteMovieById(id: String): Future[Unit] = {
    collection.deleteOne(
      Document(KEY_ID -> id)
    ).toFuture()
      .flatMap { response =>
        if (response.getDeletedCount == 0) Future.failed(new (MissingIdException))
        else Future.successful(Unit)
      }
  }

  override def insertMovie(producer:String,title:String,duration:Int,price:Double,category:String): Future[Unit] = {
    collection.insertOne(
      Document(
        KEY_ID -> UUID.randomUUID().toString,
        KEY_PRODUCER -> producer,
        KEY_TITLE -> title,
        KEY_DURATION -> duration,
        KEY_PRICE -> price,
        KEY_CATEGORY -> category
      )
    ).toFuture()
      .recoverWith {
        case ex: MongoWriteException if ex.getCode == 11000 => Future.failed(new MovieAlreadyExistsException())
        case ex => Future.failed(ex)
      }
      .map { _ => Right(Unit) }
  }


  private def mapToMovie(document: Document): Movie = Movie(
    document.getString(KEY_ID),
    document.getString(KEY_PRODUCER),
    document.getString(KEY_TITLE),
    document.getInteger(KEY_DURATION),
    BigDecimal(document.getDouble(KEY_PRICE)),
    document.getString(KEY_CATEGORY),
  )


}