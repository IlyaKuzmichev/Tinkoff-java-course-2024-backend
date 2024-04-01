package edu.java.service.update_checker;

import edu.java.models.Link;
import edu.java.models.LinkInfo;

public interface UpdateChecker {

    LinkInfo checkUpdates(Link link);

    boolean isAppropriateLink(Link link);

    String getType();
}
