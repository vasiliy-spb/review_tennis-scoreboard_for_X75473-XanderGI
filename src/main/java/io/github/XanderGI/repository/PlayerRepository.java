package io.github.XanderGI.repository;

import io.github.XanderGI.entity.Player;

import java.util.Optional;

public interface PlayerRepository {
    Optional<Player> findByName(String name);

    Player save(Player player);
}