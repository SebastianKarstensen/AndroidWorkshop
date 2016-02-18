package com.sebastiank.taskapp02;

import android.content.Context;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sebastian.
 */
public class TaskStorage {

    private List<Task> listOfTasks = new ArrayList<>();

    private String file = "taskappdata";

    public TaskStorage() {

    }

    public void addTask(Task task) {
        listOfTasks.add(task);
    }
    public void removeTask(int position) { listOfTasks.remove(position); }

    public List<Task> getListOfTasks(Context ctx) {

        try{
            listOfTasks = new ArrayList<>();

            FileInputStream fin = ctx.openFileInput(file);
            int c;
            String temp="";
            String input = "";

            Task tempTask = new Task("","",false,TaskType.Shopping);
            boolean parsingName = false;
            boolean parsingDescription = false;
            boolean parsingFinished = false;
            boolean parsingType = false;

            while( (c = fin.read()) != -1){
                temp = Character.toString((char)c);

                if(parsingType == true) {
                    if(temp.equalsIgnoreCase("]")) {
                        parsingType = false;

                        if(input.equalsIgnoreCase("0")) {
                            tempTask.setType(TaskType.PHONECALL);
                        }
                        else if(input.equalsIgnoreCase("1")) {
                            tempTask.setType(TaskType.Shopping);
                        }
                        else if(input.equalsIgnoreCase("2")) {
                            tempTask.setType(TaskType.REPAIR);
                        }
                        else {
                            tempTask.setType(TaskType.WORKOUT);
                        }
                        input = "";

                        String name = tempTask.getName();
                        String description = tempTask.getDescription();
                        boolean finished = tempTask.getFinished();
                        TaskType type = tempTask.getType();

                        listOfTasks.add(new Task(name,description,finished,type));
                    }
                    else {
                        input += Character.toString((char)c);
                    }
                }

                if(parsingFinished == true) {
                    if(temp.equalsIgnoreCase("|")) {
                        parsingFinished = false;
                        if(input.equalsIgnoreCase("1")) {
                            tempTask.setFinished(true);
                        }
                        else {
                            tempTask.setFinished(false);
                        }
                        input = "";
                        parsingType = true;
                    }
                    else {
                        input += Character.toString((char)c);
                    }
                }

                if(parsingDescription == true) {
                    if(temp.equalsIgnoreCase("|")) {
                        parsingDescription = false;
                        tempTask.setDescription(input);
                        input = "";
                        parsingFinished = true;
                    }
                    else {
                        input += Character.toString((char)c);
                    }
                }

                if(parsingName == true) {
                    if(temp.equalsIgnoreCase("|")) {
                        parsingName = false;
                        tempTask.setName(input);
                        input = "";
                        parsingDescription = true;
                    }
                    else {
                        input += Character.toString((char)c);
                    }
                }

                if(temp.equalsIgnoreCase("[")) {
                    parsingName = true;
                }
            }
        }

        catch(Exception e){

        }

        return listOfTasks;
    }

    public void saveStorage(Context ctx) {
        try {
            FileOutputStream fOut = ctx.openFileOutput(file, ctx.MODE_PRIVATE);

            String data = "";
            Task tempTask;

            for(int i = 0; i < listOfTasks.size(); i++) {
                tempTask = listOfTasks.get(i);
                data = "[" + tempTask.getName() + "|" + tempTask.getDescription() + "|";

                int finished = 0;
                if(tempTask.getFinished()) {
                    finished = 1;
                }
                data += finished + "|";

                int type = 0;
                switch (tempTask.getType()) {
                    case Shopping:
                        type = 1;
                        break;

                    case REPAIR:
                        type = 2;
                        break;

                    case WORKOUT:
                        type = 3;
                        break;

                    default:

                }
                data += type + "]";

                fOut.write(data.getBytes());

                data = "";
            }
            fOut.close();
        }

        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

}
