package com.example.todolist.domain.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Entity(tableName = "todoItemsTable")
@Parcelize
data class TodoItem(
    @PrimaryKey
    @SerializedName("id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("importance") val importance: Importance = Importance.BASIC,
    @SerializedName("deadline") val deadline: Long? = null,
    @SerializedName("done") val done: Boolean,
    @SerializedName("color") val color: String? = "#FFFFFF",
    @SerializedName("created_at") val creationDate: Long? = null,
    @SerializedName("changed_at") var modificationDate: Long? = null,
    @SerializedName("last_updated_by") val lastUpdatedBy: String? = "unknown device",
    @SerializedName("synced") val synced: Boolean = false,
    @SerializedName("deleted") val deleted: Boolean = false
) : Parcelable {
    enum class Importance {
        @SerializedName("basic")
        BASIC,
        @SerializedName("important")
        IMPORTANT,
        @SerializedName("low")
        LOW
    }
}