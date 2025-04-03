import java.time.LocalDate;
import java.time.LocalTime;

public class Reservation {
    private int id;
    private Coworking coworkingSpace;
    private String customerName;
    private String userName;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    public Reservation(
            int id,
            Coworking coworkingSpace,
            String customerName,
            String userName,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    ) {
        this.id = id;
        this.coworkingSpace = coworkingSpace;
        this.customerName = customerName;
        this.userName = userName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public Coworking getCoworkingSpace() {
        return coworkingSpace;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getUserName() {
        return userName;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }
}
