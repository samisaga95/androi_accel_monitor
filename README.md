# android_accel_monitor
On entering the patient name, ID, age, and sex, your app should instantiate a database with table name 
Name_ID_Age_Sex. The table will have four columns: a) time stamp, b) x values, c) y values, and d) z values.
The app will then initiate a service that will connect to an accelerometer and collect data at a sampling 
frequency of 1 Hz. The data will be stored in the table with the time stamp of a sample and the raw three 
axes values of the accelerometer. 

It also has the capability of uploading the data base created to a web server. When we download the database,
it plots the latest 10 data points.
