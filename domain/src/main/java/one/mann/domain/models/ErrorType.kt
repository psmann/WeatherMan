package one.mann.domain.models

/* Created by Psmann. */

sealed class ErrorType {
    object NoInternet : ErrorType()
    object NoGps : ErrorType()
    object NoLocation : ErrorType()
    data class NoResponse(val message: String = "") : ErrorType()
}