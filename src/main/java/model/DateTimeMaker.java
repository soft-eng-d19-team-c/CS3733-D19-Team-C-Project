package model;

import java.sql.Timestamp;
import java.time.LocalDate;

public class DateTimeMaker {
    LocalDate date;
    long dateInMilliseconds;

    public DateTimeMaker(LocalDate date) {
        this.date = date;
        this.dateInMilliseconds = setDateInMilliseconds();
    }

    private long setDateInMilliseconds(){
        return yearToMilliseconds(this.date.getYear()) + monthToMilliseconds(this.date.getMonthValue(), this.date.isLeapYear()) + daysToMilliseconds(this.date.getDayOfMonth());
    }

    //adds the time to the given timestamp
    public static Timestamp addTime(String time, Timestamp ts){
        long numToAdd = 0;
        String[] hoursAndMinutes = time.split(":"); //splits hours from minutes
        long hrs = Long.parseLong(hoursAndMinutes[0]); //gets the hour
        long minutes = 0;
        //if it is 12am, the hour should be set to 0
        if(hrs == 12 && hoursAndMinutes[1].charAt(2) == 'a'){
            hrs = 0;
        }
        //if it is pm, the hours need to be doubled
        if(hoursAndMinutes[1].charAt(2) == 'p'){//finds if the time is in the am or pm
            hrs *= 2;
        }
        //if it is at a half hour, add thirty minutes in milliseconds
        if(hoursAndMinutes[1].charAt(0) == '3'){//finds if this is at the hour or at the half hour
            minutes = 30;
        }
        numToAdd = hoursToMilliseconds(hrs) + minutesToMilliseconds(minutes) + ts.getTime();
//        Timestamp dateTime = new Timestamp(numToAdd);
        ts.setTime(numToAdd);
        return ts;
    }

    private static long minutesToMilliseconds(long num){
        return num * 60 * 1000;
    }

    private static long hoursToMilliseconds(long num){
        return num * 60 * 60 * 1000;
    }

    private long yearToMilliseconds(long year){
        long milliseconds = 0;
        long yearVal = year - 1970;
        for(int i = 0; i < yearVal - 1; i++){ //excludes the year of the booking
            if(yearVal%4 == 2){
                milliseconds += (366 * 24 * 60 * 60 * 1000);
            }
            else  milliseconds += (365 * 24 * 60 * 60 * 1000);
        }
        return milliseconds;
    }

    private long monthToMilliseconds(long month, boolean isLeapYear){
        long milliseconds = 0;
        for(int i = 0; i < month - 1; i++){ //excludes month of the booking
            int daysInMonth = 31;
            if(month == 3 || month == 5 || month == 8 || month == 10){ //April, June, September, November
                daysInMonth = 30;
            }
            else if(month == 1){ //if month is February
                if(isLeapYear){
                    daysInMonth = 29;
                }
                else daysInMonth = 28;
            }
            milliseconds += daysInMonth * 24 * 60 * 60 * 1000;
        }
        return milliseconds;
    }

    private long daysToMilliseconds(long day){
        return (day - 1)* 24 * 60 * 60 * 1000;
    }
}
