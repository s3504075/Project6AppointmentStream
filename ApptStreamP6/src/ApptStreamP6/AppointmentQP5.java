//Giancarlo Fruzzetti
// COP 2805 Project 5
//2-24-2023
//main program


package ApptStreamP6;

import java.time.*;
import java.time.format.FormatStyle;
import java.util.*;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import Dispatch.Dispatcher; //import local dispatcher


public class AppointmentQP5 implements CalendarReminder, Dispatcher<ReminderObj>
{

    public static final Scanner input = new Scanner(System.in); //a global scanner input

    private ArrayList<Appointment> clientappts = new ArrayList<>();//array list of client appointments
    private Queue<ReminderObj> queue = new LinkedList<ReminderObj>(); //dispatching queue

    private Stream<ReminderObj> stream = queue.stream();


    public AppointmentQP5() {
    }

    public void dispatch(ReminderObj robj) {
        //takes reminder object and adds it to queue outputs that reminder is being sent
        this.queue.add(robj);
        Reminder rem;
        rem=robj.getContact().getReminder();
        //System.out.println("current queue length is " + this.queue.size());
        if(rem==Reminder.EMAIL) {  //text reminder
            System.out.println("sending appointment email reminder to " + robj.getContact().getName()
                    +  " "  + robj.getContact().getEmail() + "\n");
            //System.out.println(O.toString() + "\n");

        }
        else { //email reminder
            System.out.println("sending appointment text reminder to " + robj.getContact().getName()
                    +  " "  + robj.getContact().getPhone() + "\n");
            //System.out.println(O.toString() + "\n");

        }
    }

    public void runAppts(ZonedDateTime reminder, AppointmentQP5 a1)
    //constructs and sends the reminders to the queue
    {
        int timecomparison;
        //String Rem;
        ZonedDateTime currentTime;
        ReminderObj robj; //reminder objectd

        for (Appointment A : a1.clientappts)
        {
            currentTime=ZonedDateTime.now();
            timecomparison=currentTime.compareTo(reminder);
            //System.out.println(timecomparison);
            if(timecomparison > 0)
            {
                robj=a1.buildReminder(A);  //build reminder object
                a1.sendReminder(robj);  //send reminder object
            }

        }

    }

    public ReminderObj buildReminder(Appointment appt) //build reminder and return to main
    {

        //datetime manipulator functions https://docs.oracle.com/javase/10/docs/api/java/time/ZonedDateTime.html#get(java.time.temporal.TemporalField)
        //StringBuilder manipulator functions https://docs.oracle.com/javase/7/docs/api/java/lang/StringBuilder.html
        //Guide to Date Time Formatter: https://www.baeldung.com/java-datetimeformatter
        StringBuilder S = new StringBuilder("");
        StringBuilder plusses = new StringBuilder("\n");
        ReminderObj O; //reminder object
        String appt_as_string; //returns final string
        //LocalDate ldt; //for printing local date
        //int hour;  //to print local hour
        //int minute; //to print local minute
        int max,min,maxlen,insloc,firststars; //setting string lengths with indexof

        //S.append("\n\nSending the following message to " + apt.getContact().getName()+ " " + appt.getContact().getPhone());
        ResourceBundle res = ResourceBundle.getBundle("ApptStreamP6.Apptproperties", appt.getContact().getLocale());
        S.append("\n+ Date: " + appt.getDtf());
        S.append("\n+ "+res.getString("Hello") + " " + appt.getContact().getName());
        S.append("\n+ "+res.getString("translaterem"));
        S.append("\n+ Provider:" + res.getString("title") + " " + "Dr. I.M.A. Quack.");
        S.append("\n+ Description: " + appt.getDescription());

        //ldt=appt.getZdt().toLocalDate();
        //hour=appt.getZdt().getHour();
        //minute=appt.getZdt().getMinute();
        //S.append("\n+ Date: " + ldt);
        //S.append("\n+ Time: " + hour + ":" + minute + " " + appt.getZdt().getZone());
        min=S.indexOf("+ Date",0);
        max=S.indexOf("Description.",0)+64; //beginning + end
        //System.out.println(max+ " " + min);
        maxlen=max-min;
        //System.out.println(maxlen);
        for (int i=0; i < maxlen+1; i++)
        {
            plusses.append("+");
        }
         S.append(plusses);
        insloc=S.indexOf("+ Date");
        //System.out.println(insloc);
        S.insert(insloc-1,plusses);
        appt_as_string=S.toString();
        O=new ReminderObj(appt.getContact(), appt_as_string, appt.getReminderTime());
        return O;

    }

    public void sendReminder(ReminderObj robj) //sends reminders with the constructed reminder objects
    {
        //System.out.println(O.toString());

        //lambda call for dispatcher
        Dispatcher<ReminderObj> d = (c)-> {
            this.queue.add(c);

            //System.out.println("current queue length is " + this.queue.size());
            if(robj.getContact().getReminder()==Reminder.TEXT) {  //text reminder
                System.out.println("sending appointment email reminder to " + c.getContact().getName()
                        +  " "  + c.getContact().getEmail() + "\n");
                //System.out.println(c.toString() + "\n");

            }
            else { //email reminder
                System.out.println("sending appointment text reminder to " + c.getContact().getName() +  " "
                        + c.getContact().getPhone() + "\n");
                //System.out.println(c.toString() + "\n");

            }
        };
        d.dispatch(robj);
    }


    public void addAppointments(Appointment... appointments) {
        for (Appointment A : appointments) {
            clientappts.add(A);
        }
    }

    public static int getRandomMonth() { //gets random month for the appointments
        // define the range
        int max = 12;
        int min = 1;
        int range = max - min + 1;

        // generate random numbers within 1 to 10
        int rand = (int) (Math.random() * range) + min;
        //System.out.println("Months ahead appt:" + rand); testing random month
        return (rand);

  }

    public static int getRandomHours() {  //gets random hours for the appointments
        // define the range
        int max = 24;
        int min = 2;
        int range = max - min + 1;

        // generate random numbers within 1 to 10
        int rand = (int) (Math.random() * range) + min;
        //System.out.println("hours before reminder:" + rand); testing random hours
        return (rand);

    }

    public static DateTimeFormatter Dtf(Appointment clientappt)
    //formats the date time with the formatter for each client
    {
        DateTimeFormatter formatter =
                DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL);
        formatter =
                formatter.localizedBy(clientappt.getContact().getLocale());
        return(formatter);
    }


    public static void main(String[] args) {


        int month, currentmonth, currentyr, numberappts;//random appt month, currentmonth, number appts to generate as per user
        ZonedDateTime currentTime, apptdate, appttime, reminder;
        var zone = ZoneId.of("US/Eastern");
        //int minushours;
        //String tbf;
        //final int appthours = 12; //set offset for the appointment time vs now
        //int timecomparison; //is current time later than reminder time
        //String Rem;  //for the final reminder string in the box
        String Date, Date2, Date3, Date4; //for the formatted date through the formatter
        //default info
        Contact client = new Contact("Olivia", "Migiano", "OliviaM@att.net",
                "904-666-2424", Reminder.EMAIL, new Locale("EN"), zone);
        Contact client2 = new Contact("Olivier", "Giroud", "olg@acmilan.com",
                "904-252-6633", Reminder.EMAIL, new Locale("FR"), zone);
        Contact client3 = new Contact("Robin", "Gosens", "RG8@intermilan.com",
                "966-222-2222", Reminder.TEXT, new Locale("DE"), zone);
        Contact client4 = new Contact("Ciro", "Immobile", "Ciro@lazio.com",
                "696-969-6666", Reminder.TEXT, new Locale("IT"),zone );
        Contact client5 = new Contact("Eric", "Zheng", "Eric@China.com",
                "232-22-2232", Reminder.EMAIL, new Locale("ZH"), zone);
        String apptitle = "Medical Appointment with Dr. I.M.A. Quack.";
        String description = "Pending Appointment.";
        AppointmentQP5 A1 = new AppointmentQP5();  //object



        System.out.print("Enter number of random appointments for the clients: ");
        numberappts = input.nextInt();


        reminder=ZonedDateTime.now(); //set this default to this moment to ensure delivery

        //set appt general information
        for (int i = 0; i < numberappts; i++) //create n random appts for 4 clients
        {
            Appointment clientappt = new Appointment(); //pass info to constructor here
            Appointment clientappt2 = new Appointment();
            Appointment clientappt3 = new Appointment();
            Appointment clientappt4 = new Appointment();
            Appointment clientappt5 = new Appointment();

            clientappt.setContact(client); //set the client info, clientappt.getContact() gets the contact
            clientappt2.setContact(client2);
            clientappt3.setContact(client3);
            clientappt4.setContact(client4);
            clientappt5.setContact(client5);

            currentTime = ZonedDateTime.now(); //get current time
            String formattedZdt = currentTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME);
            ZonedDateTime zoneddatetime = ZonedDateTime.parse(formattedZdt);
            month = AppointmentQP5.getRandomMonth(); //get random month for the appt
            currentmonth = zoneddatetime.getMonthValue(); //get current month to help set the appointment date
            //minushours = AppointmentQP5.getRandomHours();
            //reminder = apptdate.minusHours(minushours);
            //currentyr = zoneddatetime.getYear();

            //System.out.println(currentTime + " " + month + " " + currentmonth + " " + currentyr + " " + minushours);
            if (month + currentmonth < 12) { //you don't need to add a year to the current one, appt in this year
                apptdate = currentTime.plusMonths(month); //set to random month n months into the future
                //System.out.println(apptdate);
                //apptdate=ZonedDateTime.now().plusHours(appthours);

                apptdate = apptdate.plusHours(1); //add hour to initial time
                Date=apptdate.format(Dtf(clientappt));  //client 1 appointment date
                clientappt.setAppointment(apptitle, description, Date, reminder); //set appt for client 1
                //A1.addAppointments(clientappt); //add to the queue

                apptdate = apptdate.plusHours(2); //add 2 hours to the initial date for next appt
                Date=apptdate.format(Dtf(clientappt2)); //client 2 date string for reminder
                clientappt2.setAppointment(apptitle, description, Date, reminder); //set appt for client 2
                //A1.addAppointments(clientappt2); //add to the queue

                apptdate = apptdate.plusHours(3); //add 3 hours for next appt
                Date=apptdate.format(Dtf(clientappt3)); //client 3 date string for reminder
                clientappt3.setAppointment(apptitle, description, Date, reminder); //create appt object
                //A1.addAppointments(clientappt3);

                apptdate = apptdate.plusHours(4); //add 4 hours for next appt
                Date=apptdate.format(Dtf(clientappt4)); //client 4
                clientappt4.setAppointment(apptitle, description, Date, reminder); //create appt object

                apptdate = apptdate.plusHours(5); //add 5 hours for next appt
                Date=apptdate.format(Dtf(clientappt5)); //client 4
                clientappt5.setAppointment(apptitle, description, Date, reminder);//create appt object

                A1.addAppointments(clientappt,clientappt2,clientappt3,clientappt4, clientappt5);
            }
            else //overlapped to 13 mos
            {

                apptdate = currentTime.plusYears(1); //add year
                apptdate = apptdate.plusMonths(month + currentmonth - 12); //add month
                //System.out.println(apptdate);

                //reminder = apptdate.minusHours(minushours);
                //apptdate=ZonedDateTime.now().plusHours(appthours);
                apptdate = apptdate.plusHours(1); //add hour to initial time
                Date=apptdate.format(Dtf(clientappt));  //client 1 appointment date
                clientappt.setAppointment(apptitle, description, Date, reminder); //set appt for client 1
                //A1.addAppointments(clientappt); //add to the queue

                apptdate = apptdate.plusHours(2); //add 2 hours to the initial date for next appt
                Date=apptdate.format(Dtf(clientappt2)); //client 2 date string for reminder
                clientappt2.setAppointment(apptitle, description, Date, reminder); //set appt for client 2
                //A1.addAppointments(clientappt2); //add to the queue

                apptdate = apptdate.plusHours(3); //add 3 hours for next appt
                Date=apptdate.format(Dtf(clientappt3)); //client 3 date string for reminder
                clientappt3.setAppointment(apptitle, description, Date, reminder); //create appt object
                //A1.addAppointments(clientappt3);

                apptdate = apptdate.plusHours(4); //add 4 hours for next appt
                Date=apptdate.format(Dtf(clientappt4)); //client 4
                clientappt4.setAppointment(apptitle, description, Date, reminder); //create appt object
                //A1.addAppointments(clientappt4);

                apptdate = apptdate.plusHours(5); //add 5 hours for next appt
                Date=apptdate.format(Dtf(clientappt5)); //client 4
                clientappt5.setAppointment(apptitle, description, Date, reminder);//create appt object
                //A1.addAppointments(clientappt5);

                A1.addAppointments(clientappt,clientappt2,clientappt3,clientappt4, clientappt5);
            }
            //var appointmenttime = ZonedDateTime.of(apptdate, appttime, zone);
            //var remindertime = ZonedDateTime.of(reminder, appttime, zone);
            //clientappt.display();
            //System.out.println(apptitle + " " + description + " " + apptdate);
            //System.out.println(appttime + " " + reminder);
        }
        if (A1.clientappts.isEmpty() == false)
        {

            // for (Appointment A : A1.clientappts)
            // {
            //     A.display();

            // }

            A1.runAppts(reminder, A1);
         /*
            for (Appointment A : A1.clientappts)
            {
                currentTime=ZonedDateTime.now();
                timecomparison=currentTime.compareTo(reminder);
                //System.out.println(timecomparison);
                if(timecomparison > 0)
                {
                    Rem=A1.buildReminder(A);
                    A1.sendReminder(Rem);
                }

            } */
        }
        //System.out.println("starting forEach");
        A1.stream.forEach(System.out::println);

    }


}
