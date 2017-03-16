package com.holden.customviews.scatch_card;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_main);
        printAnswerCk();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void printAnswerCk(){
        double femalCk = 0;
        double malCk = 0;
        double littleCk = 0;
        double cost = 0;
        for(femalCk = 1;cost <= 100;femalCk++){
            cost = femalCk*5;
            for(malCk = 1;cost <= 100;malCk++){
                cost = femalCk*5+malCk*3;
                for(littleCk =4;cost<= 100&&littleCk<100;littleCk=littleCk+4){
                    cost++;
                    if(cost==100&&(femalCk+malCk+littleCk)==100){
                        Log.e("Chicken: ","母鸡： "+femalCk+"  公鸡"+malCk+" 小鸡 "+littleCk);
                    }
                }
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
