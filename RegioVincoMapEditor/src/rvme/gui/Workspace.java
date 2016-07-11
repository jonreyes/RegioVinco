package rvme.gui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
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
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
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
import javafx.scene.shape.Polygon;
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
import static rvme.PropertyType.PAUSE_ICON;
import static rvme.PropertyType.PAUSE_TOOLTIP;
import static rvme.PropertyType.RM_ICON;
import static rvme.PropertyType.RM_LABEL;
import static rvme.PropertyType.RM_TOOLTIP;
import rvme.data.DataManager;
import rvme.data.SubRegion;
import static rvme.PropertyType.PLAY_ICON;
import static rvme.PropertyType.PLAY_TOOLTIP;
import rvme.controller.FileController;

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
    DataManager data;
    
    RVMEController rvmeController;
    
    StackPane titleBar;
    Label title;
    FileController fileController;
    AppFileController afc;
    ToolBar fileToolBar;
    HBox fileBox;
    Button newBtn;
    Button loadBtn;
    Button saveBtn;
    Button exportBtn;
    Button exitBtn;
    
    FlowPane editToolBar;
    GridPane editGrid;
    Label bgcLabel;
    ColorPicker bgcPicker;
    Label bcLabel;
    ColorPicker bcPicker;
    VBox btBox;
    Label btLabel;
    Slider btSlider;
    Label btValue;
    Label zoomLabel;
    VBox zoomBox;
    Slider zoomSlider;
    Label zoomValue;
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
    Group region;
    DoubleProperty mapWidth;
    DoubleProperty mapHeight;
    Node selection;
    
    VBox dataView;
    Label dataLabel;
    TableView<SubRegion> mapTable;
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
        data = (DataManager) app.getDataComponent();
        props = PropertiesManager.getPropertiesManager();
        initGUI();
        reloadWorkspace();
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
    }
    
    private void initFileControls(){
        fileController = new FileController(app);
        afc = new AppFileController(app);
        newBtn.setOnAction(e -> {
            fileController.newMap();
        });
        loadBtn.setOnAction(e -> {
            fileController.loadMap();
        });
        saveBtn.setOnAction(e -> {
            fileController.saveMap();
        });
        exportBtn.setOnAction(e -> {
            fileController.exportMap();
        });
        exitBtn.setOnAction(e -> {
            fileController.exitApp();
        });
    }
    
    private void initEditControls(){
        bgcPicker.valueProperty().addListener(e->{
            rvmeController.updateBGColor();
        });
        bcPicker.valueProperty().addListener(e->{
            rvmeController.updateBorderColor();
        });
        btSlider.valueProperty().addListener(e->{
            rvmeController.updateBorderThickness();
        });
        zoomSlider.valueProperty().addListener(e->{
            rvmeController.updateZoom();
        });
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
        racBtn.setOnMouseClicked(e->{
            rvmeController.reassignColors();
        });
        dimensionsBtn.setOnMouseClicked(e->{
            dimensionsDialog.show();
        });    
    }
    
    private void initMapControls(){
        for(Node node: region.getChildren()){
            if (node instanceof Polygon){
                node.setOnMouseClicked(e->{
                    if(e.getClickCount()==2){
                        rvmeController.editSubRegion(e);
                    }
                });
            }
        }
    }
    
    private void initTitle(){
        titleBar = new StackPane();
        titleBar.setMinWidth(app.getGUI().getWindow().getWidth());
        titleBar.setMinHeight(app.getGUI().getWindow().getHeight());
        
        title = new Label(props.getProperty(APP_TITLE));
        titleBar.getChildren().add(fileBox);
        titleBar.getChildren().add(title);
        
        fileToolBar.getItems().add(titleBar);
        app.getGUI().getAppPane().setTop(fileToolBar);
    }
    
    private void initWorkspace(){
        workspace = new VBox();
        initDialogs();
        initEditToolbar();
        initEditView();
    }
    
    private void initEditView(){
        editView = new SplitPane();
        initMapView();
        initDataView();
        editView.setDividerPositions(mapWidth.get()/app.getGUI().getWindow().getWidth()+0.005);
        workspace.getChildren().add(editView);
    }
    
    private void initMapView(){
        mapView = new ScrollPane();
        mapWidth = data.mapWidthProperty();
        mapHeight = data.mapHeightProperty();
        initMapLayers();
        editView.getItems().add(mapView);
    }
    
    private void initMapLayers(){
        mapStack = new StackPane();
        mapStack.setMinSize(mapWidth.get(), mapHeight.get());

        initMapBG();
        initRegionView();
        mapView.setContent(mapStack);
    }
    
    private void initMapBG(){
        mapBG = new Rectangle();
        mapBG.widthProperty().bind(mapWidth.subtract(mapBG.strokeWidthProperty()));
        mapBG.heightProperty().bind(mapHeight.subtract(mapBG.strokeWidthProperty()));
        mapBG.fillProperty().bind(bgcPicker.valueProperty());
        mapStack.getChildren().add(mapBG);
    }
    
    private void initRegionView(){
        region = data.mapTo(mapBG);
        if (region.getChildren().size()>0){
            btSlider.setMax(region.getLayoutBounds().getWidth()/10);
            zoomSlider.setMax(mapWidth.divide(region.getLayoutBounds().getWidth()/32).get());
        }
        initMapControls();
        mapStack.getChildren().add(region);
        
    }
    
    private void initDataView(){
        dataView = new VBox();
        dataLabel = new Label(props.getProperty(DATA_LABEL));
        dataView.getChildren().add(dataLabel);
        initTableView();
        editView.getItems().add(dataView);
    }
    
    private void initTableView(){
        mapTable = new TableView<>();
        mapTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        nameColumn = new TableColumn(props.getProperty(NAME_COLUMN_HEADING));
        capitalColumn = new TableColumn(props.getProperty(CAPITAL_COLUMN_HEADING));
        leaderColumn = new TableColumn(props.getProperty(LEADER_COLUMN_HEADING));
        
        // AND LINK THE COLUMNS TO THE DATA
        nameColumn.setCellValueFactory(new PropertyValueFactory<String, String>("name"));
        capitalColumn.setCellValueFactory(new PropertyValueFactory<String, String>("capital"));
        leaderColumn.setCellValueFactory(new PropertyValueFactory<String, String>("leader"));
        
        mapTable.getColumns().add(nameColumn);
        mapTable.getColumns().add(capitalColumn);
        mapTable.getColumns().add(leaderColumn);
        mapTable.setMinHeight(mapHeight.get()*0.85);
        dataView.getChildren().add(mapTable);
    }
    
    private void updateMapTable(){
        mapTable.setItems(data.getTableItems());
        initTableControls();
    }
    
    private void initTableControls(){
        mapTable.setRowFactory(e->{
            TableRow<SubRegion> itemRow = new TableRow<>();
            itemRow.setOnMouseClicked(r->{
                if (r.getClickCount() == 2 && (! itemRow.isEmpty())){
                    rvmeController.editSubRegion(r);
                }
            });
            return itemRow;
        });
    }
    
    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initFileToolbar() {
        fileToolBar = new ToolBar();

        fileBox = new HBox();
        fileBox.setAlignment(Pos.CENTER_LEFT);
        fileBox.setSpacing(5);
        
        newBtn = initChildButton(NEW_ICON.toString(),	    NEW_TOOLTIP.toString(),	false);
        loadBtn = initChildButton(LOAD_ICON.toString(),	    LOAD_TOOLTIP.toString(),	false);
        saveBtn = initChildButton(SAVE_ICON.toString(),	    SAVE_TOOLTIP.toString(),	true);
        exportBtn = initChildButton(EXPORT_ICON.toString(), EXPORT_TOOLTIP.toString(), true);
        exitBtn = initChildButton(EXIT_ICON.toString(),	    EXIT_TOOLTIP.toString(),	false);
        
        fileBox.getChildren().addAll(newBtn,loadBtn,saveBtn,exportBtn,exitBtn);
    }
    
    /**
     * This method is used to activate/deactivate file toolbar buttons when
     * they can and cannot be used so as to provide foolproof design.
     * 
     * @param saved Describes whether the loaded Page has been saved or not.
     */
    public void updateFileControls(boolean saved, boolean exported) {
        saveBtn.setDisable(saved);
	newBtn.setDisable(false);
        loadBtn.setDisable(false);
	exportBtn.setDisable(exported);
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
        
        bgcLabel = new Label(props.getProperty(BGC_LABEL));
        bgcPicker = new ColorPicker();
        
        bcLabel = new Label(props.getProperty(BC_LABEL));
        bcPicker = new ColorPicker();
        
        btLabel = new Label(props.getProperty(BT_LABEL));
        btBox = new VBox();
        btBox.setAlignment(Pos.CENTER);
        btSlider = new Slider(0,1,0);
        btValue = new Label(String.format("%.2f",btSlider.getValue()));
        btBox.getChildren().add(btSlider);
        btBox.getChildren().add(btValue);
        
        zoomLabel = new Label(props.getProperty(ZOOM_LABEL));
        zoomBox = new VBox();
        zoomBox.setAlignment(Pos.CENTER);
        zoomSlider = new Slider(0,2,1);
        zoomValue = new Label(String.format("%.2fx",zoomSlider.getValue()));
        zoomBox.getChildren().add(zoomSlider);
        zoomBox.getChildren().add(zoomValue);
        
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
        editGrid.add(bgcLabel, 0, 0);
        editGrid.add(bgcPicker, 0, 1);
        editGrid.add(bcLabel, 1, 0);
        editGrid.add(bcPicker, 1, 1);
        editGrid.add(btLabel, 2, 0);
        editGrid.add(btBox, 2, 1);
        editGrid.add(zoomLabel, 3, 0);
        editGrid.add(zoomBox, 3, 1);
        editGrid.add(addLabel, 4, 0);
        editGrid.add(addBtn, 4, 1);
        editGrid.add(rmLabel, 5, 0);
        editGrid.add(rmBtn, 5, 1);
        editGrid.add(racLabel, 6, 0);
        editGrid.add(racBtn, 6, 1);
        editGrid.add(anthemLabel, 7, 0);
        editGrid.add(playBtn, 7, 1);
        editGrid.add(pauseBtn, 7, 1);
        editGrid.add(dimensionsLabel, 8, 0);
        editGrid.add(dimensionsBtn, 8, 1);
        
        for(Node node : editGrid.getChildren()){
            GridPane.setHalignment(node, HPos.CENTER);
        }
        
        editToolBar.getChildren().add(editGrid);
        workspace.getChildren().add(editToolBar);
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
    
    public Rectangle getMapBG(){
        return mapBG;
    }
    
    public ColorPicker getBGCPicker(){
        return bgcPicker;
    }
    
    public ColorPicker getBCPicker(){
        return bcPicker;
    }
    
    public Slider getBTSlider(){
        return btSlider;
    }
    
    public Label getBTValue(){
        return btValue;
    }
    
    public Slider getZoomSlider(){
        return zoomSlider;
    }
    
    public Label getZoomValue(){
        return zoomValue;
    }
    
    public StackPane getMapStack(){
        return mapStack;
    }
    
    public Group getRegion(){
        return region;
    }
    
    public ObservableList<SubRegion> getMapData(){
        return mapTable.getItems();
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
    
    public TableView<SubRegion> getMapTable(){
        return mapTable;
    }
    
    public NewMapDialogSingleton getNewMapDialog(){
        return newMapDialog;
    }
    
    public SubRegionDialogSingleton getSubRegionDialog(){
        return subRegionDialog;
    }
    
    @Override
    public void reloadWorkspace() {
        bgcPicker.setValue(data.getBackgroundColor());
        bcPicker.setValue(data.getBorderColor());
        mapWidth.set(data.getMapWidth());
        mapHeight.set(data.getMapHeight());
        mapStack.getChildren().remove(region);
        initRegionView();
        btSlider.setValue(data.getBorderThickness());
        zoomSlider.setValue(data.getZoom());
        updateMapTable();
        dimensionsDialog.reset();
    }

    @Override
    public void initStyle() {
        title.getStyleClass().add(CLASS_HEADING_LABEL);
        fileToolBar.getStyleClass().add(CLASS_BORDERED_PANE);
        editToolBar.getStyleClass().add(CLASS_BORDERED_PANE);
        editGrid.getStyleClass().add(CLASS_GRID_PANE);
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

