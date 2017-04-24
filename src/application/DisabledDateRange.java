package application;

import java.time.LocalDate;
import javafx.scene.control.DatePicker;

public class DisabledDateRange
{
    public final LocalDate initialDate;
    public final LocalDate endDate;


    public DisabledDateRange(LocalDate initialDate, LocalDate endDate){
        this.initialDate = initialDate;
        this.endDate = endDate;
    }

    public LocalDate getInitialDate() { return initialDate; }
    public LocalDate getEndDate() { return endDate; }

}
