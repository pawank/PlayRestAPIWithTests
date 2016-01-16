package dao

import models.{PK, Person}
import tables.AppDbContract

class PersonContract extends AppDbContract[Person] {
  override def insertRecord(entity: Person): Option[Person] = {
    println("Saved:" + entity.toString)
    Some(entity)
  }

  override def deleteRecord(entity: Person): Boolean = true

  override def updateRecord(id: PK, entity: Person): Option[Person] = Some(entity)

  override def deleteRecordById(id: PK): Boolean = id.id.size > 0

  override def getRecord(id: PK): Option[Person] = Some(Person(firstName = "test", lastName = "test", fullName = "test test", birthday = new java.util.Date().toString))

  override def finalAllRecords(): List[Person] = List(Person(firstName = "test", lastName = "test", fullName = "test test", birthday = new java.util.Date().toString))
}

/**
 * Created by pawan on 15/01/16.
 */
class PersonDAO extends PersonContract

