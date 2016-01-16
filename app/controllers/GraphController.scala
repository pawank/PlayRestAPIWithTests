package controllers

import javax.inject._
import orientdb.Db

import scala.concurrent.Future
import play.api._
import play.api.cache._
import play.api.db.slick.{DatabaseConfigProvider, HasDatabaseConfig}
import play.api.mvc._
import com.tinkerpop.blueprints.impls.orient.OrientGraph
import com.tinkerpop.blueprints.{Vertex,Edge}

import scala.collection.JavaConverters._
import scala.collection.JavaConversions._

class VehicleOrderController @Inject() (cache: CacheApi, db:Db) extends Controller {

  def test() = {
    val graph:OrientGraph = db.connection
    try{
      //graph.getEdges.map(edge => {
      //  println(edge.getLabel)
      //})
    } catch {
      case e:Exception =>
    }
  }

  def index = Action {
    test()
    Ok("Tested").as("text/plain")
  }
}
