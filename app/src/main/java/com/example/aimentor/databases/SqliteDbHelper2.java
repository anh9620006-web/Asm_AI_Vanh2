package com.example.aimentor.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/** Database độc lập chỉ dành cho chức năng Quiz. */
public class SqliteDbHelper2 extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "quiz_ai.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TOPICS = "quiz_topics";
    public static final String TABLE_QUESTIONS = "quiz_questions";
    public static final String TABLE_OPTIONS = "quiz_options";
    public static final String TABLE_ATTEMPTS = "quiz_attempts";

    public SqliteDbHelper2(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TOPICS + " ("
                + "topic_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "title TEXT NOT NULL UNIQUE, "
                + "description TEXT, "
                + "is_active INTEGER NOT NULL DEFAULT 1)");

        db.execSQL("CREATE TABLE " + TABLE_QUESTIONS + " ("
                + "question_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "topic_id INTEGER NOT NULL, "
                + "question_text TEXT NOT NULL, "
                + "explanation TEXT, "
                + "FOREIGN KEY(topic_id) REFERENCES " + TABLE_TOPICS
                + "(topic_id) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE " + TABLE_OPTIONS + " ("
                + "option_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "question_id INTEGER NOT NULL, "
                + "option_text TEXT NOT NULL, "
                + "is_correct INTEGER NOT NULL DEFAULT 0 CHECK(is_correct IN (0, 1)), "
                + "FOREIGN KEY(question_id) REFERENCES " + TABLE_QUESTIONS
                + "(question_id) ON DELETE CASCADE)");

        db.execSQL("CREATE TABLE " + TABLE_ATTEMPTS + " ("
                + "attempt_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "player_name TEXT, "
                + "topic_id INTEGER NOT NULL, "
                + "correct_count INTEGER NOT NULL DEFAULT 0, "
                + "question_count INTEGER NOT NULL, "
                + "completed_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY(topic_id) REFERENCES " + TABLE_TOPICS
                + "(topic_id))");

        insertDefaultTopics(db);
    }

    private void insertDefaultTopics(SQLiteDatabase db) {
        db.execSQL("INSERT INTO " + TABLE_TOPICS + " (title, description) VALUES "
                + "('Programming', 'Quiz về lập trình'), "
                + "('Discrete Mathematics', 'Quiz về toán rời rạc')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Khi có phiên bản mới, bổ sung migration tại đây để không làm mất dữ liệu quiz.
    }
}
