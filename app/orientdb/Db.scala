package orientdb
import com.tinkerpop.blueprints.impls.orient.OrientGraphFactory
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.orientechnologies.orient.core.config.OGlobalConfiguration
import com.tinkerpop.blueprints.{Vertex,Edge}

import javax.inject._
import scala.concurrent.Future
import play.api.inject.ApplicationLifecycle

trait AppDatabase[T] {
  def dbName:String
  def getDatabaseUrl():String
  def init():Option[T]
  def getConnection():Option[T]
  def connection:T
  def close(connection:T):Boolean
}

@Singleton
class Db @Inject() (lifecycle: ApplicationLifecycle) extends AppDatabase[OrientGraph] {
  private var dbconnection:Option[OrientGraph] = None
  val DB_URL = System.getProperty("RAPIDOR_ORIENTDB_URL","remote:localhost/databases/VehicleHistoryGraph")

  override def dbName = "VehicleHistoryGraph"

  def init():Option[OrientGraph] = {
    OGlobalConfiguration.SQL_GRAPH_CONSISTENCY_MODE.setValue("notx_sync_repair")
    val factory:OrientGraphFactory = new OrientGraphFactory(DB_URL).setupPool(1,10)
    val graph:OrientGraph  = factory.getTx()
    try {
      //g.getRawGraph().getStorage().getConfiguration().setProperty("txRequiredForSQLGraphOperations", "false")
      println(s"$dbName init done..")
      Some(graph)
    } catch {
      case e:Exception =>
        e.printStackTrace()
        None
    }
  }


  override def getDatabaseUrl(): String = DB_URL

  override def close(graph: OrientGraph): Boolean = {
    //dbconnection = None
    graph.shutdown(true)
    true
  }

  override def getConnection(): Option[OrientGraph] = dbconnection

  lifecycle.addStopHook { () =>
    dbconnection match {
      case Some(connectionDb) =>
        println(s"Database $dbName disconnected")
        Future.successful(close(connectionDb))
      case _ =>
        println(s"Database $dbName already disconnected")
        Future.successful(() => true)
    }
  }

  val connection:OrientGraph = {
    dbconnection match {
      case Some(c) => c
      case _ =>
        init().get
    }
  }



}