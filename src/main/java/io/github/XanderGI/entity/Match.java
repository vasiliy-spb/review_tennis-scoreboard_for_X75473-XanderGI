package io.github.XanderGI.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Matches")
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
}