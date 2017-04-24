package application;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class Accounting
{
    private final SimpleStringProperty Transaction_ID;
    private final SimpleStringProperty Customer_ID;
    private final SimpleStringProperty Movie_Title;
    private final SimpleStringProperty Transaction_Type;
    private final SimpleStringProperty Date_Of_Transaction;
    private final SimpleFloatProperty Transaction_Amount;
    private final SimpleFloatProperty Income;

    public Accounting(String transID, String cusID, String movieTitle, String transType, String dateOfTransaction, Float transAmount, Float income)
    {
        this.Transaction_ID = new SimpleStringProperty(transID);
        this.Customer_ID = new SimpleStringProperty(cusID);
        this.Movie_Title = new SimpleStringProperty(movieTitle);
        this.Transaction_Type = new SimpleStringProperty(transType);
        this.Date_Of_Transaction = new SimpleStringProperty(dateOfTransaction);
        this.Transaction_Amount = new SimpleFloatProperty(transAmount);
        this.Income = new SimpleFloatProperty(income);
    }

    public String getTransaction_ID() {
        return Transaction_ID.get();
    }

    public String getCustomer_ID() {
        return Customer_ID.get();
    }

    public String getMovie_Title() {
        return Movie_Title.get();
    }

    public String isTransaction_Type() {
        return Transaction_Type.get();
    }

    public String getDate_Of_Transaction() {
        return Date_Of_Transaction.get();
    }

    public float getTransaction_Amount() {
        return Transaction_Amount.get();
    }

    public float getIncome() {
        return Income.get();
    }
}
