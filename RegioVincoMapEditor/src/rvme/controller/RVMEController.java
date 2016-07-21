/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.controller;

import audio_manager.AudioManager;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
import static rvme.PropertyType.ANTHEM_ERROR_MESSAGE;
import static rvme.PropertyType.ANTHEM_ERROR_TITLE;
import static rvme.PropertyType.IMAGE_EXT_DESC;
import static rvme.PropertyType.JPG_EXT;
import static rvme.PropertyType.PNG_EXT;
import rvme.data.DataManager;
import rvme.data.SubRegion;
import rvme.gui.NewMapDialogSingleton;
import rvme.gui.RenameMapDialogSingleton;
import rvme.gui.SubRegionDialogSingleton;
import rvme.gui.Workspace;
import saf.AppTemplate;
import static saf.settings.AppStartupConstants.FILE_PROTOCOL;
import static saf.settings.AppStartupConstants.PATH_IMAGES;

/**
 *
 * @author Jon Reyes
 */
public class RVMEController {
    AppTemplate app;
    PropertiesManager props;
    AudioManager anthem;
    
    public RVMEController(AppTemplate initApp){
        app = initApp;
        props = PropertiesManager.getPropertiesManager();
    }
    
    public void updateBGColor(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        
        Color bgColor = workspace.getBGCPicker().getValue();
        dataManager.setBackgroundColor(bgColor);
        workspace.updateFileControls(false,false);
    }
    
    public void updateBorderColor(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        
        Color borderColor = workspace.getBCPicker().getValue();
        dataManager.setBorderColor(borderColor);
        workspace.updateFileControls(false,false);
    }
    
    public void updateBorderThickness(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        
        double borderThickness = workspace.getBTSlider().getValue();
        dataManager.setBorderThickness(borderThickness);
        
        workspace.getBTValue().setText(String.format("%.2f", borderThickness));
        workspace.updateFileControls(false,false);
    }
    
    public void updateZoom(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        DataManager dataManager = (DataManager) app.getDataComponent();
        
        double zoom = workspace.getZoomSlider().getValue();
        dataManager.setZoom(zoom);
        
        workspace.getZoomValue().setText(String.format("%.2fx", zoom));
        
        StackPane mapStack = workspace.getMapStack();
        for(Node n: mapStack.getChildren()){
            if(n instanceof Group){
                n.setScaleX(zoom);
                n.setScaleY(zoom);
            }
        }
        
        workspace.updateFileControls(false,false);
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
        workspace.updateFileControls(false,false);
    }
    
    public void addImage(){
        try{
            FileChooser fc = new FileChooser();
            fc.setInitialDirectory(new File(PATH_IMAGES));
            fc.setTitle(props.getProperty(ADD_TITLE));
            fc.getExtensionFilters().addAll(
		new FileChooser.ExtensionFilter(props.getProperty(IMAGE_EXT_DESC), props.getProperty(PNG_EXT)));
            File selectedImage = fc.showOpenDialog(app.getGUI().getWindow());
            if (selectedImage != null) {
                add(selectedImage);
            }
        }catch (Exception e) {
                Alert alert = new Alert(AlertType.ERROR);
                alert.setHeaderText(props.getProperty(ADD_ERROR_TITLE));
                alert.setContentText(props.getProperty(ADD_ERROR_MESSAGE));
                alert.showAndWait();
        }
    }
    
    public void add(File addImage)throws IOException{
        // CREATE IMAGE VIEW
        String imagePath = FILE_PROTOCOL + addImage.getPath();
        Image image = new Image(imagePath);
        ImageView addImageView = new ImageView(image);
        addImageView.setPreserveRatio(true);
        addImageView.setFitWidth(200);
        makeSelectable(addImageView);
        makeDraggable(addImageView);
        
        // ADD IMAGE TO DATA
        DataManager dataManager = (DataManager) app.getDataComponent();
        addImageView.setId(addImage.getCanonicalPath()+"~"+dataManager.getImages().size());
        dataManager.getImages().add(addImageView);
        
        // ADD IMAGE TO WORKSPACE
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        StackPane mapStack = workspace.getMapStack();
        mapStack.getChildren().add(addImageView);
        
        // UPDATE
        workspace.updateFileControls(false,false);
    }
    
    public void removeImage(){
        // REMOVE IMAGE IN WORKSPACE
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        StackPane mapStack = workspace.getMapStack();
        mapStack.getChildren().remove(workspace.getSelection());
        
        
        // REMOVE IMAGE IN DATA
        DataManager dataManager = (DataManager) app.getDataComponent();
        dataManager.getImages().remove(workspace.getSelection());
        
        // UPDATE
        workspace.setSelection(null);
        workspace.updateFileControls(false,false);
        workspace.updateEditControls();
    }
    
    public void makeSelectable(ImageView node){
        node.addEventFilter(
            MouseEvent.ANY,
            e->{
                // disable mouse events for all children
                // e.consume();
                Workspace workspace = (Workspace) app.getWorkspaceComponent();
                workspace.setSelection(node);
                workspace.updateEditControls();
            }
        );
    }
    
    public void makeDraggable(ImageView node) {
        final DragContext dragContext = new DragContext();
        
        node.addEventFilter(
                MouseEvent.MOUSE_PRESSED,
                e->{
                    // remember initial mouse cursor coordinates
                    // and node position
                    dragContext.mouseAnchorX = e.getX();
                    dragContext.mouseAnchorY = e.getY();
                    dragContext.initialTranslateX = node.getTranslateX();
                    dragContext.initialTranslateY = node.getTranslateY();
                });

        node.addEventFilter(
                MouseEvent.MOUSE_DRAGGED,
                e->{
                            // shift node from its initial position by delta
                            // calculated from mouse cursor movement
                            node.setTranslateX(
                                    dragContext.initialTranslateX
                                        + e.getX()
                                        - dragContext.mouseAnchorX);
                            node.setTranslateY(
                                    dragContext.initialTranslateY
                                        + e.getY()
                                        - dragContext.mouseAnchorY);
                            Workspace workspace = (Workspace) app.getWorkspaceComponent();
                            workspace.updateFileControls(false, false);
                });
                
    }
    
    public void playAnthem(){
        try {
            DataManager data = (DataManager) app.getDataComponent();
            anthem = new AudioManager();
            anthem.loadAudio(data.getName(), data.getAnthem());
            anthem.play(data.getName(), true);
            
            Workspace workspace = (Workspace) app.getWorkspaceComponent();
            workspace.getPlayButton().setVisible(false);
            workspace.getPauseButton().setVisible(true);

        } catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(props.getProperty(ANTHEM_ERROR_TITLE));
            alert.setContentText(props.getProperty(ANTHEM_ERROR_MESSAGE));
            alert.showAndWait();
        }
    }
    
    public void pauseAnthem(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        workspace.getPauseButton().setVisible(false);
        workspace.getPlayButton().setVisible(true);
        
        DataManager data = (DataManager) app.getDataComponent();
        anthem.stop(data.getName());
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
        
        workspace.updateFileControls(false,false);
    }
    
    public void newMap(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        NewMapDialogSingleton newMapDialog = workspace.getNewMapDialog();
        
        newMapDialog.reset();
        newMapDialog.show();
    }
    
    public void renameMap(){
        Workspace workspace = (Workspace) app.getWorkspaceComponent();
        RenameMapDialogSingleton renameMapDialog = workspace.getRenameMapDialog();

        renameMapDialog.reset();
        renameMapDialog.show();
    }
    
    private static final class DragContext {
        public double mouseAnchorX;
        public double mouseAnchorY;
        public double initialTranslateX;
        public double initialTranslateY;
    }
}
