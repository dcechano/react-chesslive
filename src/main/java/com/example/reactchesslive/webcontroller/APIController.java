package com.example.reactchesslive.webcontroller;

import com.example.reactchesslive.db.repo.PlayerRepo;
import com.example.reactchesslive.db.repo.h2.GameRepo;
import com.example.reactchesslive.ex.GameMatcherTimeoutException;
import com.example.reactchesslive.model.GlobalManager;
import com.example.reactchesslive.model.dto.GameDTO;
import com.example.reactchesslive.model.entity.Game;
import com.example.reactchesslive.model.entity.Player;
import com.example.reactchesslive.model.entity.TimeControl;
import com.example.reactchesslive.security.JWTUtils;
import com.example.reactchesslive.security.auth.AuthenticationRequest;
import com.example.reactchesslive.security.auth.JWS;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityExistsException;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api")
public class APIController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private final GlobalManager globalManager;
    private final GameRepo gameRepo;
    private final PlayerRepo mySqlPlayerRep;
    private final PlayerRepo h2PlayerRepo;
    private final Logger logger = Logger.getLogger(getClass().toString());

    public APIController(AuthenticationManager authenticationManager, JWTUtils jwtUtils,
                         GlobalManager globalManager, @Qualifier("mySqlPlayerRepo") PlayerRepo mySqlPlayerRepo,
                         @Qualifier("h2PlayerRepo") PlayerRepo h2PlayerRepo, GameRepo gameRepo) {

        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.globalManager = globalManager;
        this.mySqlPlayerRep = mySqlPlayerRepo;
        this.h2PlayerRepo = h2PlayerRepo;
        this.gameRepo = gameRepo;
    }


    @PostMapping(value = "/authenticate")
    public ResponseEntity<JWS> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        return ResponseEntity.ok(jwtUtils.createToken(authenticationRequest));
    }

    @PostMapping(value = "/find-game")
    public ResponseEntity<GameDTO> findGame(@RequestHeader(HttpHeaders.AUTHORIZATION) String jws,
                                            @RequestParam("time_control") String timeControl) {
        logger.info(timeControl);

        jws = jws.split(" ")[1];

        Jws<Claims> claimsJws = jwtUtils.parse(jws);
        if (claimsJws == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String username = claimsJws.getBody().getSubject();

        Player player = mySqlPlayerRep.findByUsername(username);
        Game currentGame = gameRepo.getGameByPlayer(player);
        if (!(currentGame == null)) {
            return ResponseEntity.ok(new GameDTO(currentGame));
        }

        Game game;
        try {
            h2PlayerRepo.save(player);
        } catch (EntityExistsException e) {/*Ignore*/}

        try {
            game = globalManager.createChallenge(timeControl, player);
        } catch (GameMatcherTimeoutException e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
        }

        if (game == null) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {/*ignore*/}
            game = gameRepo.getGameByPlayer(player);
        }

        logger.info(game.toString());

        return ResponseEntity.ok(new GameDTO(game));
    }

    @GetMapping("/game/{id}")
    public ResponseEntity<Game> getGame(@PathVariable String id) {

        Game game = new Game();
        game.setId(UUID.randomUUID().toString());

        Player dylan = new Player();
        dylan.setUsername("Dylan");
        dylan.setId(UUID.randomUUID().toString());

        Player donovan = new Player();
        donovan.setUsername("Donovan");
        donovan.setId(UUID.randomUUID().toString());

        game.setWhite(dylan);
        game.setBlack(donovan);
        game.setDate(LocalDateTime.now());
        return ResponseEntity.ok(game);
    }

}
