/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.stage.FileChooser;
import properties_manager.PropertiesManager;
import static rvme.PropertyType.ADD_ERROR_MESSAGE;
import static rvme.PropertyType.ADD_ERROR_TITLE;
import static rvme.PropertyType.ADD_TITLE;
import static rvme.PropertyType.EXPORT_EXT;
import static rvme.PropertyType.EXPORT_EXT_DESC;
import static rvme.PropertyType.EXPORT_TITLE;
import static rvme.PropertyType.IMAGE_EXT_DESC;
import static rvme.PropertyType.JPG_EXT;
import static rvme.PropertyType.PNG_EXT;
import static rvme.RVMEConstants.PATH_EXPORT;
import rvme.data.DataManager;
import rvme.data.SubRegion;
import rvme.gui.NewMapDialogSingleton;
import rvme.gui.SubRegionDialogSingleton;
import rvme.gui.Workspace;
import saf.AppTemplate;
import static saf.settings.AppPropertyType.LOAD_ERROR_MESSAGE;
import static saf.settings.AppPropertyType.LOAD_ERROR_TITLE;
import static saf.settings.AppPropertyType.SAVE_COMPLETED_MESSAGE;
import static saf.settings.AppPropertyType.SAVE_COMPLETED_TITLE;
import static saf.settings.AppPropertyType.SAVE_WORK_TITLE;
import static saf.settings.AppPropertyType.WORK_FILE_EXT;
import static saf.settings.AppPropertyType.WORK_FILE_EXT_DESC;
import static saf.settings.AppStartupConstants.FILE_PROTOCOL;
import static saf.settings.AppStartupConstants.PATH_IMAGES;
import static saf.settings.AppStartupConstants.PATH_WORK;
import saf.ui.AppMessageDialogSingleton;

/**
 *
 * @author Jon Reyes
 */
public class RVMEController {
    AppTemplate app;
    PropertiesManager props;
    boolean saved;
    File currentWorkFile;
    
    public RVMEController(AppTemplate initApp){
        app = initApp;
        props = PropertiesManager.getPropertiesManager();
    }
    
    public void newMap(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        NewMapDialogSingleton newMapDialog = workspace.getNewMapDialog();
        
        newMapDialog.reset();
        newMapDialog.show();
    }
    
    public void saveMap() {
        try {
	    // MAYBE WE ALREADY KNOW THE FILE
	    if (currentWorkFile != null) {
		save(currentWorkFile);
	    }
	    // OTHERWISE WE NEED TO PROMPT THE USER
	    else {
		// PROMPT THE USER FOR A FILE NAME
		FileChooser fc = new FileChooser();
                DataManager dataManager = (DataManager) app.getDataComponent();
		fc.setInitialFileName(dataManager.getName());
                fc.setInitialDirectory(dataManager.getParent());
		fc.setTitle(props.getProperty(SAVE_WORK_TITLE));
		fc.getExtensionFilters().addAll(
		new FileChooser.ExtensionFilter(props.getProperty(WORK_FILE_EXT_DESC), props.getProperty(WORK_FILE_EXT)));

		File selectedFile = fc.showSaveDialog(app.getGUI().getWindow());
		if (selectedFile != null) {
		    save(selectedFile);
		}
	    }
        } catch (Exception e) {
	    Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(props.getProperty(LOAD_ERROR_TITLE));
            alert.setContentText(props.getProperty(LOAD_ERROR_MESSAGE));
            alert.showAndWait();
        }
    }
    
    // HELPER METHOD FOR SAVING WORK
    private void save(File selectedFile) throws IOException {
	// SAVE IT TO A FILE
	app.getFileComponent().saveData(app.getDataComponent(), selectedFile.getPath());
	
	// MARK IT AS SAVED
	currentWorkFile = selectedFile;
	saved = true;
	
	// TELL THE USER THE FILE HAS BEEN SAVED
	AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
        dialog.show(props.getProperty(SAVE_COMPLETED_TITLE),props.getProperty(SAVE_COMPLETED_MESSAGE));
		    
	// AND REFRESH THE GUI, WHICH WILL ENABLE AND DISABLE
	// THE APPROPRIATE CONTROLS
	Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.updateFileControls(saved);
    }
    
    public void exportMap(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_EXPORT));
        fc.setTitle(props.getProperty(EXPORT_TITLE));
        fc.getExtensionFilters().addAll(
		new FileChooser.ExtensionFilter(props.getProperty(EXPORT_EXT_DESC), props.getProperty(EXPORT_EXT)));
        File exportFile = fc.showSaveDialog(app.getGUI().getWindow());
    }
    
    public void updateBGColor(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        
        Color bgColor = workspace.getBGCPicker().getValue();
        dataManager.setBackgroundColor(bgColor);
        workspace.updateFileControls(false);
    }
    
    public void updateBorderColor(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        
        Color borderColor = workspace.getBCPicker().getValue();
        dataManager.setBorderColor(borderColor);
        workspace.updateFileControls(false);
    }
    
    public void updateBorderThickness(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        
        double borderThickness = workspace.getBTSlider().getValue();
        dataManager.setBorderThickness(borderThickness);
        
        workspace.getBTValue().setText(String.format("%.2f", borderThickness));
        workspace.updateFileControls(false);
    }
    
    public void updateZoom(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        
        double zoom = workspace.getZoomSlider().getValue();
        dataManager.setZoom(zoom);
        
        workspace.getZoomValue().setText(String.format("%.2fx", zoom));
        
        StackPane mapStack = workspace.getMapStack();
        for(Node n: mapStack.getChildren()){
            if(n instanceof Group || n instanceof ImageView){
                n.setScaleX(zoom);
                n.setScaleY(zoom);
            }
        }
        
        workspace.updateFileControls(false);
    }
    
    public void editSubRegion(MouseEvent e){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        SubRegionDialogSingleton subRegionDialog = workspace.getSubRegionDialog();
        
        subRegionDialog.update(e);
        subRegionDialog.show();
    }
    
    public void updateTableData(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        
        ObservableList<SubRegion> mapData = workspace.getMapData();
        dataManager.setTableItems(mapData);
        workspace.updateFileControls(false);
    }
    
    public void addImage(){
         FileChooser fc = new FileChooser();
         fc.setInitialDirectory(new File(PATH_IMAGES));
         fc.setTitle(props.getProperty(ADD_TITLE));
         fc.getExtensionFilters().addAll(
		new FileChooser.ExtensionFilter(props.getProperty(IMAGE_EXT_DESC), props.getProperty(PNG_EXT), props.getProperty(JPG_EXT)));
         File selectedFile = fc.showOpenDialog(app.getGUI().getWindow());
         if (selectedFile != null) {
            try {
                String imagePath = FILE_PROTOCOL + selectedFile.getPath();
                Image image = new Image(imagePath);
                ImageView addImageView = new ImageView(image);
                addImageView.setPreserveRatio(true);
                addImageView.setFitWidth(200);
                initImageControls(addImageView);
                
                Workspace workspace = (Workspace) app.getWorkspaceComponent();
                DataManager dataManager = (DataManager) app.getDataComponent();
                
                StackPane mapStack = workspace.getMapStack();
                mapStack.getChildren().add(addImageView);
                workspace.updateFileControls(false);
                workspace.reloadWorkspace();

            } catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText(props.getProperty(ADD_ERROR_TITLE));
                alert.setContentText(props.getProperty(ADD_ERROR_MESSAGE));
                alert.showAndWait();
            }
        }
    }
    
    public void removeImage(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        StackPane mapStack = workspace.getMapStack();
        mapStack.getChildren().remove(workspace.getSelection());
        workspace.updateFileControls(false);
    }
    
    private void selectImage(ImageView imageView){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.setSelection(imageView);
    }
    
    private void initImageControls(ImageView imageView){
        imageView.setOnMouseClicked(e->{
            if(e.getClickCount()==2){
                selectImage(imageView);
            }
        });
    }
    
    public void playAnthem(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.getPlayButton().setVisible(false);
        workspace.getPauseButton().setVisible(true);
    }
    
    public void pauseAnthem(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.getPauseButton().setVisible(false);
        workspace.getPlayButton().setVisible(true);
    }
    
    public void reassignColors(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        
        ArrayList<Color> randomColors = dataManager.randomColors();
        int i = 0;
        for(Node node: workspace.getRegion().getChildren()){
            if (node instanceof Polygon){
                ((Polygon) node).setFill(randomColors.get(i));
                i++;
            }
        }
        dataManager.setMapColors(randomColors);
        
        workspace.updateFileControls(false);
    }
}
