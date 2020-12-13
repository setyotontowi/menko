package com.project.thisappistryingtomakeyoubetter.model

import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import java.util.*

@Entity
class Task {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    @ColumnInfo(name = "title")
    var title: String?

    @ColumnInfo(name = "description")
    var description: String?

    @ColumnInfo(name = "date")
    var date: Date? = null

    @ColumnInfo(name = "finish", defaultValue = "false")
    var isFinish = false

    @Ignore
    constructor(title: String, description: String) {
        this.title = title
        this.description = description
    }

    constructor(title: String, description: String, date: Date?) {
        this.title = title
        this.description = description
        this.date = date
    }
}