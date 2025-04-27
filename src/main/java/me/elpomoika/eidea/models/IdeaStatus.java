package me.elpomoika.eidea.models;

public enum IdeaStatus {
    APPROVED("ОДОБРЕНО"),
    DECLINED("ОТКЛОНЕНО"),
    PENDING("В ОЖИДАНИИ");

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
