package com.example.reactchesslive.db.repo.h2.impl;

import com.example.reactchesslive.db.repo.h2.WaitListRepo;
import com.example.reactchesslive.model.entity.Player;
import com.example.reactchesslive.model.entity.TimeControl;
import com.example.reactchesslive.model.entity.WaitingPlayer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
@Transactional(transactionManager = "h2TransactionManager")
public class WaitListRepoImpl extends H2AbstractRepoImpl<WaitingPlayer> implements WaitListRepo {

    public WaitListRepoImpl() {
        super(WaitingPlayer.class);
    }

    @Override
    public String addPlayerToWaitList(Player player, TimeControl timeControl) {
        WaitingPlayer waitingPlayer = new WaitingPlayer();
        waitingPlayer.setPlayer(player);
        String id = UUID.randomUUID().toString();
        waitingPlayer.setId(id);
        waitingPlayer.setTimeControl(timeControl);
        waitingPlayer.setCreatedAt(LocalDateTime.now());
        this.save(waitingPlayer);
        return id;
    }

    @Override
    public WaitingPlayer getWaitingPlayerByTimeControl(TimeControl timeControl, String exclusionId) {
        TypedQuery<WaitingPlayer> query = this.entityManager.createQuery(
                "SELECT w FROM WaitingPlayer w WHERE w.timeControl =: time_control AND w.player.id !=: exclusionId", WaitingPlayer.class);
        query.setParameter("time_control", timeControl);
        query.setParameter("exclusionId", exclusionId);
        List<WaitingPlayer> list = query.getResultList();
        return list.size() == 0 ? null : list.get(0);
    }

    @Override
    public List<WaitingPlayer> getWaitingPlayersByTimeControl(TimeControl timeControl) {
        TypedQuery<WaitingPlayer> query = this.entityManager.createQuery(
                "SELECT w FROM WaitingPlayer w WHERE w.timeControl =: time_control", WaitingPlayer.class);
        query.setParameter("time_control", timeControl);
        return query.getResultList();
    }

}
