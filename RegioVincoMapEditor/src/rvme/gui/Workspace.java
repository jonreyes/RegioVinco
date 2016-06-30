package rvme.gui;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import static rvme.PropertyType.ADD_IMAGE_ICON;
import static rvme.PropertyType.ADD_IMAGE_LABEL;
import static rvme.PropertyType.ADD_IMAGE_TOOLTIP;
import static rvme.PropertyType.ANTHEM_ICON;
import static rvme.PropertyType.ANTHEM_LABEL;
import static rvme.PropertyType.ANTHEM_TOOLTIP;
import static rvme.PropertyType.BC_LABEL;
import static rvme.PropertyType.BGC_LABEL;
import static rvme.PropertyType.BT_LABEL;
import static rvme.PropertyType.EXPORT_ICON;
import static rvme.PropertyType.NAME_LABEL;
import static rvme.PropertyType.RAC_ICON;
import static rvme.PropertyType.RAC_LABEL;
import static rvme.PropertyType.RAC_TOOLTIP;
import static rvme.PropertyType.ZOOM_LABEL;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import saf.controller.AppFileController;
import static saf.settings.AppPropertyType.EXIT_ICON;
import static saf.settings.AppPropertyType.EXIT_TOOLTIP;
import static saf.settings.AppPropertyType.EXPORT_TOOLTIP;
import static saf.settings.AppPropertyType.LOAD_ICON;
import static saf.settings.AppPropertyType.LOAD_TOOLTIP;
import static saf.settings.AppPropertyType.NEW_ICON;
import static saf.settings.AppPropertyType.NEW_TOOLTIP;
import static saf.settings.AppPropertyType.SAVE_ICON;
import static saf.settings.AppPropertyType.SAVE_TOOLTIP;
import static saf.settings.AppStartupConstants.FILE_PROTOCOL;
import static saf.settings.AppStartupConstants.PATH_IMAGES;

/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 *
 * @author Jon Reyes
 * @version 1.0
 */
public class Workspace extends AppWorkspaceComponent {
    
    AppTemplate app;
    
    VBox controlBox;
    
    AppFileController fileController;
    ToolBar fileToolBar;
    Button newBtn;
    Button loadBtn;
    Button saveBtn;
    Button exportBtn;
    Button exitBtn;
    
    ToolBar editToolBar;
    GridPane editGrid;
    Label nameLabel;
    TextField nameTextField;
    Label bgcLabel;
    ColorPicker bgcPicker;
    Label bcLabel;
    ColorPicker bcPicker;
    Label btLabel;
    Slider btSlider;
    Label zoomLabel;
    Slider zoomSlider;
    Label addImageLabel;
    Button addImageBtn;
    Label racLabel;
    Button racBtn;
    Label anthemLabel;
    Button anthemBtn;
    
    final double BUTTON_SIZE = 25;
    
    public Workspace(AppTemplate initApp) {
        app = initApp;
        initGUI();
    }
    
    private void initGUI(){
        initFileToolbar();
        initWorkspace();
        /*initMapView();
        initDataView();*/
    }
    
    private void initWorkspace(){
        workspace = new VBox();
        initEditToolbar();
        workspace.getChildren().add(editToolBar);
    }
    
    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initFileToolbar() {
        fileToolBar = new ToolBar();
        
        newBtn = initChildButton(NEW_ICON.toString(),	    NEW_TOOLTIP.toString(),	false);
        loadBtn = initChildButton(LOAD_ICON.toString(),	    LOAD_TOOLTIP.toString(),	false);
        saveBtn = initChildButton(SAVE_ICON.toString(),	    SAVE_TOOLTIP.toString(),	true);
        exportBtn = initChildButton(EXPORT_ICON.toString(),     EXPORT_TOOLTIP.toString(), true);
        exitBtn = initChildButton(EXIT_ICON.toString(),	    EXIT_TOOLTIP.toString(),	false);
        
        fileToolBar.getItems().addAll(newBtn,loadBtn,saveBtn,exportBtn,exitBtn);
        
	// AND NOW SETUP THEIR EVENT HANDLERS
        fileController = new AppFileController(app);
        newBtn.setOnAction(e -> {
            fileController.handleNewRequest();
        });
        loadBtn.setOnAction(e -> {
            fileController.handleLoadRequest();
        });
        saveBtn.setOnAction(e -> {
            fileController.handleSaveRequest();
        });
        exportBtn.setOnAction(e -> {
            //fileController.handleExportRequest();
        });
        exitBtn.setOnAction(e -> {
            fileController.handleExitRequest();
        });
        
        app.getGUI().getAppPane().setTop(fileToolBar);
    }
    
    private void initEditToolbar(){
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        editToolBar = new ToolBar();
        
        nameLabel = new Label(props.getProperty(NAME_LABEL));
        nameTextField = new TextField();
        bgcLabel = new Label(props.getProperty(BGC_LABEL));
        bgcPicker = new ColorPicker();
        bcLabel = new Label(props.getProperty(BC_LABEL));
        bcPicker = new ColorPicker();
        btLabel = new Label(props.getProperty(BT_LABEL));
        btSlider = new Slider();
        zoomLabel = new Label(props.getProperty(ZOOM_LABEL));
        zoomSlider = new Slider();
        addImageLabel = new Label(props.getProperty(ADD_IMAGE_LABEL));
        addImageBtn = initChildButton(ADD_IMAGE_ICON.toString(), ADD_IMAGE_TOOLTIP.toString(), false);
        racLabel = new Label(props.getProperty(RAC_LABEL));
        racBtn = initChildButton(RAC_ICON.toString(), RAC_TOOLTIP.toString(), false);
        anthemLabel = new Label(props.getProperty(ANTHEM_LABEL));
        anthemBtn = initChildButton(ANTHEM_ICON.toString(), ANTHEM_TOOLTIP.toString(), false);
        
        editGrid = new GridPane();
        editGrid.setAlignment(Pos.CENTER);
        editGrid.setHgap(10);
        editGrid.add(nameLabel, 0, 0);
        editGrid.add(nameTextField, 0, 1);
        editGrid.add(bgcLabel, 1, 0);
        editGrid.add(bgcPicker, 1, 1);
        editGrid.add(bcLabel, 2, 0);
        editGrid.add(bcPicker, 2, 1);
        editGrid.add(btLabel, 3, 0);
        editGrid.add(btSlider, 3, 1);
        editGrid.add(zoomLabel, 4, 0);
        editGrid.add(zoomSlider, 4, 1);
        editGrid.add(addImageLabel, 5, 0);
        editGrid.add(addImageBtn, 5, 1);
        editGrid.add(racLabel, 6, 0);
        editGrid.add(racBtn, 6, 1);
        editGrid.add(anthemLabel, 7, 0);
        editGrid.add(anthemBtn, 7, 1);
        for(Node node : editGrid.getChildren()){
            GridPane.setHalignment(node, HPos.CENTER);
        }
        editToolBar.getItems().add(editGrid);
    }
    
    /**
     * This is a public helper method for initializing a simple button with
     * an icon and tooltip and placing it into a toolbar.
     * 
     * @param toolbar Toolbar pane into which to place this button.
     * 
     * @param icon Icon image file name for the button.
     * 
     * @param tooltip Tooltip to appear when the user mouses over the button.
     * 
     * @param disabled true if the button is to start off disabled, false otherwise.
     * 
     * @return A constructed, fully initialized button placed into its appropriate
     * pane container.
     */
    private Button initChildButton(String icon, String tooltip, boolean disabled) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
	
	// LOAD THE ICON FROM THE PROVIDED FILE
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(icon);
        Image buttonIcon = new Image(imagePath);
	
	// NOW MAKE THE BUTTON
        Button button = new Button();
        button.setDisable(disabled);
        ImageView iconView = new ImageView(buttonIcon);
        iconView.setFitWidth(BUTTON_SIZE);
        iconView.setFitHeight(BUTTON_SIZE);
        button.setGraphic(iconView);
        Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip));
        button.setTooltip(buttonTooltip);
	
	// AND RETURN THE COMPLETED BUTTON
        return button;
    }
    

    @Override
    public void reloadWorkspace() {
    }

    @Override
    public void initStyle() {
        fileToolBar.getStyleClass().add(CLASS_BORDERED_PANE);
        editToolBar.getStyleClass().add(CLASS_BORDERED_PANE);
        nameLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        bgcLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        bcLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        btLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        zoomLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        addImageLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        racLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        anthemLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
    }

}
