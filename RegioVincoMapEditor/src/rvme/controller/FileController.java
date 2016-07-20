/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.controller;

import java.io.File;
import java.io.IOException;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;
import properties_manager.PropertiesManager;
import static rvme.PropertyType.EXPORT_ERROR_MESSAGE;
import static rvme.PropertyType.EXPORT_ERROR_TITLE;
import static rvme.PropertyType.EXPORT_EXT;
import static rvme.PropertyType.JSON_EXT;
import static rvme.PropertyType.PNG_EXT;
import static rvme.RVMEConstants.PATH_WORLD;
import rvme.data.DataManager;
import rvme.gui.Workspace;
import saf.AppTemplate;
import saf.components.AppDataComponent;
import saf.components.AppFileComponent;
import static saf.settings.AppPropertyType.LOAD_ERROR_MESSAGE;
import static saf.settings.AppPropertyType.LOAD_ERROR_TITLE;
import static saf.settings.AppPropertyType.LOAD_WORK_TITLE;
import static saf.settings.AppPropertyType.SAVE_ERROR_MESSAGE;
import static saf.settings.AppPropertyType.SAVE_ERROR_TITLE;
import static saf.settings.AppPropertyType.SAVE_UNSAVED_WORK_MESSAGE;
import static saf.settings.AppPropertyType.SAVE_UNSAVED_WORK_TITLE;
import static saf.settings.AppPropertyType.SAVE_WORK_TITLE;
import static saf.settings.AppPropertyType.WORK_FILE_EXT;
import static saf.settings.AppPropertyType.WORK_FILE_EXT_DESC;
import static saf.settings.AppStartupConstants.PATH_WORK;
import saf.ui.AppMessageDialogSingleton;
import saf.ui.AppYesNoCancelDialogSingleton;

/**
 *
 * @author Jon Reyes
 */
public class FileController {
    AppTemplate app;
    PropertiesManager props;
    boolean saved;
    boolean exported;
    File currentWorkFile;
    
     public FileController(AppTemplate initApp){
        saved = true;
        exported = true;
        app = initApp;
        props = PropertiesManager.getPropertiesManager();
    }
    
    public void renameMap()throws IOException{
        DataManager dataManager = (DataManager) app.getDataComponent();
        app.getFileComponent().saveData(app.getDataComponent(), currentWorkFile.getPath());
        File renamedFile = new File(currentWorkFile.getParent()+"/"+dataManager.getName()+props.getProperty(JSON_EXT).substring(1));
        currentWorkFile.renameTo(renamedFile);
        File renameDirectory = new File(currentWorkFile.getParentFile().getParent()+"/"+dataManager.getName());
        currentWorkFile.getParentFile().renameTo(renameDirectory);
        
    }
    
    public void newMap() throws IOException{
        DataManager dataManager = (DataManager) app.getDataComponent();
        File parentDirectory = new File(PATH_WORLD+"/"+dataManager.getParent());
        if (!parentDirectory.exists()) parentDirectory.mkdir();
        File regionDirectory = new File(parentDirectory.getPath()+"/"+dataManager.getName());
        if (!regionDirectory.exists()) regionDirectory.mkdir();
        File newFile = new File(regionDirectory.getPath()+"/"+dataManager.getName()+props.getProperty(JSON_EXT).substring(1));
        if (!newFile.exists()) newFile.createNewFile();
        app.getFileComponent().saveData(app.getDataComponent(),newFile.getPath());
        currentWorkFile = newFile;
    }
    
    public void saveMap() {
        try {
	    // MAYBE WE ALREADY KNOW THE FILE
	    if (currentWorkFile != null) {
		save(currentWorkFile);
	    }
        } catch (IOException ioe) {
            Workspace workspace = (Workspace) app.getWorkspaceComponent();
            workspace.getProgressDialog().show();
	    Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(props.getProperty(SAVE_ERROR_TITLE));
            alert.setContentText(props.getProperty(SAVE_ERROR_MESSAGE));
            alert.showAndWait();
        }
    }
    
    // HELPER METHOD FOR SAVING WORK
    private void save(File selectedFile) throws IOException {
	// SAVE IT TO A FILE
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.getProgressDialog().show();
        app.getFileComponent().saveData(app.getDataComponent(), selectedFile.getPath());
        workspace.getProgressDialog().update(0);

        
	// MARK IT AS SAVED
	currentWorkFile = selectedFile;
	saved = true;
    }
    
    /**
     * This helper method verifies that the user really wants to save their
     * unsaved work, which they might not want to do. Note that it could be used
     * in multiple contexts before doing other actions, like creating new
     * work, or opening another file. Note that the user will be
     * presented with 3 options: YES, NO, and CANCEL. YES means the user wants
     * to save their work and continue the other action (we return true to
     * denote this), NO means don't save the work but continue with the other
     * action (true is returned), CANCEL means don't save the work and don't
     * continue with the other action (false is returned).
     *
     * @return true if the user presses the YES option to save, true if the user
     * presses the NO option to not save, false if the user presses the CANCEL
     * option to not continue.
     */
    private boolean promptToSave() throws IOException {
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	
	// CHECK TO SEE IF THE CURRENT WORK HAS
	// BEEN SAVED AT LEAST ONCE
	
        // PROMPT THE USER TO SAVE UNSAVED WORK
	AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton.getSingleton();
        yesNoDialog.show(props.getProperty(SAVE_UNSAVED_WORK_TITLE), props.getProperty(SAVE_UNSAVED_WORK_MESSAGE));
        
        // AND NOW GET THE USER'S SELECTION
        String selection = yesNoDialog.getSelection();

        // IF THE USER SAID YES, THEN SAVE BEFORE MOVING ON
        if (selection.equals(AppYesNoCancelDialogSingleton.YES)) {
            // SAVE THE DATA FILE
            AppDataComponent dataManager = app.getDataComponent();
	    
	    if (currentWorkFile == null) {
		// PROMPT THE USER FOR A FILE NAME
		FileChooser fc = new FileChooser();
		fc.setInitialDirectory(new File(PATH_WORK));
		fc.setTitle(props.getProperty(SAVE_WORK_TITLE));
		fc.getExtensionFilters().addAll(
		new FileChooser.ExtensionFilter(props.getProperty(WORK_FILE_EXT_DESC), props.getProperty(WORK_FILE_EXT)));

		File selectedFile = fc.showSaveDialog(app.getGUI().getWindow());
		if (selectedFile != null) {
		    save(selectedFile);
		    saved = true;
		}
	    }
	    else {
		save(currentWorkFile);
		saved = true;
	    }
        } // IF THE USER SAID CANCEL, THEN WE'LL TELL WHOEVER
        // CALLED THIS THAT THE USER IS NOT INTERESTED ANYMORE
        else if (selection.equals(AppYesNoCancelDialogSingleton.CANCEL)) {
            return false;
        }

        // IF THE USER SAID NO, WE JUST GO ON WITHOUT SAVING
        // BUT FOR BOTH YES AND NO WE DO WHATEVER THE USER
        // HAD IN MIND IN THE FIRST PLACE
        return true;
    }
    
    public void loadMap() {
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            boolean continueToOpen = true;
            if (!saved) {
                // THE USER CAN OPT OUT HERE WITH A CANCEL
                continueToOpen = promptToSave();
            }

            // IF THE USER REALLY WANTS TO OPEN A Course
            if (continueToOpen) {
                // GO AHEAD AND PROCEED LOADING A Course
                promptToOpen();
            }
        } catch (IOException ioe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(props.getProperty(LOAD_ERROR_TITLE));
            alert.setContentText(props.getProperty(LOAD_ERROR_MESSAGE));
            alert.showAndWait();
        }
    }
    
    private void load(File selectedFile){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        try {
            AppDataComponent dataManager = app.getDataComponent();	
            AppFileComponent fileManager = app.getFileComponent();
            workspace.getProgressDialog().show();
            fileManager.loadData(dataManager, selectedFile.getAbsolutePath());
            workspace.getProgressDialog().update(1);
            saved = true;
            exported = true;
            currentWorkFile = selectedFile;
        } catch (Exception e) {
            workspace.getProgressDialog().hide();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(props.getProperty(LOAD_ERROR_TITLE));
            alert.setContentText(props.getProperty(LOAD_ERROR_MESSAGE));
            alert.showAndWait();
        }
    }
    
    /**
     * This helper method asks the user for a file to open. The user-selected
     * file is then loaded and the GUI updated. Note that if the user cancels
     * the open process, nothing is done. If an error occurs loading the file, a
     * message is displayed, but nothing changes.
     */
    private void promptToOpen() {
	// WE'LL NEED TO GET CUSTOMIZED STUFF WITH THIS
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	
        // AND NOW ASK THE USER FOR THE FILE TO OPEN
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_WORK));
	fc.setTitle(props.getProperty(LOAD_WORK_TITLE));
        File selectedFile = fc.showOpenDialog(app.getGUI().getWindow());
        // ONLY OPEN A NEW FILE IF THE USER SAYS OK
        if (selectedFile != null) {
            load(selectedFile);
        }
    }
    
    public void exportMap(){
        try{
            if(currentWorkFile != null){
                DataManager dataManager = (DataManager) app.getDataComponent();
                File exportDirectory = new File(PATH_WORLD+"/"+dataManager.getParent()+"/"+dataManager.getName());
                File exportFile = new File(exportDirectory.getPath()+"/"+dataManager.getName()+props.getProperty(EXPORT_EXT).substring(1));
                export(exportFile);
            }
        } catch (Exception e){
            Workspace workspace = (Workspace) app.getWorkspaceComponent();
            workspace.getProgressDialog().hide();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(props.getProperty(EXPORT_ERROR_TITLE));
            alert.setContentText(props.getProperty(EXPORT_ERROR_MESSAGE));
            alert.showAndWait();
        }
    }
    
    // HELPER METHOD FOR EXPORTING WORK
    private void export(File exportFile) throws IOException {
        // EXPORT IT TO A FILE
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.getProgressDialog().show();
        app.getFileComponent().exportData(app.getDataComponent(), exportFile.getPath());
        workspace.getProgressDialog().update(2);
        
        // EXPORT PNG
        DataManager dataManager = (DataManager) app.getDataComponent();
        SnapshotParameters params = new SnapshotParameters();   
        WritableImage image = workspace.getMapStack().snapshot(params, null);
        File file = new File(exportFile.getParent()+"/"+dataManager.getName()+props.getProperty(PNG_EXT).substring(1));
        System.out.println(file.getPath());
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        
	// MARK IT AS EXPORTED
	exported = true;
    }
    
    /**
     * This method will exit the application, making sure the user doesn't lose
     * any data first.
     * 
     */
    public void exitApp() {
        try {
            // WE MAY HAVE TO SAVE CURRENT WORK
            boolean continueToExit = true;
            if (!saved) {
                // THE USER CAN OPT OUT HERE
                continueToExit = promptToSave();
            }

            // IF THE USER REALLY WANTS TO EXIT THE APP
            if (continueToExit) {
                // EXIT THE APPLICATION
                System.exit(0);
            }
        } catch (IOException ioe) {
                AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
		PropertiesManager props = PropertiesManager.getPropertiesManager();
                dialog.show(props.getProperty(SAVE_ERROR_TITLE), props.getProperty(SAVE_ERROR_MESSAGE));
        }
    }
    
    public boolean isSaved(){
        return saved;
    }
    
    public boolean isExported(){
        return exported;
    }
    
    public void markSaved(boolean saved){
        this.saved = saved;
    }
    
    public void markExported(boolean exported){
        this.exported = exported;
    }
}
