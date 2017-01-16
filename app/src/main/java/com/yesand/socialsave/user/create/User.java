package com.yesand.socialsave.user.create;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Serina on 1/12/17.
 */

@SuppressWarnings({"WeakerAccess", "unused"})
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
    int totalSavings;

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
        scoreHistory = new ArrayList<>(8);
        for(int i = 0; i < scoreHistory.size(); i++)
            scoreHistory.add(0);
        streak = 0;
        totalSavings = 0;
    }
}
