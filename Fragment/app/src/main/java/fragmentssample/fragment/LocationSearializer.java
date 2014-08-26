package fragmentssample.fragment;

import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;

/**
 * Created by lal.chand on 22/08/14.
 */
public class LocationSearializer implements JsonDeserializer <Results> {


    @Override
    public Results deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {

//        Geometry geometry = new Geometry();
        Results results = new Results();

        if(json.isJsonNull()) return null;
        else if (json.isJsonObject())
            return handleObject(json.getAsJsonObject(), context, results);
        else if (json.isJsonArray()){
            Log.w("From location Searilizer","It is a json array");
            return null;
        }

        else {
            System.out.println("it's not object");
            return null;
        }
    }


    public JsonObject getJsonObect(Map.Entry<String, JsonElement> Entry){
        if(Entry != null) {
            if (Entry.getValue() != null) {
                if (!Entry.getValue().isJsonObject()) {
                    return null;
                }
                JsonObject jsonObject = Entry.getValue().getAsJsonObject();
                return jsonObject;
            } else {
                return null;
            }
        }else {
            return null;
        }
    }

    public void setLatLang(Map.Entry<String, JsonElement> latLngLocationEntry,Results results){

        if(latLngLocationEntry != null){
            if(latLngLocationEntry.getKey().equalsIgnoreCase("lat")){
                Number lat = latLngLocationEntry.getValue().getAsNumber();
                Log.e("from locationSerializer","" + lat);
                results.getGeometry().getLocation().setLat(lat);
            }
            else if(latLngLocationEntry.getKey().equalsIgnoreCase("lng")){
                Number lng = latLngLocationEntry.getValue().getAsNumber();
                Log.e("from locationSerializer","" + lng);
                results.getGeometry().getLocation().setLng(lng);
            }else{
                results.getGeometry().getLocation().isConsistant = false;
            }
        }
    }

    public void setContent(Map.Entry<String, JsonElement> locationEntry,Results results){
        if(locationEntry != null)
        {
            if (locationEntry.getKey().equalsIgnoreCase("geometry")) {
                JsonObject geometryObj = getJsonObect(locationEntry);
                for (Map.Entry<String, JsonElement> geometryEntry : geometryObj.entrySet()) {
                    JsonObject latLngLocationObj = getJsonObect(geometryEntry);

                    if(geometryEntry.getKey().equalsIgnoreCase("location")) {
                        for (Map.Entry<String, JsonElement> latLngLocationEntry : latLngLocationObj.entrySet()) {
                            setLatLang(latLngLocationEntry, results);
                        }
                    }
                }
                results.getGeometry().getLocation().isGeomatry = true;
            }
            else if(locationEntry.getKey().equalsIgnoreCase("formatted_address")){
                results.setFormatted_address(locationEntry.getValue().getAsString());
            }
        }else {
            results.getGeometry().getLocation().isConsistant = false;
        }
    }

    public void getJsonResults(Results results, Map.Entry<String, JsonElement> DataEntry){
        if(DataEntry.getValue().isJsonArray()){
            JsonArray linksArray = DataEntry.getValue().getAsJsonArray();
            if(linksArray.size() == 0){
                results.getGeometry().getLocation().isConsistant = false;
            }else {
                for (JsonElement je : linksArray) {
                    Log.e("size of array","" + linksArray.size());
                    printJson(je,results);
                }
            }
        }
        else {
            Log.e("From Location serializer","results is not json array");
            results.getGeometry().getLocation().isConsistant = false;
        }
    }

    public  void printJson(JsonElement jsonElement,Results results) {
        if (jsonElement.isJsonObject()) {
            Set<Map.Entry<String, JsonElement>> ens = ((JsonObject) jsonElement).entrySet();
            if (ens != null) {
                for (Map.Entry<String, JsonElement> en : ens) {
                    setContent(en,results);
                }
            }
        }

        // Check whether jsonElement is Array or not
        else if (jsonElement.isJsonArray()) {
            JsonArray jarr = jsonElement.getAsJsonArray();
            // Iterate JSON Array to JSON Elements
            for (JsonElement je : jarr) {
                printJson(je,results);
            }
        }

        // Check whether jsonElement is NULL or not
        else if (jsonElement.isJsonNull()) {
            // print null
            System.out.println("null");
        }
        // Check whether jsonElement is Primitive or not
        else if (jsonElement.isJsonPrimitive()) {
        }

    }



    private Results handleObject(JsonObject json, JsonDeserializationContext context, Results results){

        for(Map.Entry<String, JsonElement> Entry : json.entrySet()) {
            if (Entry != null) {
                if (Entry.getKey() != null) {
                    if (Entry.getKey().equalsIgnoreCase("results")) {
                        getJsonResults(results, Entry);
                    } else if (Entry.getKey().equalsIgnoreCase("status")) {
                        String val = Entry.getValue().getAsString();
                        if (val.equalsIgnoreCase("ZERO_RESULTS")) {
                            Log.e("here status is not ok", val);
                            return null;
                        }
                    }
                }
            }
        }
        return results;

    }

}
