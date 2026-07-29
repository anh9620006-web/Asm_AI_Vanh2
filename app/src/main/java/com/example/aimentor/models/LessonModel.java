package com.example.aimentor.models;

public class LessonModel {

        private final int id;
        private final int courseId;
        private final String title;
        private final String content;
        private final int position;
        private final String updatedAt;

        public LessonModel(int id, int courseId, String title, String content,
                           int position, String updatedAt) {
            this.id = id;
            this.courseId = courseId;
            this.title = title;
            this.content = content;
            this.position = position;
            this.updatedAt = updatedAt;
        }

        public int getId() {
            return id;
        }

        public int getCourseId() {
            return courseId;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public int getPosition() {
            return position;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }

