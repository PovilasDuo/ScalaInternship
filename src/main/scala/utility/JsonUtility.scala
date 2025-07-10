package utility

import cats.data.{Validated, ValidatedNel}

object JsonUtility {
  def extractField[T](obj: ujson.Obj, key: String)(extract: ujson.Value => T): ValidatedNel[String, T] =
    Validated.condNel(
      obj.value.contains(key),
      extract(obj(key)),
      s"Missing field: $key"
    )
}
