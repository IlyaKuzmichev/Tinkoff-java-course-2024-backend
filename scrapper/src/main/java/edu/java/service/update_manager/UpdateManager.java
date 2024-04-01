package edu.java.service.update_manager;

import edu.java.models.Link;
import java.util.Collection;

public interface UpdateManager {
    void execute(Collection<Link> links);
}
