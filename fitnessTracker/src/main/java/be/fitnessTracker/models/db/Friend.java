package be.fitnessTracker.models.db;

import be.fitnessTracker.internal.Constants.DbConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tblFriends")
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "serial")
    private String id;
    @Column(name = "friendOf", nullable = false, length = DbConstants.MAX_COLUMN_LENGTH)
    private String friendOf;
    @Column(name = "friendId", nullable = false, length = DbConstants.MAX_COLUMN_LENGTH)
    private String friendId;
    @Column(name = "friendEmail", nullable = false, length = DbConstants.MAX_COLUMN_LENGTH)
    private String friendEmail;
    @Column(name = "friendUsername", nullable = false, length = DbConstants.MAX_COLUMN_LENGTH)
    private String friendUsername;

    public static Friend friendFromUser(User user, User friend) {
        Friend fr = new Friend();
        fr.setFriendOf(user.getId());
        fr.setFriendId(friend.getId());
        fr.setFriendEmail(friend.getEmail());
        fr.setFriendUsername(friend.getUsername());
        return fr;
    }
}
