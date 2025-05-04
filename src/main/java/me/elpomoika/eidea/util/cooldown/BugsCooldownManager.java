package me.elpomoika.eidea.util.cooldown;

import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BugsCooldownManager implements CooldownManager {
    private final Map<UUID, Instant> map;

    public BugsCooldownManager() {
        this.map = new HashMap<>();
    }

    @Override
    public void setCooldown(UUID key, Duration duration) {
        map.put(key, Instant.now().plus(duration));
    }

    @Override
    public boolean hasCooldown(UUID key) {
        Instant cooldown = map.get(key);
        return cooldown != null && Instant.now().isBefore(cooldown);
    }
    @Override
    public Duration getRemainingCooldown(UUID key) {
        Instant cooldown = map.get(key);
        Instant now = Instant.now();
        if (cooldown != null && now.isBefore(cooldown)) {
            return Duration.between(now, cooldown);
        } else {
            return Duration.ZERO;
        }
    }
}
