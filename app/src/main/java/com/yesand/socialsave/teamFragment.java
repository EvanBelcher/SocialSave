package com.yesand.socialsave;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

/**
 * Created by puyus on 1/12/2017.
 */

public class TeamFragment extends TabMainFragment {
    private View myView;
    private SwipeRefreshLayout refresher;
    private String teamName;
    private LinearLayout scroller;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.team_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myView = view;

        refresher = (SwipeRefreshLayout) view.findViewById(R.id.refreshView);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
        refresh();
    }
    public void refresh(){
        ResourceManager.getCurrUser().child(Constants.GROUP_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String groupId = (String) dataSnapshot.getValue();
                FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, Object> users = (HashMap<String, Object>) dataSnapshot.getValue();
                        int i = 1;
                        for (String userName : users.keySet()){
                            HashMap<String, Object> user = (HashMap<String, Object>) users.get(userName);
                            if (user.get(Constants.GROUP_ID).equals(groupId)){
                                double lastScore = Double.parseDouble(String.valueOf(user.get(Constants.LAST_SCORE)));
                                if (lastScore > 80){
                                    // Happy
                                    if (i == 1) {
                                        ((ImageView) myView.findViewById(R.id.user_emoji1)).setImageResource(R.drawable.smiley3);
                                        ((TextView) myView.findViewById(R.id.person1)).setText(userName);
                                        ((TextView) myView.findViewById(R.id.points1)).setText(lastScore + "");
                                    }
                                    else{
                                        ((ImageView) myView.findViewById(R.id.user_emoji2)).setImageResource(R.drawable.smiley3);
                                        ((TextView) myView.findViewById(R.id.person2)).setText(userName);
                                        ((TextView) myView.findViewById(R.id.points2)).setText(lastScore + "");
                                    }
                                }
                                else if (lastScore > 50){
                                    if (i == 1) {
                                        ((ImageView) myView.findViewById(R.id.user_emoji1)).setImageResource(R.drawable.neutral3);
                                        ((TextView) myView.findViewById(R.id.person1)).setText(userName);
                                        ((TextView) myView.findViewById(R.id.points1)).setText(lastScore + "");
                                    }
                                    else{
                                        ((ImageView) myView.findViewById(R.id.user_emoji2)).setImageResource(R.drawable.neutral3);
                                        ((TextView) myView.findViewById(R.id.person2)).setText(userName);
                                        ((TextView) myView.findViewById(R.id.points2)).setText(lastScore + "");
                                    }
                                }
                                else{
                                    if (i == 1) {
                                        ((ImageView) myView.findViewById(R.id.user_emoji1)).setImageResource(R.drawable.frowny3);
                                        ((TextView) myView.findViewById(R.id.person1)).setText(userName);
                                        ((TextView) myView.findViewById(R.id.points1)).setText(lastScore + "");
                                    }
                                    else{
                                        ((ImageView) myView.findViewById(R.id.user_emoji2)).setImageResource(R.drawable.frowny3);
                                        ((TextView) myView.findViewById(R.id.person2)).setText(userName);
                                        ((TextView) myView.findViewById(R.id.points2)).setText(lastScore + "");
                                    }
                                }
                                i++;
                            }
                        }
                        refresher.setRefreshing(false);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        refresher.setRefreshing(false);
                    }
                });

                FirebaseDatabase.getInstance().getReference().child("groups").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String, Object> groups = (HashMap<String, Object>) dataSnapshot.getValue();
                        HashMap<String, Object> group = (HashMap<String, Object>) groups.get(groupId);

                        ((TextView) myView.findViewById(R.id.team_name_text)).setText(String.valueOf(group.get("name")));
                        ((TextView) myView.findViewById(R.id.pointsText)).setText(String.valueOf((int)Double.parseDouble(String.valueOf(group.get("score"))) + " Points"));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        refresher.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                refresher.setRefreshing(false);
            }
        });
    }
}
