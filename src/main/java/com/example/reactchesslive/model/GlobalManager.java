package com.example.reactchesslive.model;

import com.example.reactchesslive.db.repo.PlayerRepo;
import com.example.reactchesslive.db.repo.h2.GameRepo;
import com.example.reactchesslive.db.repo.h2.PairedPlayersRepo;
import com.example.reactchesslive.db.repo.h2.WaitListRepo;
import com.example.reactchesslive.ex.GameMatcherTimeoutException;
import com.example.reactchesslive.model.entity.Game;
import com.example.reactchesslive.model.entity.Player;
import com.example.reactchesslive.model.entity.TimeControl;
import com.example.reactchesslive.model.entity.WaitingPlayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.logging.Logger;

@Component
public class GlobalManager {

    private final Logger logger = Logger.getLogger(getClass().toString());

    private PlayerRepo playerRepo;

    private WaitListRepo waitListRepo;

    private PairedPlayersRepo pairedPlayersRepo;

    private GameRepo gameRepo;

    private static Game createGame(Player player1, Player player2) {
        Game game = new Game();
        game.setId(UUID.randomUUID().toString());
        if (System.currentTimeMillis() % 2 == 0) {
            game.setWhite(player1);
            game.setBlack(player2);
        } else {
            game.setBlack(player1);
            game.setWhite(player2);
        }

        return game;
    }

    private Game awaitChallenge(Player player, TimeControl timeControl) throws GameMatcherTimeoutException{
        long timeoutTime = System.currentTimeMillis() + 15000;

        WaitingPlayer matchedPlayer = waitListRepo.getWaitingPlayerByTimeControl(timeControl, player.getId());
        if (matchedPlayer == null) {
            String waitId = waitListRepo.addPlayerToWaitList(player, timeControl);

            if (pairedPlayersRepo.isPaired(player)) {
                waitListRepo.deleteById(waitId);
                return null;

            } else {
                while (matchedPlayer == null) {
                    if (pairedPlayersRepo.isPaired(player)) {
                        return null;
                    }
                    if (System.currentTimeMillis() > timeoutTime) {
                        waitListRepo.deleteById(waitId);
                        throw new GameMatcherTimeoutException("Match maker timed out!");
                    }
                    matchedPlayer = waitListRepo.getWaitingPlayerByTimeControl(timeControl, player.getId());
                }
                waitListRepo.delete(matchedPlayer);
                waitListRepo.deleteById(waitId);
            }
        } else {
            waitListRepo.delete(matchedPlayer);
            return pair(player, matchedPlayer.getPlayer(), timeControl);
        }

        return pair(player, matchedPlayer.getPlayer(), timeControl);
    }

    private Game pair(Player player1, Player player2, TimeControl timeControl) {
        pairedPlayersRepo.setPairedPlayers(player1, player2);
        Game game = createGame(player1, player2);
        game.setTimeControl(timeControl);
        gameRepo.save(game);
        return game;
    }

    public Game createChallenge(String timeControl, Player player) {
        TimeControl time = TimeControl.valueOf(timeControl);
        return awaitChallenge(player, time);
    }

    @Autowired
    public void setPlayerRepo(@Qualifier("h2PlayerRepo") PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    @Autowired
    public void setWaitListRepo(WaitListRepo waitListRepo) {
        this.waitListRepo = waitListRepo;
    }

    @Autowired
    public void setPairedPlayersRepo(PairedPlayersRepo pairedPlayersRepo) {
        this.pairedPlayersRepo = pairedPlayersRepo;
    }

    @Autowired
    public void setGameRepo(@Qualifier("h2GameRepo") GameRepo gameRepo) {
        this.gameRepo = gameRepo;
    }
}
