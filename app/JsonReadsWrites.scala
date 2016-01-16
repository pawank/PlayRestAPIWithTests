package utils.json

import models._
import models.common._
import models.enums._
import org.joda.time.LocalDateTime
import play.api.libs.json._

object EnumUtils {
  def enumReads[E <: Enumeration](enum: E): Reads[E#Value] = new Reads[E#Value] {
    def reads(json: JsValue): JsResult[E#Value] = json match {
      case JsString(s) => {
        try {
          JsSuccess(enum.withName(s))
        } catch {
          case _: NoSuchElementException => JsError(s"Enumeration expected of type: '${enum.getClass}', but it does not appear to contain the value: '$s'")
        }
      }
      case _ => JsError("String value expected for the enumeration")
    }
  }

  implicit def enumWrites[E <: Enumeration]: Writes[E#Value] = new Writes[E#Value] {
    def writes(v: E#Value): JsValue = JsString(v.toString)
  }

  implicit def enumFormat[E <: Enumeration](enum: E): Format[E#Value] = {
    Format(EnumUtils.enumReads(enum), EnumUtils.enumWrites)
  }
}

/*
case class SearchResults[T](
                             elements: List[T],
                             page: Int,
                             pageSize: Int,
                             total :Int
                             )
object SearchResults
{
  implicit def searchResultsReads[T](implicit fmt: Reads[T]): Reads[SearchResults[T]] = new Reads[SearchResults[T]] {
    def reads(json: JsValue): SearchResults[T] = new SearchResults[T] (

      (json \ "elements") match {
        //case JsArray(ts) => ts.map(t => Json.fromJson(t))(fmt)
        case _ => throw new RuntimeException("Elements MUST be a list")
      },
      (json \ "page").as[Int],
      (json \ "pageSize").as[Int],
      (json \ "total").as[Int]
    )
  }

  implicit def searchResultsWrites[T](implicit fmt: Writes[T]): Writes[SearchResults[T]] = new Writes[SearchResults[T]] {
    def writes(ts: SearchResults[T]) = JsObject(Seq(
      "page" -> JsNumber(ts.page),
      "pageSize" -> JsNumber(ts.pageSize),
      "total" -> JsNumber(ts.total),
      "elements" -> JsArray(ts.elements.map(Json.toJson(_)))
    ))
  }
}
*/
object JsonReadsWrites {
  import play.api.libs.json._

  implicit val pkRead = new Reads[models.PK] {
    def reads(json: JsValue) = {
      JsSuccess(models.PK(json.toString()))
    }
  }
  implicit val pkWrite = new Writes[models.PK] {
    def writes(obj:models.PK):JsValue = {
      JsString(obj.id.toString)
    }
  }
  implicit val enabledReadFormat = new Reads[Active] {
    def reads(json: JsValue) = {
      json match {
        case JsBoolean(b) =>
          b match {
          case true => JsSuccess(Enabled)
          case false => JsSuccess(Disabled)
          }
        case _ => JsError("Boolean value expected for Active type")
      }
    }
  }
  implicit val disabledWriteFormat = new Writes[Active] {
    def writes(obj:Active):JsValue = {
      obj match {
        case Enabled => JsBoolean(true)
        case Disabled => JsBoolean(false)
      }
    }
  }
  implicit val mobileRead = new Reads[models.Mobile] {
    def reads(json: JsValue) = {
      JsSuccess(models.Mobile(json.toString()))
    }
  }
  implicit val mobileWrite = new Writes[models.Mobile] {
    def writes(obj:models.Mobile):JsValue = {
      JsString(obj.value)
    }
  }

  implicit val coreUrlRead = new Reads[models.Url] {
    def reads(json: JsValue) = {
      JsSuccess(models.Url(json.toString()))
    }
  }
  implicit val coreUrlWrite = new Writes[models.Url] {
    def writes(obj:models.Url):JsValue = {
      JsString(obj.value)
    }
  }
  implicit val pincodeRead = new Reads[models.Pincode] {
    def reads(json: JsValue) = {
      JsSuccess(models.Pincode(json.toString()))
    }
  }
  implicit val pincodeWrite = new Writes[models.Pincode] {
    def writes(obj:models.Pincode):JsValue = {
      JsString(obj.value)
    }
  }

  implicit val emailRead = new Reads[models.Email] {
    def reads(json: JsValue) = {
      JsSuccess(models.Email(json.toString()))
    }
  }
  implicit val emailWrite = new Writes[models.Email] {
    def writes(obj:models.Email):JsValue = {
      JsString(obj.value)
    }
  }

  implicit val urlRead = new Reads[java.net.URL] {
    def reads(json: JsValue) = {
      //println(s"############################################### reading url ${json.toString().replace("\"", "")}")
      JsSuccess(new java.net.URL(json.toString().replace("\"", "")))
    }
  }
  implicit val urlWrite = new Writes[java.net.URL] {
    def writes(obj:java.net.URL):JsValue = {
      JsString(obj.toString)
    }
  }

  implicit val jodaLocalDateTimeWrites: Writes[LocalDateTime] = new Writes[LocalDateTime] {
    def writes(d: org.joda.time.LocalDateTime): JsValue = JsString(d.toString())
  }
  implicit val jodaLocalDateTimeReads = new Reads[LocalDateTime]{
    def reads(json: JsValue) = {
      JsSuccess(new LocalDateTime(json.toString()))
    }
  }

  implicit val appDateTimeRead = new Reads[models.AppDateTime] {
    def reads(json: JsValue) = {
      JsSuccess(models.AppDateTime(json.toString().toLong))
    }
  }
  implicit val appDateTimeWrite = new Writes[models.AppDateTime] {
    def writes(obj:models.AppDateTime):JsValue = {
      JsNumber(obj.value)
    }
  }
  implicit val addressTypeEnumFormat = EnumUtils.enumFormat(AddressType)

  implicit val personFormats = Json.format[Person]

  implicit def personListReadFormat(implicit fmt: Reads[Person]):Reads[List[Person]] = new Reads[List[Person]] {
    def reads(obj:JsValue) = obj match {
        case JsArray(ts) =>
          /*
          var result:List[Person] = List.empty
          ts.foreach(x => {
            val p:Person = Json.fromJson(x)(fmt).get
            result = result.::(p)
          })
          JsSuccess(result)
          */
          JsSuccess(ts.map(x => Json.fromJson(x)(fmt).get).toList)
        case _ => throw new RuntimeException("Elements MUST be a list")
    }
  }

  implicit val personListWriteFormat = new Writes[List[Person]] {
    def writes(objs:List[Person]):JsValue = {
      JsArray(objs.map(Json.toJson(_)))
    }
  }
  implicit val datesMetadataFormats = Json.format[DatesMetadata]
  implicit val optionalUsersMetadataFormats = Json.format[OptionalUsersMetadata]
  implicit val usersMetadataFormats = Json.format[UsersMetadata]
  implicit val urlFormats = Json.format[Url]
  /**
   * Writer for List[JsValue] so that play action can export the value as proper Json
   */
  implicit val jsValueListWriteFormat = new Writes[List[JsValue]] {
    def writes(obj:List[JsValue]):JsValue = {
      JsArray(obj)
    }
  }

  implicit def genericListReadFormat[T](implicit fmt: Reads[T]):Reads[List[T]] = new Reads[List[T]] {
    def reads(obj:JsValue) = obj match {
      case JsArray(ts) =>
        JsSuccess(ts.map(x => Json.fromJson(x)(fmt).get).toList)
      case _ => throw new RuntimeException("Input must be an array")
    }
  }

  implicit val commonlyUsedJsonFormats = Json.format[AppMessage]
}
