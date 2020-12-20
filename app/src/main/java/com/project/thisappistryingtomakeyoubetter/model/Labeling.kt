package com.project.thisappistryingtomakeyoubetter.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Labeling(
        @PrimaryKey(autoGenerate = true)
        val id: Int,
        var task: Task,
        var label: Label
)
