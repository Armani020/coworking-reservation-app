import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CoworkingReservationApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final List<Coworking> coworkingSpaces = new ArrayList<>();
    private static final List<Reservation> reservations = new ArrayList<>();
    private static final String COWORKING_FILE = "src/coworkings.txt";

    private static User currentUser = null;
    private static int nextReservationId = 1;

    public static void main(String[] args) {
        initCoworkingSpaces();
        showWelcomeMessage();

        showMainMenu();
    }

    private static void initCoworkingSpaces() {
        loadCoworkingSpacesFromFile();
    }

    private static void loadCoworkingSpacesFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(COWORKING_FILE))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] strings = line.split(",");

                if (strings.length < 5) {
                    throw new InvalidFileInputException("Invalid line in file: " + line);
                }

                int id = Integer.parseInt(strings[0]);
                String name = strings[1];
                String type = strings[2];
                int price = Integer.parseInt(strings[3]);
                boolean available = Boolean.parseBoolean(strings[4]);

                coworkingSpaces.add(new Coworking(id, name, type, price, available));
            }
        } catch (InvalidFileInputException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error reading coworking spaces file: " + e.getMessage());
        }
    }

    private static void showWelcomeMessage() {
        System.out.println();
        System.out.println("===============================================");
        System.out.println("  WELCOME TO COWORKING SPACE RESERVATION SYSTEM");
        System.out.println("===============================================");
    }

    private static void showMainMenu() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\n===== MAIN MENU =====");
            System.out.println("1. Admin Login");
            System.out.println("2. User Login");
            System.out.println("3. Exit");
            System.out.print("Select an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    adminLogin();
                    break;
                case 2:
                    userLogin();
                    break;
                case 3:
                    exit = true;
                    System.out.println("Thank you for using the system. Goodbye!");
                    break;
                default:
                    System.out.println();
                    System.out.println("ERROR: Invalid option. Please try again.");
            }
        }
    }

    private static void adminLogin() {
        System.out.print("Enter admin username: ");
        String username = scanner.nextLine();
        System.out.print("Enter admin password: ");
        String password = scanner.nextLine();

        if (username.equals("admin") && password.equals("admin")) {
            currentUser = new User(1, username, password, Role.ADMIN);
            showAdminMenu();
        } else {
            System.out.println("Invalid admin credentials!");
        }
    }

    private static void userLogin() {
        System.out.print("Enter customer username: ");
        String username = scanner.nextLine();
        System.out.print("Enter customer password: ");
        String password = scanner.nextLine();

        if (!username.isEmpty() && !password.isEmpty()) {
            currentUser = new User(2, username, password, Role.CUSTOMER);
            showCustomerMenu();
        } else {
            System.out.println("Invalid customer credentials!");
        }
    }

    /**
     * Method that shows Admin menu
     */
    private static void showAdminMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n===== ADMIN MENU =====");
            System.out.println("1. Add a new coworking space");
            System.out.println("2. Remove a coworking space");
            System.out.println("3. View all reservations");
            System.out.println("4. Back to main menu");
            System.out.print("Select an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    addCoworkingSpace();
                    break;
                case 2:
                    removeCoworkingSpace();
                    break;
                case 3:
                    viewAllReservations();
                    break;
                case 4:
                    back = true;
                    currentUser = null;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addCoworkingSpace() {
        System.out.println("\n----- ADD NEW COWORKING SPACE -----");

        int id = coworkingSpaces.size() + 1;

        System.out.print("Enter name of new coworking: ");
        String name = scanner.nextLine();

        System.out.print("Enter space type (open space, private, room): ");
        String type = scanner.nextLine();

        System.out.print("Enter price per hour: ");
        int price = getIntInput();

        Coworking newSpace = new Coworking(id, name, type, price, true);
        coworkingSpaces.add(newSpace);

        System.out.println("Coworking space added successfully!");
    }

    private static void removeCoworkingSpace() {
        System.out.println("\n----- REMOVE COWORKING SPACE -----");
        showAllCoworkingSpaces();

        System.out.print("Enter the ID of the space to remove: ");
        int id = getIntInput();

        boolean removed = coworkingSpaces.removeIf(space -> space.getId() == id);

        if (removed) {
            System.out.println("Coworking space removed successfully!");
        } else {
            System.out.println("No coworking space found with that ID!");
        }
    }

    private static void viewAllReservations() {
        System.out.println("\n----- ALL RESERVATIONS -----");

        if (reservations.isEmpty()) {
            System.out.println("There are no reservations yet.");
            return;
        }

        System.out.println("ID\tSpace\tCustomer\tDate\t\tStart Time\tEnd Time");
        System.out.println("----------------------------------------------------------------------");

        for (Reservation reservation : reservations) {
            System.out.printf("%d\t%s\t%s\t%s\t%s\t%s\n",
                    reservation.getId(),
                    reservation.getCoworkingSpace().getName(),
                    reservation.getCustomerName(),
                    reservation.getDate(),
                    reservation.getStartTime(),
                    reservation.getEndTime());
        }
    }

    private static void showCustomerMenu() {
        boolean back = false;

        while (!back) {
            System.out.println("\n===== CUSTOMER MENU =====");
            System.out.println("1. Browse available spaces");
            System.out.println("2. Make a reservation");
            System.out.println("3. View my reservations");
            System.out.println("4. Cancel a reservation");
            System.out.println("5. Back to main menu");
            System.out.print("Select an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    browseAvailableCoworkingSpaces();
                    break;
                case 2:
                    makeReservation();
                    break;
                case 3:
                    viewMyReservations();
                    break;
                case 4:
                    cancelReservation();
                    break;
                case 5:
                    back = true;
                    currentUser = null;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void browseAvailableCoworkingSpaces() {
        System.out.println("\n----- AVAILABLE COWORKING SPACES -----");
        showAllCoworkingSpaces();
    }

    private static void makeReservation() {
        System.out.println("\n----- MAKE A RESERVATION -----");

        showAllCoworkingSpaces();

        System.out.print("Enter the ID of the space you want to reserve: ");
        int spaceId = getIntInput();

        Coworking selectedCoworking = coworkingSpaces.stream()
                .filter(coworking -> coworking.getId() == spaceId && coworking.isAvailable())
                .findFirst()
                .orElse(null);

        if (selectedCoworking == null) {
            System.out.println("Invalid space ID or the space is unavailable!");
            return;
        }

        System.out.print("Enter your name: ");
        String customerName = scanner.nextLine();

        LocalDate date = null;
        while (date == null) {
            System.out.print("Enter date (YYYY-MM-DD): ");
            String dateStr = scanner.nextLine();
            try {
                date = LocalDate.parse(dateStr);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format! Please use YYYY-MM-DD.");
            }
        }

        LocalTime startTime = null;
        while (startTime == null) {
            System.out.print("Enter start time (HH:MM): ");
            String timeStr = scanner.nextLine();
            try {
                startTime = LocalTime.parse(timeStr);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format! Please use HH:MM (24-hour format).");
            }
        }

        LocalTime endTime = null;
        while (endTime == null) {
            System.out.print("Enter end time (HH:MM): ");
            String timeStr = scanner.nextLine();
            try {
                endTime = LocalTime.parse(timeStr);
                if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
                    System.out.println("End time must be after start time!");
                    endTime = null;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Invalid time format! Please use HH:MM (24-hour format).");
            }
        }

        Reservation reservation = new Reservation(
                nextReservationId++,
                selectedCoworking,
                customerName,
                currentUser.getUsername(),
                date,
                startTime,
                endTime
        );

        reservations.add(reservation);
        System.out.println("Reservation created successfully! Your reservation ID is: " + reservation.getId());
    }

    private static void viewMyReservations() {
        System.out.println("\n----- MY RESERVATIONS -----");

        List<Reservation> myReservations = reservations.stream()
                .filter(reservation -> reservation.getUserName().equals(currentUser.getUsername()))
                .toList();

        if (myReservations.isEmpty()) {
            System.out.println("You have no reservations.");
            return;
        }

        System.out.println("ID\tSpace\tDate\t\tStart Time\tEnd Time");
        System.out.println("--------------------------------------------------------------");

        for (Reservation reservation : myReservations) {
            System.out.printf("%d\t%s\t%s\t%s\t%s\n",
                    reservation.getId(),
                    reservation.getCoworkingSpace().getName(),
                    reservation.getDate(),
                    reservation.getStartTime(),
                    reservation.getEndTime());
        }
    }

    private static void cancelReservation() {
        System.out.println("\n----- CANCEL RESERVATION -----");

        List<Reservation> myReservations = reservations.stream()
                .filter(reservation -> reservation.getUserName().equals(currentUser.getUsername()))
                .toList();

        if (myReservations.isEmpty()) {
            System.out.println("You have no reservations to cancel.");
            return;
        }

        System.out.println("ID\tSpace\tDate\t\tStart Time\tEnd Time");
        System.out.println("--------------------------------------------------------------");

        for (Reservation reservation : myReservations) {
            System.out.printf("%d\t%s\t%s\t%s\t%s\n",
                    reservation.getId(),
                    reservation.getCoworkingSpace().getName(),
                    reservation.getDate(),
                    reservation.getStartTime(),
                    reservation.getEndTime());
        }

        System.out.print("Enter the ID of the reservation you want to cancel: ");
        int reservationId = getIntInput();

        boolean canceled = false;
        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);
            if (reservation.getId() == reservationId && reservation.getUserName().equals(currentUser.getUsername())) {
                reservations.remove(i);
                canceled = true;
                System.out.println("Reservation canceled successfully!");
                break;
            }
        }

        if (!canceled) {
            System.out.println("Invalid reservation ID or you don't have permission to cancel this reservation!");
        }
    }

    private static void showAllCoworkingSpaces() {
        if (coworkingSpaces.isEmpty()) {
            System.out.println("There are no coworking spaces available.");
            return;
        }

        System.out.println("ID\tName\t\tType\t\tPrice/Hour\tAvailability");
        System.out.println("----------------------------------------------------------------------");

        for (Coworking coworking : coworkingSpaces) {
            System.out.printf("%d\t%-15s\t%-10s\t%s$\t\t%s\n",
                    coworking.getId(),
                    coworking.getName(),
                    coworking.getType(),
                    coworking.getPrice(),
                    coworking.isAvailable() ? "Available" : "Unavailable");
        }
    }

    private static int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return 0;
        }
    }
}