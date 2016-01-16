package orientdb

import com.tinkerpop.blueprints.impls.orient.OrientGraph
import play.api.inject.{Binding, Module}
import play.api.{Configuration, Environment}

class DbModule extends Module {
  override def bindings(environment: Environment, configuration: Configuration): Seq[Binding[_]] = Seq(bind[AppDatabase[OrientGraph]].to[Db])
}
