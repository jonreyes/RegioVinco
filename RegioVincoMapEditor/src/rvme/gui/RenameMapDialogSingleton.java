/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.gui;

import java.net.URL;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import static rvme.PropertyType.NAME_LABEL;
import static rvme.PropertyType.OK_LABEL;
import static rvme.PropertyType.RENAME_ERROR_MESSAGE;
import static rvme.PropertyType.RENAME_ERROR_TITLE;
import static rvme.PropertyType.RMDIALOG_TITLE;
import rvme.data.DataManager;
import saf.AppTemplate;
import static saf.components.AppStyleArbiter.CLASS_BORDERED_PANE;
import static saf.components.AppStyleArbiter.CLASS_PROMPT_LABEL;
import static saf.components.AppStyleArbiter.CLASS_SUBHEADING_LABEL;
import static saf.settings.AppPropertyType.APP_CSS;
import static saf.settings.AppPropertyType.APP_PATH_CSS;
import saf.ui.AppGUI;

/**
 *
 * @author Jon Reyes
 */
public class RenameMapDialogSingleton extends Stage{
    static RenameMapDialogSingleton singleton = null;
    
    AppTemplate app;
    AppGUI gui;
    
    DataManager data;
    PropertiesManager props;
    
    VBox messagePane;
    Scene messageScene;
    Label messageLabel;
    
    Label nameLabel;
    TextField nameTextField;
    
    Button okBtn;
    
    final double SPACE = 10;
    final double SCALEW = 0.2;
    final double SCALEH = 0.2;
    private HBox nameBox;
    
    /**
     * Note that the constructor is private since it follows
     * the singleton design pattern.
     * 
     * @param primaryStage The owner of this modal dialog.
     */
    private RenameMapDialogSingleton(){}
    
    /**
     * A static accessor method for getting the singleton object.
     * 
     * @return The one singleton dialog of this object type.
     */
    public static RenameMapDialogSingleton getSingleton() {
	if (singleton == null)
	    singleton = new RenameMapDialogSingleton();
	return singleton;
    }
    
    public void init(AppTemplate initApp){
        app = initApp;
        gui = app.getGUI();
        data = (DataManager) app.getDataComponent();
        props = PropertiesManager.getPropertiesManager();
        initModality(Modality.WINDOW_MODAL);
        initOwner(gui.getWindow());
        initGUI();
        initHandlers();
        initStyleSheet();
        initStyle();
    }
    
    private void initGUI(){
        nameBox = new HBox();
        nameBox.setAlignment(Pos.CENTER);
        nameBox.setSpacing(SPACE);
        nameLabel = new Label(props.getProperty(NAME_LABEL));
        nameTextField = new TextField();

        nameBox.getChildren().addAll(nameLabel,nameTextField);
        
        okBtn = new Button(props.getProperty(OK_LABEL));
        
        messageLabel = new Label(props.getProperty(RMDIALOG_TITLE));
        messagePane = new VBox();
        messagePane.setAlignment(Pos.CENTER);
        messagePane.getChildren().add(messageLabel);
        messagePane.getChildren().add(nameBox);
        messagePane.getChildren().add(okBtn);
        messageScene = new Scene(messagePane, SCALEW*gui.getWindow().getWidth(), SCALEH*gui.getWindow().getHeight());
        this.setScene(messageScene);
        this.setTitle(props.getProperty(RMDIALOG_TITLE));
    }
    
    private void initHandlers(){
        okBtn.setOnAction(e->{
            submitRenameMap();
        });
    }
    
    private void submitRenameMap(){
        String name = nameTextField.getText();
        try{
            if(name.isEmpty()){throw new Exception();}
            data.setName(name);
            Workspace workspace = (Workspace) app.getWorkspaceComponent();
            workspace.fileController.renameMap();
            this.hide();
        }
        catch(Exception e){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setHeaderText(props.getProperty(RENAME_ERROR_TITLE));
            alert.setContentText(props.getProperty(RENAME_ERROR_MESSAGE));
            alert.showAndWait();
        }
    }
    
    public void reset(){
        nameTextField.setText(data.getName());
    }
    
    private void initStyleSheet(){
         // LOADING CSS
	String stylesheet = props.getProperty(APP_PATH_CSS);
	stylesheet += props.getProperty(APP_CSS);
	URL stylesheetURL = app.getClass().getResource(stylesheet);
	String stylesheetPath = stylesheetURL.toExternalForm();
        messageScene.getStylesheets().add(stylesheetPath);
    }
    
    private void initStyle(){
        messagePane.getStyleClass().add(CLASS_BORDERED_PANE);
        messageLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        nameLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
    }
}
