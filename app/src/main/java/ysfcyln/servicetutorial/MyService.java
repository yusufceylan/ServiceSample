package ysfcyln.servicetutorial;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.Random;

public class MyService extends Service {

    private int mRandomNumber;
    private boolean mIsRandomGeneratorOn;

    private final int MIN = 0;
    private final int MAX = 100;

    public MyService() {
    }

    // Returns the MyService instance
    public class MyServiceBinder extends Binder {
        public MyService getService(){
            return MyService.this;
        }
    }

    private IBinder mBinder = new MyServiceBinder();

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        //throw new UnsupportedOperationException("Not yet implemented");
        Log.i("Service Demo", "In onBind");
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("Service Demo", "In onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.i("Service Demo", "In onRebind");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopRandomNumberGenerator();
        Log.i("Service Demo", "Service Destroyed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Service Demo", "In onStartCommand, thread id: " + Thread.currentThread().getId());

        mIsRandomGeneratorOn = true;
        new Thread(new Runnable() {
            @Override
            public void run() {
                startRandomNumberGenerator();
            }
        }).start();

        return START_STICKY;
    }

    // Generate random number
    private void startRandomNumberGenerator(){
        while (mIsRandomGeneratorOn){
            try {
                Thread.sleep(1000);
                if (mIsRandomGeneratorOn){
                    mRandomNumber = new Random().nextInt(MAX)+MIN;
                    Log.i("Service Demo", "In onStartCommand, thread id: " + Thread.currentThread().getId() + " Random Number: " + mRandomNumber);
                }
            } catch (Exception e){
                Log.i("Service Demo", "Thread Interrupted");
            }
        }
    }

    // Stops generate random number
    private void stopRandomNumberGenerator(){
        mIsRandomGeneratorOn = false;
    }

    public int getRandomNumber(){
        return mRandomNumber;
    }

}
