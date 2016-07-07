package rvme.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
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

/**
 * This class serves as the file management component for this application,
 * providing all I/O services.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public class FileManager implements AppFileComponent {
    // FOR JSON SAVING AND LOADING
    static final String JSON_X = "X";
    static final String JSON_Y = "Y";
    
    static final String JSON_SUBREGION_POLYGONS = "SUBREGION_POLYGONS";
    
    static final String JSON_SUBREGIONS = "SUBREGIONS";
    
    @Override
    public void loadData(AppDataComponent data, String filePath) throws IOException {
        // CLEAR THE OLD DATA OUT
	DataManager dataManager = (DataManager)data;
	dataManager.reset();
	
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
        
	// NOW BUILD THE JSON ARRAY FOR THE GEOMETRY
	ObservableList<Polygon> geometry = dataManager.getGeometry();
        JsonArrayBuilder subRegionsArrayBuilder = Json.createArrayBuilder();
	for (Polygon polygon : geometry) {
            JsonArrayBuilder polygonArrayBuilder = Json.createArrayBuilder();
            for(int i = 0; i < polygon.getPoints().size(); i+=2){
                JsonObject pointJson = Json.createObjectBuilder()
                        .add(JSON_X, polygon.getPoints().get(i))
                        .add(JSON_Y, polygon.getPoints().get(i+1)).build();
                polygonArrayBuilder.add(pointJson);
            }
            JsonArray polygonsArray = polygonArrayBuilder.build();
            subRegionsArrayBuilder.add(polygonsArray);
	}
	JsonArray subRegionsArray = subRegionsArrayBuilder.build();
	
	// THEN PUT IT ALL TOGETHER IN A JsonObject
	JsonObject dataManagerJSO = Json.createObjectBuilder()
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void importData(AppDataComponent data, String filePath) throws IOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
