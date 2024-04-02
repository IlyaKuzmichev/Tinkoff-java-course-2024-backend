package edu.java.domain.jpa.entities;

import edu.java.models.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Users {
    @Id
    @Column(name = "id", unique = true, nullable = false)
    private Long id;

    @Column(name = "user_status", nullable = false, columnDefinition = "user_status_enum DEFAULT 'base'")
    @Convert(converter = UserStatusConverter.class)
    private User.Status userStatus;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<UserTrackedLinks> trackedLinks;
}
