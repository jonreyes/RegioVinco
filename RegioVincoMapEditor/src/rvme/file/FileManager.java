package rvme.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.JsonNumber;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.json.JsonWriter;
import javax.json.JsonWriterFactory;
import javax.json.stream.JsonGenerator;
import saf.components.AppDataComponent;
import saf.components.AppFileComponent;
import rvme.data.DataManager;
import rvme.data.SubRegion;

/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class FileManager implements AppFileComponent {
    // FOR JSON SAVING AND LOADING
    static final String JSON_BACKGROUND = "background";
    static final String JSON_BORDER_COLOR = "border_color";
    static final String JSON_BORDER_THICKNESS = "border_thickness";
    static final String JSON_ZOOM = "zoom";
    
    static final String JSON_WIDTH = "width";
    static final String JSON_HEIGHT = "height";
    
    static final String JSON_X = "X";
    static final String JSON_Y = "Y";
    
    static final String JSON_SUBREGIONS = "SUBREGIONS";
    static final String JSON_SUBREGION_COLOR = "SUBREGION_COLOR";
    static final String JSON_SUBREGION_POLYGONS = "SUBREGION_POLYGONS";
    
    static final String JSON_NAME = "name";
    
    static final String JSON_HAS_CAPITALS = "subregions_have_capitals";
    static final String JSON_HAS_FLAGS = "subregions_have_flags";
    static final String JSON_HAS_LEADERS = "subregions_have_leaders";
    
    static final String JSON_ANTHEM = "anthem";

    static final String JSON_CAPITAL = "capital";
    static final String JSON_LEADER = "leader";
    static final String JSON_LEADER_IMAGE = "leader_image";
    static final String JSON_FLAG = "flag";
    
    static final String JSON_RED = "red";
    static final String JSON_GREEN = "green";
    static final String JSON_BLUE = "blue";
    
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        // CLEAR THE OLD DATA OUT
	DataManager dataManager = (DataManager)data;
	dataManager.reset();
	
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        
        Color backgroundColor = Color.valueOf(json.getString(JSON_BACKGROUND));
        dataManager.setBackgroundColor(backgroundColor);
        
        Color borderColor = Color.valueOf(json.getString(JSON_BORDER_COLOR));
        dataManager.setBorderColor(borderColor);
        
        double borderThickness = getDataAsDouble(json,JSON_BORDER_THICKNESS);
        dataManager.setBorderThickness(borderThickness);
        
        double zoom = getDataAsDouble(json,JSON_ZOOM);
        dataManager.setZoom(zoom);
        
        double width = getDataAsDouble(json, JSON_WIDTH);
        dataManager.setMapWidth(width);
        
        double height = getDataAsDouble(json, JSON_HEIGHT);
        dataManager.setMapHeight(height);
        
        String fileName = json.getString(JSON_NAME);
        dataManager.setName(fileName);
        
        boolean hasCapitals = json.getBoolean(JSON_HAS_CAPITALS);
        dataManager.hasCapitals(hasCapitals);
        
        boolean hasFlags = json.getBoolean(JSON_HAS_FLAGS);
        dataManager.hasFlags(hasFlags);
        
        boolean hasLeaders = json.getBoolean(JSON_HAS_FLAGS);
        dataManager.hasLeaders(hasLeaders);
        
        String anthem = json.getString(JSON_ANTHEM);
        dataManager.setAnthem(anthem);
        
        // AND NOW LOAD ALL THE SUBREGIONS
        boolean tableEmpty = dataManager.getTableItems().isEmpty();
	JsonArray jsonSubRegionArray = json.getJsonArray(JSON_SUBREGIONS);
	for (int i = 0; i < jsonSubRegionArray.size(); i++) {
            // LOAD SUBREGION
	    JsonObject jsonSubRegion = jsonSubRegionArray.getJsonObject(i);
            
            // LOAD SUBREGION TABLE DATA
            SubRegion subregion = new SubRegion();
            String name = jsonSubRegion.getString(JSON_NAME);
            subregion.setName(name);
            String capital = jsonSubRegion.getString(JSON_CAPITAL);
            subregion.setCapital(capital);
            String leader = jsonSubRegion.getString(JSON_LEADER);
            subregion.setLeader(leader);
            String leaderImage = jsonSubRegion.getString(JSON_LEADER_IMAGE);
            subregion.setLeaderImage(leaderImage);
            String flag = jsonSubRegion.getString(JSON_FLAG);
            subregion.setFlag(flag);
            
            if(tableEmpty){
                dataManager.getTableItems().add(subregion);
            }
            else{
                dataManager.getTableItems().set(i, subregion);
            }
            
            // LOAD SUBREGION COLOR
            Color mapColor = Color.valueOf(jsonSubRegion.getString(JSON_SUBREGION_COLOR));
            dataManager.getMapColors().add(mapColor);
            // LOAD SUBREGION POLYGONS
	    JsonArray jsonSubRegionPolygonArray = jsonSubRegion.getJsonArray(JSON_SUBREGION_POLYGONS);
            for (int j = 0; j < jsonSubRegionPolygonArray.size(); j++){
                // LOAD SUBREGION POLYGON
                JsonArray jsonSubRegionPolygon = jsonSubRegionPolygonArray.getJsonArray(j);
                Polygon subregionpolygon = new Polygon();
                for(int k = 0; k < jsonSubRegionPolygon.size(); k++){
                    // LOAD SUBREGION POINT
                    JsonObject jsonPoint = jsonSubRegionPolygon.getJsonObject(k);
                    for(int l = 0; l < jsonPoint.size(); l++){
                        Double[] point = loadPoint(jsonPoint);
                        subregionpolygon.getPoints().addAll(point);
                    }
                }
                dataManager.getGeometry().add(subregionpolygon);
            }
	}
    }
    
    public void loadGeometry(AppDataComponent data, String filePath) throws IOException {
        // CLEAR THE OLD GEOMETRY
	DataManager dataManager = (DataManager)data;
	dataManager.setGeometry(new ArrayList<Polygon>());
	
	// LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        
        // AND NOW LOAD ALL THE SUBREGIONS
	JsonArray jsonSubRegionArray = json.getJsonArray(JSON_SUBREGIONS);
	for (int i = 0; i < jsonSubRegionArray.size(); i++) {
            // LOAD SUBREGION
	    JsonObject jsonSubRegion = jsonSubRegionArray.getJsonObject(i);
            // LOAD SUBREGION POLYGONS
	    JsonArray jsonSubRegionPolygonArray = jsonSubRegion.getJsonArray(JSON_SUBREGION_POLYGONS);
            for (int j = 0; j < jsonSubRegionPolygonArray.size(); j++){
                // LOAD SUBREGION POLYGON
                JsonArray jsonSubRegionPolygon = jsonSubRegionPolygonArray.getJsonArray(j);
                Polygon subregionpolygon = new Polygon();
                for(int k = 0; k < jsonSubRegionPolygon.size(); k++){
                    // LOAD SUBREGION POINT
                    JsonObject jsonPoint = jsonSubRegionPolygon.getJsonObject(k);
                    for(int l = 0; l < jsonPoint.size(); l++){
                        Double[] point = loadPoint(jsonPoint);
                        subregionpolygon.getPoints().addAll(point);
                    }
                }
                dataManager.getGeometry().add(subregionpolygon);
            }
	}
    }
    
    public double getDataAsDouble(JsonObject json, String dataName) {
	JsonValue value = json.get(dataName);
	JsonNumber number = (JsonNumber)value;
	return number.bigDecimalValue().doubleValue();	
    }
    
    public int getDataAsInt(JsonObject json, String dataName) {
        JsonValue value = json.get(dataName);
        JsonNumber number = (JsonNumber)value;
        return number.bigIntegerValue().intValue();
    }
    
    public Double[] loadPoint(JsonObject jsonPoint){
        // LOAD X
        double x = getDataAsDouble(jsonPoint,JSON_X);
        // LOAD Y
        double y = getDataAsDouble(jsonPoint,JSON_Y);
        Double[] point = new Double[]{x,y};
        return point;
    }
    
    // HELPER METHOD FOR LOADING DATA FROM A JSON FORMAT
    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
	InputStream is = new FileInputStream(jsonFilePath);
	JsonReader jsonReader = Json.createReader(is);
	JsonObject json = jsonReader.readObject();
	jsonReader.close();
	is.close();
	return json;
    }

    @Override
    public void saveData(AppDataComponent data, String filePath) throws IOException {
	DataManager dataManager = (DataManager)data;
        
        Color backgroundColor = dataManager.getBackgroundColor();
        Color borderColor = dataManager.getBorderColor();
        double borderThickness = dataManager.getBorderThickness();
        double zoom = dataManager.getZoom();
        
        double mapWidth = dataManager.getMapWidth();
        double mapHeight = dataManager.getMapHeight();
        
        String fileName = dataManager.getName();
        boolean hasCapitals = dataManager.hasCapitals();
        boolean hasFlags = dataManager.hasFlags();
        boolean hasLeaders = dataManager.hasLeaders();
        
        String anthem = dataManager.getAnthem();
        
	// NOW BUILD THE JSON ARRAY FOR THE SUBREGIONS
	ArrayList<Polygon> geometry = dataManager.getGeometry();
        ObservableList<SubRegion> tableData = dataManager.getTableItems();
        ArrayList<Color> mapColors = dataManager.getMapColors();
        JsonArrayBuilder subregionsArrayBuilder = Json.createArrayBuilder();
	
        int i = 0;
        boolean tableEmpty = tableData.isEmpty();
        for (Polygon polygon : geometry) {
            JsonArrayBuilder polygonArrayBuilder = Json.createArrayBuilder();
            for(int j = 0; j < polygon.getPoints().size(); j+=2){
                // SAVE POINT
                JsonObject pointJson = Json.createObjectBuilder()
                        .add(JSON_X, polygon.getPoints().get(j))
                        .add(JSON_Y, polygon.getPoints().get(j+1)).build();
                polygonArrayBuilder.add(pointJson);
            }
            // SAVE SUBREGION POLYGONS
            JsonArray polygonsArray = polygonArrayBuilder.build();
            JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
            arrayBuilder.add(polygonsArray);
            JsonArray array = arrayBuilder.build();
            
            SubRegion subregion = new SubRegion();
            if (!tableEmpty){
                // SAVE SUBREGION TABLE DATA
                subregion = tableData.get(i);
            }
            
            String name = subregion.getName();
            String capital = subregion.getCapital();
            String leader = subregion.getLeader();
            String leaderImagePath = subregion.getLeaderImage();
            String flagPath = subregion.getFlag();
                
            String mapColor = (!mapColors.isEmpty())?mapColors.get(i).toString():"";
            i++;
            JsonObject subregionJson  = Json.createObjectBuilder()
                    .add(JSON_NAME, name)
                    .add(JSON_CAPITAL, capital)
                    .add(JSON_LEADER, leader)
                    .add(JSON_LEADER_IMAGE, leaderImagePath)
                    .add(JSON_FLAG, flagPath)
                    .add(JSON_SUBREGION_COLOR, mapColor)
                    .add(JSON_SUBREGION_POLYGONS,array)
                    .build();
            subregionsArrayBuilder.add(subregionJson);
	}
        
        // SAVE SUBREGION
	JsonArray subRegionsArray = subregionsArrayBuilder.build();
	
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_BACKGROUND, backgroundColor.toString())
                .add(JSON_BORDER_COLOR, borderColor.toString())
                .add(JSON_BORDER_THICKNESS, borderThickness)
                .add(JSON_ZOOM, zoom)
                .add(JSON_WIDTH, mapWidth)
                .add(JSON_HEIGHT, mapHeight)
                .add(JSON_NAME, fileName)
                .add(JSON_HAS_CAPITALS, hasCapitals)
                .add(JSON_HAS_FLAGS, hasFlags)
                .add(JSON_HAS_LEADERS, hasLeaders)
                .add(JSON_ANTHEM, anthem)
                .add(JSON_SUBREGIONS, subRegionsArray)
		.build();
	
	// AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }

    @Override
    public void exportData(AppDataComponent data, String filePath) throws IOException {
        DataManager dataManager = (DataManager)data;
        
        String fileName = dataManager.getName();
        boolean hasCapitals = dataManager.hasCapitals();
        boolean hasFlags = dataManager.hasFlags();
        boolean hasLeaders = dataManager.hasLeaders();
        
        ArrayList<Color> mapColors = dataManager.getMapColors();
        ObservableList<SubRegion> tableData = dataManager.getTableItems();
        JsonArrayBuilder subregionsArrayBuilder = Json.createArrayBuilder();
	int i = 0;
        for (SubRegion subregion : tableData) {
            String name = subregion.getName();
            String capital = subregion.getCapital();
            String leader = subregion.getLeader();
            Color mapColor = (!mapColors.isEmpty())?mapColors.get(i):Color.BLACK;
            int red = (int) (mapColor.getRed() * 255);
            int green = (int) (mapColor.getGreen() * 255);
            int blue = (int) (mapColor.getBlue() * 255);
            i++;
            JsonObject subregionJson  = Json.createObjectBuilder()
                    .add(JSON_NAME, name)
                    .add(JSON_CAPITAL, capital)
                    .add(JSON_LEADER, leader)
                    .add(JSON_RED, red)
                    .add(JSON_GREEN, green)
                    .add(JSON_BLUE, blue)
                    .build();
            subregionsArrayBuilder.add(subregionJson);
        }
        // SAVE SUBREGION
	JsonArray subRegionsArray = subregionsArrayBuilder.build();
	
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
                .add(JSON_NAME, fileName)
                .add(JSON_HAS_CAPITALS, hasCapitals)
                .add(JSON_HAS_FLAGS, hasFlags)
                .add(JSON_HAS_LEADERS, hasLeaders)
                .add(JSON_SUBREGIONS.toLowerCase(), subRegionsArray)
		.build();
        
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
	Map<String, Object> properties = new HashMap<>(1);
	properties.put(JsonGenerator.PRETTY_PRINTING, true);
	JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
	StringWriter sw = new StringWriter();
	JsonWriter jsonWriter = writerFactory.createWriter(sw);
	jsonWriter.writeObject(dataManagerJSO);
	jsonWriter.close();

	// INIT THE WRITER
	OutputStream os = new FileOutputStream(filePath);
	JsonWriter jsonFileWriter = Json.createWriter(os);
	jsonFileWriter.writeObject(dataManagerJSO);
	String prettyPrinted = sw.toString();
	PrintWriter pw = new PrintWriter(filePath);
	pw.write(prettyPrinted);
	pw.close();
    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        // CLEAR OLD DATA
        DataManager dataManager = (DataManager)data;
        
        // LOAD THE JSON FILE WITH ALL THE DATA
	JsonObject json = loadJSONFile(filePath);
        
        ArrayList<Color> newMapColors = new ArrayList<>();
        
        // IMPORT 
        
        // FILE NAME
        String fileName = json.getString(JSON_NAME);
        dataManager.setName(fileName);
        
        // HAS CAPITALS
        boolean hasCapitals = json.getBoolean(JSON_HAS_CAPITALS);
        dataManager.hasCapitals(hasCapitals);
        
        // HAS FLAGS
        boolean hasFlags = json.getBoolean(JSON_HAS_FLAGS);
        dataManager.hasFlags(hasFlags);
        
        // HAS LEADERS
        boolean hasLeaders = json.getBoolean(JSON_HAS_FLAGS);
        dataManager.hasLeaders(hasLeaders);
        
        // AND NOW LOAD ALL THE SUBREGIONS
        boolean tableEmpty = dataManager.getTableItems().isEmpty();
	JsonArray jsonSubRegionArray = json.getJsonArray(JSON_SUBREGIONS.toLowerCase());
	for (int i = 0; i < jsonSubRegionArray.size(); i++) {
            // LOAD SUBREGION
	    JsonObject jsonSubRegion = jsonSubRegionArray.getJsonObject(i);
            
            // LOAD SUBREGION TABLE DATA
            
            SubRegion subRegion = new SubRegion();
            // NAME
            String name = jsonSubRegion.getString(JSON_NAME);
            subRegion.setName(name);
            // CAPITAL
            String capital = (hasCapitals)?jsonSubRegion.getString(JSON_CAPITAL):subRegion.getCapital();
            subRegion.setCapital(capital);
            // LEADER
            String leader = (hasLeaders)?jsonSubRegion.getString(JSON_LEADER):subRegion.getLeader();
            subRegion.setLeader(leader);
            
            if(tableEmpty){
                dataManager.getTableItems().add(subRegion);
            }
            else{
                dataManager.getTableItems().set(i, subRegion);
            }
            
            // RED
            int red = jsonSubRegion.getInt(JSON_RED);
            // GREEN
            int green = jsonSubRegion.getInt(JSON_GREEN);
            // BLUE           
            int blue = jsonSubRegion.getInt(JSON_BLUE);
            
            // COLOR
            Color newColor = Color.rgb(red,green,blue);
            newMapColors.add(newColor);
        }
        
        dataManager.setMapColors(newMapColors);
    }
}
