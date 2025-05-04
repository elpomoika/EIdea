package me.elpomoika.eidea.util.cooldown;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public interface CooldownManager {
    public Duration getRemainingCooldown(UUID key);
    public boolean hasCooldown(UUID key);
    public void setCooldown(UUID key, Duration duration);
}
