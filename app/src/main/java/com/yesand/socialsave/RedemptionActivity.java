package com.yesand.socialsave;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

public class RedemptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redemption);
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        TextView textViewAmzn = (TextView) findViewById(R.id.amzn_points);
        ImageButton amznButton = (ImageButton) findViewById(R.id.amzn_button);
        ImageButton sjButton = (ImageButton) findViewById(R.id.sj_hosp_button);
        ImageButton wwfButton = (ImageButton) findViewById(R.id.wwf_button);


    }
}
