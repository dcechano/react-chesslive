package com.example.reactchesslive.db.repo.h2;

import com.example.reactchesslive.db.repo.AbstractRepo;
import com.example.reactchesslive.model.entity.Game;
import com.example.reactchesslive.model.entity.Player;
import com.example.reactchesslive.model.entity.TimeControl;

import java.util.Optional;

public interface GameRepo extends AbstractRepo<Game> {

    Game getGameByPlayer(Player player);


}
