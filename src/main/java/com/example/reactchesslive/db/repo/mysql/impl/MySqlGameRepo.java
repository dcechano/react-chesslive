package com.example.reactchesslive.db.repo.mysql.impl;

import com.example.reactchesslive.db.repo.mysql.GameRepo;
import com.example.reactchesslive.model.entity.Game;
import com.example.reactchesslive.model.entity.Player;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Transactional
@Repository
public class MySqlGameRepo extends MySqlAbstractRepo<Game> implements GameRepo {

    public MySqlGameRepo() {
        super(Game.class);
    }

    @Override
    public List<Game> findGamesByPlayer(Player player) {
        TypedQuery<Game> query = this.entityManager.createQuery(
                "SELECT g FROM Game g WHERE g.white =: player OR g.black =: player ORDER BY g.date asc", Game.class);
        query.setParameter("player", player);
        return query.getResultList();
    }

    @Override
    public List<Game> findGamesByUsername(String username) {
        TypedQuery<Game> query = this.entityManager.createQuery(
                "SELECT g FROM Game g WHERE g.white.username =: username OR g.black.username =: username",
                Game.class);
        query.setParameter("username", username);

        return query.getResultList();
    }

}
