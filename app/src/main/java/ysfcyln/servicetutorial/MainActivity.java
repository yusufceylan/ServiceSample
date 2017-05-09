package ysfcyln.servicetutorial;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    // https://www.youtube.com/playlist?list=PLfuE3hOAeWhbm-_mNEbVdQuaac7Rd4TgZ

    private TextView mServiceStatus;
    private Button mBtnStartService, mBtnStopService, mBtnBindService, mBtnUnbindService, mBtnGetRandomNumber;

    private MyService myService;
    private boolean isServiceBound;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("Service Demo", "MainActivity thread id: " + Thread.currentThread().getId());

        mServiceStatus = (TextView) findViewById(R.id.tv_service_status);
        mBtnStartService = (Button) findViewById(R.id.btn_start_service);
        mBtnStopService = (Button) findViewById(R.id.btn_stop_service);
        mBtnBindService = (Button) findViewById(R.id.btn_bind_service);
        mBtnUnbindService = (Button) findViewById(R.id.btn_unbind_service);
        mBtnGetRandomNumber = (Button) findViewById(R.id.btn_get_random_number);

        mBtnStartService.setOnClickListener(this);
        mBtnStopService.setOnClickListener(this);
        mBtnBindService.setOnClickListener(this);
        mBtnUnbindService.setOnClickListener(this);
        mBtnGetRandomNumber.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_start_service:
                startService(new Intent(this, MyService.class));
                break;
            case R.id.btn_stop_service:
                stopService(new Intent(this, MyService.class));
                break;
            case R.id.btn_bind_service:
                bindMyService();
                break;
            case R.id.btn_unbind_service:
                unbindMyService();
                break;
            case R.id.btn_get_random_number:
                getRandomNumber();
                break;
        }
    }


    private void bindMyService(){
        if(serviceConnection == null){
            serviceConnection = new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName name, IBinder service) {
                    MyService.MyServiceBinder myServiceBinder = (MyService.MyServiceBinder) service;
                    myService = myServiceBinder.getService();
                    isServiceBound=true;
                }

                @Override
                public void onServiceDisconnected(ComponentName name) {
                    isServiceBound = false;
                }
            };
        }

        bindService(new Intent(this,MyService.class),serviceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unbindMyService(){
        if (isServiceBound){
            unbindService(serviceConnection);
            isServiceBound=false;
        }
    }

    private void getRandomNumber(){
        if(isServiceBound){
            mServiceStatus.setText("Random Number: " + myService.getRandomNumber());
        } else {
            mServiceStatus.setText("Service not bound");
        }
    }


}
