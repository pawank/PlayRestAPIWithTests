package utils

import java.util.UUID
import models.AppDateTime
import org.joda.time.{DateTimeZone, DateTime}

object Utils {
  val APP_TIMEZONE_DEFAULT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z"
  implicit def stackTrace(exp: Throwable): String = {
      import java.io.PrintWriter
      import java.io.StringWriter

    val sw: StringWriter = new StringWriter();
    val pw: PrintWriter = new PrintWriter(sw)
    exp.printStackTrace(pw)
    sw.toString()
  }

  def currentUTCDateTime = DateTime.now(DateTimeZone.UTC)

  def currentAppDateTimeAsString = DateTime.now(DateTimeZone.UTC).toString(APP_TIMEZONE_DEFAULT)

  def currentUTCDateTimeAsString = DateTime.now(DateTimeZone.UTC).toString(APP_TIMEZONE_DEFAULT)

  def currentAppDateTime = DateTime.now(DateTimeZone.UTC).getMillis

  def currentUTCAppDateTime = AppDateTime(DateTime.now(DateTimeZone.UTC).getMillis)

  def generateUUID = UUID.randomUUID().toString

  def cleanString(input:String):String = input.trim

  def currentDateTimeInMillis = DateTime.now.getMillis

  def formatAppDateTime(dt:AppDateTime, default:String = APP_TIMEZONE_DEFAULT) = new DateTime(dt.value,DateTimeZone.UTC).toString(default)

  /** Convert an instance of type 'T' to the requested object form
    *
    * {{{as[models.Employee](obj) match {
    * case Some(x) => x
    * case _ =>
    * }
    * }}}
    */
  import scala.reflect.runtime.universe._
  def as[T: TypeTag](term: Any): Option[T] =
    if (reflect.runtime.currentMirror.reflect(term).symbol.toType <:< typeOf[T])
      Some(term.asInstanceOf[T])
    else
      None
}
