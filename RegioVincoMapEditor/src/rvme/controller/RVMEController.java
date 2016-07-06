/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.controller;

import java.io.File;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import properties_manager.PropertiesManager;
import static rvme.PropertyType.ADD_ERROR_MESSAGE;
import static rvme.PropertyType.ADD_ERROR_TITLE;
import static rvme.PropertyType.ADD_TITLE;
import static rvme.PropertyType.EXPORT_TITLE;
import static rvme.PropertyType.IMAGE_EXT_DESC;
import static rvme.PropertyType.JPG_EXT;
import static rvme.PropertyType.PNG_EXT;
import rvme.data.DataManager;
import rvme.data.SubRegion;
import rvme.gui.Workspace;
import saf.AppTemplate;
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
    
    public RVMEController(AppTemplate initApp){
        app = initApp;
        props = PropertiesManager.getPropertiesManager();
    }
    
    public void exportMap(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File(PATH_WORK));
        fc.setTitle(props.getProperty(EXPORT_TITLE));
        fc.getExtensionFilters().addAll(
		new FileChooser.ExtensionFilter(props.getProperty(WORK_FILE_EXT_DESC), props.getProperty(WORK_FILE_EXT)));
        File exportFile = fc.showSaveDialog(app.getGUI().getWindow());
    }
    
    public void updateBGColor(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        
        Color bgColor = workspace.getBGCPicker().getValue();
        dataManager.setBGColor(bgColor);
        workspace.getMapBG().setFill(bgColor);
    }
    
    public void updateBorderColor(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        
        Color borderColor = workspace.getBCPicker().getValue();
        dataManager.setBorderColor(borderColor);
        workspace.getMapBorder().setStroke(borderColor);
    }
    
    public void updateBorderThickness(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        
        double borderThickness = workspace.getBTSlider().getValue();
        dataManager.setBorderThickness(borderThickness);
        workspace.getBTSlider().setValue(borderThickness);
    }
    
    public void updateZoom(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        
        double zoom = workspace.getZoomSlider().getValue();
        dataManager.setZoom(zoom);
        workspace.getZoomSlider().setValue(zoom);
    }
    
    public void updateTableData(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        
        ObservableList<SubRegion> mapData = workspace.getMapData();
        dataManager.setMapData(mapData);
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
                initImageControls(addImageView);
                
                Workspace workspace = (Workspace) app.getWorkspaceComponent();
                DataManager dataManager = (DataManager) app.getDataComponent();
                
                StackPane mapStack = workspace.getMapStack();
                mapStack.getChildren().add(addImageView);
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
}
