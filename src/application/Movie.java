package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Movie {
    private final SimpleStringProperty Movie_ID;
    private final SimpleStringProperty Movie_Title;
    private final SimpleStringProperty Rating;
    private final SimpleStringProperty Main_Actor;
    private final SimpleStringProperty Supporting_Actor;
    private final SimpleStringProperty Director;
    private final SimpleStringProperty Genre;
    private final SimpleStringProperty Release_Date;
    private final SimpleStringProperty Rental_Price;
    private final SimpleStringProperty Purchase_Price;
    private final SimpleIntegerProperty On_Hand_Quantity;
    private final SimpleIntegerProperty Total_Quantity;


    public Movie(String id, String title, String rating, String mainActor, String suppActor, String director,
                 String genre, String releaseDate, String rentPrice, String purPrice, Integer onHandQuant, Integer totalQuant)
    {
        this.Movie_ID = new SimpleStringProperty(id);
        this.Movie_Title = new SimpleStringProperty(title);
        this.Rating = new SimpleStringProperty(rating);
        this.Main_Actor = new SimpleStringProperty(mainActor);
        this.Supporting_Actor = new SimpleStringProperty(suppActor);
        this.Director = new SimpleStringProperty(director);
        this.Genre = new SimpleStringProperty(genre);
        this.Release_Date = new SimpleStringProperty(releaseDate);
        this.Rental_Price = new SimpleStringProperty(rentPrice);
        this.Purchase_Price = new SimpleStringProperty(purPrice);
        this.Total_Quantity = new SimpleIntegerProperty(totalQuant);
        this.On_Hand_Quantity = new SimpleIntegerProperty(onHandQuant);
    }

    public Movie(String id, String title, String rating, String genre, String releaseDate, String rentPrice, String purPrice,  Integer onHandQuant, Integer totalQuant)
    {
        this.Movie_ID = new SimpleStringProperty(id);
        this.Movie_Title = new SimpleStringProperty(title);
        this.Rating = new SimpleStringProperty(rating);
        this.Main_Actor = new SimpleStringProperty(null);
        this.Supporting_Actor = new SimpleStringProperty(null);
        this.Director = new SimpleStringProperty(null);
        this.Genre = new SimpleStringProperty(genre);
        this.Release_Date = new SimpleStringProperty(releaseDate);
        this.Rental_Price = new SimpleStringProperty(rentPrice);
        this.Purchase_Price = new SimpleStringProperty(purPrice);
        this.Total_Quantity = new SimpleIntegerProperty(totalQuant);
        this.On_Hand_Quantity = new SimpleIntegerProperty(onHandQuant);
    }

    public String getMovie_ID(){
        return Movie_ID.get();
    }

    public String getMovie_Title(){
        return Movie_Title.get();
    }

    public String getRating(){
        return Rating.get();
    }

    public String getMain_Actor(){
        return Main_Actor.get();
    }

    public String getSupporting_Actor(){
        return Supporting_Actor.get();
    }

    public String getDirector(){
        return Director.get();
    }

    public String getGenre(){
        return Genre.get();
    }

    public String getRelease_Date(){
        return Release_Date.get();
    }

    public String getRental_Price(){
        return Rental_Price.get();
    }

    public String getPurchase_Price(){
        return Purchase_Price.get();
    }

    public Integer getTotal_Quantity(){
        return Total_Quantity.get();
    }

    public Integer getOn_Hand_Quantity(){
        return On_Hand_Quantity.get();
    }


}