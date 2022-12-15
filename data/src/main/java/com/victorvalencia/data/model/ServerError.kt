package com.victorvalencia.data.model;

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class ServerError(
    val httpErrorCode: Int? = null,
    val status: String? = null,
) : Parcelable
