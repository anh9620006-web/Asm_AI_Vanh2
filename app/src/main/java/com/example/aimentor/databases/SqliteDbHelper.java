package com.example.aimentor.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SqliteDbHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "studyAI";
    // Tang version moi khi thay doi schema de onUpgrade() chay migration.
    private static final int DB_VERSION = 5;

    // dinh nghia thong tin bang "Users" luu tru tai khoan
    protected static final String TABLE_USERS = "users"; // ten bang du lieu
    // dinh nghia cac cot nam trong bang users do
    protected  static final String ID_USER = "id";
    protected static final String USERNAME_USER = "username";
    protected  static final String PASSWORD_USER = "password";
    protected  static final String EMAIL_USER = "email";
    protected static final String PHONE_USER = "phone";
    protected static final String ROLE_USER = "role";

    //dinh nghia thong tin bang Categoy
    protected static final String CATEGORY_TABLE = "categories";
    protected static final String ID_CATEGORY="id";
    protected  static final String NAME_CATEGORY="name_category";
    protected static final String STATUS_CATEGORY = "status_category";
    protected static final String DESCRIPION_CATEGORY="descripion_category";

    // ngay tao du lieu va ngay cap nhat du lieu
    protected static final String CREATED_AT = "createdAt";
    protected static final String UPDATED_AT = "updatedAt";

    // Cache khoa hoc tren may. serverId dung de dong bo voi API sau nay.
    protected static final String COURSE_TABLE = "courses";
    protected static final String LESSON_TABLE = "lessons";
    protected static final String COURSE_PROGRESS_TABLE = "course_progress";

    public SqliteDbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String usersTable = " CREATE TABLE " + TABLE_USERS + " ( "
                            + ID_USER + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + USERNAME_USER + " VARCHAR(30) NOT NULL, "
                            + PASSWORD_USER + " VARCHAR(200) NOT NULL, "
                            + EMAIL_USER    + " VARCHAR(60) NOT NULL, "
                            + PHONE_USER    + " VARCHAR(20), "
                            + ROLE_USER     + " TINYINT DEFAULT(1), "
                            + CREATED_AT    + " DATETIME, "
                            + UPDATED_AT    + " DATETIME ) ";

        String categoryTable = " CREATE TABLE " + CATEGORY_TABLE+" ( "
                + ID_CATEGORY+" INTEGER PRIMARY KEY AUTOINCREMENT, "
                +NAME_CATEGORY +" VARCHAR(100) NOT NULL, "
                + DESCRIPION_CATEGORY +" VARCHAR(200), "
                +STATUS_CATEGORY+" TINYINT DEFAULT(1), "
                +CREATED_AT + " DATETIME, "
                + UPDATED_AT + " DATETIME) ";

        db.execSQL(usersTable); // thuc thi cau lenh SQL va tao bang du lieu
        db.execSQL(categoryTable);
        createCourseTables(db);
    }

    private void createCourseTables(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS " + COURSE_TABLE + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "server_id INTEGER UNIQUE, "
                + "category_id INTEGER, "
                + "title TEXT NOT NULL, "
                + "description TEXT, "
                + "thumbnail_url TEXT, "
                + "is_published INTEGER NOT NULL DEFAULT 1 CHECK(is_published IN (0, 1)), "
                + "server_updated_at TEXT, "
                + "created_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                + "updated_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY(category_id) REFERENCES " + CATEGORY_TABLE + "(" + ID_CATEGORY + ") ON DELETE SET NULL)");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + LESSON_TABLE + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "server_id INTEGER UNIQUE, "
                + "course_id INTEGER NOT NULL, "
                + "title TEXT NOT NULL, "
                + "content TEXT, "
                + "video_url TEXT, "
                + "position INTEGER NOT NULL DEFAULT 0, "
                + "server_updated_at TEXT, "
                + "FOREIGN KEY(course_id) REFERENCES " + COURSE_TABLE + "(id) ON DELETE CASCADE, "
                + "UNIQUE(course_id, position))");

        db.execSQL("CREATE TABLE IF NOT EXISTS " + COURSE_PROGRESS_TABLE + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "user_id INTEGER NOT NULL, "
                + "course_id INTEGER NOT NULL, "
                + "last_lesson_id INTEGER, "
                + "progress_percent REAL NOT NULL DEFAULT 0 CHECK(progress_percent BETWEEN 0 AND 100), "
                + "updated_at TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP, "
                + "FOREIGN KEY(user_id) REFERENCES " + TABLE_USERS + "(" + ID_USER + ") ON DELETE CASCADE, "
                + "FOREIGN KEY(course_id) REFERENCES " + COURSE_TABLE + "(id) ON DELETE CASCADE, "
                + "FOREIGN KEY(last_lesson_id) REFERENCES " + LESSON_TABLE + "(id) ON DELETE SET NULL, "
                + "UNIQUE(user_id, course_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 5) {
            // Khong DROP bang cu: tai khoan va category cua nguoi dung duoc giu lai.
            createCourseTables(db);
        }
    }
}
