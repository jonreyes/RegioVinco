/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.gui;

import java.net.URL;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import static rvme.PropertyType.DDIALOG_TITLE;
import static rvme.PropertyType.HEIGHT_LABEL;
import static rvme.PropertyType.OK_LABEL;
import static rvme.PropertyType.WIDTH_LABEL;
import saf.AppTemplate;
import static saf.components.AppStyleArbiter.CLASS_BORDERED_PANE;
import static saf.components.AppStyleArbiter.CLASS_GRID_PANE;
import static saf.components.AppStyleArbiter.CLASS_PROMPT_LABEL;
import static saf.components.AppStyleArbiter.CLASS_SUBHEADING_LABEL;
import static saf.settings.AppPropertyType.APP_CSS;
import static saf.settings.AppPropertyType.APP_PATH_CSS;
import saf.ui.AppGUI;

/**
 *
 * @author xion
 */
public class DimensionsDialogSingleton extends Stage {
    static DimensionsDialogSingleton singleton = null;
    
    PropertiesManager props;
    
    AppTemplate app;
    AppGUI gui;
    
    VBox messagePane;
    Scene messageScene;
    Label messageLabel;
    
    GridPane ddGrid;
    
    Label widthLabel;
    TextField widthTextField;
    Label heightLabel;
    TextField heightTextField;
    
    Button okBtn;
    
    final double SCALEW = 0.25;
    final double SCALEH = 0.25;
    /**
     * Note that the constructor is private since it follows
     * the singleton design pattern.
     * 
     * @param primaryStage The owner of this modal dialog.
     */
    private DimensionsDialogSingleton(){}
    
    /**
     * A static accessor method for getting the singleton object.
     * 
     * @return The one singleton dialog of this object type.
     */
    public static DimensionsDialogSingleton getSingleton() {
	if (singleton == null)
	    singleton = new DimensionsDialogSingleton();
	return singleton;
    }
    
    public void init(AppTemplate initApp){
        app = initApp;
        gui = app.getGUI();
        props = PropertiesManager.getPropertiesManager();
        initModality(Modality.WINDOW_MODAL);
        initOwner(gui.getWindow());
        initGUI();
        initHandlers();
        initStyleSheet();
        initStyle();
    }
    
    private void initGUI(){
        widthLabel = new Label(props.getProperty(WIDTH_LABEL));
        widthTextField = new TextField();
        heightLabel = new Label(props.getProperty(HEIGHT_LABEL));
        heightTextField = new TextField();
        okBtn = new Button(props.getProperty(OK_LABEL));
        
        ddGrid = new GridPane();
        ddGrid.add(widthLabel, 0, 0);
        ddGrid.add(widthTextField, 1, 0);
        ddGrid.add(heightLabel, 0, 1);
        ddGrid.add(heightTextField, 1, 1);
        
        messageLabel = new Label(props.getProperty(DDIALOG_TITLE));
        messagePane = new VBox();
        messagePane.setAlignment(Pos.CENTER);
        messagePane.getChildren().add(messageLabel);
        messagePane.getChildren().add(ddGrid);
        messagePane.getChildren().add(okBtn);
        messageScene = new Scene(messagePane, SCALEW*gui.getWindow().getWidth(), SCALEH*gui.getWindow().getHeight());
        this.setScene(messageScene);
        this.setTitle(props.getProperty(DDIALOG_TITLE));
    }
   
    private void initHandlers(){
        okBtn.setOnAction(e->this.hide());
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
        ddGrid.getStyleClass().add(CLASS_GRID_PANE);
        messageLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        widthLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        heightLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
    }
    
}