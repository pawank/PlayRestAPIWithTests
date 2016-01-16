package controllers

import javax.inject._
import dao.PersonContract
import orientdb.Db
import play.api.libs.json.{Writes, Reads}
import scala.concurrent.Future
import models.{Person}
import play.api._
import play.api.cache._
import play.api.mvc._


class PersonController @Inject() (cache: CacheApi, db:Db, dbContract:PersonContract) extends CrudController[Person](dbContract) {
  implicit val reader: Reads[Person] = utils.json.JsonReadsWrites.personFormats
  implicit val writer: Writes[Person] = utils.json.JsonReadsWrites.personFormats
  implicit val readerForList: Reads[List[Person]] =  utils.json.JsonReadsWrites.personListReadFormat
  implicit val writerForList: Writes[List[Person]] = utils.json.JsonReadsWrites.personListWriteFormat

  def index = Action.async {
    Future.successful(Ok("Persons").as("text/plain"))
  }
}
