package `in`.ranium.asteroidsneostats.utility

data class Resource<out T>(
    val status: Status,
    val data: T?,
    val message:String?,
    val responseCode: Int
)
{
    companion object{

        fun <T> success(data:T?, code: Int): Resource<T> {
            return Resource(Status.SUCCESS, data, null, code)
        }

        fun <T> error(msg:String, data:T?, code: Int): Resource<T> {
            return Resource(Status.ERROR, data, msg, code)
        }

    }
}