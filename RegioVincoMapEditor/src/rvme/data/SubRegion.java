/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.data;

import java.io.File;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Jon Reyes
 */
public class SubRegion {
    final StringProperty name;
    final StringProperty capital;
    final StringProperty leader;
    final ObjectProperty<File> flag;
    final ObjectProperty<File> leaderImage;
    
    public SubRegion(){
        name = new SimpleStringProperty("");
        capital = new SimpleStringProperty("");
        leader = new SimpleStringProperty("");
        leaderImage = new SimpleObjectProperty();
        flag = new SimpleObjectProperty();
    }
    
    public SubRegion(String name, String capital, String leader){
        this();
        this.name.set(name);
        this.capital.set(capital);
        this.leader.set(leader);
    }
    
    public StringProperty nameProperty() {
        return name;
    }

    public StringProperty capitalProperty() {
        return capital;
    }

    public StringProperty leaderProperty() {
        return leader;
    }
    
    public ObjectProperty leaderImageProperty(){
        return leaderImage;
    }
    
    public ObjectProperty flagProperty(){
        return flag;
    }
    
    public String getName(){
        return name.get();
    }
    
    public String getCapital(){
        return capital.get();
    }
    
    public String getLeader(){
        return leader.get();
    }
    
    public File getLeaderImage(){
        return leaderImage.get();
    }
    
    public File getFlag(){
        return flag.get();
    }
    
    public void setName(String name){
        this.name.set(name);
    }
    
    public void setCapital(String capital){
        this.capital.set(capital);
    }
    
    public void setLeader(String leader){
        this.leader.set(leader);
    }
    
    public void setLeaderImage(File leaderImage){
        this.leaderImage.set(leaderImage);
    }
    
    public void setFlag(File flag){
        this.flag.set(flag);
    }
}