//Giancarlo Fruzzetti
// COP 2805 Project 5
//2-24-2023
//Calendar Reminder Public Interface


package ApptStreamP6;

public interface CalendarReminder
{
    // build a reminder using an appointment
   // public String buildReminder(Appointment appt);
    // send a reminder using contact's preferred notification method
   // public void sendReminder(String reminder);

    public ReminderObj buildReminder(Appointment appt);
    // send a reminder using contact's preferred notification method
    public void sendReminder(ReminderObj reminder);


}
