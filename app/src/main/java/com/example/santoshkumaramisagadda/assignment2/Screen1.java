package com.example.santoshkumaramisagadda.assignment2;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

//imported graphview
import com.example.santoshkumaramisagadda.assignment2.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.Viewport;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.GridLabelRenderer;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;


public class Screen1 extends AppCompatActivity implements SensorEventListener {


    SQLiteDatabase db;
    private SensorManager senSensorManager;
    private Sensor senAccelerometer;
    float accelValuesX[] = new float[11];
    float accelValuesY[] = new float[11];
    float accelValuesZ[] = new float[11];
    int index = 0;
    String st;
    int flag=0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen1);

        //data
        senSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);

        final GraphView graph = (GraphView) findViewById(R.id.graph);

        // customize a little bit viewport
        Viewport viewport = graph.getViewport();
        viewport.setYAxisBoundsManual(true);
        viewport.setXAxisBoundsManual(true);
        viewport.setMinY(0);
        viewport.setMaxY(10);
        viewport.setMinX(0);
        viewport.setMaxX(11);
        viewport.setScrollable(true);

        //setting labels for axes
        graph.getGridLabelRenderer().setHorizontalAxisTitle("Time (in sec)");
        graph.getGridLabelRenderer().setVerticalAxisTitle("Values of x,y and z axes");

        View b1=findViewById(R.id.save);
        View b2=findViewById(R.id.run);
        View b3=findViewById(R.id.stop);
        View b4=findViewById(R.id.Upload);

        b3.setEnabled(false);//The user cannot hit stop without first starting the graph

        //when you click on SAVE
        b1.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                if(checkData())
                    createTable();
                flag=1;
            };
        });

        b4.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                upload();
            }
        });

        //when you click on RUN
        b2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                EditText t1=(EditText)findViewById(R.id.idText);
                EditText t2=(EditText)findViewById(R.id.ageText);
                EditText t3=(EditText)findViewById(R.id.nameText);
                RadioButton rb1=(RadioButton) findViewById(R.id.radioFemale);
                RadioButton rb2=(RadioButton)findViewById(R.id.radioMale);

                String st=t3.getText().toString()+"_"+t1.getText().toString()+"_"+t2.getText().toString()+"_";
                if(rb1.isChecked())
                    st+="Female";
                else
                    st+="Male";

                float[] arr_x= new float[11];
                float[] arr_y= new float[11];
                float[] arr_z= new float[11];
                try
                {
                    SQLiteDatabase.openDatabase(Environment.getExternalStorageDirectory()+"/Android/Data/CSE535_Assignment2/group33", null,0);
                    db.beginTransaction();
                    try
                    {
                        //to extract data
                        Cursor c1 = db.rawQuery("SELECT x_values FROM "+st,null);
                        Cursor c2 = db.rawQuery("SELECT y_values FROM "+st,null);
                        Cursor c3 = db.rawQuery("SELECT z_values FROM "+st,null);
                        int a=0;
                        c1.moveToFirst();
                        c2.moveToFirst();
                        c3.moveToFirst();
                        do{
                            arr_x[a]=c1.getFloat(c1.getColumnIndex("x_values"));
                            arr_y[a]=c2.getFloat(c2.getColumnIndex("y_values"));
                            arr_z[a]=c3.getFloat(c3.getColumnIndex("z_values"));
                            a++;
                        }while(c1.moveToNext());
                    }
                    catch (SQLiteException e) {
                        System.out.print("Error");
                    }
                    finally {
                        db.endTransaction();
                    }
                }catch (SQLException e){

                    Toast.makeText(Screen1.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
                final LineGraphSeries<DataPoint> series1 = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0, arr_x[0]),
                        new DataPoint(1, arr_x[1]),
                        new DataPoint(2, arr_x[2]),
                        new DataPoint(3, arr_x[3]),
                        new DataPoint(4, arr_x[4]),
                        new DataPoint(5, arr_x[5]),
                        new DataPoint(6, arr_x[6]),
                        new DataPoint(7, arr_x[7]),
                        new DataPoint(8, arr_x[8]),
                        new DataPoint(9, arr_x[9]),
                        new DataPoint(10, arr_x[10])
                });
                series1.setTitle("X-axis");
                series1.setColor(Color.GREEN);
                series1.setDrawDataPoints(true);
                series1.setDataPointsRadius(10);
                series1.setThickness(8);

                final LineGraphSeries<DataPoint> series2 = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0, arr_y[0]),
                        new DataPoint(1, arr_y[1]),
                        new DataPoint(2, arr_y[2]),
                        new DataPoint(3, arr_y[3]),
                        new DataPoint(4, arr_y[4]),
                        new DataPoint(5, arr_y[5]),
                        new DataPoint(6, arr_y[6]),
                        new DataPoint(7, arr_y[7]),
                        new DataPoint(8, arr_y[8]),
                        new DataPoint(9, arr_y[9]),
                        new DataPoint(10, arr_y[10])
                });
                series2.setTitle("Y-axis");
                series2.setColor(Color.RED);
                series2.setDrawDataPoints(true);
                series2.setDataPointsRadius(10);
                series2.setThickness(8);

                final LineGraphSeries<DataPoint> series3 = new LineGraphSeries<>(new DataPoint[] {
                        new DataPoint(0, arr_z[0]),
                        new DataPoint(1, arr_z[1]),
                        new DataPoint(2, arr_z[2]),
                        new DataPoint(3, arr_z[3]),
                        new DataPoint(4, arr_z[4]),
                        new DataPoint(5, arr_z[5]),
                        new DataPoint(6, arr_z[6]),
                        new DataPoint(7, arr_z[7]),
                        new DataPoint(8, arr_z[8]),
                        new DataPoint(9, arr_z[9]),
                        new DataPoint(10, arr_z[10])
                });
                series3.setTitle("Z-axis");
                series3.setColor(Color.BLUE);
                series3.setDrawDataPoints(true);
                series3.setDataPointsRadius(10);
                series3.setThickness(8);

                startGraph(graph,series1);
                startGraph(graph,series2);
                startGraph(graph,series3);

            };
        });


        //when you click CLEAR
        b3.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                stopGraph(graph);
            };
        });
    }

    protected void createTable()
    {


        EditText t1=(EditText)findViewById(R.id.idText);
        EditText t2=(EditText)findViewById(R.id.ageText);
        EditText t3=(EditText)findViewById(R.id.nameText);
        RadioButton rb1=(RadioButton) findViewById(R.id.radioFemale);
        RadioButton rb2=(RadioButton)findViewById(R.id.radioMale);

        st=t3.getText().toString()+"_"+t1.getText().toString()+"_"+t2.getText().toString()+"_";
        if(rb1.isChecked())
            st+="Female";
        else
            st+="Male";

        final String CREATE_TABLE =
                "CREATE TABLE "+st+"("
                        + "time STRING PRIMARY KEY,"
                        + "x_values integer,"
                        + "y_values integer,"
                        + "z_values integer);";

        try{
            db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()+"/Android/Data/CSE535_Assignment2/group33", null);
            db.beginTransaction();
            try {
                db.execSQL(CREATE_TABLE);
                db.setTransactionSuccessful(); //commit your changes
            }
            catch (SQLiteException e) {
                Toast.makeText(Screen1.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
            finally {
                db.endTransaction();
            }
        }catch (SQLException e){

            Toast.makeText(Screen1.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        flag=1;


    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // TODO Auto-generated method stub

            Sensor mySensor = sensorEvent.sensor;

            if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER && flag==1)
            {
                while (index <= 10)
                {

                    accelValuesX[index] = sensorEvent.values[0];
                    accelValuesY[index] = sensorEvent.values[1];
                    accelValuesZ[index] = sensorEvent.values[2];

                    senSensorManager.unregisterListener(this);

                    try {
                        if(index==0)
                        {
                            Toast.makeText(this, "Wait...Data is being saved.", Toast.LENGTH_LONG).show();
                        }
                        storeData(index);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    index++;
                    //Function to store data into table
                    Toast.makeText(this,"Data Saved",Toast.LENGTH_SHORT).show();
                }
                index=0;
            }

    }



    public void storeData(int i) throws InterruptedException {

        db = SQLiteDatabase.openOrCreateDatabase(Environment.getExternalStorageDirectory()+"/Android/Data/CSE535_Assignment2/group33", null);
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        //Toast.makeText(this, "Reached", Toast.LENGTH_LONG).show();
        Thread.sleep(1000);
        db.execSQL( "insert into "+ st +"(time,x_values,y_values,z_values) values ('"+ts+"', '"+accelValuesX[i]+"', '"+accelValuesY[i]+"', '"+accelValuesZ[i]+"' );" );

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }


    protected boolean checkData()
    {
        EditText t1=(EditText)findViewById(R.id.idText);
        EditText t2=(EditText)findViewById(R.id.ageText);
        EditText t3=(EditText)findViewById(R.id.nameText);
        RadioButton rb1=(RadioButton) findViewById(R.id.radioFemale);
        RadioButton rb2=(RadioButton)findViewById(R.id.radioMale);

        if(t1.getText().toString()!=null && t2.getText().toString()!=null && t3.getText().toString()!=null && (rb1.isChecked() || rb2.isChecked()))
            return true;
        else {
            Toast.makeText(this,"Information Missing",Toast.LENGTH_LONG).show();
            return false;
        }
    }

    protected void startGraph(GraphView graph,LineGraphSeries series)
    {
        graph.addSeries(series); //plot the data in the graphview
        View b2=findViewById(R.id.stop);
        View b1=findViewById(R.id.run);
        //b1.setEnabled(false);
        b2.setEnabled(true);

    }

    protected void stopGraph (GraphView graph)
    {
        View b2=findViewById(R.id.stop);
        View b1=findViewById(R.id.run);
        //b1.setEnabled(true);
        b2.setEnabled(false);
        graph.removeAllSeries(); //clears the graphview
    }

    protected int upload()
    {

        /************* Php script path ****************/
        String upLoadServerUri = "http://10.218.110.136/CSE535Fall17Folder/UploadToServer.php";

        String fileName = Environment.getExternalStorageDirectory()+"/Android/Data/CSE535_Assignment2/group33";

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(fileName);

        if (!sourceFile.isFile())
        {
            return 0;
        }
        else
        {
            int serverResponseCode=0;
            try {

                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(upLoadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", fileName);

                dos = new DataOutputStream(conn.getOutputStream());

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""+ fileName + "\"" + lineEnd);
                //dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {

                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();


                if(serverResponseCode == 200)
                {
                            Toast.makeText(this, "File Upload Complete.", Toast.LENGTH_SHORT).show();

                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {

                ex.printStackTrace();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return serverResponseCode;

        } // End else block
    }
}






