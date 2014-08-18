package com.panafold.helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
 
public class MyReciever extends BroadcastReceiver
{
      
    @Override
    public void onReceive(Context context, Intent intent)
    {
       Intent service1 = new Intent(context, MyAlarmService.class);
       context.startService(service1);
       System.out.println("onRecieve");
       System.out.println("onRecieve");
       System.out.println("onRecieve");
       System.out.println("onRecieve");
       
    }   
}
