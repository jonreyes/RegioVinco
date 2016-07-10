/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test_bed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.junit.Test;
import static rvme.RVMEConstants.PATH_ANDORRA;
import rvme.data.DataManager;
import rvme.file.FileManager;
import static saf.settings.AppStartupConstants.PATH_WORK;

/**
 *
 * @author Jon Reyes
 */
public class TestExport {
    
    String[] importFileContents;
    String[] exportFileContents;
    
    private DataManager data;
    private FileManager instance;
    
    private File importFile;
    private File exportFile;
    
    private StringBuilder importFileSB;
    private StringBuilder exportFileSB;
    
    /**
     * Test of exportData method, of class FileManager.
     */
    @Test
    public void testExportData() throws Exception {
        System.out.println("FileManager Unit Test: Export Data\n");
        
        // INSTANTIATE DATA AND FILE MANAGER
        instance = new FileManager();
        data = new DataManager();
        
        // IMPORT THE EXPORT FILE TO TEST
        String importFileName = "Andorra.rvm"; 
        String importFilePath = PATH_ANDORRA+importFileName;
        importFile(importFilePath);
        
        // EXPORT THE IMPORT DATA TO- A NEW EXPORT FILE
        String exportFileName = "testExportAndorra.rvm";
        String exportFilePath = PATH_WORK+exportFileName;
        exportImportFile(exportFilePath);
        
         // COMPARE CONTENTS
        compareExportToImport();
    }
    
    
    private void compareExportToImport() {
        System.out.println("\nCOMPARING EXPORT FILE TO IMPORT FILE...");
        
        importFileContents = importFileSB.toString().split("\\s");
        exportFileContents = exportFileSB.toString().split("\\s");
        
        // ASSERT EXPORT FILE SIZE == ORIGINAL IMPORT FILE SIZE
        boolean equivalentSize = exportFileContents.length == importFileContents.length;
        assert(equivalentSize);
        int size = 0;
        if(equivalentSize){
            size = importFileContents.length;
        }
        
        // ASSERT EXPORT FILE CONTENTS == ORIGINAL IMPORT FILE CONTENTS
        System.out.println("\nEXPORT:IMPORT");
        
        int success = 0;
        if(equivalentSize){
            for(int i = 0; i < exportFileContents.length; i++){
                String x = importFileContents[i];
                String y = exportFileContents[i];
                System.out.printf("LINE%d:\n\'%s\'==\'%s\'\n",i, x, y);
                // SUCCESS
                assert(x.equals(y));
                if(x.equals(y)){ 
                    System.out.println("SUCCESS!\n");
                    success++;
                }
            }
        }
        
        assert(success==size);
        if(success==size) System.out.println("TESTS PASSED: 100%");
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
    
    public void exportImportFile(String exportFilePath) throws IOException{
        // EXPORT FILE
        exportFile = new File(exportFilePath);
        System.out.println("EXPORTING DATA...");
        instance.exportData(data, exportFilePath);
        
        // ASSERT DATA WAS EXPORTED
        assert(exportFile.exists());
        System.out.println("EXPORT COMPLETE\n");
        
        // SAVE AND DISPLAY EXPORTED FILE CONTENTS
        System.out.println("THE FOLLOWING DATA WAS EXPORTED:\n");
        exportFileSB = new StringBuilder();
        
        if (exportFile.exists()){
            // CHECK EXPORT DATA OUTPUT
            System.out.println("File exported at:");
            System.out.println(exportFile.getCanonicalPath());
            try (BufferedReader br = new BufferedReader(new FileReader(exportFile.getCanonicalPath()))) {
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                    exportFileSB.append(line);
                }
            }
        }
    }
}

