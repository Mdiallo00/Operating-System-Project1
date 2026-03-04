// import java.io.BufferedReader;
// import java.io.FileReader;
// import java.io.IOException;
// import java.util.ArrayList;
// import java.util.List;

// public class Project1_menu {
//     public static void main(String args[]){
//   //System.out.println("Hello from Diallo");

 
//  // processes.setPid(12); 
//    // System.out.println(process1.getPid());
//    // System.out.println(process1.toString());
//  List<Processes> allProcesses = new ArrayList<>();

//        String fileName = "processses.txt";
//         // Delimiter as a regex. Use "\|" for a pipe character, "," for a comma.
//         String delimiterRegex = " ";

//         try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
//             String line;
           
//             Processes process = new Processes(1,2,3,4);
//             while ((line = br.readLine()) != null) {
//                     // instantiate an object of type Processes.
                  
//                     int pid;
//                     int Arrival_Time;
//                     int Burst_Time;
//                     int Priority;

//                     pid = Character.getNumericValue(line.charAt(0));
//                     Arrival_Time = Character.getNumericValue(line.charAt(2));
//                     Burst_Time = Character.getNumericValue(line.charAt(4));
//                     Priority = Character.getNumericValue(line.charAt(6));

                    
//                     process.setPid(pid);
//                     process.setArrival_Time(Arrival_Time);
//                     process.setBurst_Time(Burst_Time);
//                     process.setPriority(Priority);

//                     allProcesses.add(process);

                    
//                     process = new Processes();
//                 // lineCount++;
//                 // System.out.println("First " + lineCount + ":: " +line);
//                 // System.out.println(line.charAt(0));
//                 // //System.out.println(line.charAt(1));
//                 // System.out.println(line.charAt(2));
//                 // System.out.println(line.charAt(4));
//                 // System.out.println(line.charAt(6));
                
//                 // Split the line into an array of strings using the delimiter
//                // String[] words = line.split(delimiterRegex);
               
//                 // Process each word"
//                 // for (String word : words) {
                   
//                 //     System.out.println("Word: " + word.trim());
//                 // }

//                 // process1.setPid()


//                 // process2.setPid()
//                 // process3.setPid()
//                 // process4.setPid()
//             }
//         } catch (IOException e) {
//             e.printStackTrace();
       
// }

// System.out.println("PID  Arrival_Time  Burst_Time  Priority");
//     for(Processes task: allProcesses)
//         {
//             System.out.println(task.toString());
//         }

//             Step2Schedulers step2Schedulers = new Step2Schedulers();


//         // private List<Processes> allProcesses;


//     //private List<Processes> processes;


//          // Step 2: run FCFS
//         Step2Schedulers.ScheduleResult fcfsResult =
//                 Step2Schedulers.fcfs(allProcesses);

//         Step2Schedulers.printMetrics("FCFS", fcfsResult);


//        // Step 2: run SJF
//         Step2Schedulers.ScheduleResult sjfResult =
//                 Step2Schedulers.sjfNonPreemptive(allProcesses);

//         Step2Schedulers.printMetrics("SJF", sjfResult);

// }


   

    



// }















import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Project1_menu {

    public static void main(String[] args) {

        List<Processes> allProcesses = new ArrayList<>();
        String fileName = "processses.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            // Optional: if your file has a header, skip it
            // line = br.readLine();

            while ((line = br.readLine()) != null) {

                // If you have blank lines, skip
                if (line.trim().isEmpty()) continue;

                Processes process = new Processes();

                int pid = Character.getNumericValue(line.charAt(0));
                int Arrival_Time = Character.getNumericValue(line.charAt(2));
                int Burst_Time = Character.getNumericValue(line.charAt(4));
                int Priority = Character.getNumericValue(line.charAt(6));

                process.setPid(pid);
                process.setArrival_Time(Arrival_Time);
                process.setBurst_Time(Burst_Time);
                process.setPriority(Priority);

                allProcesses.add(process);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("PID  Arrival_Time  Burst_Time  Priority");
        for (Processes task : allProcesses) {
            System.out.println(task.toString());
        }

        // Step 2: run FCFS
        Step2Schedulers.ScheduleResult fcfsResult = Step2Schedulers.fcfs(allProcesses);
        Step2Schedulers.printMetrics("FCFS", fcfsResult);

        // Step 2: run SJF
        Step2Schedulers.ScheduleResult sjfResult = Step2Schedulers.sjfNonPreemptive(allProcesses);
        Step2Schedulers.printMetrics("SJF", sjfResult);
    }
}




