package com.yesand.socialsave;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.GraphViewXML;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import com.reimaginebanking.api.nessieandroidsdk.NessieError;
import com.reimaginebanking.api.nessieandroidsdk.NessieResultsListener;
import com.reimaginebanking.api.nessieandroidsdk.models.Purchase;

import com.yesand.socialsave.settings.SettingsActivity;

import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by puyus on 1/12/2017.
 */

@SuppressWarnings("ConstantConditions")
public class UserFragment extends TabMainFragment {

    private ProgressBar moneyLeft;
    private SwipeRefreshLayout refresher;
    private TextView availibleFundsMessage;
    private View myView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        myView = view;

        ImageButton settings = (ImageButton) getView().findViewById(R.id.account_settings_icon);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });

        GraphView mGraphView = (GraphView) view.findViewById(R.id.graph);
        mGraphView.removeAllSeries();


        moneyLeft = (ProgressBar) view.findViewById(R.id.progressBar);

        availibleFundsMessage = (TextView) view.findViewById(R.id.current_saving_text);

        refresher = (SwipeRefreshLayout) view.findViewById(R.id.refreshView);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshPage();
    }

    public void refreshPage() {

        final TextView username = (TextView) myView.findViewById(R.id.username);
        ResourceManager.getCurrUser().child(Constants.NAME).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username.setText((String) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                username.setText("Happy Funtime");
            }
        });


        final TextView lastScore = (TextView) myView.findViewById(R.id.last_week_score_text);
        ResourceManager.getCurrUser().child(Constants.LAST_SCORE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lastScore.setText("Last Week's Score: " + String.valueOf(dataSnapshot.getValue().toString()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                lastScore.setText("...");
            }
        });


        final TextView totalSavings = (TextView) myView.findViewById(R.id.total_savings_text);
        ResourceManager.getCurrUser().child(Constants.TOTAL_SAVINGS).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                totalSavings.setText("Total Savings: " + ResourceManager.getMoneyFormatter().format(dataSnapshot != null && dataSnapshot.getValue() != null ? dataSnapshot.getValue() : 0));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                totalSavings.setText("...");
            }
        });


        // Graph me here lol
        final GraphViewXML graph = (GraphViewXML) myView.findViewById(R.id.graph);
        ResourceManager.getCurrUser().child(Constants.HISTORY).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                @SuppressWarnings("unchecked") ArrayList<Long> objs = (dataSnapshot != null && dataSnapshot.getValue() != null) ? (ArrayList<Long>) dataSnapshot.getValue() : new ArrayList<Long>();

                DataPoint[] points = new DataPoint[objs.size()];
                for (int i = 0; i < objs.size(); i++) {
                    points[i] = new DataPoint((double) i + 1, objs.get(i));
                }

                BarGraphSeries<DataPoint> series = new BarGraphSeries<>(points);
                graph.removeAllSeries();
                graph.addSeries(series);

                series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
                    @Override
                    public int get(DataPoint data) {
                        return Color.rgb((int) data.getX() * 255 / 4, (int) Math.abs(data.getY() * 255 / 6), 100);
                    }
                });

                series.setSpacing(50);

                series.setDrawValuesOnTop(true);
                series.setValuesOnTopColor(Color.WHITE);
                series.setValuesOnTopSize(50);

                graph.getViewport().setYAxisBoundsManual(true);
                graph.getViewport().setMinY(0);
                graph.getViewport().setMaxY(110);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        ResourceManager.getCurrUser().child(Constants.NESSIE_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String customerId = (String) dataSnapshot.getValue();

                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = "http://api.reimaginebanking.com/customers/" + customerId + "/accounts?key=" + ResourceManager.getNessieKey();

                JsonArrayRequest jsArrRequest = new JsonArrayRequest
                        (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    ResourceManager.getNessieClient().PURCHASE.getPurchasesByAccount((String) ((JSONObject) response.get(0)).get("_id"), new NessieResultsListener() {
                                        @Override
                                        public void onSuccess(Object result) {
                                            Calendar startOfWeek = ResourceManager.getStartOfThisWeek();
                                            SimpleDateFormat nessieDateFormat = ResourceManager.getNessieDateFormat();

                                            @SuppressWarnings("unchecked") List<Purchase> purchases = (List<Purchase>) result;

                                            double amountSpent = 0;
                                            for (Purchase purchase : purchases) {
                                                try {
                                                    Date date = nessieDateFormat.parse(purchase.getPurchaseDate());

                                                    if (startOfWeek.getTime().getTime() <= date.getTime()) {
                                                        amountSpent += purchase.getAmount();
                                                    }
                                                } catch (ParseException e) {
                                                    // Handle?
                                                }
                                            }

                                            final double finalAmountSpent = amountSpent;

                                            ResourceManager.getCurrUser().child(Constants.GOAL).addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    double goal = Double.parseDouble(dataSnapshot.getValue().toString());

                                                    moneyLeft.setMax((int) goal);
                                                    moneyLeft.setProgress(Math.max(0, (int) goal - (int) finalAmountSpent));

                                                    availibleFundsMessage.setText(ResourceManager.getMoneyFormatter().format(goal - finalAmountSpent) + " left / " + ResourceManager.getMoneyFormatter().format(goal) + " budget:");

                                                    refresher.setRefreshing(false);
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    Constants.error("Error0..." + databaseError.getMessage(), false, getActivity());
                                                    refresher.setRefreshing(false);
                                                }
                                            });
                                        }

                                        @Override
                                        public void onFailure(NessieError error) {
                                            Constants.error("Error1..." + error.getMessage(), false, getActivity());
                                            refresher.setRefreshing(false);
                                        }
                                    });
                                } catch (Exception e) {
                                    Constants.error("Error5..." + e.getMessage(), false, getActivity());
                                    refresher.setRefreshing(false);
                                }
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Constants.error("Error6..." + error.getMessage(), false, getActivity());
                                refresher.setRefreshing(false);
                            }
                        });
                queue.add(jsArrRequest);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Constants.error("Error3..." + databaseError.getMessage(), false, getActivity());
                refresher.setRefreshing(false);
            }
        });
        Toast.makeText(getActivity(), "Waiting for refresh...", Toast.LENGTH_SHORT).show();
    }
}