package rvme.data;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
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
    
    String fileName;
    File parent;
    
    Color backgroundColor;
    Color borderColor;
    
    double borderThickness;
    double zoom;
    
    DoubleProperty mapWidth;
    DoubleProperty mapHeight;
    
    ArrayList<Color> mapColors;
    ObservableList<SubRegion> mapData;
    ObservableList<Polygon> geometry;
    
    /**
     * This constructor creates the data manager and sets up the
     *
     * @param initApp The application within which this data manager is serving.
     */
    public DataManager(AppTemplate initApp) {
        app = initApp;
        this.reset();
    }
    
    public Group mapTo(Rectangle bounds){
        if (mapColors.isEmpty()) mapColors = randomColors();
        Group map = new Group();
        int i = 0;
        for(Polygon polygon : geometry){
            Polygon mapPolygon = new Polygon();
            mapPolygon.setStroke(Color.BLACK);
            mapPolygon.setStrokeWidth(0.01);
            mapPolygon.setFill(mapColors.get(i));
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
        return map;
    }
    
    public ArrayList<Color> randomColors(){
        ArrayList<Color> randomColors = new ArrayList<>();
        int size = geometry.size();
        if(size>0){
            double value = 255;
            for (int i = 0; i < value; i+= value/size){
                Color random = Color.rgb(i, i, i);
                randomColors.add(random);
            }
        }
        Collections.shuffle(randomColors);
        return randomColors;
    }
    
    public double mapXto(double x, double w){
        return (0.5+x/360) * w;
    }
    
    public double mapYto(double y, double h){
        return (0.5-y/180) * h;
    }
    
    public ObservableList<Polygon> getGeometry(){
        return geometry;
    }
    
    public Color getBGColor(){
        return backgroundColor;
    }
    
    public Color getBorderColor(){
        return borderColor;
    }
    
    public double getBorderThickness(){
        return borderThickness;
    }
    
    public double getZoom(){
        return zoom;
    }
    
    public void setZoom(double z){
        zoom = z;
    }
    
    public void setBGColor(Color bgColor){
        backgroundColor = bgColor;
    }
    
    public void setBorderColor(Color bColor){
        borderColor = bColor;
    }
    
    public void setBorderThickness(double bt){
        borderThickness = bt;
    }
    
    public DoubleProperty mapWidthProperty(){
        return mapWidth;
    }
    
    public DoubleProperty mapHeightProperty(){
        return mapHeight;
    }
    
    public ArrayList<Color> getMapColors(){
        return mapColors;
    }
    
    public void setMapColors(ArrayList<Color> colors){
        mapColors = colors;
    }
    
    public void setMapData(ObservableList<SubRegion> mapData){
        this.mapData = mapData;
    }
    
    public String getFileName(){
        return fileName;
    }
    
    public void setFileName(String name){
        fileName = name;
    }
    
    public File getParent(){
        return parent;
    }
    
    public void setParent(File parent) {
        this.parent = parent;
    }
    
    @Override
    public void reset() {
        backgroundColor = Color.WHITE;
        borderColor = Color.BLACK;
        borderThickness = 0;
        zoom = 1;
        mapWidth = new SimpleDoubleProperty(802);
        mapHeight = new SimpleDoubleProperty(536);
        geometry = FXCollections.observableArrayList();
        mapColors = randomColors();
    }
}
