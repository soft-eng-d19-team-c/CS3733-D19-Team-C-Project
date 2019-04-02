package model;

import java.sql.Timestamp;
import java.time.LocalDate;

public class DateTimeMaker {
    LocalDate date;
    int dateInMilliseconds;

    public DateTimeMaker(LocalDate date) {
        this.date = date;
        this.dateInMilliseconds = setDateInMilliseconds();
    }

    private int setDateInMilliseconds(){
        return yearToMilliseconds(this.date.getYear()) + monthToMilliseconds(this.date.getMonthValue(), this.date.isLeapYear()) + daysToMilliseconds(this.date.getDayOfMonth());
    }

    public Timestamp addTime(String time){
        int numToAdd = 0;
        String[] hoursAndMinutes = time.split(":");
        int hrs = Integer.parseInt(hoursAndMinutes[0]); //gets the hour
        int ampm = 0;
        int minutes = 0;
        if(hrs == 12){
            hrs = 0;
        }
        if(hoursAndMinutes[1].charAt(2) == 'p'){//finds if the time is in the am or pm
            ampm = 1;
        }
        if(hoursAndMinutes[1].charAt(0) == '3'){//finds if this is at the hour or at the half hour
            minutes = 30;
        }
        numToAdd = hoursToMilliseconds(hrs) + minutesToMilliseconds(minutes) + hoursToMilliseconds(ampm * 12) + this.dateInMilliseconds;
        Timestamp dateTime = new Timestamp(numToAdd);
        return dateTime;
    }

    private static int minutesToMilliseconds(int num){
        return num * 60 * 1000;
    }

    private static int hoursToMilliseconds(int num){
        return num * 60 * 60 * 1000;
    }

    private int yearToMilliseconds(int year){
        int milliseconds = 0;
        int yearVal = year - 1970;
        for(int i = 0; i < yearVal - 1; i++){ //excludes the year of the booking
            if(yearVal%4 == 2){
                milliseconds += 366 * 24 * 60 * 60 * 1000;
            }
            else  milliseconds += 365 * 24 * 60 * 60 * 1000;
        }
        return milliseconds;
    }

    private int monthToMilliseconds(int month, boolean isLeapYear){
        int milliseconds = 0;
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

    private int daysToMilliseconds(int day){
        return (day - 1)* 24 * 60 * 60 * 1000;
    }
}
