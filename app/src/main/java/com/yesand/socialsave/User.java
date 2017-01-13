package com.yesand.socialsave;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serina on 1/12/17.
 */

public class User {
    public String name;
    public String password;
    public String nessieId;
    public String groupId;
    public double incomePerWeek;
    public double nextIncomePerWeek;
    public double goal;
    public double nextGoal;
    public int score;
    public List<Integer> scoreHistory;
    public int streak;

    public User()
    {
    }
    public User(String username, String userpassword, String userID)
    {
        name = username;
        password = userpassword;
        nessieId = userID;
        groupId = "";
        incomePerWeek = 0.0;
        nextIncomePerWeek = incomePerWeek;
        goal = 0.0;
        nextGoal = goal;
        score = 0;
        scoreHistory = new ArrayList<Integer>(8);
        streak = 0;
    }
}
