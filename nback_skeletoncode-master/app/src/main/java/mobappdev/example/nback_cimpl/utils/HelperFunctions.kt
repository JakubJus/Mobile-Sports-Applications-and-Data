package mobappdev.example.nback_cimpl.utils

fun mapNumberToLetter(number: Int): CharSequence {
    require(number in 1..26){"Error in HelperFunctions:mapNumberToLetter"}
    return ('A'.code + number - 1).toChar().toString()
}