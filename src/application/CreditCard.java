package application;

import javafx.beans.property.SimpleStringProperty;

public class CreditCard {
    private final SimpleStringProperty Customer_ID;
    private final SimpleStringProperty Name_On_Card;
    private final SimpleStringProperty Card_Type;
    private final SimpleStringProperty Expiration_Date;
    private final SimpleStringProperty Credit_Card_Number;
    private final SimpleStringProperty Card_Verification_Value;

    public CreditCard(String id, String nameOnCard, String cardType, String cardNumber, String expirationDate, String cvv)
    {
        this.Customer_ID = new SimpleStringProperty(id);
        this.Name_On_Card = new SimpleStringProperty(nameOnCard);
        this.Card_Type = new SimpleStringProperty(cardType);
        this.Credit_Card_Number = new SimpleStringProperty(cardNumber);
        this.Expiration_Date = new SimpleStringProperty(expirationDate);
        this.Card_Verification_Value = new SimpleStringProperty(cvv);
    }

    public String getCustomer_ID() {
        return Customer_ID.get();
    }
    public String getName_On_Card() {
        return Name_On_Card.get();
    }
    public String getCard_Type() {
        return Card_Type.get();
    }
    public String getExpiration_Date() {
        return Expiration_Date.get();
    }
    public String getCredit_Card_Number() {
        return Credit_Card_Number.get();
    }
    public String getCard_Verification_Value() {
        return Card_Verification_Value.get();
    }
}
