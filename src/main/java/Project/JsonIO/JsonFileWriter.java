package Project.JsonIO;// import necessary libraries
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;

import Project.Instance;
import Project.Dataset;
import Project.Label;
import Project.User;
import org.json.JSONArray;
import org.json.JSONObject;

//JsonFileWriter class
public class JsonFileWriter {
    private JSONObject newJSONObject(){
	JSONObject newjsonobject = new JSONObject();

	//This part enable us to print everything in order
	try{
	    Field changeMap = newjsonobject.getClass().getDeclaredField("map");
	    changeMap.setAccessible(true);
	    changeMap.set(newjsonobject,new LinkedHashMap<>());
	    changeMap.setAccessible(false);
	}catch(IllegalAccessException | NoSuchFieldException e){
	    e.printStackTrace();
        }
        return newjsonobject;
    }
    // export method takes dataset and users as parameters then puts the information to a json object
    public void export(Dataset dataset, ArrayList<User> users, String path){
	 // dataset part   
	JSONObject details = newJSONObject();
        details.put("dataset id",dataset.getId());
        details.put("dateset name",dataset.getDatasetName());
        details.put("maximum number of labels per instance",dataset.getMaxNumberOfLabelsPerInstance());

	 // label part
        JSONArray classLabels = new JSONArray();
        for(Label label: dataset.getLabels()){
            JSONObject classLabel=newJSONObject();
            classLabel.put("label id",label.getId());
            classLabel.put("label text",label.getText());
            classLabels.put(classLabel);
        }
        details.put("class labels",classLabels);

        // instances part
        JSONArray instances = new JSONArray();
        for (Instance instance:dataset.getInstances()) {
            JSONObject instanceObject=newJSONObject();
            instanceObject.put("id",instance.getId());
            instanceObject.put("instance",instance.getInstance());
            instances.put(instanceObject);
        }
        details.put("instances",instances);

        // writing user information
        JSONArray userList = new JSONArray();
        for(User user:users){
            JSONObject userJSONobject = newJSONObject();
            userJSONobject.put("user id",user.getUserID());
            userJSONobject.put("user name",user.getUserName());
            userJSONobject.put("user type",user.getUserType());
            userList.put(userJSONobject);
        }
        details.put("users",userList);

	    
	 // writing the results of assignments
        JSONArray assignments = new JSONArray();
        for (Instance instance:dataset.getInstances()) {
            for(User user:users){
                try {
                    for (Instance usr_inst:user.getInstances(dataset)){
                        if (instance.getId()==usr_inst.getId()){
                            JSONObject assignmentObject=newJSONObject();
                            assignmentObject.put("instance id",instance.getId());
                            JSONArray labelIDs = new JSONArray();
                            for(Label label:usr_inst.getLabels()) {
                                labelIDs.put(label.getId());
                            }
                            assignmentObject.put("class label ids",labelIDs);
                            assignmentObject.put("user id",user.getUserID());
                            // date time operations
                            LocalDateTime myDateObj = LocalDateTime.now();
                            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy , HH:mm:ss");
                            String formattedDate = myDateObj.format(myFormatObj);
                            assignmentObject.put("date time",formattedDate);
                            assignments.put(assignmentObject);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        details.put("class label assignments",assignments);

        try (FileWriter file = new FileWriter(path+".tmp")) {
            file.write(details.toString(2));
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        while(true){
            File f1 = new File(path+".tmp");
            if(f1.length()!=0){
                File f2=new File(path);
                f2.delete();
                f2=new File(path);
                if(f1.renameTo(f2))break;
            }
        }

    }

}