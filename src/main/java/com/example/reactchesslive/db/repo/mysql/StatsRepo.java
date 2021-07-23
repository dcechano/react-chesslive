package com.example.reactchesslive.db.repo.mysql;

import com.example.reactchesslive.db.repo.AbstractRepo;
import com.example.reactchesslive.model.entity.Statistics;

public interface StatsRepo extends AbstractRepo<Statistics> {

    Statistics getStatsByUsername(String username);

}
