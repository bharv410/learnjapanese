package com.panafold.helpers;
import com.panafold.R;
import com.panafold.main.MainActivity;

import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
                            
 
public class MyAlarmService extends Service 
{
      
   private NotificationManager mManager;
 
    @Override
    public IBinder onBind(Intent arg0)
    {
       // TODO Auto-generated method stub
        return null;
    }
 
    @Override
    public void onCreate() 
    {
       // TODO Auto-generated method stub  
       super.onCreate();
       System.out.println("Created");
    }
 
   @Override
   public int onStartCommand(Intent intent, int flags, int startId)
   {
	   
System.out.println("Start");    
Intent intent1 = new Intent(this, MainActivity.class);
       PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);
       
       NotificationCompat.Builder mBuilder =
    		    new NotificationCompat.Builder(this)
    		    .setSmallIcon(R.drawable.ic_launcher)
    		    .setContentTitle("Time to learn another new Japanese Word!")
    		    .setContentText("Tanuki");
       mBuilder.setContentIntent(pIntent);
       
    // Gets an instance of the NotificationManager service
       mManager= 
               (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
       // Builds the notification and issues it.
       mManager.notify(01, mBuilder.build());
       super.onStartCommand(intent, flags, startId);
       return START_NOT_STICKY;
    }
 
    @Override
    public void onDestroy() 
    {
        // TODO Auto-generated method stub
        super.onDestroy();
    }
 
}