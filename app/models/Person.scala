package models

import java.util.Date

import play.api.libs.json._

case class Person(firstName:String, lastName:String, fullName:String, birthday:String) extends AppBaseModel {
  override def name = "Person"
}
