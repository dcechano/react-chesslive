package com.example.reactchesslive.db.repo;

import com.example.reactchesslive.model.entity.Player;
import com.example.reactchesslive.model.entity.Statistics;

public interface PlayerRepo extends AbstractRepo<Player> {

    Player findByUsername(String username);

    Statistics findStatsByUsername(String username);

    void deleteByUsername(String username);

}
