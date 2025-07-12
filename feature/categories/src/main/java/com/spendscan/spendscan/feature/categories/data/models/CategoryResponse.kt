package com.spendscan.spendscan.feature.categories.data.models

import com.google.gson.annotations.SerializedName

data class CategoryResponse(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("emoji") val emoji: String,
    @SerializedName("isIncome") val isIncome: Boolean
)