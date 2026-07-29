package com.example.aimentor.models;

public class CourseModel {
    private final int serverId;
    private final String title;
    private final String description;
    private final String thumbnailUrl;
    private final boolean published;
    private final String updatedAt;

    public CourseModel(int serverId, String title, String description, String thumbnailUrl,
                       boolean published, String updatedAt) {
        this.serverId = serverId;
        this.title = title;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.published = published;
        this.updatedAt = updatedAt;
    }

    public int getServerId() {
        return serverId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public boolean isPublished() {
        return published;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
