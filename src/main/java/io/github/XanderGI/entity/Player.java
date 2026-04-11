package io.github.XanderGI.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode (onlyExplicitlyIncluded = true)
@NoArgsConstructor
@Entity
@Table(name = "Players")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @EqualsAndHashCode.Include
    @Column(unique = true)
    private String name;
}