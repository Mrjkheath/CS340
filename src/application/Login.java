package application;

import javafx.beans.property.SimpleStringProperty;

public class Login
{
    private final SimpleStringProperty Associate_ID;
    private final SimpleStringProperty Password;

    public Login(String associateID, String password)
    {
        this.Associate_ID = new SimpleStringProperty(associateID);
        this.Password = new SimpleStringProperty(password);
    }

    public String getAssociate_ID()
    {
         return Associate_ID.get();
    }

    public String getPassword()
    {
        return Password.get();
    }
}
