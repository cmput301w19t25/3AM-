package comnickdchee.github.a3am;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
/*
    Testing for channel_ID
    Please ignore.
 */
public class ExampleService extends Service {

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        return super.onStartCommand(intent,flags,startID);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent){
        return null;
    }
}
