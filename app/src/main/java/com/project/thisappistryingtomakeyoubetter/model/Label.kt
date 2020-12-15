package com.project.thisappistryingtomakeyoubetter.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Label(
        @PrimaryKey(autoGenerate = true) val id: Int,
        val name:String?,
        val color: Int?
        )
