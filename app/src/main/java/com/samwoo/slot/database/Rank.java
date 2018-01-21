package com.samwoo.slot.database;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Administrator on 2018/1/21.
 */
@Entity
public class Rank {
    private static final String TAG = "Rank";
    @Id(autoincrement = true)
    private Long id;
    private String winner;
    private int score;
    private String time;

    @Generated(hash = 884252866)
    public Rank(Long id, String winner, int score, String time) {
        this.id = id;
        this.winner = winner;
        this.score = score;
        this.time = time;
    }

    @Generated(hash = 531117843)
    public Rank() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWinner() {
        return this.winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
