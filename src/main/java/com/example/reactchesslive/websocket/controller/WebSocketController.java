package com.example.reactchesslive.websocket.controller;

import com.example.reactchesslive.db.repo.PlayerRepo;
import com.example.reactchesslive.db.repo.h2.PairedPlayersRepo;
import com.example.reactchesslive.db.repo.mysql.GameRepo;
import com.example.reactchesslive.model.dto.GameDTO;
import com.example.reactchesslive.model.entity.Game;
import com.example.reactchesslive.model.entity.Player;
import com.example.reactchesslive.model.utils.StatisticsUtils;
import com.example.reactchesslive.websocket.messaging.ChatMessage;
import com.example.reactchesslive.websocket.messaging.GameUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
public class WebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    private GameRepo mySqlGameRepo;

    private com.example.reactchesslive.db.repo.h2.GameRepo h2GameRepo;

    private PlayerRepo playerRepo;

    private PairedPlayersRepo pairedPlayersRepo;


    public WebSocketController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/updateOpponent")
    public void sendToUser(@Payload GameUpdate gameUpdate, Principal principal) {
        messagingTemplate.convertAndSendToUser(gameUpdate.getTo(), "/queue/update", gameUpdate);
    }

    @MessageMapping("/gameOver")
    public void gameOver(@Payload GameDTO gameDto) {
        StatisticsUtils.updateStats(gameDto);

        Optional<Game> gameOp = h2GameRepo.findById(gameDto.getGameId());
        if (gameOp.isEmpty()) {
            return;
        }

        Game game = gameOp.get();

        Player white = playerRepo.findByUsername(gameDto.getWhite());
        pairedPlayersRepo.removePairing(white);
        game.setPgn(gameDto.getPgn());
        game.setResult(gameDto.getResult());
        game.setDate(LocalDateTime.now());
        game.setWhite(playerRepo.findByUsername(gameDto.getWhite()));
        game.setBlack(playerRepo.findByUsername(gameDto.getBlack()));
        mySqlGameRepo.save(game);
        h2GameRepo.delete(game);
    }

    @MessageMapping("/message")
    public void send(@Payload ChatMessage chatMessage) {
        messagingTemplate.convertAndSendToUser(chatMessage.getTo(), "/queue/message", chatMessage);
    }

    @Autowired
    public void setMySqlGameRepo(@Qualifier("mySqlGameRepo") GameRepo gameRepo) {
        this.mySqlGameRepo = gameRepo;
    }

    @Autowired
    public void setH2GameRepo(@Qualifier("h2GameRepo") com.example.reactchesslive.db.repo.h2.GameRepo gameRepo) {
        this.h2GameRepo = gameRepo;
    }

    @Autowired
    public void setPlayerRepo(@Qualifier("mySqlPlayerRepo") PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    @Autowired
    public void setPairedPlayersRepo(PairedPlayersRepo pairedPlayersRepo) {
        this.pairedPlayersRepo = pairedPlayersRepo;
    }

}
