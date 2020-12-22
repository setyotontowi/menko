package com.project.thisappistryingtomakeyoubetter.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class LabelWithTask(
        @Embedded val label: Label,
        @Relation(
                parentColumn = "id",
                entity = Task::class,
                entityColumn = "id",
                associateBy = Junction(
                        value = Labeling::class,
                        parentColumn = "labelId",
                        entityColumn = "taskId"
                )
        )
        val tasks: List<Task>
)
