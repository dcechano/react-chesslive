package com.example.reactchesslive.db.repo.h2;

import com.example.reactchesslive.db.repo.AbstractRepo;
import com.example.reactchesslive.model.entity.Player;
import com.example.reactchesslive.model.entity.TimeControl;
import com.example.reactchesslive.model.entity.WaitingPlayer;

import java.util.List;

public interface WaitListRepo extends AbstractRepo<WaitingPlayer> {
    String addPlayerToWaitList(Player player, TimeControl timeControl);

    WaitingPlayer getWaitingPlayerByTimeControl(TimeControl timeControl, String exclusionId);

    List<WaitingPlayer> getWaitingPlayersByTimeControl(TimeControl timeControl);

}
