package fragmentssample.fragment;
import android.graphics.pdf.PdfDocument;
import android.util.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import org.json.JSONObject;
import java.lang.reflect.Type;
import java.security.PrivateKey;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import static java.lang.System.exit;
import static java.lang.System.load;

import org.json.JSONArray;


/**
 * Created by lal.chand on 16/07/14.
 */
public class PageSerializer implements JsonDeserializer<Pages> {

    /*
            To handle a dynamic tag reponse
     */
    @Override
    public Pages deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Boolean check = false;
        Pages pages=new Pages();
        if(json.isJsonNull()) return null;
        else if (json.isJsonObject())
            return handleObject(json.getAsJsonObject(), context,pages);
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

    public  void setWikititle (Pages pages,Map.Entry<String, JsonElement> DataEntry)
    {
        if (DataEntry.getValue() != null) {
            JsonElement value = DataEntry.getValue();
            pages.getWikiEntity().setTitle(value.getAsString());
        } else {
            pages.getWikiEntity().IsConsistant = false;
            Log.e("from titile", "title value is null");
        }
    }


    public  void setWikipageid (Pages pages,Map.Entry<String, JsonElement> DataEntry)
    {
        if (DataEntry.getValue() != null) {
            pages.getWikiEntity().setPageid(DataEntry.getValue().getAsNumber());
        } else {
            pages.getWikiEntity().IsConsistant = false;
            Log.e("from pageid", "pageid value is null;");
        }
    }

    public  void setWikins (Pages pages,Map.Entry<String, JsonElement> DataEntry)
    {
        if (DataEntry.getValue() != null) {
            pages.getWikiEntity().setNs(DataEntry.getValue().getAsNumber());
 //           Log.e("from ns", DataEntry.getValue().toString());
        } else {
            // just cross check is there is mandatry to set flag flase
            pages.getWikiEntity().IsConsistant = false;
            Log.e("from ns", "ns value is null");
        }
    }

    public  void setWikiextract (Pages pages,Map.Entry<String, JsonElement> DataEntry)
    {
        if (DataEntry.getValue() != null) {
            String discreption = DataEntry.getValue().getAsString();
            pages.getWikiEntity().setExtract(discreption);
//            Log.e("from extract", DataEntry.getValue().toString());
        } else {
            pages.getWikiEntity().IsConsistant = false;
//            pages.getWikiEntity().setExtract("No definition found");
            Log.e("from extract", "extract value is null");
        }
    }

    public void setWikiInternalLinks(Pages pages,Map.Entry<String, JsonElement> DataEntry){
            if(DataEntry.getValue().isJsonArray()){
                JsonArray linksArray = DataEntry.getValue().getAsJsonArray();
                for (JsonElement je : linksArray) {
                    Log.e("size of array","" + linksArray.size());
                        printJson(je,pages);
                }
            }
    }

    public static void printJson(JsonElement jsonElement,Pages pages) {
        // Check whether jsonElement is JsonObject or not
        if (jsonElement.isJsonObject()) {
            Set<Map.Entry<String, JsonElement>> ens = ((JsonObject) jsonElement).entrySet();
            if (ens != null) {
                // Iterate JSON Elements with Key values
                for (Map.Entry<String, JsonElement> en : ens) {
                    Log.e("key ",  en.getKey());
                    String val = en.getValue().getAsString();
                    if(en.getKey().equalsIgnoreCase("title")) {
                        if (!pages.getWikiEntity().getTitle().isEmpty())
                            if (val.toLowerCase().contains(pages.getWikiEntity().getTitle().toLowerCase())) {
                                pages.getWikiEntity().setLinks(val);
                                Log.e("value", val);
                            }
                        printJson(en.getValue(), pages);
                    }
                }
            }
        }

        // Check whether jsonElement is Arrary or not
        else if (jsonElement.isJsonArray()) {
            JsonArray jarr = jsonElement.getAsJsonArray();
            // Iterate JSON Array to JSON Elements
            for (JsonElement je : jarr) {
                printJson(je,pages);
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




    public void setWikicontent (Pages pages, Map.Entry<String, JsonElement> DataEntry){
        if (DataEntry != null) {
            if (DataEntry.getKey() != null) {
                if (DataEntry.getKey().equalsIgnoreCase("title")) {
                    setWikititle(pages,DataEntry);
                } else if (DataEntry.getKey().equalsIgnoreCase("pageid")) {
                    setWikipageid(pages,DataEntry);
                } else if (DataEntry.getKey().equalsIgnoreCase("ns")) {
                    setWikins(pages,DataEntry);
                } else if (DataEntry.getKey().equalsIgnoreCase("extract")) {
                    setWikiextract(pages,DataEntry);
                } else if (DataEntry.getKey().equalsIgnoreCase("missing")) {
                    pages.getWikiEntity().IsConsistant = false;
                } else if (DataEntry.getKey().equalsIgnoreCase("links")) {
                    setWikiInternalLinks(pages,DataEntry);
                }

            } else {
                pages.getWikiEntity().IsConsistant = false;
                Log.e("DataEntry Key", "DataEntry Key is found null");
            }

        } else {
            pages.getWikiEntity().IsConsistant = false;
            Log.e("DataEntry", "DataEntry is found null");
        }
    }

/*
        It parsing the JSON response and  extract the desired data from response
 */

    private Pages handleObject(JsonObject json, JsonDeserializationContext context,Pages pages){
        boolean CheckExtract = false;
        //=result1.getPages();

        for(Map.Entry<String, JsonElement> Entry : json.entrySet()) {
            if(Entry.getKey().equalsIgnoreCase("query")) {
                JsonObject PagesObj = getJsonObect(Entry);
                if (PagesObj != null) {
                    for (Map.Entry<String, JsonElement> PageEntry : PagesObj.entrySet()) {
                        JsonObject WEObj = getJsonObect(PageEntry);
                        if (WEObj != null) {
                            for (Map.Entry<String, JsonElement> WikiEntry : WEObj.entrySet()) {
                                JsonObject DataObj = getJsonObect(WikiEntry);
                                if (DataObj != null) {
                                    for (Map.Entry<String, JsonElement> DataEntry : DataObj.entrySet()) {
                                        setWikicontent(pages, DataEntry);
                                        CheckExtract = true;
                                    }
                                }
                            }
                        }
                    }
                    if (!CheckExtract) pages.getWikiEntity().IsConsistant = false;
                } else {
                    Log.e("PageObj", "PageObj is null");
                    pages.getWikiEntity().IsConsistant = false;
                }
            }
            else{
                Log.e("pageserializer",Entry.getKey().toString());
            }
        }


     return pages;
    }
}
