public class Processes {
    private int pid;
    private int Arrival_Time;
    private int Burst_Time;
    private int Priority;

    public Processes(){}

    public Processes(
        int pid, int Arrival_Time, int Burst_Time, int Priority
    ){
        this.pid=pid;
        this.Arrival_Time= Arrival_Time;
        this.Burst_Time = Burst_Time;
        this.Priority = Priority;

    }

    public void setPid(int pid){
        this.pid= pid;
    }

    public int getPid(){ 
        return pid;
    }

    public void setArrival_Time(int Arrival_Time){
        this.Arrival_Time= Arrival_Time;

    }

    public int getArrival_Time(){
        return Arrival_Time;
    }


    public void setBurst_Time(int Burst_Time){
        this.Burst_Time= Burst_Time;
    }


     public int getBurst_Time(){
        return Burst_Time;
    }

       public void setPriority(int Priority){
        this.Priority= Priority;
    }

    public int getPriority(){
        return Priority;
    }


    public String toString(){
        return pid + "         "+Arrival_Time + "           "+ Burst_Time + "             "+ Priority;
    }
}
