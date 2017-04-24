package application;


import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Customer
{

    private final SimpleStringProperty Customer_ID;
    private final SimpleStringProperty First_Name;
    private final SimpleStringProperty Last_Name;
    private final SimpleIntegerProperty Num_Rentals;
    private final SimpleStringProperty Email;
    private final SimpleStringProperty Phone_Number;
    private final SimpleStringProperty Street_Address;
    private final SimpleStringProperty APT;
    private final SimpleStringProperty City;
    private final SimpleStringProperty State;
    private final SimpleStringProperty Zip_Code;
    private final SimpleStringProperty Date_Of_Birth;

    public Customer(String id, String firstName, String lastName, Integer numRent, String email, String phoneNumber, String streetAddress, String APT, String city, String state, String zipCode, String dateOfBirth)
    {
        this.Customer_ID = new SimpleStringProperty(id);
        this.First_Name = new SimpleStringProperty(firstName);
        this.Last_Name = new SimpleStringProperty(lastName);
        this.Num_Rentals = new SimpleIntegerProperty(numRent);
        this.Email = new SimpleStringProperty(email);
        this.Phone_Number = new SimpleStringProperty(phoneNumber);
        this.Street_Address = new SimpleStringProperty(streetAddress);
        this.APT = new SimpleStringProperty(APT);
        this.City = new SimpleStringProperty(city);
        this.State = new SimpleStringProperty(state);
        this.Zip_Code = new SimpleStringProperty(zipCode);
        this.Date_Of_Birth = new SimpleStringProperty(dateOfBirth);
    }


    public String getCustomer_ID() {
        return Customer_ID.get();
    }

    public String getFirst_Name() {
        return First_Name.get();
    }

    public String getLast_Name() {
        return Last_Name.get();
    }

    public String getEmail() {
        return Email.get();
    }

    public String getPhone_Number() {
        return Phone_Number.get();
    }

    public String getStreet_Address() {
        return Street_Address.get();
    }

    public String getAPT() {
        return APT.get();
    }

    public String getCity() {
        return City.get();
    }

    public String getState(){
        return State.get();
    }

    public String getZip_Code() {
        return Zip_Code.get();
    }

    public  String getDate_Of_Birth() {
        return Date_Of_Birth.get();
    }

    public Integer getNum_Rentals(){
        return Num_Rentals.get();
    }

    public void setCustomer_ID(String customer_ID) {
        this.Customer_ID.set(customer_ID);
    }

    public void setFirst_Name(String first_Name) {
        this.First_Name.set(first_Name);
    }

    public void setLast_Name(String last_Name) {
        this.Last_Name.set(last_Name);
    }

    public void setEmail(String email) {
        this.Email.set(email);
    }

    public void setPhone_Number(String phone_Number) {
        this.Phone_Number.set(phone_Number);
    }

    public void setStreet_Address(String street_Address) {
        this.Street_Address.set(street_Address);
    }

    public void setAPT(String APT) {
        this.APT.set(APT);
    }

    public void setCity(String city) {
        this.City.set(city);
    }

    public void setState(String state) {
        this.State.set(state);
    }

    public void setZip_Code(String zip_Code) {
        this.Zip_Code.set(zip_Code);
    }

    public void setDate_Of_Birth(String date_of_birth) {
        this.Date_Of_Birth.set(date_of_birth);
    }
}
