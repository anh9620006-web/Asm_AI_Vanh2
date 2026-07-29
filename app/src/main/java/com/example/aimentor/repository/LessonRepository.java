package com.example.aimentor.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.aimentor.databases.SqliteDbHelper;
import com.example.aimentor.models.LessonModel;

import java.util.ArrayList;
import java.util.List;

public class LessonRepository extends SqliteDbHelper {

    public LessonRepository(@Nullable Context context) {
        super(context);
    }


    // Lưu Lesson lấy từ API vào SQLite
    public void saveLessonsFromServer(List<LessonModel> lessons) {

        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();

        try {

            for (LessonModel lesson : lessons) {

                ContentValues values = new ContentValues();

                values.put("server_id", lesson.getId());
                values.put("course_id", lesson.getCourseId());
                values.put("title", lesson.getTitle());
                values.put("content", lesson.getContent());
                values.put("position", lesson.getPosition());
                values.put("server_updated_at", lesson.getUpdatedAt());
                values.put("updated_at", lesson.getUpdatedAt());


                int changed = db.update(
                        LESSON_TABLE,
                        values,
                        "server_id = ?",
                        new String[]{
                                String.valueOf(lesson.getId())
                        }
                );


                if (changed == 0) {
                    db.insertOrThrow(
                            LESSON_TABLE,
                            null,
                            values
                    );
                }
            }

            db.setTransactionSuccessful();

        } finally {

            db.endTransaction();
            db.close();

        }
    }



    // Lấy Lesson theo CourseId
    public List<LessonModel> getLessonsByCourse(int courseId) {

        List<LessonModel> lessons = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();


        try (
                Cursor cursor = db.query(
                        LESSON_TABLE,
                        new String[]{
                                "server_id",
                                "course_id",
                                "title",
                                "content",
                                "position",
                                "server_updated_at"
                        },
                        "course_id = ?",
                        new String[]{
                                String.valueOf(courseId)
                        },
                        null,
                        null,
                        "position ASC"
                )
        ) {


            while (cursor.moveToNext()) {

                lessons.add(new LessonModel(

                        cursor.getInt(0),  // id
                        cursor.getInt(1),  // courseId
                        cursor.getString(2), // title
                        cursor.isNull(3) ? "" : cursor.getString(3), // content
                        cursor.getInt(4), // position
                        cursor.isNull(5) ? "" : cursor.getString(5)

                ));

            }

        } finally {

            db.close();

        }


        return lessons;
    }
}