package kate.tutorial.kotlin.exceptions

class BadParamException(override val message: String = "Illegal Parameter"): Exception()
