package com.example.reactchesslive.db.repo.h2;

import com.example.reactchesslive.db.repo.AbstractRepo;
import com.example.reactchesslive.model.entity.Game;
import com.example.reactchesslive.model.entity.Player;

public interface GameRepo extends AbstractRepo<Game> {

    Game getGameByPlayer(Player player);

}
