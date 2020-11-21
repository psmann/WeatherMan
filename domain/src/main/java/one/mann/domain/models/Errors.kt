package one.mann.domain.models

/* Created by Psmann. */

sealed class Errors {
    object NoInternet : Errors()
    object NoGps : Errors()
    object NoLocation : Errors()
    data class NoResponse(val message: String = "") : Errors()
}