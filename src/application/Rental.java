package application;

import javafx.beans.property.SimpleStringProperty;

public class Rental
{
    private final SimpleStringProperty Cust_ID;
    private final SimpleStringProperty FirstName;
    private final SimpleStringProperty LastName;
    private final SimpleStringProperty Movie_ID;
    private final SimpleStringProperty Movie_Title;


    public Rental(String custId, String fName, String lName, String movieID, String title)
    {
        this.Cust_ID = new SimpleStringProperty(custId);
        this.FirstName = new SimpleStringProperty(fName);
        this.LastName = new SimpleStringProperty(lName);
        this.Movie_ID = new SimpleStringProperty(movieID);
        this.Movie_Title = new SimpleStringProperty(title);
    }

    public String getCust_ID(){
        return Cust_ID.get();
    }

    public String getFirstName(){
        return FirstName.get();
    }

    public String getLastName(){
        return LastName.get();
    }
    public String getMovie_ID(){
        return Movie_ID.get();
    }
    public String getMovie_Title() {
        return Movie_Title.get();
    }
}
