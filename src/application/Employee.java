package application;


import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;

public class Employee 
{
    private final SimpleStringProperty Associate_ID;
    private final SimpleStringProperty First_Name;
    private final SimpleStringProperty Last_Name;
    private final SimpleStringProperty Associate_Start_Date;
    private final SimpleStringProperty Associate_Birthday;
    private final SimpleStringProperty Social_Security_Number;
    private final SimpleStringProperty Phone_Number;
    private final SimpleFloatProperty Hourly_Rate;

    public Employee(String id, String firstName, String lastName, String associateStartDate, String associateBirthday, String socialSecurityNumber, String phoneNumber, Float hourlyRate)
    {
        this.Associate_ID = new SimpleStringProperty(id);
        this.First_Name = new SimpleStringProperty(firstName);
        this.Last_Name = new SimpleStringProperty(lastName);
        this.Associate_Start_Date = new SimpleStringProperty(associateStartDate);
        this.Associate_Birthday = new SimpleStringProperty(associateBirthday);
        this.Social_Security_Number = new SimpleStringProperty(socialSecurityNumber);
        this.Phone_Number = new SimpleStringProperty(phoneNumber);
        this.Hourly_Rate = new SimpleFloatProperty(hourlyRate);
    }

    public String getAssociate_ID()
    {
        return Associate_ID.get();
    }

    public String getFirst_Name()
    {
        return First_Name.get();
    }

    public String getLast_Name()
    {
        return Last_Name.get();
    }

    public String getAssociate_Start_Date()
    {
        return Associate_Start_Date.get();
    }

    public String getAssociate_Birthday()
    {
        return Associate_Birthday.get();
    }

    public String getSocial_Security_Number()
    {
        return Social_Security_Number.get();
    }

    public String getPhone_Number()
    {
        return Phone_Number.get();
    }

    public Float getHourly_Rate()
    {
        return Hourly_Rate.get();
    }

}
