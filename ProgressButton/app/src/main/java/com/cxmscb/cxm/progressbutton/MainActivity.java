package com.cxmscb.cxm.progressbutton;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private ProgressButton progressButton;


    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            progressButton.setProgress(msg.arg1);
        }
    };

    private int z=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressButton = (ProgressButton) findViewById(R.id.pb);
        progressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new ProgressThread().start();


            }
        });
        progressButton.setOnFinishListener(new ProgressButton.OnFinishListener() {
            @Override
            public void onFinish() {
                progressButton.setText("完 成");
               //progressButton.initState(); initial the state of progressbtn
                Toast.makeText(MainActivity.this,"添加完成",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public  class ProgressThread extends Thread{

        private int z = 0;
        @Override
        public void run() {
            for(int i = 0;i<10;i++){
                z+=10;
                if(i%2==0) try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message msg = Message.obtain();
                msg.arg1 = z;
                handler.sendMessage(msg);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
