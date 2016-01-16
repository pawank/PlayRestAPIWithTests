package models

import java.util.UUID

trait AppBaseModel {
  def name:String
}

case class PK(id:String) {
  //We need string in forms not the PK object, by default
  override def toString = id.toString
  def ===(pk:Option[PK]):Boolean = pk match {
    case Some(cid) => id.compareTo(cid.id) == 0
    case _ => false
  }
}
object PK {
  def equalTo(first:Option[PK], second:Option[PK]) = if ((first.isDefined) && (second.isDefined)) {first.get.===(second)} else false
}

case class Email(value:String) {
  override def toString = value
}

object Email {
  implicit def stringToEmail(email:String) = Email(value = email)
}

case class Mobile(value:String)

object Mobile {
  implicit def stringToMobile(mobile:String) = Mobile(value = mobile)
}

case class PersonName(firstName:String, lastName:String)

case class Url(value:String)

case class Pincode(value:String)

case class IP(address:String)

case class AppDateTime(value:Long) {
  override def toString = value.toString
  def <(dt:AppDateTime) = value < dt.value
  def <=(dt:AppDateTime) = value <= dt.value
  def >(dt:AppDateTime) = value > dt.value
  def >=(dt:AppDateTime) = value >= dt.value
}

trait WithPrimaryKey[PKType] {
  def id: Option[PKType]
}


