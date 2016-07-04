package rvme.gui;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import properties_manager.PropertiesManager;
import static rvme.PropertyType.ADD_ICON;
import static rvme.PropertyType.ANTHEM_ICON;
import static rvme.PropertyType.ANTHEM_LABEL;
import static rvme.PropertyType.ANTHEM_TOOLTIP;
import static rvme.PropertyType.BC_LABEL;
import static rvme.PropertyType.BGC_LABEL;
import static rvme.PropertyType.BT_LABEL;
import static rvme.PropertyType.CAPITAL_COLUMN_HEADING;
import static rvme.PropertyType.DATA_LABEL;
import static rvme.PropertyType.DIMENSIONS_ICON;
import static rvme.PropertyType.DIMENSIONS_LABEL;
import static rvme.PropertyType.DIMENSIONS_TOOLTIP;
import static rvme.PropertyType.EXPORT_ICON;
import static rvme.PropertyType.FLAG_COLUMN_HEADING;
import static rvme.PropertyType.LEADER_IMAGE_COLUMN_HEADING;
import static rvme.PropertyType.LEADER_NAME_COLUMN_HEADING;
import static rvme.PropertyType.NAME_COLUMN_HEADING;
import static rvme.PropertyType.NAME_LABEL;
import static rvme.PropertyType.RAC_ICON;
import static rvme.PropertyType.RAC_LABEL;
import static rvme.PropertyType.RAC_TOOLTIP;
import static rvme.PropertyType.ZOOM_LABEL;
import rvme.controller.RVMEController;
import saf.AppTemplate;
import saf.components.AppWorkspaceComponent;
import saf.controller.AppFileController;
import static saf.settings.AppPropertyType.APP_TITLE;
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
import static rvme.PropertyType.ADD_LABEL;
import static rvme.PropertyType.ADD_TOOLTIP;
import static rvme.PropertyType.MAP_IMAGE;
import static rvme.PropertyType.RM_ICON;
import static rvme.PropertyType.RM_LABEL;
import static rvme.PropertyType.RM_TOOLTIP;

/**
 * This class serves as the workspace component for this application, providing
 * the user interface controls for editing work.
 *
 * @author Jon Reyes
 * @version 1.0
 */
public class Workspace extends AppWorkspaceComponent {
    
    AppTemplate app;
    PropertiesManager props;
    
    RVMEController rvmeController;
    
    Label title;
    AppFileController fileController;
    ToolBar fileToolBar;
    Button newBtn;
    Button loadBtn;
    Button saveBtn;
    Button exportBtn;
    Button exitBtn;
    
    FlowPane editToolBar;
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
    Label addLabel;
    Button addBtn;
    Label rmLabel;
    Button rmBtn;
    Label racLabel;
    Button racBtn;
    Label anthemLabel;
    Button anthemBtn;
    Label dimensionsLabel;
    Button dimensionsBtn;
    
    SplitPane editView;
    ScrollPane mapView;
    StackPane mapStack;
    Rectangle mapBG;
    ImageView mapDummy;
    Node selection;
    
    VBox dataView;
    Label dataLabel;
    
    TableView dataTable;
    TableColumn nameColumn;
    TableColumn capitalColumn;
    TableColumn flagColumn;
    TableColumn leaderNameColumn;
    TableColumn leaderImageColumn;
    
    NewMapDialogSingleton newMapDialog;
    SubRegionDialogSingleton subRegionDialog;
    DimensionsDialogSingleton dimensionsDialog;
    
    final double BUTTON_SIZE = 20;
    
    public Workspace(AppTemplate initApp) {
        app = initApp;
        props = PropertiesManager.getPropertiesManager();
        initGUI();
        initControls();
    }
    
    private void initGUI(){
        initFileToolbar();
        initTitle();
        initWorkspace();
    }
    
    private void initControls(){
        rvmeController =  new RVMEController(app);
        initFileControls();
        initEditControls();
        mapDummy.setOnMouseClicked(e->{
            subRegionDialog.show();
        });
    }
    
    private void initFileControls(){
        fileController = new AppFileController(app);
        newBtn.setOnAction(e -> {
            newMapDialog.show();
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
    }
    
    private void initEditControls(){
        addBtn.setOnMouseClicked(e->{
            rvmeController.addImage();
        });
        rmBtn.setOnMouseClicked(e->{
           rvmeController.removeImage();
        });
        dimensionsBtn.setOnMouseClicked(e->{
            dimensionsDialog.show();
        });    
    }
    
    private void initTitle(){
        title = new Label(props.getProperty(APP_TITLE));
        fileToolBar.getItems().add(title);
    }
    
    private void initWorkspace(){
        workspace = new VBox();
        initEditToolbar();
        initEditView();
        workspace.getChildren().add(editToolBar);
        workspace.getChildren().add(editView);
        initDialogs();
    }
    
    private void initEditView(){
        editView = new SplitPane();
        editView.setDividerPositions(0.5);
        initMapView();
        initDataView();
        editView.getItems().add(mapView);
        editView.getItems().add(dataView);
    }
    
    private void initMapView(){
        mapView = new ScrollPane();
        mapStack = new StackPane();
        mapDummy = initImageView(MAP_IMAGE.toString());
        initMapBG();
        mapStack.getChildren().add(mapDummy);
        mapView.setContent(mapStack);
    }
    
    private void initMapBG(){
        mapBG = new Rectangle();
        mapBG.widthProperty().bind(mapView.widthProperty().subtract(mapBG.strokeWidthProperty()));
        mapBG.heightProperty().bind(mapView.heightProperty().subtract(mapBG.strokeWidthProperty()));
        mapBG.fillProperty().bind(bgcPicker.valueProperty());
        mapBG.strokeProperty().bind(bcPicker.valueProperty());
        mapBG.strokeWidthProperty().bind(btSlider.valueProperty());
        mapStack.getChildren().add(mapBG);
    }
    
    private void initDataView(){
        dataView = new VBox();
        dataLabel = new Label(props.getProperty(DATA_LABEL));
        initTableView();
        dataView.getChildren().add(dataLabel);
        dataView.getChildren().add(dataTable);
    }
    
    private void initTableView(){
        dataTable = new TableView();
        
        nameColumn = new TableColumn(props.getProperty(NAME_COLUMN_HEADING));
        capitalColumn = new TableColumn(props.getProperty(CAPITAL_COLUMN_HEADING));
        flagColumn = new TableColumn(props.getProperty(FLAG_COLUMN_HEADING));
        leaderNameColumn = new TableColumn(props.getProperty(LEADER_NAME_COLUMN_HEADING));
        leaderImageColumn = new TableColumn(props.getProperty(LEADER_IMAGE_COLUMN_HEADING));
        
        // AND LINK THE COLUMNS TO THE DATA
        nameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
        capitalColumn.setCellValueFactory(new PropertyValueFactory<String, String>("capital"));
        flagColumn.setCellValueFactory(new PropertyValueFactory<ImageView, String>("flag"));
        leaderNameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("leader name"));
        leaderImageColumn.setCellValueFactory(new PropertyValueFactory<ImageView, String>("leader image"));
        
        
        // SCALE THE COLUMN SIZES
        nameColumn.prefWidthProperty().bind(dataTable.widthProperty().multiply(0.2));
        capitalColumn.prefWidthProperty().bind(dataTable.widthProperty().multiply(0.2));
        flagColumn.prefWidthProperty().bind(dataTable.widthProperty().multiply(0.2));
        leaderImageColumn.prefWidthProperty().bind(dataTable.widthProperty().multiply(0.2));
        leaderNameColumn.prefWidthProperty().bind(dataTable.widthProperty().multiply(0.2));
        
        dataTable.getColumns().add(nameColumn);
        dataTable.getColumns().add(capitalColumn);
        dataTable.getColumns().add(flagColumn);
        dataTable.getColumns().add(leaderImageColumn);
        dataTable.getColumns().add(leaderNameColumn);
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
        exportBtn = initChildButton(EXPORT_ICON.toString(), EXPORT_TOOLTIP.toString(), true);
        exitBtn = initChildButton(EXIT_ICON.toString(),	    EXIT_TOOLTIP.toString(),	false);
        
        fileToolBar.getItems().addAll(newBtn,loadBtn,saveBtn,exportBtn,exitBtn);
        
        app.getGUI().getAppPane().setTop(fileToolBar);
    }
    
    private void initEditToolbar(){
        editToolBar = new FlowPane();
        
        nameLabel = new Label(props.getProperty(NAME_LABEL));
        nameTextField = new TextField();
        
        bgcLabel = new Label(props.getProperty(BGC_LABEL));
        bgcPicker = new ColorPicker();
        
        bcLabel = new Label(props.getProperty(BC_LABEL));
        bcPicker = new ColorPicker();
        bcPicker.setValue(Color.BLACK);
        
        btLabel = new Label(props.getProperty(BT_LABEL));
        btSlider = new Slider();
        
        zoomLabel = new Label(props.getProperty(ZOOM_LABEL));
        zoomSlider = new Slider();
        
        addLabel = new Label(props.getProperty(ADD_LABEL));
        addBtn = initChildButton(ADD_ICON.toString(), ADD_TOOLTIP.toString(), false);
        
        rmLabel = new Label(props.getProperty(RM_LABEL));
        rmBtn = initChildButton(RM_ICON.toString(), RM_TOOLTIP.toString(), false);
        
        racLabel = new Label(props.getProperty(RAC_LABEL));
        racBtn = initChildButton(RAC_ICON.toString(), RAC_TOOLTIP.toString(), false);
        
        anthemLabel = new Label(props.getProperty(ANTHEM_LABEL));
        anthemBtn = initChildButton(ANTHEM_ICON.toString(), ANTHEM_TOOLTIP.toString(), false);
        
        dimensionsLabel = new Label(props.getProperty(DIMENSIONS_LABEL));
        dimensionsBtn = initChildButton(DIMENSIONS_ICON.toString(), DIMENSIONS_TOOLTIP.toString(), false);
        
        editGrid = new GridPane();
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
        editGrid.add(addLabel, 5, 0);
        editGrid.add(addBtn, 5, 1);
        editGrid.add(rmLabel, 6, 0);
        editGrid.add(rmBtn, 6, 1);
        editGrid.add(racLabel, 7, 0);
        editGrid.add(racBtn, 7, 1);
        editGrid.add(anthemLabel, 8, 0);
        editGrid.add(anthemBtn, 8, 1);
        editGrid.add(dimensionsLabel, 9, 0);
        editGrid.add(dimensionsBtn, 9, 1);
        
        for(Node node : editGrid.getChildren()){
            GridPane.setHalignment(node, HPos.CENTER);
        }
        
        editToolBar.getChildren().add(editGrid);
    }
    
    private void initDialogs(){
        newMapDialog = NewMapDialogSingleton.getSingleton();
        newMapDialog.init(app);
        subRegionDialog = SubRegionDialogSingleton.getSingleton();
        subRegionDialog.init(app);
        dimensionsDialog = DimensionsDialogSingleton.getSingleton();
        dimensionsDialog.init(app);
    }
    
    public ImageView initImageView(String img){
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(img);
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(600);
        imageView.setFitHeight(600);
        return imageView;
    }
    
    /**
     * This is a private helper method for initializing a simple button with
     * an icon and tooltip.
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
    
    public StackPane getMapStack(){
        return mapStack;
    }
    
    public Node getSelection(){
        return selection;
    }
    
    public void setSelection(ImageView imageView){
        selection = imageView;
        System.out.println(selection.toString()+"selected");
    }
    
    @Override
    public void reloadWorkspace() {
    }

    @Override
    public void initStyle() {
        title.getStyleClass().add(CLASS_HEADING_LABEL);
        fileToolBar.getStyleClass().add(CLASS_BORDERED_PANE);
        editToolBar.getStyleClass().add(CLASS_BORDERED_PANE);
        editGrid.getStyleClass().add(CLASS_GRID_PANE);
        nameLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        bgcLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        bcLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        btLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        zoomLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        addLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        rmLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        racLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        anthemLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        dimensionsLabel.getStyleClass().add(CLASS_PROMPT_LABEL);
        dataView.getStyleClass().add(CLASS_BORDERED_PANE);
        dataLabel.getStyleClass().add(CLASS_HEADING_LABEL);
    }

}
