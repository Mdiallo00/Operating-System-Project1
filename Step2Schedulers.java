
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Step2Schedulers {

    
    public static class Segment {
        public final String label; 
        public final int start;
        public final int end;

        public Segment(String label, int start, int end) {
            this.label = label;
            this.start = start;
            this.end = end;
        }
    }

    
    public static class ScheduleRow {
        public final int pid;
        public final int arrival;
        public final int burst;

        public final int start;
        public final int finish;
        public final int wt;
        public final int tat;

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

    // Now ScheduleResult includes both: segments + rows
    public static class ScheduleResult {
        public final List<Segment> gantt;
        public final List<ScheduleRow> rows;

        public ScheduleResult(List<Segment> gantt, List<ScheduleRow> rows) {
            this.gantt = gantt;
            this.rows = rows;
        }
    }

    //  First comes first served
    public static ScheduleResult fcfs(List<Processes> input) {
        List<Processes> processes = new ArrayList<>(input);
        processes.sort(Comparator
                .comparingInt(Processes::getArrival_Time)
                .thenComparingInt(Processes::getPid));

        List<Segment> gantt = new ArrayList<>();
        List<ScheduleRow> rows = new ArrayList<>();
        int time = 0;

        for (Processes p : processes) {
            int at = p.getArrival_Time();
            int bt = p.getBurst_Time();

            // idle block if CPU is waiting
            if (time < at) {
                gantt.add(new Segment("IDLE", time, at));
                time = at;
            }

            int start = time;
            int finish = time + bt;
            time = finish;

            gantt.add(new Segment("P" + p.getPid(), start, finish));
            rows.add(new ScheduleRow(p.getPid(), at, bt, start, finish));
        }

        return new ScheduleResult(gantt, rows);
    }

    // ---------------- SJF (Non-preemptive) ----------------
    public static ScheduleResult sjfNonPreemptive(List<Processes> input) {
        List<Processes> remaining = new ArrayList<>(input);

        List<Segment> gantt = new ArrayList<>();
        List<ScheduleRow> rows = new ArrayList<>();

        int time = 0;

        while (!remaining.isEmpty()) {
            List<Processes> ready = new ArrayList<>();
            int nextArrival = Integer.MAX_VALUE;

            for (Processes p : remaining) {
                if (p.getArrival_Time() <= time) ready.add(p);
                else nextArrival = Math.min(nextArrival, p.getArrival_Time());
            }

            if (ready.isEmpty()) {
                // CPU idle until next arrival
                gantt.add(new Segment("IDLE", time, nextArrival));
                time = nextArrival;
                continue;
            }

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

            gantt.add(new Segment("P" + chosen.getPid(), start, finish));
            rows.add(new ScheduleRow(chosen.getPid(), at, bt, start, finish));

            remaining.remove(chosen);
        }

        return new ScheduleResult(gantt, rows);
    }

        // Display Print Gantt chart
    public static void printGanttChart(List<Segment> segments) {
        // Line 1: | P1 | P2 | ...
        StringBuilder top = new StringBuilder();
        top.append("|");
        for (Segment s : segments) {
            top.append(" ").append(s.label).append(" |");
        }
        System.out.println(top);

        // Line 2: timeline numbers (start of first, then each end)
        StringBuilder times = new StringBuilder();
        times.append(segments.get(0).start);

        for (Segment s : segments) {
            // spacing just to keep numbers readable under the chart
            int pad = 3 + s.label.length();
            times.append(" ".repeat(Math.max(1, pad)));
            times.append(s.end);
        }
        System.out.println(times);
    }

    // Display WT/TAT + averages 
    public static void printMetrics(List<ScheduleRow> rows) {
        // Print by PID
        List<ScheduleRow> sorted = new ArrayList<>(rows);
        sorted.sort(Comparator.comparingInt(r -> r.pid));

        double sumWT = 0;
        double sumTAT = 0;

        System.out.println("\nPID  AT  BT  WT  TAT");
        for (ScheduleRow r : sorted) {
            System.out.printf("%-4d %-3d %-3d %-3d %-3d%n",
                    r.pid, r.arrival, r.burst, r.wt, r.tat);
            sumWT += r.wt;
            sumTAT += r.tat;
        }

        System.out.printf("\nAverage WT: %.2f%n", sumWT / sorted.size());
        System.out.printf("Average TAT: %.2f%n", sumTAT / sorted.size());
    }
}