package org.chorem.ecd.model.stream;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * @author Julien Gaston (gaston@codelutin.com)
 */
public class SignageStream {

    private UUID id;

    private String name;

    private Integer timing;

    private LocalDateTime creationDateTime;

    private LocalDateTime lastUpdateDateTime;

    private List<SignageStreamFrame> frames;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTiming() {
        return timing;
    }

    public void setTiming(Integer timing) {
        this.timing = timing;
    }

    public LocalDateTime getCreationDateTime() {
        return creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public LocalDateTime getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(LocalDateTime lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public List<SignageStreamFrame> getFrames() {
        return frames;
    }

    public void setFrames(List<SignageStreamFrame> frames) {
        this.frames = frames;
    }
}
