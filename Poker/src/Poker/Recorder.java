package Poker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Recorder{
    String fileName;
    File file;
    FileWriter filewriter;
    private boolean active;
    public Recorder(String fileName)
    {
        active = true;
        this.fileName = fileName;
        try{
            file = new File(fileName);
            filewriter = new FileWriter(file);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        }catch(IOException e){
            System.out.println(e);
        }
    }

    public void write (String information){
        if(!active)
            return;
        try{
            filewriter.write(information);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void fileClose()
    {
        if(!active)
            return;
        try {
            filewriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }
}