package rvme.data;

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
    
    Color backgroundColor;
    Color borderColor;
    
    ObservableList<Polygon> geometry;
    
    /**
     * This constructor creates the data manager and sets up the
     *
     * @param initApp The application within which this data manager is serving.
     */
    // SUBREGIONS
    
    public DataManager(AppTemplate initApp) {
        app = initApp;
        this.reset();
    }
    
    public Group mapTo(Rectangle bounds){
        Group map = new Group();
        for(Polygon polygon : geometry){
            int i = 0;
            Polygon mapPolygon = new Polygon();
            for(double p : polygon.getPoints()){
                double value = 0;
                if(i%2==0) value = mapXto(p, bounds.getWidth());
                else value = mapYto(p, bounds.getHeight());
                mapPolygon.getPoints().add(value);
                i++;
            }
            mapPolygon.setStroke(Color.BLACK);
            mapPolygon.setFill(Color.GREENYELLOW);               
            map.getChildren().add(mapPolygon);
        }
        return map;
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
    
    public void setBGColor(Color bgColor){
        this.backgroundColor = bgColor;
    }
    
    public void setBorderColor(Color bColor){
        this.borderColor = bColor;
    }
    
    @Override
    public void reset() {
        geometry = FXCollections.observableArrayList();
    }
}
