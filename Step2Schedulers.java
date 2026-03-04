import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Step2Schedulers {

    /**
     * Holds Step 2 computed values without changing your Processes.java.
     */
    public static class ScheduleRow {
        public final int pid;
        public final int arrival;
        public final int burst;

        public final int start;   // start time
        public final int finish;  // completion/finish time
        public final int wt;      // waiting time
        public final int tat;     // turnaround time

        public ScheduleRow(int pid, int arrival, int burst, int start, int finish) {
            this.pid = pid;
            this.arrival = arrival;
            this.burst = burst;
            this.start = start;
            this.finish = finish;

            this.tat = finish - arrival;
            this.wt = tat - burst;
        }
    }

    public static class ScheduleResult {
        public final List<ScheduleRow> rows; // per-process results
        public final int totalTime;          // time when schedule ends

        public ScheduleResult(List<ScheduleRow> rows, int totalTime) {
            this.rows = rows;
            this.totalTime = totalTime;
        }
    }

    // =========================================================
    // FCFS (First-Come, First-Served) — non-preemptive
    // =========================================================
    public static ScheduleResult fcfs(List<Processes> input) {
        // Make a copy so we don't reorder the original list from Step 1
        List<Processes> processes = new ArrayList<>(input);

        // Sort by arrival time, then PID (tie-breaker)
        processes.sort(Comparator
                .comparingInt(Processes::getArrival_Time)
                .thenComparingInt(Processes::getPid));

        List<ScheduleRow> out = new ArrayList<>();
        int time = 0;

        for (Processes p : processes) {
            int at = p.getArrival_Time();
            int bt = p.getBurst_Time();

            // If CPU is idle, jump forward
            if (time < at) time = at;

            int start = time;
            int finish = time + bt;
            time = finish;

            out.add(new ScheduleRow(p.getPid(), at, bt, start, finish));
        }

        return new ScheduleResult(out, time);
    }

    // =========================================================
    // SJF (Shortest Job First) — non-preemptive
    // =========================================================
    public static ScheduleResult sjfNonPreemptive(List<Processes> input) {
        // We'll repeatedly pick the shortest burst among READY processes.
        List<Processes> remaining = new ArrayList<>(input);
        List<ScheduleRow> out = new ArrayList<>();

        int time = 0;

        while (!remaining.isEmpty()) {
            // Build "ready" list: arrived by current time
            List<Processes> ready = new ArrayList<>();
            int nextArrival = Integer.MAX_VALUE;

            for (Processes p : remaining) {
                if (p.getArrival_Time() <= time) {
                    ready.add(p);
                } else {
                    nextArrival = Math.min(nextArrival, p.getArrival_Time());
                }
            }

            // If nothing is ready, CPU is idle: jump to next arrival
            if (ready.isEmpty()) {
                time = nextArrival;
                continue;
            }

            // Pick shortest burst (tie-break: arrival, then PID)
            ready.sort(Comparator
                    .comparingInt(Processes::getBurst_Time)
                    .thenComparingInt(Processes::getArrival_Time)
                    .thenComparingInt(Processes::getPid));

            Processes chosen = ready.get(0);

            int at = chosen.getArrival_Time();
            int bt = chosen.getBurst_Time();

            int start = time;
            int finish = time + bt;
            time = finish;

            out.add(new ScheduleRow(chosen.getPid(), at, bt, start, finish));

            // Remove chosen from remaining
            remaining.remove(chosen);
        }

        return new ScheduleResult(out, time);
    }

    // =========================================================
    // Optional helper: print WT/TAT + averages (still Step 2)
    // =========================================================
    public static void printMetrics(String title, ScheduleResult result) {
        System.out.println("\n=== " + title + " ===");
        System.out.println("PID  AT  BT  ST  FT  WT  TAT");

        // For clean grading output, sort by PID when printing
        List<ScheduleRow> rows = new ArrayList<>(result.rows);
        rows.sort(Comparator.comparingInt(r -> r.pid));

        double sumWT = 0;
        double sumTAT = 0;

        for (ScheduleRow r : rows) {
            System.out.printf("%-4d %-3d %-3d %-3d %-3d %-3d %-3d%n",
                    r.pid, r.arrival, r.burst, r.start, r.finish, r.wt, r.tat);
            sumWT += r.wt;
            sumTAT += r.tat;
        }

        System.out.printf("Average WT: %.2f%n", sumWT / rows.size());
        System.out.printf("Average TAT: %.2f%n", sumTAT / rows.size());
    }
}