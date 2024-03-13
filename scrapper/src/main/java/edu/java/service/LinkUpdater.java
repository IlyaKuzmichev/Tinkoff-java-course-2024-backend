package edu.java.service;

import edu.java.models.LinkInfo;
import java.util.Optional;

public interface LinkUpdater {
    Optional<String> update(LinkInfo linkInfo);
}
