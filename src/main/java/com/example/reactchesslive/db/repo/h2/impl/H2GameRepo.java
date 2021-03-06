package com.example.reactchesslive.db.repo.h2.impl;

import com.example.reactchesslive.db.repo.h2.GameRepo;
import com.example.reactchesslive.model.entity.Game;
import com.example.reactchesslive.model.entity.Player;
import com.example.reactchesslive.model.entity.TimeControl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.Optional;

@Repository
@Transactional(transactionManager = "h2TransactionManager")
public class H2GameRepo extends H2AbstractRepoImpl<Game> implements GameRepo {

    public H2GameRepo() {
        super(Game.class);
    }

    @Override
    public Game getGameByPlayer(Player player) {

        TypedQuery<Game> query = this.entityManager.createQuery(
                "SELECT g FROM Game g WHERE g.black =: player OR g.white =: player", Game.class);
        query.setParameter("player", player);

        Game game = null;
        try {
            game = query.getSingleResult();
        } catch (NoResultException e) {
//        ignore
        }
        return game;
    }

}
