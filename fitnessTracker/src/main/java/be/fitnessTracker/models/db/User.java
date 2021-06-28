package be.fitnessTracker.models.db;

import be.fitnessTracker.internal.Constants.DbConstants;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user")
@Table(name = "tblUsers")
public class User {
    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(updatable = false, nullable = false, unique = true, length = DbConstants.UUID_LENGTH)
    private String id;
    @Column(name = "username", nullable = false, length = DbConstants.MAX_COLUMN_LENGTH)
    private String username;
    @Column(name = "email", nullable = false, unique = true, length = DbConstants.MAX_COLUMN_LENGTH)
    private String email;
    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;
}
