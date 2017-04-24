package application;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class RentalCustomer {

    private final SimpleStringProperty Cust_ID;
    private final SimpleStringProperty FirstName;
    private final SimpleStringProperty LastName;
    private final SimpleIntegerProperty NumRentals;
    private final SimpleStringProperty CC_NUM;
    private final SimpleStringProperty CC_Exp;

    public RentalCustomer(String id, String firstName, String lastName, Integer numRentals, String ccNumber, String ccExpiration)
    {
        this.Cust_ID = new SimpleStringProperty(id);
        this.FirstName = new SimpleStringProperty(firstName);
        this.LastName = new SimpleStringProperty(lastName);
        this.NumRentals = new SimpleIntegerProperty(numRentals);
        this.CC_NUM = new SimpleStringProperty(ccNumber);
        this.CC_Exp = new SimpleStringProperty(ccExpiration);
    }

    public String getCust_ID()
    {
        return Cust_ID.get();
    }

    public String getFirstName()
    {
        return FirstName.get();
    }

    public String getLastName()
    {
        return LastName.get();
    }

    public Integer getNumRentals()
    {
        return NumRentals.get();
    }

    public String getCC_NUM()
    {
        return CC_NUM.get();
    }

    public String getCC_Exp()
    {
        return CC_Exp.get();
    }
}