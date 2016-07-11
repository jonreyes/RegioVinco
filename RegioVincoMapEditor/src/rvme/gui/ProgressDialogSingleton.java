/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rvme.gui;

import java.net.URL;
import java.util.concurrent.locks.ReentrantLock;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import static rvme.PropertyType.EXPORT_COMPLETED_MESSAGE;
import static rvme.PropertyType.EXPORT_COMPLETED_TITLE;
import static rvme.PropertyType.EXPORT_PROGRESS_TITLE;
import static rvme.PropertyType.LOAD_PROGRESS_TITLE;
import static rvme.PropertyType.SAVE_PROGRESS_TITLE;
import saf.AppTemplate;
import static saf.components.AppStyleArbiter.CLASS_BORDERED_PANE;
import static saf.components.AppStyleArbiter.CLASS_SUBHEADING_LABEL;
import static saf.settings.AppPropertyType.APP_CSS;
import static saf.settings.AppPropertyType.APP_PATH_CSS;
import static saf.settings.AppPropertyType.SAVE_COMPLETED_MESSAGE;
import static saf.settings.AppPropertyType.SAVE_COMPLETED_TITLE;
import saf.ui.AppGUI;

/**
 *
 * @author Jon Reyes
 */
public class ProgressDialogSingleton extends Stage{
    static ProgressDialogSingleton singleton = null;
    
    AppTemplate app;
    AppGUI gui;
    
    PropertiesManager props;
    
    VBox messagePane;
    Scene messageScene;
    Label messageLabel;

    ProgressBar bar;
    ProgressIndicator indicator;
    ReentrantLock progressLock;
    int type;
    
    /**
     * Note that the constructor is private since it follows
     * the singleton design pattern.
     * 
     * @param primaryStage The owner of this modal dialog.
     */
    private ProgressDialogSingleton(){}
    
    /**
     * A static accessor method for getting the singleton object.
     * 
     * @return The one singleton dialog of this object type.
     */
    public static ProgressDialogSingleton getSingleton() {
	if (singleton == null)
	    singleton = new ProgressDialogSingleton();
	return singleton;
    }
    
    public void init(AppTemplate initApp){
        app = initApp;
        gui = app.getGUI();
        props = PropertiesManager.getPropertiesManager();
        initModality(Modality.WINDOW_MODAL);
        initOwner(gui.getWindow());
        initGUI();
        initStyleSheet();
        initStyle();
    }
    
    private void initGUI(){
        progressLock = new ReentrantLock();

        bar = new ProgressBar(0);      
        indicator = new ProgressIndicator(0);
        
        messageLabel = new Label();
        messagePane = new VBox();
        messagePane.setAlignment(Pos.CENTER);
        messagePane.getChildren().add(bar);
        messagePane.getChildren().add(indicator);
        messagePane.getChildren().add(messageLabel);
        
        double w = Screen.getPrimary().getVisualBounds().getWidth();
        double h = Screen.getPrimary().getVisualBounds().getHeight();
        messageScene = new Scene(messagePane, w/8, h/8);
        this.setScene(messageScene);
    }
    
    public Task progressTask(){
        return new Task<Void>() {
            double max = 100;
            double perc;
            @Override
            protected Void call() throws Exception {
                try {
                    progressLock.lock();
                    for (int i = 0; i <= max; i++) {
                        perc = i/max;
                            
                        // THIS WILL BE DONE ASYNCHRONOUSLY VIA MULTITHREADING
                        Platform.runLater(()->{
                            bar.setProgress(perc);
                            indicator.setProgress(perc);
                            int c = (int) (perc*max) % 3;
                            String message = props.getProperty((type==0)?SAVE_PROGRESS_TITLE:(type==1)?LOAD_PROGRESS_TITLE:EXPORT_PROGRESS_TITLE)+"...";
                            if(perc*max%16==0)messageLabel.setText(message.substring(0, message.length()-c));
                        });

                        // SLEEP EACH FRAME
                        Thread.sleep(10);
                    }
                }finally {progressLock.unlock();}
                return null;
            }
        };
    }
    
    public Task completeTask(){
        return new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    progressLock.lock();
                    // THIS WILL BE DONE ASYNCHRONOUSLY VIA MULTITHREADING
                    Platform.runLater(()->{
                        Workspace workspace = (Workspace) app.getWorkspaceComponent();
                        if (type == 0){
                            // COMPLETE SAVE
                            // TELL THE USER THE FILE HAS BEEN SAVED
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText(props.getProperty(SAVE_COMPLETED_TITLE));
                            alert.setContentText(props.getProperty(SAVE_COMPLETED_MESSAGE));
                            alert.showAndWait();
                        }
                        else if(type==1){
                            // COMPLETE LOAD
                            workspace.reloadWorkspace();
                            //MAKE SURE THE WORKSPACE IS ACTIVATED
                            workspace.activateWorkspace(app.getGUI().getAppPane());
                        }
                        else if(type==2){
                            // TELL THE USER THE FILE HAS BEEN EXPORTED
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setHeaderText(props.getProperty(EXPORT_COMPLETED_TITLE));
                            alert.setContentText(props.getProperty(EXPORT_COMPLETED_MESSAGE));
                            alert.showAndWait();
                        }
                        // AND REFRESH THE GUI, WHICH WILL ENABLE AND DISABLE
                        // THE APPROPRIATE CONTROLS
                        workspace.updateFileControls(workspace.fileController.isSaved(), workspace.fileController.isExported());
                        hide();
                    });
                }finally {progressLock.unlock();}
                return null;
            }
        };
    }
    
    public void update(int type){
        this.type = type;
        this.setTitle(props.getProperty((type==0)?SAVE_PROGRESS_TITLE:(type==1)?LOAD_PROGRESS_TITLE:EXPORT_PROGRESS_TITLE));
        // THIS GETS THE THREAD ROLLING
        Thread thread = new Thread(progressTask());
        thread.start();
        thread = new Thread(completeTask());
        thread.start();
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
    }
}
