package com.example.reactchesslive.model.entity;

import javax.persistence.*;

@Entity
@Table(name = "PAIRED_PLAYERS")
public class PairedPlayer extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "WHITE")
    private Player white;

    @OneToOne
    @JoinColumn(name = "BLACK")
    private Player black;

    public PairedPlayer() {
    }

    public PairedPlayer(Player white, Player black) {
        this.white = white;
        this.black = black;
    }

    public Player getWhite() {
        return white;
    }

    public void setWhite(Player white) {
        this.white = white;
    }

    public Player getBlack() {
        return black;
    }

    public void setBlack(Player black) {
        this.black = black;
    }

    @Override
    public String toString() {
        return "PairedPlayers{" +
                "white=" + white +
                ", black=" + black +
                "} " + super.toString();
    }
}
