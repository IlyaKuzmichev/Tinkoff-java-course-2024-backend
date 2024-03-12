package edu.java.service.update_checker;

import edu.java.models.Link;
import java.util.Optional;

public interface UpdateChecker {

    Optional<String> checkUpdates(Link link);

    boolean isCommonChecker(Link link);
}
