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
import static rvme.RVMEConstants.PATH_ANDORRA;
import static rvme.RVMEConstants.PATH_RAW_MAP_DATA;
import rvme.data.DataManager;
import rvme.file.FileManager;
import static saf.settings.AppStartupConstants.PATH_WORK;

/**
 *
 * @author xion
 */
public class TestSave {
    
    /**
     * Test of saveData method, of class FileManager.
     */
    @Test
    public void testSaveData() throws Exception {
        System.out.println("FileManager Unit Test: Save Data");
        
        // INSTANTIATE MANAGERS
        FileManager instance = new FileManager();
        DataManager data = new DataManager();

        // NAME THE FILE
        String fileName = "Andorra";
        
        // HARD CODE DATA VALUES
        System.out.println("SETTING UP HARD CODE VALUES...");
        
        // IMPORT AVAILABLE VALUES FROM EXPORT FILE
        data.setName(fileName);
        String andorraRVM = "Andorra.rvm";
        String andorraPath = PATH_ANDORRA+andorraRVM;
        File andorraFile = new File(andorraPath);
        assert(andorraFile.exists());
        
        System.out.println("EXPORT FILE IMPORTED!");
        instance.importData(data, andorraPath);

        // LOAD GEOMETRY FILE
        String geoFileName = "Andorra.json";
        String geoPath = PATH_RAW_MAP_DATA+geoFileName;
        File geoFile = new File(geoPath);
        assert(geoFile.exists());
        
        System.out.println("GEOMETRY FILE LOADED!");
        instance.loadGeometry(data, geoPath);

        // SET COLORS
        data.setMapColors(data.randomColors());
        
        // COMPLETE SAVE DATA
        System.out.println("SAVING DATA...");
        String savePath = PATH_WORK+fileName+".json";
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

