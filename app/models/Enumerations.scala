package models.enums

import models.common.DefaultEnum

object DateRange extends DefaultEnum{
  type DateRange = Value
  val TODAY, YESTERDAY, LAST_24_HOURS, LAST_7_DAYS, LAST_15_DAYS, LAST_30_DAYS, CURRENT_MONTH, CURRENT_YEAR, CUSTOM_PERIOD = Value
  override def default = {
    DateRange.TODAY
  }

}

object Salutation extends DefaultEnum{
  type Salutation = Value
  val MR, MRS, MS, DR = Value
  override def default = {
    Salutation.MR
  }
}

object AddressType extends DefaultEnum {
  type AddressType = Value
  val OFFICE,HOME,SHIPPING,BILLING = Value
  val optionsOfficeHomeOnly = values.filter(a => a.toString != AddressType.SHIPPING.toString && a.toString != AddressType.BILLING.toString).map(v =>{
    (v.toString,v.toString)
  }).toList
  override def default = {
    AddressType.HOME
  }
}
object ShippingAddressType extends DefaultEnum {
  type ShippingAddressType = Value
  val OFFICE,HOME = Value
  override def default = {
    ShippingAddressType.HOME
  }
}

object EntityStatus extends DefaultEnum{
  type EntityStatus = Value
  val CREATED, DELETED, UPDATED, INTERNAL, NO_STATUS = Value
  override def default = {
    EntityStatus.NO_STATUS
  }
}


