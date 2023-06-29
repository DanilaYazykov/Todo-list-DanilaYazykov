package com.example.todolist.domain.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class TodoItem(
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("importance") val importance: Importance,
    @SerializedName("deadline") val deadline: Long? = null,
    @SerializedName("done") val done: Boolean,
    @SerializedName("color") val color: String? = null,
    @SerializedName("created_at") val creationDate: Long? = null,
    @SerializedName("changed_at") val modificationDate: Long? = null,
    @SerializedName("last_updated_by") val lastUpdatedBy: String? = null,
): Parcelable