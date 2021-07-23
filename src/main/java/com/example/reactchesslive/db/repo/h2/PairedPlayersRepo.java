package com.example.reactchesslive.db.repo.h2;

import com.example.reactchesslive.db.repo.AbstractRepo;
import com.example.reactchesslive.model.entity.PairedPlayer;
import com.example.reactchesslive.model.entity.Player;

public interface PairedPlayersRepo extends AbstractRepo<PairedPlayer> {

    void setPairedPlayers(Player white, Player black);

    Player getPairedPlayer(Player player);

    void removePairing(Player player);

    boolean isPaired(Player player);

}
