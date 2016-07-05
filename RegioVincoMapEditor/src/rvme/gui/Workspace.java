package rvme.gui;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
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
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import properties_manager.PropertiesManager;
import static rvme.PropertyType.ADD_ICON;
import static rvme.PropertyType.ANTHEM_LABEL;
import static rvme.PropertyType.BC_LABEL;
import static rvme.PropertyType.BGC_LABEL;
import static rvme.PropertyType.BT_LABEL;
import static rvme.PropertyType.CAPITAL_COLUMN_HEADING;
import static rvme.PropertyType.DATA_LABEL;
import static rvme.PropertyType.DIMENSIONS_ICON;
import static rvme.PropertyType.DIMENSIONS_LABEL;
import static rvme.PropertyType.DIMENSIONS_TOOLTIP;
import static rvme.PropertyType.EXPORT_ICON;
import static rvme.PropertyType.LEADER_COLUMN_HEADING;
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
import static rvme.PropertyType.PAUSE_ICON;
import static rvme.PropertyType.PAUSE_TOOLTIP;
import static rvme.PropertyType.RM_ICON;
import static rvme.PropertyType.RM_LABEL;
import static rvme.PropertyType.RM_TOOLTIP;
import rvme.data.DataManager;
import rvme.data.SubRegion;
import static rvme.PropertyType.PLAY_ICON;
import static rvme.PropertyType.PLAY_TOOLTIP;

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
    HBox fileBox;
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
    Button playBtn;
    Button pauseBtn;
    Label dimensionsLabel;
    Button dimensionsBtn;
    
    SplitPane editView;
    ScrollPane mapView;
    StackPane mapStack;
    Rectangle mapBG;
    ImageView mapDummy;
    Group region;
    DoubleProperty mapWidth;
    DoubleProperty mapHeight;
    Node selection;
    
    VBox dataView;
    Label dataLabel;
    
    TableView<SubRegion> dataTable;
    TableColumn nameColumn;
    TableColumn capitalColumn;
    TableColumn leaderColumn;
    
    NewMapDialogSingleton newMapDialog;
    SubRegionDialogSingleton subRegionDialog;
    DimensionsDialogSingleton dimensionsDialog;
    
    final double BUTTON_SIZE = 20;
    
    public Workspace(AppTemplate initApp) {
        app = initApp;
        app.getGUI().getWindow().setResizable(false);
        props = PropertiesManager.getPropertiesManager();
        initGUI();
        initControls();
    }
    
    private void initGUI(){
        initWorkspace();
        initFileToolbar();
        initTitle();
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
        playBtn.setOnMouseClicked(e->{
            rvmeController.playAnthem();
        });
        pauseBtn.setOnMouseClicked(e->{
            rvmeController.pauseAnthem();
        });
        dimensionsBtn.setOnMouseClicked(e->{
            dimensionsDialog.show();
        });    
        
    }
    
    private void initTitle(){
        title = new Label(props.getProperty(APP_TITLE));
        title.setTranslateX(250);
        fileToolBar.getItems().add(title);
    }
    
    private void initWorkspace(){
        workspace = new VBox();
        initDialogs();
        initEditToolbar();
        initEditView();
        workspace.getChildren().add(editToolBar);
        workspace.getChildren().add(editView);
    }
    
    private void initEditView(){
        editView = new SplitPane();
        initMapView();
        initDataView();
        editView.getItems().add(mapView);
        editView.getItems().add(dataView);
        editView.setDividerPositions(mapWidth.get()/app.getGUI().getWindow().getWidth());
    }
    
    private void initMapView(){
        mapView = new ScrollPane();
        mapWidth = dimensionsDialog.mapWidthProperty();
        mapHeight = dimensionsDialog.mapHeightProperty();
        initMapLayers();
        mapView.setContent(mapStack);
    }
    
    private void initMapLayers(){
        mapStack = new StackPane();
        
        mapStack.setMinSize(mapWidth.get(), mapHeight.get());
        mapStack.scaleXProperty().bind(zoomSlider.valueProperty());
        mapStack.scaleYProperty().bind(zoomSlider.valueProperty());

        mapDummy = initImageView(MAP_IMAGE.toString(),mapWidth.get()/2);
        mapDummy.fitWidthProperty().bind(mapWidth.divide(2));
        mapDummy.fitHeightProperty().bind(mapWidth.divide(2));
        initMapBG();
        mapStack.getChildren().add(mapDummy);
        initMapBorder();
        initRegionView();
        
    }
    
    private void initMapBG(){
        mapBG = new Rectangle();
        mapBG.widthProperty().bind(mapWidth.subtract(mapBG.strokeWidthProperty()));
        mapBG.heightProperty().bind(mapHeight.subtract(mapBG.strokeWidthProperty()));
        mapBG.fillProperty().bind(bgcPicker.valueProperty());
        mapStack.getChildren().add(mapBG);
    }
    
    private void initMapBorder(){
        Rectangle mapBorder = new Rectangle();
        mapBorder.setFill(null);
        mapBorder.widthProperty().bind(mapWidth.subtract(mapBorder.strokeWidthProperty()));
        mapBorder.heightProperty().bind(mapHeight.subtract(mapBorder.strokeWidthProperty()));
        mapBorder.strokeProperty().bind(bcPicker.valueProperty());
        DoubleBinding strokeSize = btSlider.valueProperty().multiply(mapStack.heightProperty());
        mapBorder.strokeWidthProperty().bind(strokeSize);
        mapStack.getChildren().add(mapBorder);
    }
    
    private void initRegionView(){
        DataManager dataManager = (DataManager) app.getDataComponent();
        region = dataManager.mapTo(mapBG);
        region.scaleXProperty().bind(zoomSlider.valueProperty());
        region.scaleYProperty().bind(zoomSlider.valueProperty());
        mapStack.getChildren().add(region);
    }
    
    private void initDataView(){
        dataView = new VBox();
        dataLabel = new Label(props.getProperty(DATA_LABEL));
        initTableView();
        dataView.getChildren().add(dataLabel);
        dataView.getChildren().add(dataTable);
    }
    
    private void initTableView(){
        dataTable = new TableView<>();
        dataTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        nameColumn = new TableColumn(props.getProperty(NAME_COLUMN_HEADING));
        capitalColumn = new TableColumn(props.getProperty(CAPITAL_COLUMN_HEADING));
        leaderColumn = new TableColumn(props.getProperty(LEADER_COLUMN_HEADING));
        
        // AND LINK THE COLUMNS TO THE DATA
        nameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
        capitalColumn.setCellValueFactory(new PropertyValueFactory<String, String>("capital"));
        leaderColumn.setCellValueFactory(new PropertyValueFactory<String, String>("leader"));
        
        
        // SCALE THE COLUMN SIZES
        nameColumn.prefWidthProperty().bind(dataTable.widthProperty().multiply(0.2));
        capitalColumn.prefWidthProperty().bind(dataTable.widthProperty().multiply(0.2));
        leaderColumn.prefWidthProperty().bind(dataTable.widthProperty().multiply(0.2));
        
        dataTable.getColumns().add(nameColumn);
        dataTable.getColumns().add(capitalColumn);
        dataTable.getColumns().add(leaderColumn);

        String a = "TEST";
        String b = "DUMMY";
        String c = "DATA";
        
        ObservableList<SubRegion> data = FXCollections.observableArrayList();
        data.add(new SubRegion("North Korea","Pyongyang","Kim Jong Un"));
        data.add(new SubRegion(a,b,c));
        data.add(new SubRegion(b,c,a));
        data.add(new SubRegion(c,b,a));
        data.add(new SubRegion(a,a,a));
        data.add(new SubRegion(b,b,b));
        data.add(new SubRegion(c,c,c));
        
        dataTable.setItems(data);
        
        dataTable.setMinHeight(mapHeight.get()*0.85);
    }
    
    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initFileToolbar() {
        fileToolBar = new ToolBar();
        
        fileBox = new HBox();
        fileBox.setAlignment(Pos.CENTER);
        fileBox.setSpacing(10);
        newBtn = initChildButton(NEW_ICON.toString(),	    NEW_TOOLTIP.toString(),	false);
        loadBtn = initChildButton(LOAD_ICON.toString(),	    LOAD_TOOLTIP.toString(),	false);
        saveBtn = initChildButton(SAVE_ICON.toString(),	    SAVE_TOOLTIP.toString(),	true);
        exportBtn = initChildButton(EXPORT_ICON.toString(), EXPORT_TOOLTIP.toString(), true);
        exitBtn = initChildButton(EXIT_ICON.toString(),	    EXIT_TOOLTIP.toString(),	false);
        
        fileBox.getChildren().addAll(newBtn,loadBtn,saveBtn,exportBtn,exitBtn);
        fileToolBar.getItems().add(fileBox);
        
        app.getGUI().getAppPane().setTop(fileToolBar);
    }
    
    /**
     * This method is used to activate/deactivate file toolbar buttons when
     * they can and cannot be used so as to provide foolproof design.
     * 
     * @param saved Describes whether the loaded Page has been saved or not.
     */
    public void updateFileControls(boolean saved) {
        saveBtn.setDisable(saved);

	newBtn.setDisable(false);
        loadBtn.setDisable(false);
	exportBtn.setDisable(saved);
        exitBtn.setDisable(false);
    }
    
    public void updateEditControls(){
        if(selection == null){
            rmBtn.setDisable(true);
        }
        else{
            rmBtn.setDisable(false);
        }
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
        btSlider = new Slider(0,0.5,0);

        zoomLabel = new Label(props.getProperty(ZOOM_LABEL));
        zoomSlider = new Slider(0,16,1);
        
        addLabel = new Label(props.getProperty(ADD_LABEL));
        addBtn = initChildButton(ADD_ICON.toString(), ADD_TOOLTIP.toString(), false);
        
        rmLabel = new Label(props.getProperty(RM_LABEL));
        rmBtn = initChildButton(RM_ICON.toString(), RM_TOOLTIP.toString(), true);
        
        racLabel = new Label(props.getProperty(RAC_LABEL));
        racBtn = initChildButton(RAC_ICON.toString(), RAC_TOOLTIP.toString(), false);
        
        anthemLabel = new Label(props.getProperty(ANTHEM_LABEL));
        
        playBtn = initChildButton(PLAY_ICON.toString(), PLAY_TOOLTIP.toString(), false);
        pauseBtn = initChildButton(PAUSE_ICON.toString(), PAUSE_TOOLTIP.toString(), false);
        pauseBtn.setVisible(false);
        
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
        editGrid.add(playBtn, 8, 1);
        editGrid.add(pauseBtn, 8, 1);
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
    
    public ImageView initImageView(String img, double size){
        String imagePath = FILE_PROTOCOL + PATH_IMAGES + props.getProperty(img);
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
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
    
    public Button getPlayButton(){
        return playBtn;
    }
    
    public Button getPauseButton(){
        return pauseBtn;
    }
    
    public Node getSelection(){
        return selection;
    }
    
    public void setSelection(ImageView imageView){
        selection = imageView;
    }
    
    @Override
    public void reloadWorkspace() {
        initMapView();
        updateEditControls();
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

