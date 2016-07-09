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
    final StringProperty flagPath;
    final StringProperty leaderPath;
    
    public SubRegion(){
        name = new SimpleStringProperty("");
        capital = new SimpleStringProperty("");
        leader = new SimpleStringProperty("");
        leaderPath = new SimpleStringProperty("");
        flagPath = new SimpleStringProperty("");
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
    
    public StringProperty leaderImageProperty(){
        return leaderPath;
    }
    
    public StringProperty flagProperty(){
        return flagPath;
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
    
    public String getLeaderPath(){
        return leaderPath.get();
    }
    
    public String getFlagPath(){
        return flagPath.get();
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
    
    public void setLeaderPath(String leaderImage){
        this.leaderPath.set(leaderImage);
    }
    
    public void setFlagPath(String flag){
        this.flagPath.set(flag);
    }
}