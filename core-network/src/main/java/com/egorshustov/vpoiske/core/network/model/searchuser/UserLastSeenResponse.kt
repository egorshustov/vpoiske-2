package com.egorshustov.vpoiske.core.network.model.searchuser

import com.egorshustov.vpoiske.core.common.utils.UnixSeconds
import com.egorshustov.vpoiske.core.model.data.UserLastSeen
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserLastSeenResponse(

    @SerialName("time")
    val timeUnixSeconds: Int? = null,

    @SerialName("platform")
    val platformId: Int? = null
)

fun UserLastSeenResponse?.asExternalModel(): UserLastSeen? = this?.let {
    UserLastSeen(
        time = timeUnixSeconds?.let(::UnixSeconds),
        platformId = platformId
    )
}