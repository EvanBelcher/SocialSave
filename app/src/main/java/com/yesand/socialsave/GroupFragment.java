package com.yesand.socialsave;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yesand.socialsave.group.RedemptionActivity;
import com.yesand.socialsave.settings.SettingsActivity;

import java.util.HashMap;

/**
 * Created by puyus on 1/12/2017.
 */

public class GroupFragment extends TabMainFragment {
    private View myView;
    private SwipeRefreshLayout refresher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.group_fragment, container, false);
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

        @SuppressWarnings("ConstantConditions") ImageButton settings = (ImageButton) getView().findViewById(R.id.account_settings_icon);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        Button redeemButton = (Button) getView().findViewById(R.id.redeem_button);
        redeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RedemptionActivity.class);
                startActivity(intent);
            }
        });

        refresher = (SwipeRefreshLayout) view.findViewById(R.id.refreshView);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    public void refresh() {
        ResourceManager.getCurrUser().child(Constants.GROUP_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String groupId = (dataSnapshot.getValue().toString().charAt(0) == '-') ? dataSnapshot.getValue().toString() : "-" + dataSnapshot.getValue();
                FirebaseDatabase.getInstance().getReference().child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        @SuppressWarnings("unchecked") HashMap<String, Object> users = (HashMap<String, Object>) dataSnapshot.getValue();
                        int i = 1;
                        for (String userName : users.keySet()) {
                            @SuppressWarnings("unchecked") HashMap<String, Object> user = (HashMap<String, Object>) users.get(userName);
                            if (user.get(Constants.GROUP_ID).equals(groupId)) {
                                double lastScore = Double.parseDouble(String.valueOf(user.get(Constants.LAST_SCORE)));
                                if (lastScore > 80) {
                                    // Happy
                                    if (i == 1) {
                                        ((ImageView) myView.findViewById(R.id.user_emoji1)).setImageResource(R.drawable.smiley3);
                                        ((TextView) myView.findViewById(R.id.person1)).setText(userName);
                                        ((TextView) myView.findViewById(R.id.points1)).setText(String.valueOf(lastScore));
                                    } else {
                                        ((ImageView) myView.findViewById(R.id.user_emoji2)).setImageResource(R.drawable.smiley3);
                                        ((TextView) myView.findViewById(R.id.person2)).setText(userName);
                                        ((TextView) myView.findViewById(R.id.points2)).setText(String.valueOf(lastScore));
                                    }
                                } else if (lastScore > 50) {
                                    if (i == 1) {
                                        ((ImageView) myView.findViewById(R.id.user_emoji1)).setImageResource(R.drawable.neutral3);
                                        ((TextView) myView.findViewById(R.id.person1)).setText(userName);
                                        ((TextView) myView.findViewById(R.id.points1)).setText(String.valueOf(lastScore));
                                    } else {
                                        ((ImageView) myView.findViewById(R.id.user_emoji2)).setImageResource(R.drawable.neutral3);
                                        ((TextView) myView.findViewById(R.id.person2)).setText(userName);
                                        ((TextView) myView.findViewById(R.id.points2)).setText(String.valueOf(lastScore));
                                    }
                                } else {
                                    if (i == 1) {
                                        ((ImageView) myView.findViewById(R.id.user_emoji1)).setImageResource(R.drawable.frowny3);
                                        ((TextView) myView.findViewById(R.id.person1)).setText(userName);
                                        ((TextView) myView.findViewById(R.id.points1)).setText(String.valueOf(lastScore));
                                    } else {
                                        ((ImageView) myView.findViewById(R.id.user_emoji2)).setImageResource(R.drawable.frowny3);
                                        ((TextView) myView.findViewById(R.id.person2)).setText(userName);
                                        ((TextView) myView.findViewById(R.id.points2)).setText(String.valueOf(lastScore));
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
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        DataSnapshot group = dataSnapshot.child(groupId);

                        ((TextView) myView.findViewById(R.id.group_name_text)).setText(group.child("name").getValue().toString());
                        ((TextView) myView.findViewById(R.id.pointsText)).setText(String.valueOf((int) Double.parseDouble(group.child("score").getValue().toString()) + " Points"));
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
        Toast.makeText(getActivity(), "Waiting for refresh...", Toast.LENGTH_SHORT).show();
    }
}
