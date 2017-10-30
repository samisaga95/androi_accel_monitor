package com.example.santoshkumaramisagadda.assignment2;

/**
 * Created by santoshkumaramisagadda on 9/28/17.
 */

/**
 * Created by santoshkumaramisagadda on 9/28/17.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.widget.Toast;

import java.io.File;

public class Senso extends Service implements SensorEventListener{

    private SensorManager accelManage;
    private Sensor senseAccel;
    float accelValuesX[] = new float[10];
    float accelValuesY[] = new float[10];
    float accelValuesZ[] = new float[10];
    int index = 0;
    SQLiteDatabase db;
    int k=0;
    Bundle b;
    String st;
    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // TODO Auto-generated method stub
        Sensor mySensor = sensorEvent.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            index++;
            accelValuesX[index] = sensorEvent.values[0];
            accelValuesY[index] = sensorEvent.values[1];
            accelValuesZ[index] = sensorEvent.values[2];
            if(index >= 10){
                index = 0;
                accelManage.unregisterListener(this);
                //callFallRecognition();
                storeData();
                //Function to store data into table
                accelManage.registerListener(this, senseAccel, SensorManager.SENSOR_DELAY_NORMAL);
            }
        }
    }

    public void storeData()
    {
        //File dbfile= getDatabasePath("group33.db");
        db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()+"/Android/Data/group33", null);


        for (int i=0;i<10;i++)
        {
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            Toast.makeText(this, "Reached", Toast.LENGTH_LONG).show();
            db.execSQL( "insert into "+ st +"(time,x_values,y_values,z_values) values ('"+ts+"', '"+accelValuesX[i]+"', '"+accelValuesY[i]+"', '"+accelValuesZ[i]+"' );" );
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onCreate(){
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        accelManage = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senseAccel = accelManage.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        accelManage.registerListener(this, senseAccel, SensorManager.SENSOR_DELAY_NORMAL);

    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        b = intent.getExtras();
        st = b.getString("TableName");
        //Toast.makeText(Senso.this, phoneNumber, Toast.LENGTH_LONG).show();
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.
        //k = 0;
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub

        return null;
    }

}


