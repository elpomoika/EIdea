package me.elpomoika.eidea.models;

public enum FeedbackType {
    IDEA(0),
    BUG(1);

    private final int id;

    FeedbackType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static FeedbackType fromId(int id) {
        for (FeedbackType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid FeedbackType id: " + id);
    }
}
