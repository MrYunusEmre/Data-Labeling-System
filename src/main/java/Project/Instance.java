package Project;

import java.util.ArrayList;

public class Instance {

    //Class attributes definitions
    private long id;
    private String instance;
    private ArrayList<Label> labels ;
    private long timeElapsed ;

    //Overloaded constructor
    public Instance(long id, String instance) {
        this.id = id;
        this.instance = instance;
        labels = new ArrayList<>();
    }

    //Get method for ID
    public long getId() {
        return id;
    }

    //Set method for ID
    public void setId(long id) {
        this.id = id;
    }

    //Get method for com.Instance
    public String getInstance() {
        return instance;
    }

    //Set method for com.Instance
    public void setInstance(String instance) {
        this.instance = instance;
    }

    //Get method for Labels
    public ArrayList<Label> getLabels() {
        return labels ;
    }

    //Set method for Labels
    public void setLabels(ArrayList<Label> labels) {
        this.labels = labels;
    }

    //This method is for add label to the instance
    public void addLabelToInstance(Label label){
        labels.add(label);
    }

    //This method is for to write the labels
    void writeLabels(){
        for (Label label : labels) {
            System.out.println("label id ->" +label.getId());
        }
    }

    public long getTimeElapsed() {
        return timeElapsed;
    }

    public void setTimeElapsed(long timeElapsed) {
        this.timeElapsed = timeElapsed;
    }
}