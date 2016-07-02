/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.gui;

import java.net.URL;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import static rvme.PropertyType.CAPITAL_LABEL;
import static rvme.PropertyType.DETAILS_HEADING_LABEL;
import static rvme.PropertyType.FLAG_LABEL;
import static rvme.PropertyType.LEADER_IMAGE_LABEL;
import static rvme.PropertyType.LEADER_NAME_LABEL;
import static rvme.PropertyType.NAME_LABEL;
import static rvme.PropertyType.SRDIALOG_TITLE;
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
 * @author Jon Reyes
 */
public class SubRegionDialogSingleton extends Stage{
    static SubRegionDialogSingleton singleton = null;
    
    AppTemplate app;
    AppGUI gui;
    
    VBox messagePane;
    Scene messageScene;
    Label messageLabel;
    
    GridPane srGrid;
    
    Label nameLabel;
    TextField nameTextField;
    Label capitalLabel;
    TextField capitalTextField;
    Label flagLabel;
    ImageView flagImageView;
    Label leaderNameLabel;
    TextField leaderTextField;
    Label leaderImageLabel;
    ImageView leaderImageView;
    
    final double SCALEW = 0.31;
    final double SCALEH = 0.31;
    /**
     * Note that the constructor is private since it follows
     * the singleton design pattern.
     * 
     * @param primaryStage The owner of this modal dialog.
     */
    private SubRegionDialogSingleton(){}
    
    /**
     * A static accessor method for getting the singleton object.
     * 
     * @return The one singleton dialog of this object type.
     */
    public static SubRegionDialogSingleton getSingleton() {
	if (singleton == null)
	    singleton = new SubRegionDialogSingleton();
	return singleton;
    }
    
    public void init(AppTemplate initApp){
        app = initApp;
        gui = app.getGUI();
        initModality(Modality.WINDOW_MODAL);
        initOwner(gui.getWindow());
        initGUI();
        initHandlers();
        initStyleSheet();
        initStyle();
    }
    
    private void initGUI(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        nameLabel = new Label(props.getProperty(NAME_LABEL));
        nameTextField = new TextField();
        capitalLabel = new Label(props.getProperty(CAPITAL_LABEL));
        capitalTextField = new TextField();
        flagLabel = new Label(props.getProperty(FLAG_LABEL));
        flagImageView = new ImageView();
        leaderNameLabel = new Label(props.getProperty(LEADER_NAME_LABEL));
        leaderTextField = new TextField();
        leaderImageLabel = new Label(props.getProperty(LEADER_IMAGE_LABEL));
        leaderImageView = new ImageView();
        
        srGrid = new GridPane();
        srGrid.add(nameLabel, 0, 0);
        srGrid.add(nameTextField, 1, 0);
        srGrid.add(capitalLabel, 0, 1);
        srGrid.add(capitalTextField, 1, 1);
        srGrid.add(flagLabel, 0, 2);
        srGrid.add(flagImageView, 1, 2);
        srGrid.add(leaderNameLabel, 0, 3);
        srGrid.add(leaderTextField, 1, 3);
        srGrid.add(leaderImageLabel, 0, 4);
        srGrid.add(leaderImageView, 1, 4);
        
        messageLabel = new Label(props.getProperty(SRDIALOG_TITLE));
        messagePane = new VBox();
        messagePane.getChildren().add(messageLabel);
        messagePane.getChildren().add(srGrid);
        messageScene = new Scene(messagePane, SCALEW*gui.getWindow().getWidth(), SCALEH*gui.getWindow().getHeight());
        this.setScene(messageScene);
        this.setTitle(props.getProperty(SRDIALOG_TITLE));
    }
    
    private void initHandlers(){
        
    }
    
    private void initStyleSheet(){
         // LOADING CSS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
	String stylesheet = props.getProperty(APP_PATH_CSS);
	stylesheet += props.getProperty(APP_CSS);
	URL stylesheetURL = app.getClass().getResource(stylesheet);
	String stylesheetPath = stylesheetURL.toExternalForm();
        messageScene.getStylesheets().add(stylesheetPath);
    }
    
    private void initStyle(){
        messagePane.getStyleClass().add(CLASS_BORDERED_PANE);
        srGrid.getStyleClass().add(CLASS_GRID_PANE);
        messageLabel.getStyleClass().add(CLASS_SUBHEADING_LABEL);
        nameLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        capitalLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        flagLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        leaderNameLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        leaderImageLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
    }
    
}