/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.data;

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
    final StringProperty flag;
    final StringProperty leaderImage;
    
    public SubRegion(){
        name = new SimpleStringProperty("");
        capital = new SimpleStringProperty("N/A");
        leader = new SimpleStringProperty("N/A");
        leaderImage = new SimpleStringProperty("");
        flag = new SimpleStringProperty("");
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
        return leaderImage;
    }
    
    public StringProperty flagProperty(){
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
    
    public String getLeaderImage(){
        return leaderImage.get();
    }
    
    public String getFlag(){
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
    
    public void setLeaderImage(String leaderImage){
        this.leaderImage.set(leaderImage);
    }
    
    public void setFlag(String flag){
        this.flag.set(flag);
    }

    @Override
    public String toString() {
        return "SubRegion{" + 
                "\n name=" + this.getName() + 
                "\n capital=" + this.getCapital() + 
                "\n leader=" + this.getLeader() + 
                "\n flagPath=" + this.getFlag() + 
                "\n leaderPath=" + this.getFlag() + 
                '}';
    }
    
}