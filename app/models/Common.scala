package models.common

import java.net.URL

import models._
import org.joda.time.DateTime
import scala.language.existentials

sealed trait Active {
  def toString: String
}
case object Enabled extends Active {
  override val toString = "Enabled"
}
case object Disabled extends Active {
  override val toString = "Disabled"
}

trait DefaultEnum extends Enumeration {
  self =>
  def default: self.Value
  def options: List[Tuple2[String,String]] = values.map { value => (value.toString, value.toString.capitalize) }.toList
}

trait Attribute {
  def name: String
  def value: String
}

trait WithOptionalUsersMetadata{
  def usersMetadata : OptionalUsersMetadata
}
case class OptionalUsersMetadata(createdById:Option[PK], modifiedById: Option[PK], deletedById: Option[PK])

case class UsersMetadata(createdById:PK, modifiedById: Option[PK])

trait WithUsersMetadata{
  def usersMetadata : UsersMetadata
}

case class DatesMetadata(createdOn: AppDateTime, modifiedOn: Option[AppDateTime])
object DatesMetadata {
  def data = new DatesMetadata(createdOn = utils.Utils.currentUTCAppDateTime, modifiedOn = Some(utils.Utils.currentUTCAppDateTime))
}

trait WithDatesMetadata{
  def datesMetadata : DatesMetadata
}

case class Contact( name: String, email: models.Email, mobile: models.Mobile, landLine: String, fax: Option[String])

//
//case class Authentication(id: Long, password: String, hasher: String, authenticationType: AuthenticationType.Value, active: Boolean, userId: Long) extends WithPrimaryKey[PK]
//case class AuthenticationSansId( password: String, hasher: String, authenticationType: AuthenticationType.Value, active: Boolean, userId: Long)
//
//case class Country( id:Int, code:String, name:String, dates:DatesMetadata, users:UsersMetadata ) extends WithPrimaryKey[Int]
//case class CountrySansId( code:String, name:String, dates:DatesMetadata, users:UsersMetadata )

case class NameCode(code:String, name:String)
object NameCode {
  import play.api.libs.json._
  implicit val implNameCodeFormats = Json.format[NameCode]
}

case class AB(id:String, name:String, createdOn:DateTime, url:URL)
object AB {
  import play.api.libs.json._
  import utils.json.JsonReadsWrites.{urlRead,urlWrite}
  implicit val implAB = Json.format[AB]
}
case class ABC(a:Long, b:String, c:Boolean)
object ABC {
  import play.api.libs.json._
  implicit val implABC = Json.format[ABC]
}



trait StatusMessage {
  def code:Int
  def message:String
  def detail:Option[String]
}

case class AppMessage(code:Int, message:String, detail:Option[String] = None) extends StatusMessage


object LiveOrDraftStatus extends DefaultEnum{
  type LiveOrDraftStatus = Value
  val LIVE,DRAFT,OTHER = Value
  override def default = {
    LiveOrDraftStatus.DRAFT
  }
}

/*
 * Base CQRS + ES command of the application
 * All other commands should extends it
 */
trait AppBaseCommand {
  def tenantId:PK
  def expectedVersion: Long
}

trait AppCommand extends AppBaseCommand {
  def id: Option[PK]
}

trait AppCommandWithPK extends AppBaseCommand {
  def id: PK
}

trait AppEvent {
  def tenantId:PK
  def id:PK
  def version: Long
}

sealed trait AppError {
  def code:String
  def message:String
  def detail:Option[String]
}

case class GeneralError(code:String, message:String, detail:Option[String] = None) extends AppError
object GeneralError {
  def apply(code:String) = new GeneralError(code = code, message = helpers.DebugInfo.codes(code))
  def apply(code:String, detail:String) = new GeneralError(code = code, message = helpers.DebugInfo.codes(code), detail = Some(detail))
}
case class DAOError(code:String, message:String, detail:Option[String], module:Option[String] = None) extends AppError
object DAOError {
  def apply(code:String, detail:String) = new DAOError(code = code, message = helpers.DebugInfo.codes(code), detail = Some(detail))
}
case class CQRSError(module:String, code:String, message:String, detail:Option[String] = None) extends AppError
case class UIError(code:String, message:String, detail:Option[String], module:String) extends AppError
