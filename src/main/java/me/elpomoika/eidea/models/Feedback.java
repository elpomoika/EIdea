package me.elpomoika.eidea.models;

import lombok.Getter;
import lombok.ToString;

import java.util.UUID;

@Getter
@ToString
public class Feedback {
    private final int id;
    private final UUID uuid;
    private final String idea;
    private final Status status;
    private final FeedbackType type;

    public Feedback(int id, UUID uuid, String idea, Status status, FeedbackType type) {
        this.id = id;
        this.uuid = uuid;
        this.idea = idea;
        this.status = status;
        this.type = type;
    }
}
