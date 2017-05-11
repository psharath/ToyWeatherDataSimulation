# Toy Weather  Game Simulator
This is a toy weather simulator for game, which actually works by creating a model from sample data and then generating weather data from this model. The file ToyWeatherRest.java is used to download the  weather data using  weather.properties and save it in the local data  folder. The toyweather.hql  file is used to create external hive table on hdfs location‘/tmp/work/weatherdata’.  Filemove.sh is used to  execute the hdfs file move command and toyweather.hql file.  Currently I have generated data for 10 places defined in weather.properties file. The generated data file name is generated_weather_data.psv.

# Requirements

This application requires below  
 Java:
     It needs Java 1.7 and uses below Java packages.
•	JSON ( for read the data from Rest API )
•	FileWriter ,BufferReader( for writing generated data into local file )
•	Wunderground RestAPI ( to download weather data from wunderground )
Hadoop:
It needs Hadoop 2.x and Hive 2.x

