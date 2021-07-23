package com.example.reactchesslive.db.repo.mysql;

import com.example.reactchesslive.db.repo.AbstractRepo;
import com.example.reactchesslive.model.entity.Game;
import com.example.reactchesslive.model.entity.Player;

import java.util.List;

public interface GameRepo extends AbstractRepo<Game> {

    List<Game> findGamesByPlayer(Player player);

    List<Game> findGamesByUsername(String username);

}
