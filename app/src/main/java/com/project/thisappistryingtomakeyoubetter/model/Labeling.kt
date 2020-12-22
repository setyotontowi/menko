package com.project.thisappistryingtomakeyoubetter.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["taskId", "labelId"])
data class Labeling(
        val taskId: Int,
        var labelId: Int
)
