package com.example.lalchand.myapplication;

import android.util.Log;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by lal.chand on 06/08/14.
 */
public class MultipleResultSerializer implements JsonDeserializer<Pages> {

    @Override
    public Pages deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Pages pages=new Pages();
        if(json.isJsonNull()) return null;
        else if (json.isJsonObject())
            return handleObject(json.getAsJsonObject(), context,pages);
        else {
            System.out.println("it's not object");
            return null;
        }
    }


    public void setWikiurl(Pages pages,Map.Entry<String, JsonElement> DataEntry){
        if (DataEntry.getValue() != null) {
            pages.getWikiEntity().setMultipleResultUrl(DataEntry.getValue().toString());
//            Log.e("from extract", DataEntry.getValue().toString());
        } else {
            pages.getWikiEntity().IsConsistant = false;
//            pages.getWikiEntity().setExtract("No definition found");
            Log.e("from mobilizer", "fullurl value is null");
        }
    }

    public  void SetwikiMultipleResult (Pages pages,Map.Entry<String, JsonElement> DataEntry ){
        if (DataEntry != null) {
            if (DataEntry.getKey() != null) {
                if (DataEntry.getKey().equalsIgnoreCase("fullurl")) {
                    setWikiurl(pages,DataEntry);
                }
                else if(DataEntry.getKey().equalsIgnoreCase("redirect")){
                    pages.getWikiEntity().isRedirect = true;
                }
                else if (DataEntry.getKey().equalsIgnoreCase("missing")) {
                    pages.getWikiEntity().IsConsistant = false;
//                    pages.getWikiEntity().setExtract("No definition found");
                    //                   Log.e("from pageserializer Missing", "No definition found");
                }

            }else {
                pages.getWikiEntity().IsConsistant = false;
                Log.e("DataEntry Key", "DataEntry Key is found null");
            }

        }else {
            pages.getWikiEntity().IsConsistant = false;
            Log.e("DataEntry", "DataEntry is found null");
        }
    }

    private Pages handleObject(JsonObject json, JsonDeserializationContext context,Pages pages){
        boolean CheckExtract = false;
        PageSerializer pageSerializer = new PageSerializer();
        //=result1.getPages();
        for(Map.Entry<String, JsonElement> Entry : json.entrySet()) {
            JsonObject PagesObj = pageSerializer.getJsonObect(Entry);
            if(PagesObj != null) {
                for (Map.Entry<String, JsonElement> PageEntry : PagesObj.entrySet()) {
                    JsonObject WEObj = pageSerializer.getJsonObect(PageEntry);
                    if (WEObj != null) {
                        for (Map.Entry<String, JsonElement> WikiEntry : WEObj.entrySet()) {
                            JsonObject DataObj = pageSerializer.getJsonObect(WikiEntry);
                            if (DataObj != null) {
                                for (Map.Entry<String, JsonElement> DataEntry : DataObj.entrySet()) {
                                    SetwikiMultipleResult(pages, DataEntry);
                                    CheckExtract = true;
                                }
                            }
                        }
                    }
                }
                if(! CheckExtract) pages.getWikiEntity().IsConsistant = false;
            }else {
                Log.e("PageObj", "PageObj is null");
                pages.getWikiEntity().IsConsistant = false;
            }
        }
        return pages;
    }



}
