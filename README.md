# Crab the cookies lover 

## Building application

This console application needs [Java SDK 17](https://openjdk.org/projects/jdk/17/) to be installed on your computer.
Once you installed it. You can build using following step.

1. Open Terminal app or Console(on windows) at this folder
2. Run `./gradlew build`
3. Run in terminal `./build/scripts/crab -f cookie-sample.csv -d 2018-12-09`

## Few Requirements on sample file
1. Cookies log file must be in CSV format with `cookies` as first columns and `timestamp` as second column.
2. Cookies log file must contain entries in descending order of time stamp.