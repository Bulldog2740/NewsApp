package com.yehor.newsapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class Source(
    val id: @RawValue Any,
    val name: String
): Parcelable