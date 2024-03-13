package edu.java.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.time.OffsetDateTime;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public abstract class LinkInfo {
    private Link link;

    private Optional<OffsetDateTime> updateTime;
}
