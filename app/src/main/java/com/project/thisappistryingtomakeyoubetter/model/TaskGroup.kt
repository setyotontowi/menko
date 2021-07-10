package com.project.thisappistryingtomakeyoubetter.model

import java.util.*

data class TaskGroup(
    val date: Date,
    val tasks: List<TaskWithLabel>
)
