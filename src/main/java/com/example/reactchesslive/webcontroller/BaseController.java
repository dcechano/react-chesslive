package com.example.reactchesslive.webcontroller;

import com.example.reactchesslive.db.repo.PlayerRepo;
import com.example.reactchesslive.db.repo.h2.GameRepo;
import com.example.reactchesslive.db.repo.mysql.StatsRepo;
import com.example.reactchesslive.model.GlobalManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseController {


    private final GlobalManager globalManager;

    private final com.example.reactchesslive.db.repo.mysql.GameRepo mySqlGameRepo;

    private final GameRepo h2GameRepo;

    private StatsRepo statsRepo;

    private final PlayerRepo playerRepo;

    private final PasswordEncoder passwordEncoder;


    public BaseController(GlobalManager globalManager, @Qualifier("h2GameRepo") GameRepo h2GameRepo,
                          com.example.reactchesslive.db.repo.mysql.GameRepo mySqlGameRepo,
                          @Qualifier("mySqlPlayerRepo") PlayerRepo playerRepo,
                          PasswordEncoder passwordEncoder) {
        this.globalManager = globalManager;
        this.h2GameRepo = h2GameRepo;
        this.mySqlGameRepo = mySqlGameRepo;
        this.playerRepo = playerRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/chesslive")
    public String landing() {
        return "index";
    }

    @Autowired
    public void setStatsRepo(StatsRepo statsRepo) {
        this.statsRepo = statsRepo;
    }
}