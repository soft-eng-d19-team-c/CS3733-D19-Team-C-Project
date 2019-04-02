package model;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.Instant;

public class DateTimeMaker {
    Date date;

    public Timestamp addTime(String time){
        Instant instant = this.date.toInstant();
        int numToAdd = 0;
        String[] hoursAndMinutes = time.split("/:/g");
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
        numToAdd = minutesToMilliseconds(hrs*60) + minutesToMilliseconds(minutes) + minutesToMilliseconds(ampm * 12 * 60);
        Timestamp dateTime = new Timestamp(0);
        return dateTime;
    }

    private static int minutesToMilliseconds(int num){
        return num * 60 * 1000;
    }
}
