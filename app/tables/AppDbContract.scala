package tables

import models.PK

/**
 * Created by pawan on 15/01/16.
 */
trait AppDbContract[E] {
  def insertRecord(entity:E):Option[E]
  def updateRecord(id:PK,entity:E):Option[E]
  def getRecord(id:PK):Option[E]
  def deleteRecord(entity:E):Boolean
  def deleteRecordById(id:PK):Boolean
  def finalAllRecords():List[E]
}
