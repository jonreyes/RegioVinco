package rvme.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import saf.components.AppDataComponent;
import saf.AppTemplate;

/**
 * This class serves as the data management component for this application.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class DataManager implements AppDataComponent {

    // THIS IS A SHARED REFERENCE TO THE APPLICATION
    AppTemplate app;
    
    String name;
    File parent;
    
    boolean hasCapitals;
    boolean hasFlags;
    boolean hasLeaders;
    
    ObjectProperty<Color> backgroundColor;
    ObjectProperty<Color> borderColor;
    
    DoubleProperty borderThickness;
    DoubleProperty zoom;
    
    String anthem;
    
    DoubleProperty mapWidth;
    DoubleProperty mapHeight;
    
    ArrayList<ImageView> images;
    ArrayList<Color> mapColors;
    ObjectProperty<ObservableList<SubRegion>> tableItems;
    ArrayList<Polygon> geometry;
    
    /**
     * This constructor creates the data manager and sets up the
     *
     * @param initApp The application within which this data manager is serving.
     */
    public DataManager(AppTemplate initApp) {
        app = initApp;
        this.reset();
    }

    public DataManager() {
        this.reset();
    }

    public Group mapTo(Rectangle bounds){
        Group map = new Group();  
        if (!geometry.isEmpty()){
            if(mapColors.isEmpty()) mapColors = randomColors();
            int i = 0;
            for(Polygon polygon : geometry){
                Polygon mapPolygon = new Polygon();
                mapPolygon.strokeProperty().bind(borderColor);
                mapPolygon.strokeWidthProperty().bind(borderThickness);
                mapPolygon.setFill(mapColors.get(i));
                mapPolygon.setId(String.valueOf(i));
                i++;
                int j = 0;
                for(double p: polygon.getPoints()){
                    if(j%2==0) p = mapXto(p, bounds.getWidth());
                    else p = mapYto(p, bounds.getHeight());
                    mapPolygon.getPoints().add(p);
                    j++;
                }
                map.getChildren().add(mapPolygon);
            }
        }
        return map;
    }
   
    public ArrayList<Color> randomColors(){
        ArrayList<Color> randomColors = new ArrayList<>();
        Random rand = new Random();
        for (int i=0; i<geometry.size(); i++){
            int c = rand.nextInt(255);
            randomColors.add(Color.rgb(c, c, c));
        }
        return randomColors;
    }
    
    public double mapXto(double x, double w){
        return (0.5+x/360) * w;
    }
    
    public double mapYto(double y, double h){
        return (0.5-y/180) * h;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getParent() {
        return parent;
    }

    public void setParent(File parent) {
        this.parent = parent;
    }
    
    public boolean hasCapitals(){
        return hasCapitals;
    }
    
    public boolean hasFlags(){
        return hasFlags;
    }
    
    public boolean hasLeaders(){
        return hasLeaders;
    }
    
    public void hasCapitals(boolean value){
        hasCapitals = value;
    }
    
    public void hasFlags(boolean value){
        hasFlags = value;
    }
    
    public void hasLeaders(boolean value){
        hasLeaders = value;
    }

    public Color getBackgroundColor() {
        return backgroundColor.get();
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor.set(backgroundColor);
    }

    public Color getBorderColor() {
        return borderColor.get();
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor.set(borderColor);
    }

    public double getBorderThickness() {
        return borderThickness.get();
    }

    public void setBorderThickness(double borderThickness) {
        this.borderThickness.set(borderThickness);
    }

    public double getZoom() {
        return zoom.get();
    }

    public void setZoom(double zoom) {
        this.zoom.set(zoom);
    }
    
    public String getAnthem(){
        return anthem;
    }
    
    public void setAnthem(String anthem){
        this.anthem = anthem;
    }
    
    public DoubleProperty mapWidthProperty(){
        return mapWidth;
    }
    
    public double getMapWidth() {
        return mapWidth.get();
    }

    public void setMapWidth(double mapWidth) {
        this.mapWidth.set(mapWidth);
    }

    public DoubleProperty mapHeightProperty(){
        return mapHeight;
    }
    
    public double getMapHeight() {
        return mapHeight.get();
    }

    public void setMapHeight(double mapHeight) {
        this.mapHeight.set(mapHeight);
    }
    
    public ArrayList<Double[]> getPoints(){
        ArrayList<Double[]> points = new ArrayList<>();
        for(ImageView view: images){
            Double[] newPoint = new Double[]{view.getTranslateX(),view.getTranslateY()};
            points.add(newPoint);
        }
        return points;
    }
    
    public ArrayList<ImageView> getImages(){
        return images;
    }
    
    public ObjectProperty<ObservableList<SubRegion>> tableItemsProperty(){
        return tableItems;
    }
    
    public ObservableList<SubRegion> getTableItems() {
        return tableItems.get();
    }

    public void setTableItems(ObservableList<SubRegion> tableItems) {
        this.tableItems.set(tableItems);
    }
    
    public ArrayList<Color> getMapColors(){
        return mapColors;
    }
    
    public void setMapColors(ArrayList<Color> colors){
        mapColors = colors;
    }
    
    public ArrayList<Polygon> getGeometry() {
        return geometry;
    }

    public void setGeometry(ArrayList<Polygon> geometry) {
        this.geometry = geometry;
    }
    
    @Override
    public void reset() {
        hasCapitals = false;
        hasFlags = false;
        hasLeaders = false;
        anthem = "";
        backgroundColor = new SimpleObjectProperty(Color.WHITE);
        borderColor = new SimpleObjectProperty(Color.BLACK);
        borderThickness = new SimpleDoubleProperty(0);
        zoom = new SimpleDoubleProperty(1);
        mapWidth = new SimpleDoubleProperty(802);
        mapHeight = new SimpleDoubleProperty(536);
        images = new ArrayList<>();
        tableItems = new SimpleObjectProperty(FXCollections.observableArrayList());
        geometry = new ArrayList<>(); 
        mapColors = new ArrayList<>();
    }

    @Override
    public String toString() {
        
        return "DataManager{" + 
                "\n fileName=" + name + 
                "\n parent=" + parent + 
                "\n hasCapitals=" + hasCapitals + 
                "\n hasFlags=" + hasFlags + 
                "\n hasLeaders=" + hasLeaders + 
                "\n backgroundColor=" + backgroundColor + 
                "\n borderColor=" + borderColor + 
                "\n borderThickness=" + borderThickness + 
                "\n zoom=" + zoom + 
                "\n mapWidth=" + mapWidth + 
                "\n mapHeight=" + mapHeight + 
                "\n mapColors=" + mapColors + 
                "\n tableItems=" + tableItems + 
                "\n tableSize=" + tableItems.get().size()+
                "\n geometry=" + geometry + 
                "\n}";
    }
}
