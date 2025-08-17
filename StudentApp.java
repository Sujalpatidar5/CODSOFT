import java.io.*;
import java.util.*;

// Class to store student details
class StudentRecord {
    private int roll;
    private String fullName;
    private String grade;

    public StudentRecord(int roll, String fullName, String grade) {
        this.roll = roll;
        this.fullName = fullName;
        this.grade = grade;
    }

    public int getRoll() { return roll; }
    public String getFullName() { return fullName; }
    public String getGrade() { return grade; }

    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setGrade(String grade) { this.grade = grade; }

    // Save to file format: roll|name|grade
    public String toStorageLine() {
        return roll + "|" + fullName.replace("|", " ") + "|" + grade.replace("|", " ");
    }

    public static StudentRecord fromStorageLine(String line) {
        String[] parts = line.split("\\|", -1);
        if (parts.length != 3) return null;
        try {
            int roll = Integer.parseInt(parts[0].trim());
            String name = parts[1].trim();
            String grade = parts[2].trim();
            return new StudentRecord(roll, name, grade);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "Roll: " + roll + " | Name: " + fullName + " | Grade: " + grade;
    }
}

public class StudentApp {
    private static final String DATA_FILE = "students.txt";
    private static final Set<String> VALID_GRADES = new HashSet<>(
            Arrays.asList("A+", "A", "B+", "B", "C+", "C", "D", "F")
    );

    private final List<StudentRecord> records = new ArrayList<>();
    private final Scanner scanner = new Scanner(System.in);

    public StudentApp() {
        loadRecords();
    }

    // Save records to file
    private void saveRecords() {
        try (PrintWriter out = new PrintWriter(new FileWriter(DATA_FILE))) {
            for (StudentRecord r : records) {
                out.println(r.toStorageLine());
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load records from file
    private void loadRecords() {
        File f = new File(DATA_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            int added = 0;
            while ((line = br.readLine()) != null) {
                StudentRecord r = StudentRecord.fromStorageLine(line);
                if (r != null && getByRoll(r.getRoll()) == null && isValidName(r.getFullName()) && isValidGrade(r.getGrade())) {
                    records.add(r);
                    added++;
                }
            }
            if (added > 0) System.out.println("Loaded " + added + " records from file.");
        } catch (IOException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    // Check if roll is valid
    private boolean isValidRoll(int roll) {
        return roll > 0;
    }

    // Check if name is valid
    private boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty() && name.matches("[a-zA-Z\\s]+");
    }

    // Check if grade is valid
    private boolean isValidGrade(String grade) {
        if (grade == null) return false;
        return VALID_GRADES.contains(grade.toUpperCase());
    }

    // Add a new student
    public void addRecord(int roll, String name, String grade) {
        if (!isValidRoll(roll)) {
            System.out.println("Invalid roll. Use a positive number.");
            return;
        }
        if (!isValidName(name)) {
            System.out.println("Invalid name. Use letters and spaces only.");
            return;
        }
        if (!isValidGrade(grade)) {
            System.out.println("Invalid grade. Use: " + VALID_GRADES);
            return;
        }
        if (getByRoll(roll) != null) {
            System.out.println("Roll " + roll + " already exists!");
            return;
        }
        records.add(new StudentRecord(roll, name.trim(), grade.toUpperCase()));
        saveRecords();
        System.out.println("Student added!");
    }

    // Show all students
    public void showRecords() {
        if (records.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("\n--- All Students (" + records.size() + ") ---");
        for (StudentRecord r : records) System.out.println(r);
    }

    // Search by roll
    public void findByRoll(int roll) {
        if (!isValidRoll(roll)) {
            System.out.println("Invalid roll.");
            return;
        }
        StudentRecord r = getByRoll(roll);
        if (r == null) System.out.println("Not found.");
        else System.out.println("Found: " + r);
    }

    // Search by name
    public void findByName(String name) {
        if (!isValidName(name)) {
            System.out.println("Invalid name input.");
            return;
        }
        boolean found = false;
        System.out.println("\n--- Search results for \"" + name + "\" ---");
        for (StudentRecord r : records) {
            if (r.getFullName().toLowerCase().contains(name.toLowerCase())) {
                System.out.println(r);
                found = true;
            }
        }
        if (!found) System.out.println("No matching students.");
    }

    // Update student details
    public void updateRecord(int roll, String newName, String newGrade) {
        StudentRecord r = getByRoll(roll);
        if (r == null) {
            System.out.println("Student not found.");
            return;
        }
        if (newName != null && !newName.trim().isEmpty()) {
            if (!isValidName(newName)) {
                System.out.println("Invalid name. Not updated.");
            } else {
                r.setFullName(newName.trim());
            }
        }
        if (newGrade != null && !newGrade.trim().isEmpty()) {
            if (!isValidGrade(newGrade)) {
                System.out.println("Invalid grade. Not updated.");
            } else {
                r.setGrade(newGrade.toUpperCase());
            }
        }
        saveRecords();
        System.out.println("Record updated: " + r);
    }

    // Delete a student
    public void deleteRecord(int roll) {
        Iterator<StudentRecord> it = records.iterator();
        while (it.hasNext()) {
            if (it.next().getRoll() == roll) {
                it.remove();
                saveRecords();
                System.out.println("Student deleted.");
                return;
            }
        }
        System.out.println("Student not found.");
    }

    // Sort by roll
    public void sortByRoll() {
        records.sort(Comparator.comparingInt(StudentRecord::getRoll));
        System.out.println("Sorted by roll.");
    }

    // Sort by name
    public void sortByName() {
        records.sort(Comparator.comparing(r -> r.getFullName().toLowerCase()));
        System.out.println("Sorted by name.");
    }

    // Get student by roll
    private StudentRecord getByRoll(int roll) {
        for (StudentRecord r : records) if (r.getRoll() == roll) return r;
        return null;
    }

    // Show menu
    public void menu() {
        while (true) {
            System.out.println("\n=== Student App ===");
            System.out.println("1. Add Student");
            System.out.println("2. Show All Students");
            System.out.println("3. Find by Roll");
            System.out.println("4. Find by Name");
            System.out.println("5. Update Student");
            System.out.println("6. Delete Student");
            System.out.println("7. Sort by Roll");
            System.out.println("8. Sort by Name");
            System.out.println("9. Count Students");
            System.out.println("10. Exit");
            System.out.print("Choose (1-10): ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Enter a number 1-10.");
                continue;
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter Roll: ");
                    int roll = safeReadInt();
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Grade (A+, A, B+, B, C+, C, D, F): ");
                    String grade = scanner.nextLine();
                    addRecord(roll, name, grade);
                    break;
                case 2:
                    showRecords();
                    break;
                case 3:
                    System.out.print("Enter Roll to find: ");
                    findByRoll(safeReadInt());
                    break;
                case 4:
                    System.out.print("Enter Name to search: ");
                    findByName(scanner.nextLine());
                    break;
                case 5:
                    System.out.print("Enter Roll to update: ");
                    roll = safeReadInt();
                    System.out.print("Enter New Name (leave blank to keep): ");
                    String newName = scanner.nextLine();
                    System.out.print("Enter New Grade (leave blank to keep): ");
                    String newGrade = scanner.nextLine();
                    updateRecord(roll, newName, newGrade);
                    break;
                case 6:
                    System.out.print("Enter Roll to delete: ");
                    deleteRecord(safeReadInt());
                    break;
                case 7:
                    sortByRoll();
                    saveRecords();
                    break;
                case 8:
                    sortByName();
                    saveRecords();
                    break;
                case 9:
                    System.out.println("Total students: " + records.size());
                    break;
                case 10:
                    System.out.print("Exit? (yes/no): ");
                    String ans = scanner.nextLine().trim();
                    if (ans.equalsIgnoreCase("yes")) {
                        saveRecords();
                        System.out.println("Saved and exiting. Bye!");
                        return;
                    }
                    break;
                default:
                    System.out.println("Choose between 1 and 10.");
            }
        }
    }

    // Safe input for numbers
    private int safeReadInt() {
        while (true) {
            String token = scanner.nextLine().trim();
            try {
                int v = Integer.parseInt(token);
                if (v <= 0) {
                    System.out.print("Enter positive number: ");
                    continue;
                }
                return v;
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. Try again: ");
            }
        }
    }

    public static void main(String[] args) {
        StudentApp app = new StudentApp();
        app.menu();
    }
}