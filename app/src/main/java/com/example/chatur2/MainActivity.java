package com.example.chatur2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button ask = findViewById(R.id.ask);
        ask.setOnClickListener(this);
    }

    public void sentMessage(String msg) {
        float density = getApplicationContext().getResources().getDisplayMetrics().density;
        RelativeLayout container = new RelativeLayout(this);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        container.setLayoutParams(lp);

        TextView sMessage = new TextView(this);

        lp = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
        );
        lp.setMargins(0,Math.round(10*density),Math.round(10*density),0);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        sMessage.setText(msg);
        sMessage.setBackgroundColor(Color.parseColor("#018786"));
        sMessage.setTextColor(Color.parseColor("#ffffff"));
        sMessage.setLayoutParams(lp);
        sMessage.setPadding(Math.round(5*density),Math.round(5*density),Math.round(5*density),Math.round(5*density));

        container.addView(sMessage);
        LinearLayout main = findViewById(R.id.main);
        main.addView(container);
    }
    Runnable rn = new Runnable() {
        @Override
        public void run() {
            EditText query = findViewById(R.id.query);
            String url = "https://www.google.com/search?q=" + query.getText().toString().replaceAll(" ", "+");
            try {
                Document doc = Jsoup.connect(url).get();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                            Element res = doc.select(".xpc .kCrYt").first();
                            String resStr = "";
                            if (res != null) {
                                resStr = (res.text().toString()) + "\n\n";
                            }
                            res = doc.select(".Ey4n2").first();
                            if (res != null) {
                                resStr += (res.text().toString()) + "\n\n";
                            }
                            res = doc.select(".ZINbbc").get(1);
                            if (res != null) {
                                resStr += (res.text().toString()) + "\n\n";
                            }

                            float density = getApplicationContext().getResources().getDisplayMetrics().density;
                            RelativeLayout container = new RelativeLayout(getApplicationContext());

                            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            container.setLayoutParams(lp);

                            TextView rMessage = new TextView(getApplicationContext());

                            lp = new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                            lp.setMargins(Math.round(10 * density), Math.round(10 * density), 0, 0);

                            if (resStr.length() > 0) {
                                rMessage.setText(resStr);
                                resStr = "";
                            } else {
                                rMessage.setText("No Results Found");
                            }
                            rMessage.setBackgroundColor(Color.parseColor("#000000"));
                            rMessage.setTextColor(Color.parseColor("#ffffff"));
                            rMessage.setLayoutParams(lp);
                            rMessage.setMaxWidth(Math.round(300 * density));
                            rMessage.setPadding(Math.round(5 * density), Math.round(5 * density), Math.round(5 * density), Math.round(5 * density));
                            findViewById(R.id.ask).setEnabled(true);
                            container.addView(rMessage);
                            LinearLayout main = findViewById(R.id.main);
                            main.addView(container);
                    }
                });

            }catch (Exception e){
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),"No Internet Connection",Toast.LENGTH_SHORT).show();
                    }
                });
                findViewById(R.id.ask).setEnabled(true);
            }
            query.setText("");
        }
    };

    @Override
    public void onClick(View v) {
        EditText query = findViewById(R.id.query);
        ScrollView result = findViewById(R.id.result);
        v.setEnabled(false);
        if(query.getText().length() > 0){
            sentMessage(query.getText().toString());
//            receivedMessage(v);
            Thread receiveMessage = new Thread(rn);
            receiveMessage.start();
            receiveMessage.run();
        }else{
            Toast.makeText(getApplicationContext(),"Please Provide Input",Toast.LENGTH_SHORT).show();
        }
    }
}