package com.yesand.socialsave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import android.widget.ProgressBar;

/**
 * Created by puyus on 1/12/2017.
 */

public class UserFragment extends TabMainFragment {

    private GraphView mGraphView;
    private ProgressBar moneyLeft;
    private SwipeRefreshLayout refresher;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.user_fragment, container , false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mGraphView = (GraphView) getView().findViewById(R.id.graph);
        mGraphView.removeAllSeries();

        moneyLeft = (ProgressBar) view.findViewById(R.id.progressBar);

        refresher = (SwipeRefreshLayout) view.findViewById(R.id.refreshView);
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshPage();
            }
        });

        refreshPage();
    }

    public void refreshPage(){
        moneyLeft.setMax(300);
        moneyLeft.setProgress(50);

        refresher.setRefreshing(false);
    }
}
