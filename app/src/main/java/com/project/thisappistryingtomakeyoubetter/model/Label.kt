package com.project.thisappistryingtomakeyoubetter.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Label(
        @PrimaryKey(autoGenerate = true) val id: Int,
        var name:String?,
        var color: Int?
        ): Serializable
