package helpers

import models.PK
import play.api.mvc.PathBindable


object AppPathBinders {
  implicit def pkPathBindable(implicit _pkBinder: PathBindable[String]) = new PathBindable[PK] {

    def bind(key: String, value: String): Either[String, PK] =
      for {
        pk <- _pkBinder.bind(key, value).right
      } yield PK(pk)

    def unbind(key: String, pk: PK): String =
      _pkBinder.unbind(key, pk.id.toString)
  }
}