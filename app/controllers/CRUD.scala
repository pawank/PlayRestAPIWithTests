package controllers

import models.{AppBaseModel, PK}
import org.joda.time.{LocalDateTime,DateTime}
import play.api.libs.json._
import models.common._
import play.api.mvc.{AnyContent, Result, Controller, Action}
import tables.{AppDbContract}


import javax.inject._


/**
 * CRUD and other related actions for `Controller` with desired output type e.g. JsValue, Html etc
 * @tparam OT
 */
trait CRUDActions[OT,PK] extends Controller{
  import utils.json.JsonReadsWrites.commonlyUsedJsonFormats
  /**
   * POST: Create / save an incoming object as Json
   * @return JSON of input
   */
  def create: Action[OT]

  /**
   * GET: Get the object represented by incoming 'id'
   * @param id
   * @return JSON of object
   */
  def get(id: PK): Action[AnyContent]

  /**
   * PUT: Update (partially / fully) incoming object (as Json) with the object available with 'id' in the data storage system
   * @param id
   * @return Updated JSON of object
   */
  def update(id: PK): Action[OT]

  /**
   * DELETE: Delete an object represented by incoming 'id'
   * @param id
   * @return Status of delete action
   */
  def delete(id: PK): Action[AnyContent]

  /**
   * GET: Find records and filter based on pageIndex and limit as input query parameters
   * @return JSON array of objects
   */
  def getAll: Action[AnyContent]

  /**
   * POST: Filter results based on parameters from incoming request as JSON
   * @return JSON array of objects
   */
  def search:Action[OT]

  /**
   * Default Json success message
   * @param message
   * @return
   */
  def Js200(message:String = "Success") = Json.toJson(AppMessage(code = 200, message = message))

  /**
   * Default bad request Json message
   * @param message
   * @return
   */
  def Js400(message:String = "BadRequest") = Json.toJson(AppMessage(code = 400, message = message))

  /**
   * Default Json error message
   * @param message
   * @return
   */
  def Js500(message:String = "Error") = Json.toJson(AppMessage(code = 500, message = message))

  /**
   * Default Json error message
   * @param message
   * @return
   */
  def JsX(code:Int, message:String) = Json.toJson(AppMessage(code = code, message = message))
}

  /**
  Points to consider while returning http status codes for RESTful requests:

    DELETE

  200 (if your want send some additional data in the Response) or 204 (recommended).

  202 Operation deleted has not been committed yet.

  If there's nothing to delete, use 204 or 404 (DELETE operation is idempotent, delete an already deleted item is operation sucessful, so you can return 204, but it's true that idempotent doesn't implies necessarily the response)

  Other errors:
    400 Bad Request (Malformed syntax or a bad query is strange but possible).
    401 Unauthorized
    403 Forbidden: Authentication failure or invalid Application ID.
    405 Not Allowed. Sure.
    409 Resource Conflict can be possible in complex systems.
    501, 502 in case of errors.
  PUT

  If you're updating an element of a collection

  200/204 with the same reasons as DELETE above.
  202 if the operation has not been commited yet.
    The referenced element doesn't exists:

    PUT can be 201 (if you created the element because that is your behaviour)
  404 If you don't want to create elements via PUT.

  400 Bad Request (Malformed syntax or a bad query more common than in case of DELETE).

  401 Unauthorized
  403 Forbidden: Authentication failure or invalid Application ID.
  405 Not Allowed. Sure.
  409 Resource Conflict can be possible in complex systems, as in DELETE.
    And 501, 502 in case of errors.
  */

  abstract class CrudController[E <: AppBaseModel] @Inject() (dbContract:AppDbContract[E]) extends CRUDActions[JsValue,PK] {
    import scala.concurrent.ExecutionContext.Implicits.global
    import utils.json.JsonReadsWrites._

    implicit def reader: Reads[E]
    implicit def writer: Writes[E]
    implicit def readerForList: Reads[List[E]]
    implicit def writerForList: Writes[List[E]]
    /*
    implicit val reader: Reads[E] = implicitly[Reads[E]]
    implicit val writer: Writes[E] = implicitly[Writes[E]]
    implicit val readerForList: Reads[List[E]] = implicitly[Reads[List[E]]]
    implicit val writerForList: Writes[List[E]] = implicitly[Writes[List[E]]]
    */
    def create = Action.async(parse.json) { implicit request =>
        scala.concurrent.Future {
          println(s"create:${request.body}")
          Json.fromJson[E](request.body)(reader).map { entity =>
            val persistedEntity: E = {
              println("Creating entity: " + entity)
              dbContract.insertRecord(entity).get
            }
            Created(Json.toJson(persistedEntity)(writer))
          }.recoverTotal { e => BadRequest(JsError.toJson(e)) }
        }
    }

    def bulkCreate = Action.async(parse.json) { implicit request =>
      scala.concurrent.Future {
        println(s"bulkCreate:${request.body}")
        Json.fromJson[List[E]](request.body)(readerForList).map { entities =>
          println(entities)
          val persistedEntityList:List[E] = entities.map(entity => {
            println("Creating entity: " + entity)
            dbContract.insertRecord(entity).get
          })
          println("Bulk create done")
          Created(Json.toJson(persistedEntityList)(writerForList))
        }.recoverTotal { e => BadRequest(JsError.toJson(e)) }
      }
    }

    def findById(id:PK):Option[E] = dbContract.getRecord(id)

    def deleteById(id:PK):Int = if (dbContract.deleteRecordById(id)) 1 else 0

    def getAbstract(id:PK)(f: Any => Result) = Action.async(parse.json) { implicit request =>
        scala.concurrent.Future {
          val obj:Option[E] = this.findById(id)
          f(obj)
      }
    }

    def get(id:PK) = Action.async { implicit request =>
        scala.concurrent.Future {
          val obj:Option[E] = this.findById(id)
          if (obj.isDefined) {
             Ok(Json.toJson(obj.get)(writer))
          } else {
            NotFound(JsX(NOT_FOUND,"Object for the given ID not found"))
          }
        }
    }


    def getAll = Action.async { implicit request =>
        scala.concurrent.Future {
          val skip = request.queryString.get("skip").flatMap(_.headOption.map(_.toInt)).getOrElse(0)
          val pageSize = request.queryString.get("pageSize").flatMap(_.headOption.map(_.toInt)).getOrElse(0)
          println(dbContract != null)
          val records:List[E] = dbContract.finalAllRecords()
          println(records)
          Ok(Json.toJson(records)(writerForList))
        }
    }

    def search = Action.async(parse.json) { implicit request =>
      scala.concurrent.Future {
        val records:List[E] = dbContract.finalAllRecords()
        Ok(Json.toJson(records)(writerForList))
      }

    }

    def delete(id:PK) = Action.async { implicit request =>
        scala.concurrent.Future {
          val deletedId = this.deleteById(id)
          if (deletedId >= 0) {
            Ok(Json.obj("id" -> deletedId))
          } else {
            NotFound(JsX(NOT_FOUND,"Object for the given ID not found"))
          }
        }

    }

    def update(id:PK) = Action.async(parse.json) { implicit request =>
        scala.concurrent.Future {
          println(s"update:${request.body}")
          this.findById(id) match {
            case Some(existingEntity) =>
              Json.fromJson[E](request.body)(reader).map { entity =>

                  Ok(Json.toJson(entity)(writer))

              }.recoverTotal { e => BadRequest(JsError.toJson(e)) }
            case _ =>
              NotFound(JsX(NOT_FOUND,"Object for the given ID not found"))
          }
        }

    }
  }