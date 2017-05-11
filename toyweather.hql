CREATE EXTERNAL TABLE IF NOT EXISTS toyweather (location String, position String,
date date, condition String,temperature String,pressure String,humidity String);
COMMENT ‘Weather details’
ROW FORMAT DELIMITED
FIELDS TERMINATED BY ‘|’
LINES TERMINATED BY ‘\n’
LOCATION ‘/tmp/work/weatherdata’;