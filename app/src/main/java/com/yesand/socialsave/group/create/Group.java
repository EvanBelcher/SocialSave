package com.yesand.socialsave.group.create;

/**
 * Created by evbel on 1/14/2017.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
public class Group {
    public String name;
    public int score;
    public int streak;
    public Alert alert;

    public Group() {

    }

    public Group(String name) {
        this.name = name;
        score = 0;
        streak = 0;
        alert = null;
    }

}

@SuppressWarnings("WeakerAccess")
class Alert {
    public int cost;
    public String from;
    public String message;

    public Alert() {
    }

    public Alert(int cost, String from, String message) {
        this.cost = cost;
        this.from = from;
        this.message = message;
    }
}