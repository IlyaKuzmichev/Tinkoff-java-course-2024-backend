package edu.java.domain.jpa.entities;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_tracked_links")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserTrackedLinks {
    @EmbeddedId
    private UserTrackedLinksPK id;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "link_id", insertable = false, updatable = false)
    private Links link;
}
