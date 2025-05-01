package me.elpomoika.eidea.models;

import lombok.Getter;

import java.util.UUID;

@Getter

public class Idea {
    private final int id;
    private final UUID uuid;
    private final String idea;
    private final IdeaStatus status;

    public Idea(int id, UUID uuid, String idea, IdeaStatus status) {
        this.id = id;
        this.uuid = uuid;
        this.idea = idea;
        this.status = status;
    }
}
