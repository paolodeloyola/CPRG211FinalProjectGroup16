import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner sc = new Scanner(System.in);
    private static final EmployeeDAO dao = new EmployeeDAO();

    public static void main(String[] args) {
        try {
            Database.init();
        } catch (SQLException e) {
            System.out.println("DB Error: " + e.getMessage());
            return;
        }

        while (true) {
            printMenu();
            int choice = readInt("Choice: ");

            try {
                if (choice == 0) break;
                if (choice == 1) createFlow();
                else if (choice == 2) readByIdFlow();
                else if (choice == 3) listAllFlow();
                else if (choice == 4) listActiveFlow();
                else if (choice == 5) updateFlow();
                else if (choice == 6) deleteFlow();
                else System.out.println("Invalid option.");
            } catch (InvalidInputException | RecordNotFoundException | DuplicateRecordException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("DB Error: " + e.getMessage());
            }

            System.out.println();
        }
        sc.close();
    }

    private static void printMenu() {
        System.out.println("Employee Management (SQLite)");
        System.out.println("1) Create");
        System.out.println("2) Read by ID");
        System.out.println("3) Read all");
        System.out.println("4) Read active only");
        System.out.println("5) Update");
        System.out.println("6) Delete");
        System.out.println("0) Exit");
    }

    private static void createFlow() throws InvalidInputException, DuplicateRecordException, SQLException {
        String name = readLine("Full name: ");
        String email = readLine("Email: ");
        String title = readLine("Job title: ");
        Integer dept = readNullableInt("Department ID (blank for none): ");
        boolean active = readYesNo("Active (y/n): ");

        int publicId = dao.createEmployee(name, email, title, dept, active);
        System.out.println("Created employee ID: " + publicId);
    }

    private static void readByIdFlow() throws RecordNotFoundException, SQLException {
        int id = readInt("Employee ID: ");
        System.out.println(dao.getEmployeeById(id));
    }

    private static void listAllFlow() throws SQLException {
        List<Employee> list = dao.getAllEmployees();
        if (list.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        for (Employee e : list) System.out.println(e);
    }

    private static void listActiveFlow() throws SQLException {
        List<Employee> list = dao.getActiveEmployees();
        if (list.isEmpty()) {
            System.out.println("No active employees found.");
            return;
        }
        for (Employee e : list) System.out.println(e);
    }

    private static void updateFlow() throws InvalidInputException, RecordNotFoundException, DuplicateRecordException, SQLException {
        int id = readInt("Employee ID to update: ");

        Employee current = dao.getEmployeeById(id);
        System.out.println("Leave field blank to keep current \nCurrent: " + current);

        String name = readLineKeepOld("Full Name: ", current.getFullName());
        String email = readLineKeepOld("E-mail: ", current.getEmail());
        String title = readLineKeepOld("Job Title: ", current.getJobTitle());
        Integer dept = readIntKeepOldNullable("Department ID: ", current.getDepartmentId());
        boolean active = readYesNoKeepOld("Active (y/n): ", current.isActive());

        dao.updateEmployee(id, name, email, title, dept, active);
        System.out.println("Updated.");
    }

    private static void deleteFlow() throws RecordNotFoundException, SQLException {
        int id = readInt("Employee ID to delete: ");
        dao.deleteEmployee(id);
        System.out.println("Deleted.");
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Enter an integer.");
            }
        }
    }

    private static Integer readNullableInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (s.isEmpty()) return null;
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Enter an integer or leave blank.");
            }
        }
    }

    private static boolean readYesNo(String prompt) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim().toLowerCase();
            if (s.equals("y") || s.equals("yes")) return true;
            if (s.equals("n") || s.equals("no")) return false;
            System.out.println("Type y or n.");
        }
    }

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine();
    }

    private static String readLineKeepOld(String prompt, String oldVal) {
        System.out.print(prompt);
        String s = sc.nextLine();
        if (s.trim().isEmpty()) return oldVal;
        return s;
    }

    private static Integer readIntKeepOldNullable(String prompt, Integer oldVal) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim();
            if (s.isEmpty()) return oldVal;
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Enter an integer or leave blank.");
            }
        }
    }

    private static boolean readYesNoKeepOld(String prompt, boolean oldVal) {
        while (true) {
            System.out.print(prompt);
            String s = sc.nextLine().trim().toLowerCase();
            if (s.isEmpty()) return oldVal;
            if (s.equals("y") || s.equals("yes")) return true;
            if (s.equals("n") || s.equals("no")) return false;
            System.out.println("Type y or n, or leave blank.");
        }
    }
}
