package me.elpomoika.eidea.models;

public enum IdeaStatus {
    APPROVED("Одобрено"),
    DECLINED("Отклонено"),
    PENDING("В Ожидании");

    private final String displayName;

    IdeaStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean equalsIgnoreCase(String status) {
        return this.displayName.equalsIgnoreCase(status);
    }
}
