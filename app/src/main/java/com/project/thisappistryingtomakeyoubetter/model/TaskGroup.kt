package com.project.thisappistryingtomakeyoubetter.model

import java.util.*

data class TaskGroup(
        val date: Date,
        var tasks: List<TaskWithLabel>
)
