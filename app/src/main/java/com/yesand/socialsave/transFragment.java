package com.yesand.socialsave;

import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.GraphView;
import com.reimaginebanking.api.nessieandroidsdk.NessieError;
import com.reimaginebanking.api.nessieandroidsdk.NessieResultsListener;
import com.reimaginebanking.api.nessieandroidsdk.models.Account;
import com.reimaginebanking.api.nessieandroidsdk.models.Purchase;
import com.reimaginebanking.api.nessieandroidsdk.requestclients.NessieClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by puyus on 1/12/2017.
 */

public class TransFragment extends TabMainFragment {

    private TextView dateOfTransaction;
    private SwipeRefreshLayout refresher;
    private TextView titleTransaction;
    private TextView moneyTransaction;
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.trans_fragment, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        refreshPage();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        moneyTransaction = (TextView) view.findViewById(R.id.moneyTransaction);
        dateOfTransaction = (TextView) view.findViewById(R.id.dateOfTransaction);
        titleTransaction = (TextView) view.findViewById(R.id.titleTransaction);

        refresher = (SwipeRefreshLayout) view.findViewById(R.id.refreshView);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        });
    }

    public void refreshPage(){
        ResourceManager.getCurrUser().child(Constants.NESSIE_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                String customerId = (String) dataSnapshot.getValue();
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url = "http://api.reimaginebanking.com/customers/" + customerId + "/accounts?key=" + ResourceManager.getNessieKey();
                JsonArrayRequest jsArrRequest = new JsonArrayRequest
                        (com.android.volley.Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try {
                                    ResourceManager.getNessieClient().PURCHASE.getPurchasesByAccount((String)((JSONObject) response.get(0)).get("_id"), new NessieResultsListener() {
                                        @Override
                                        public void onSuccess(Object result) {
                                            List<Purchase> purchases = (List<Purchase>) result;
                                            for(Purchase purchase: purchases)
                                            {
                                                dateOfTransaction.setText(purchase.getPurchaseDate() + "\n\n" + dateOfTransaction.getText() + "\n");
                                                titleTransaction.setText(purchase.getDescription() + "\n\n" + titleTransaction.getText().toString() + "\n");
                                                moneyTransaction.setText(ResourceManager.getMoneyFormatter().format(purchase.getAmount()) + "\n\n" + moneyTransaction.getText().toString());
                                            }
                                            refresher.setRefreshing(false);
                                        }
                                        @Override
                                        public void onFailure(NessieError error) {
                                            Toast.makeText(getActivity(), "Error1..." + error.getMessage(), Toast.LENGTH_LONG).show();
                                            refresher.setRefreshing(false);
                                        }
                                    });
                                } catch (Exception e) {
                                    Toast.makeText(getActivity(), "Error5..." + e.getMessage(), Toast.LENGTH_LONG).show();
                                    refresher.setRefreshing(false);}
                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getActivity(), "Error6..." + error.getMessage(), Toast.LENGTH_LONG).show();
                                refresher.setRefreshing(false);
                            }
                        });
                queue.add(jsArrRequest);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "Error3...", Toast.LENGTH_LONG).show();
                refresher.setRefreshing(false);
            }
        });
    }
}




