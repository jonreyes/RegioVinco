package rvme.data;

import java.util.ArrayList;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;
import rvme.gui.Workspace;
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
    
    /**
     * This constructor creates the data manager and sets up the
     *
     * @param initApp The application within which this data manager is serving.
     */
    public DataManager(AppTemplate initApp){
	// KEEP THE APP FOR LATER
	app = initApp;
    }

    /**
     * 
     */
    @Override
    public void reset() {

    }
}
