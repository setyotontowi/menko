package com.project.thisappistryingtomakeyoubetter.util;

import android.graphics.Color;

import androidx.room.TypeConverter;

import com.project.thisappistryingtomakeyoubetter.model.Label;
import com.project.thisappistryingtomakeyoubetter.model.Task;

import java.util.Date;

public class Converter {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static int TaskToIntId(Task task){return task.getId();}

    @TypeConverter
    public static Task TaskIdToTask(int id){
        Task task = new Task("", "");
        task.setId(id);
        return task;
    }

    @TypeConverter
    public static int LabelToIntId(Label label){return label.getId();}

    @TypeConverter
    public static Label LabelIdToTask(int id){return new Label(id, null, null);}

}
