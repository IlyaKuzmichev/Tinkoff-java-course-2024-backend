package edu.java.domain.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserTrackedLinksPK implements Serializable {
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "link_id")
    private Long linkId;
}
