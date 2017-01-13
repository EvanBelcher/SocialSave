package com.yesand.socialsave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
                        for (String userName : users.keySet()){
                            HashMap<String, Object> user = (HashMap<String, Object>) users.get(userName);
                            if (user.get(Constants.GROUP_ID).equals(groupId)){
                                double lastScore = Double.parseDouble(String.valueOf(user.get(Constants.LAST_SCORE)));
                                if (lastScore > 80){
                                    // Happy
                                }
                                else if (lastScore > 50){
                                    // Meh
                                }
                                else{
                                    // Sad
                                }
                                refresher.setRefreshing(false);
                            }
                        }
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
