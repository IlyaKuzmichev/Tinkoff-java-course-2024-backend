package edu.java.domain.jpa.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "github_links")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class GithubLinks {
    @Id
    @Column(name = "link_id")
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "link_id")
    private Links link;

    @Column(name = "last_update", nullable = false)
    private OffsetDateTime lastUpdate;

    @Column(name = "last_push", nullable = false)
    private OffsetDateTime lastPush;

    @Column(name = "pull_requests_count", nullable = false, columnDefinition = "INTEGER DEFAULT 0")
    private Integer pullRequestsCount;
}
