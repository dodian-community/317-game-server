package net.dodian.events;

import lombok.Data;
import org.springframework.stereotype.Component;

@Data
@Component
public abstract class GameEvent {
    protected boolean cancelled = false;

    public void cancel() {
        this.cancelled = true;
    }
}
