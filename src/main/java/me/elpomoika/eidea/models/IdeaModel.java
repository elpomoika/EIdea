package me.elpomoika.eidea.models;

import java.util.UUID;

public class IdeaModel {
    private final int id;
    private final UUID uuid;
    private final String idea;
    private final String status;

    public IdeaModel(int id, UUID uuid, String idea, String status) {
        this.id = id;
        this.uuid = uuid;
        this.idea = idea;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getIdea() {
        return idea;
    }

    public String getStatus() {
        return status;
    }
}
