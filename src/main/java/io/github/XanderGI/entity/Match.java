package io.github.XanderGI.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "Matches", check = {
        @CheckConstraint(name = "chk_matches_players_not_equal", constraint = "Player1 <> Player2"),
        @CheckConstraint(name = "chk_matches_valid_winner", constraint = "Winner = Player1 OR Winner = Player2")
})
public class Match {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "Player1")
    private Player playerOne;
    @ManyToOne
    @JoinColumn(name = "Player2")
    private Player playerTwo;
    @ManyToOne
    @JoinColumn(name = "Winner")
    private Player winner;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Match match)) {
            return false;
        }

        return getId() != null && getId().equals(match.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}