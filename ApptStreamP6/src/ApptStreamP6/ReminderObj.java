//Giancarlo Fruzzetti
// COP 2805 Project 5
//2-24-2023
//Reminder Object



package ApptStreamP6;

import java.time.ZonedDateTime;

public class ReminderObj {

    private Contact contact;
    private String remindertxt;
    private ZonedDateTime zdt;

    ReminderObj() {}

    ReminderObj(Contact C, String rtext, ZonedDateTime apptime)
    {
        this.contact=C;
        this.remindertxt=rtext;
        this.zdt=apptime;
    }

    public Contact getContact() {
        return contact;
    }

    public String getReminder()
    {
        return remindertxt;
    }

    public ZonedDateTime getTime()
    {
        return zdt;
    }


    @Override
    public String toString() {
        //String s = "Contact: " + this.contact  + " \nreminder: " + this.remindertxt + " \nappt reminder time: " + this.zdt;
        String t = this.remindertxt;
        return t;
    }

}
