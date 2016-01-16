package orientdb

import dao.{PersonContract, PersonDAO}
import models.{AppBaseModel, Person}
import play.api.cache.CacheApi
import play.api.inject.{Binding, Module}
import play.api.{Configuration, Environment}
import tables.AppDbContract

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import play.api.{ Configuration, Environment }

class PersonModule extends Module {
   override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = Seq(bind[PersonContract].to[PersonDAO])
}
