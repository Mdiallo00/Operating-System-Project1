import java.io.*;
import java.util.*;

public class Project1_menu {

    private static final String FILE_NAME = "processses.txt";

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n==============================");
            System.out.println("CPU Scheduling Simulator");
            System.out.println("==============================");
            System.out.println("1) Create/Overwrite processes file");
            System.out.println("2) Load and display processes");
            System.out.println("3) Run FCFS (Gantt + WT/TAT)");
            System.out.println("4) Run SJF (Non-preemptive) (Gantt + WT/TAT)");
            System.out.println("5) Exit");
            System.out.print("Choose an option: ");
            System.out.flush();

            int choice = readInt(sc);
            switch (choice) {
                case 1 -> createProcessesFile(sc, FILE_NAME);
                case 2 -> {
                    List<Processes> processes = loadProcesses(FILE_NAME);
                    printLoadedProcesses(processes);
                }
                case 3 -> runFCFS(FILE_NAME);
                case 4 -> runSJF(FILE_NAME);
                case 5 -> {
                    System.out.println("Goodbye.");
                    return;
                }
                default -> System.out.println("Invalid choice. Please choose 1-5.");
            }
        }
    }

    // OPTION 1: Create/Overwrite processes file from user input
    private static void createProcessesFile(Scanner sc, String fileName) {
        System.out.print("How many processes do you want to enter? ");
        System.out.flush();
        int n = readInt(sc);

        Set<Integer> usedPids = new HashSet<>();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            bw.write("PID Arrival_Time Burst_Time Priority");
            bw.newLine();

            for (int i = 1; i <= n; i++) {
                System.out.println("\nProcess " + i + ":");

                int pid;
                while (true) {
                    System.out.print("  PID (integer): ");
                    pid = readInt(sc);
                    if (usedPids.contains(pid)) {
                        System.out.println("  PID already used. Enter a unique PID.");
                    } else {
                        usedPids.add(pid);
                        break;
                    }
                }

                System.out.print("  Arrival Time (>= 0): ");
                int at = readInt(sc);
                while (at < 0) {
                    System.out.print("  Arrival Time must be >= 0. Re-enter: ");
                    at = readInt(sc);
                }

                System.out.print("  Burst Time (> 0): ");
                int bt = readInt(sc);
                while (bt <= 0) {
                    System.out.print("  Burst Time must be > 0. Re-enter: ");
                    bt = readInt(sc);
                }

                System.out.print("  Priority (integer): ");
                int pr = readInt(sc);

                bw.write(pid + " " + at + " " + bt + " " + pr);
                bw.newLine();
            }

            System.out.println("\nSaved processes to: " + fileName);
            System.out.println("Tip: choose option 2 to verify what was saved.");

        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }

    // Load processes from file
    private static List<Processes> loadProcesses(String fileName) {
        List<Processes> allProcesses = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean firstLine = true;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Skip header
                if (firstLine) {
                    firstLine = false;
                    if (line.toLowerCase().contains("pid")) continue;
                }

                String[] parts = line.split("\\s+");
                if (parts.length < 4) continue;

                int pid = Integer.parseInt(parts[0]);
                int at  = Integer.parseInt(parts[1]);
                int bt  = Integer.parseInt(parts[2]);
                int pr  = Integer.parseInt(parts[3]);

                Processes p = new Processes();
                p.setPid(pid);
                p.setArrival_Time(at);
                p.setBurst_Time(bt);
                p.setPriority(pr);

                allProcesses.add(p);
            }

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName + " (choose option 1 to create it)");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number in file. Each line must be: PID AT BT Priority");
        }

        return allProcesses;
    }

    private static void printLoadedProcesses(List<Processes> processes) {
        if (processes.isEmpty()) {
            System.out.println("No processes loaded.");
            return;
        }

        System.out.println("\nPID  Arrival_Time  Burst_Time  Priority");
        for (Processes p : processes) {
            System.out.println(p.toString());
        }
    }

    // Run FCFS
    private static void runFCFS(String fileName) {
        List<Processes> processes = loadProcesses(fileName);
        if (processes.isEmpty()) return;

        Step2Schedulers.ScheduleResult result = Step2Schedulers.fcfs(processes);

        System.out.println("\n=== FCFS Gantt Chart ===");
        Step2Schedulers.printGanttChart(result.gantt);

        System.out.println("\n=== FCFS Metrics ===");
        Step2Schedulers.printMetrics(result.rows);
    }

    // Run SJF
    private static void runSJF(String fileName) {
        List<Processes> processes = loadProcesses(fileName);
        if (processes.isEmpty()) return;

        Step2Schedulers.ScheduleResult result = Step2Schedulers.sjfNonPreemptive(processes);

        System.out.println("\n=== SJF (Non-preemptive) Gantt Chart ===");
        Step2Schedulers.printGanttChart(result.gantt);

        System.out.println("\n=== SJF Metrics ===");
        Step2Schedulers.printMetrics(result.rows);
    }

    // ✅ Input helper: safe integer reading using nextInt + consume newline
    private static int readInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.print("Please enter a valid integer: ");
            sc.next();
        }
        int value = sc.nextInt();
        sc.nextLine();
        return value;
    }
}
