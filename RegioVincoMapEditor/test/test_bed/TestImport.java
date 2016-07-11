package test_bed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.paint.Color;
import org.junit.Test;
import static rvme.RVMEConstants.PATH_ANDORRA;
import rvme.data.DataManager;
import rvme.data.SubRegion;
import rvme.file.FileManager;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Jon Reyes
 */
public class TestImport {

    private DataManager data;
    private FileManager instance;
    
    private File importFile;
    private StringBuilder importFileSB;
    private ArrayList<String> parsedDataManager;
    private ArrayList<String> parsedImportData;
     /**
     * Test of loadData method, of class FileManager.
     */
    @Test
    public void testImportData() throws Exception {
        System.out.println("FileManager Unit Test: Import Data\n");
        
        // INSTANTIATE DATA AND FILE MANAGERS
        instance = new FileManager();
        data = new DataManager();
        
        // SPECIFY DATA TO IMPORT
        String importFileName = "Andorra.rvm";
        String importFilePath = PATH_ANDORRA+importFileName;
        // IMPORT FILE
        importFile(importFilePath);
        
        compareDataToImport();
    }
    
    public void importFile(String importFilePath) throws IOException{
        // IMPORT FILE
        importFile = new File(importFilePath);
        
        // ASSERT VALID IMPORT FILE
        assert(importFile.exists());
        if(importFile.exists()){    
            // DISPLAY IMPORT PROGRESS
            System.out.println("IMPORTING DATA...");
            instance.importData(data, importFilePath);
            System.out.println("IMPORT COMPLETE\n");

            // VALIDATE THE IMPORT
            // DISPLAY NEW CONTENTS OF THE DATA MANAGER
            System.out.println("THE FOLLOWING DATA WAS IMPORTED:\n");
            System.out.println(data+"\n");

            // DISPLAY THE ORIGINAL CONTENTS OF THE IMPORT FILE
            importFileSB = new StringBuilder();
            try (BufferedReader br = new BufferedReader(new FileReader(importFile.getCanonicalPath()))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                //System.out.println(line);
                importFileSB.append(line);
                }
            }
        }
    }
    
    private void compareDataToImport() {
        buildComparableDataFormat();
        
        System.out.println("\nCOMPARING EXPORT FILE TO IMPORT FILE...\n");
        
        boolean equivalentSize = parsedDataManager.size()==parsedImportData.size();
        assert(equivalentSize);
        
        System.out.println("DATA==IMPORT\n");
        int success = 0;
        if (equivalentSize){
            for(int i = 0; i < parsedImportData.size(); i+=2){
                String managerData = parsedDataManager.get(i+1);
                String importData = parsedImportData.get(i+1);
                boolean equivalent = managerData.equals(importData);
                assert(equivalent);
                if(equivalent){
                    System.out.printf("%s==%s\nSUCCESS!\n\n",managerData, importData);
                    success++;
                }
            }
        }
        
        assert(success==parsedImportData.size()/2);
        System.out.println("TESTS PASSED 100%");
    }

    private void buildComparableDataFormat() {
        String[] importFileContents = importFileSB.toString().split(":|=|\\{|\\}|\"|,|\\[|\\]");
        String[] dataManagerContents = data.toString().split("\\n|=|\\[|\\]|\\{|\\}|,|:|.*Property|value|DataManager|\\W|parent|null");
        
        
        // GET DATA FROM ORIGINAL IMPORT FILE IN COMPARABLE FORMAT
        parsedImportData = new ArrayList<>();
        for(String s: importFileContents){
            if(!s.startsWith(" ")&&s.length()>0&&!s.equals("subregions")){
                if(s.endsWith(" ")) s = s.split(" ")[0];
                parsedImportData.add(s);
            }
        }
        
        // DISPLAY THE ORIGINAL CONTENTS OF THE IMPORT FILE
        System.out.println("ORIGINAL CONTENTS OF IMPORT FILE:\n");
        for(int i = 0; i < parsedImportData.size(); i+=2){
            System.out.printf("%s:%s\n", parsedImportData.get(i), parsedImportData.get(i+1));
        }
        
        // GET CURRENT DATA FROM DATA MANAGER IN COMPARABLE FORMAT
        parsedDataManager = new ArrayList<>();
        for(String s : dataManagerContents){
            if(s.length()>0&&parsedDataManager.size()<8) parsedDataManager.add(s);
        }
        
        int v = 255;
        int i = 0;
        for(SubRegion s : data.getTableItems()){
            parsedDataManager.add("name");
            parsedDataManager.add(s.getName());
            parsedDataManager.add("capital");
            parsedDataManager.add(s.getCapital());
            parsedDataManager.add("leader");
            parsedDataManager.add(s.getLeader());
            Color c = data.getMapColors().get(i);
            parsedDataManager.add("red");
            parsedDataManager.add(String.valueOf((int)(c.getRed()*v)));
            parsedDataManager.add("green");
            parsedDataManager.add(String.valueOf((int)(c.getGreen()*v)));
            parsedDataManager.add("blue");
            parsedDataManager.add(String.valueOf((int)(c.getBlue()*v)));
            i++;
        }
        
        // DISPLAY NEW CONTENTS OF THE DATA MANAGER
        System.out.println("\nNEW CONTENTS OF DATA MANAGER:\n");
        for(i = 0; i < parsedDataManager.size(); i+=2){
            System.out.printf("%s:%s\n", parsedDataManager.get(i), parsedDataManager.get(i+1));
        }
    }
}
