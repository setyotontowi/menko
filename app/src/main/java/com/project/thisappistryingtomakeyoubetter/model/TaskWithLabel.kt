package com.project.thisappistryingtomakeyoubetter.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TaskWithLabel (
    @Embedded val task: Task,
    @Relation(
            parentColumn = "id",
            entity = Label::class,
            entityColumn = "id",
            associateBy = Junction(
                    value = Labeling::class,
                    parentColumn = "taskId",
                    entityColumn = "labelId")
    )
    val labels: List<Label> = ArrayList()
)