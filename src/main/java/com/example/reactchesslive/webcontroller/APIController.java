package com.example.reactchesslive.webcontroller;

import com.example.reactchesslive.model.entity.Game;
import com.example.reactchesslive.model.entity.Player;
import com.example.reactchesslive.security.JWTUtils;
import com.example.reactchesslive.security.auth.AuthenticationRequest;
import com.example.reactchesslive.security.auth.JWS;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@CrossOrigin(value = "http://localhost:3000", allowCredentials = "true")
@RequestMapping("/api")
public class APIController {

    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;
    private Logger logger = Logger.getLogger(getClass().toString());


    public APIController(AuthenticationManager authenticationManager, JWTUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping(value = "/authenticate")
    public ResponseEntity<JWS> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        logger.info("===================/authenticate endpoint hit===================");
        String username = authenticationRequest.getUsername();
        String password = authenticationRequest.getPassword();
        logger.info("Logging log in credentials: " + username + " " + password);
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        return ResponseEntity.ok(jwtUtils.createToken(authenticationRequest));
    }

    @GetMapping("/restricted")
    public String restricted() {
        return "You made it into a restricted resource";
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

    @GetMapping("/secret")
    public String getKey() {
        return Encoders.BASE64URL.encode(Keys.secretKeyFor(SignatureAlgorithm.HS256).getEncoded());
    }

}
