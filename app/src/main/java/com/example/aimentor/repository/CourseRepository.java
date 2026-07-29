package com.example.aimentor.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import com.example.aimentor.databases.SqliteDbHelper;
import com.example.aimentor.models.CourseModel;

import java.util.ArrayList;
import java.util.List;

public class CourseRepository extends SqliteDbHelper {
    public CourseRepository(@Nullable Context context) {
        super(context);
    }

    public void saveCoursesFromServer(List<CourseModel> courses) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (CourseModel course : courses) {
                ContentValues values = new ContentValues();
                values.put("server_id", course.getServerId());
                values.put("title", course.getTitle());
                values.put("description", course.getDescription());
                values.put("thumbnail_url", course.getThumbnailUrl());
                values.put("is_published", course.isPublished() ? 1 : 0);
                values.put("server_updated_at", course.getUpdatedAt());
                values.put("updated_at", course.getUpdatedAt());

                int changed = db.update(COURSE_TABLE, values, "server_id = ?",
                        new String[]{String.valueOf(course.getServerId())});
                if (changed == 0) db.insertOrThrow(COURSE_TABLE, null, values);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public List<CourseModel> getCachedCourses() {
        List<CourseModel> courses = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        try (Cursor cursor = db.query(COURSE_TABLE,
                new String[]{"server_id", "title", "description", "thumbnail_url", "is_published", "server_updated_at"},
                "is_published = 1", null, null, null, "updated_at DESC")) {
            while (cursor.moveToNext()) {
                courses.add(new CourseModel(
                        cursor.getInt(0), cursor.getString(1), cursor.isNull(2) ? null : cursor.getString(2),
                        cursor.isNull(3) ? null : cursor.getString(3), cursor.getInt(4) == 1,
                        cursor.isNull(5) ? "" : cursor.getString(5)
                ));
            }
        } finally {
            db.close();
        }
        return courses;
    }
}
