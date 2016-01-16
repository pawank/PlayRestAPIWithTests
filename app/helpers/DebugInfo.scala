package helpers

case class DebugInfo(cls:String, fn:String, line:String) {
  override def toString = {
    s"Class: $cls, function: $fn and line: $line"
  }
}

object DebugInfo {
  val codes:Map[String,String] = Map("C000" -> "Object cannot be created",
    "E001" -> s"Exception has occurred",
    "E002" -> s"Database session not found",
    "E003" -> s"Entity already exists",
    "E004" -> s"Error has occurred while updating existing object",
    "E005" -> s"Object cannot be casted to desired type",
    "E006" -> s"Object state is invalid",
    "E007" -> s"Oops! Developer has make a mistake. It will be rectified soon",
    "E008" -> s"Entity is not enabled",
    "E009" -> s"Command execution has failed",
    "E010" -> s"Value is empty",
    "E011" -> s"Database error has occurred while insert the record",
    "E012" -> s"Database error has occurred while updating the record",
    "E013" -> s"Database error has occurred while fetching the record",
    "E014" -> s"Database error has occurred while deleting the record",
    "E015" -> s"API exception has occurred while processing the input request",
    "E016" -> s"Controller asObjectExecuteWithDbSession error",
    "V001" -> s"Validation of field failed",
    "V002" -> s"Command version does not match with object version",
    "V003" -> s"Start date must come before end date",
    "V004" -> s"Termination date must come after start date",
    "V005" -> s"Index and search validation failed",
    "D23000" ->	"integrity_constraint_violation",
    "D23001"	-> "restrict_violation",
    "D23502"	-> "not_null_violation",
    "D23503"	-> "foreign_key_violation",
    "D23505"	-> "unique_violation",
    "D23514"	-> "check_violation",
    "D23P01"	-> "exclusion_violation",
    "NF01" -> s"No matching case found",
    "NF02" -> s"Entity does not exists").withDefaultValue("Oops! Error")
}

