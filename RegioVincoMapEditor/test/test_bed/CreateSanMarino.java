/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_bed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.junit.Test;
import static rvme.RVMEConstants.PATH_EUROPE;
import static rvme.RVMEConstants.PATH_RAW_MAP_DATA;
import rvme.data.DataManager;
import rvme.data.SubRegion;
import rvme.file.FileManager;
import static saf.settings.AppStartupConstants.PATH_WORK;

/**
 *
 * @author xion
 */
public class CreateSanMarino {
    
    /**
     * Test of saveData method, of class FileManager.
     */
    @Test
    public void testSaveData() throws Exception {
        System.out.println("FileManager Unit Test: Save Data");
        
        // INSTANTIATE MANAGERS
        FileManager instance = new FileManager();
        DataManager data = new DataManager();

        // SPECIFY SUBREGION
        String subRegionName = "San Marino";
        
        // HARD CODE DATA VALUES
        System.out.println("SETTING UP HARD CODE VALUES...");
        
        // IMPORT AVAILABLE VALUES FROM EXPORT FILE
        data.setName(subRegionName);
        String subRegionRVM = subRegionName+".rvm";
        String subRegionFolder = PATH_EUROPE+subRegionName+"/";
        String subRegionPath = subRegionFolder+subRegionRVM;
        File subRegionFile = new File(subRegionPath);
        assert(subRegionFile.exists());
        
        System.out.println("EXPORT FILE IMPORTED!");
        instance.importData(data, subRegionPath);
        
        // SET LEADER IMAGES AND FLAGS
        int i = 0;
        for(SubRegion s: data.getTableItems()){
            String leaderImage = s.getLeader()+".png";
            String leaderImagePath = subRegionFolder+leaderImage;
            data.getTableItems().get(i).setLeaderImage(leaderImagePath);
            String flag = s.getName()+" Flag.png";
            String flagPath = subRegionFolder+flag;
            data.getTableItems().get(i).setFlag(flagPath);
            i++;
        }
        
        // LOAD GEOMETRY FILE
        String geoFileName = subRegionName+".json";
        String geoPath = PATH_RAW_MAP_DATA+geoFileName;
        File geoFile = new File(geoPath);
        assert(geoFile.exists());
        
        System.out.println("GEOMETRY FILE LOADED!");
        instance.loadGeometry(data, geoPath);
        
        // SET COLORS
        data.setMapColors(data.randomColors());
        
        // SET ANTHEM
        String subRegionAnthem = subRegionName + " National Anthem.mid";
        String anthemPath = subRegionFolder+subRegionAnthem;
        data.setAnthem(anthemPath);
        
        // COMPLETE SAVE DATA
        System.out.println("SAVING DATA...");
        String savePath = PATH_WORK+subRegionName+".json";
        File savedFile = new File(savePath);
        instance.saveData(data, savePath);
        
        // CHECK SAVE DATA EXISTS
        assert(savedFile.exists());
        System.out.println("SAVE COMPLETE!");
        if(savedFile.exists()){
            // CHECK SAVE DATA OUTPUT
            System.out.println("File saved at:");
            System.out.println(savedFile.getCanonicalPath());
            try (BufferedReader br = new BufferedReader(new FileReader(savedFile.getCanonicalPath()))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
            }
        }
    }
}

