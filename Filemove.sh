#!/bin/sh
#-----------------------------------------------------------------#
#  Script  Name: Filemove.sh
#  Process Name: File move
#  Description : Moving file from local to hdfs and creation of external hive table
#  Change Log
#  Changed By               Date           Description
#  -----------------     ----------      -------------------------
#  Sharath Pappula        11/05/17            Initial
#-----------------------------------------------------------------#

#Debug
#set -x

# Variables Declaration

LOCALFILEPATH=/user/home/weatherdata.txt
HDFSFILEPATH=/tmp/work/weatherdata
HQLPATH=/LAND/ToYWEATHER

#Moving file from local to hdfs

hdfs dfs -put  ${LOCALFILEPATH} ${HDFSFILEPATH}

#Execution of hql file for Creating of external hive table

hive -f ${HQLPATH}/toyweather.hql