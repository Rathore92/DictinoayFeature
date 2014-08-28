package com.example.lalchand.myapplication;

import android.util.Log;

import com.example.lalchand.myapplication.HttpNetwork;
import com.example.lalchand.myapplication.MobileSerializer;
import com.example.lalchand.myapplication.MultipleResultSerializer;
import com.example.lalchand.myapplication.PageSerializer;
import com.example.lalchand.myapplication.Pages;
import com.example.lalchand.myapplication.WordSearchInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
//import  java.awt.Dialog.*;

/**
 * Created by lal.chand on 23/07/14.
 */
public class WikiParser {
    String Pageid = "" ;
    public String MobileUrl = "";
    public  String message = "";
    public int indexforMutlipleResutl = 0;
    public ArrayList<String> links = new ArrayList<String>();

    public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
    private WordSearchInterface listener;
    Pages PagesResponse = new Pages();
    Pages mobileResponse = new Pages();
    Pages mutpleResultResponse = new Pages();

    public WikiParser(WordSearchInterface listener) {
        if(listener==null) {
            throw new NullPointerException();
        }
        this.listener=listener;
    }


    // helper class for generating url for given word.
    public  class  Generateurl {
        private String title = "title";
        private String from = "from";
        HttpNetwork httpNetwork = new HttpNetwork();
        Generateurl () {
        };
        public void GenerateWikiurl() throws IOException, JSONException {
            // uses google Gson library
            GsonBuilder gsonBuilder=new GsonBuilder();
            /*
             Because wikientity is dynamic so need Type adaptor, It also handle the JSON parsing using GSON library
             */
            gsonBuilder.registerTypeAdapter(Pages.class,new PageSerializer());
            Gson gson = gsonBuilder.create();
            //It deal with all network request and josn parse gives content for inputstream
            InputStream source = httpNetwork.JsonParse(httpNetwork.GenerateNetworkRequest(message));
            if(source == null)
            {
                Log.e("response from json parsing is : ", source.toString());
                return ;
            }
            Reader reader = new InputStreamReader(source);
            // parse from Gson google library
            PagesResponse = gson.fromJson(reader, Pages.class);

            if(PagesResponse == null)      Log.e("PageResponse/myActivity","Page Response is null");
            else {
                if (PagesResponse.getWikiEntity().IsConsistant == false ||
                        PagesResponse.getWikiEntity().getExtract()== "" ||
                        PagesResponse.getWikiEntity().getExtract() == null) {
                    PagesResponse.getWikiEntity().IsConsistant = false;
                    PagesResponse.getWikiEntity().setExtract("No definition found");
                    Log.e("from my activity",PagesResponse.getWikiEntity().getExtract().toString());
                    getdescription();
                }   // if description is found for given word then generate a request for wiki link
                else {
                    Log.e("title from my activity",PagesResponse.getWikiEntity().getTitle().toString());
                    Pageid  = PagesResponse.getWikiEntity().getPageid().toString();
                    Log.e("pageid from my activity",Pageid);
                    Log.e("ns from my activity", PagesResponse.getWikiEntity().getNs().toString());
                    Log.e("extract from my activity",PagesResponse.getWikiEntity().getExtract().toString());
                    ArrayList<String> lists = PagesResponse.getWikiEntity().getLinks();
                    if(lists.size() != 0){
                        for(int i = 0; i <lists.size() && i <10 ;i++)
                        {
                            String title = lists.get(i);
                            GenerateMultipleResultUrl(title);
                        }
                    }
                    GenerateMobileUrl();                     // to generate a wiki link
                }

            }

        }


        public void GenerateMobileUrl() throws IOException, JSONException {
            GsonBuilder gsonBuilder=new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Pages.class,new MobileSerializer());
            Gson gson = gsonBuilder.create();
            InputStream source = httpNetwork.JsonParse(httpNetwork.GenerateMobileNetworkRequest(Pageid));
            if(source == null){
                Log.e("response from json parsing is : ", source.toString());
                return;
            }
            Reader reader = new InputStreamReader(source);
            mobileResponse = gson.fromJson(reader, Pages.class);
            if(mobileResponse.getWikiEntity().IsConsistant == true) {
                String WebUrl = mobileResponse.getWikiEntity().getFullurl().toString();
                Log.e("from Generatemobileurl wikiparser (Weburl) ",WebUrl);
                if(WebUrl != null)
                MobileUrl = WebUrl.substring(0, 11) + "m" + "." + WebUrl.substring(11, WebUrl.length());
                else
                    Log.e("Wikiparse","Web url is null");
            }
            else {
                MobileUrl = "";
            }
            Log.e("from mobile url",MobileUrl);
            getdescription();

        }

        public void GenerateMultipleResultUrl(String title) throws IOException, JSONException {
            GsonBuilder gsonBuilder=new GsonBuilder();
            gsonBuilder.registerTypeAdapter(Pages.class,new MultipleResultSerializer());
            Gson gson = gsonBuilder.create();
            InputStream source = httpNetwork.JsonParse(httpNetwork.GenerateMultipleResultNetworkRequest(title));
            if(source == null){
                Log.e("response from json parsing is : ", source.toString());
                return;
            }
            Reader reader = new InputStreamReader(source);
            mutpleResultResponse = gson.fromJson(reader, Pages.class);
            if(mutpleResultResponse.getWikiEntity().IsConsistant == true) {
                String linkurl = new String();
                if(mutpleResultResponse.getWikiEntity().getMultipleResultUrl() != null) {
                    linkurl = mutpleResultResponse.getWikiEntity().getMultipleResultUrl().toString();
                    links.add(linkurl.substring(0, 11) + "m" + "." + linkurl.substring(11, linkurl.length()));
                    Log.e("from mobile url",links.get(indexforMutlipleResutl));
                    indexforMutlipleResutl++;
                }
                else
                    Log.e("from wikiparser","linkurl is null");
                Log.e("from Generatemultipleresulturl wikiparser (Weburl) ",linkurl);
            }
            else {
                Log.e("from wikiparse","No one of the url is not perfect");
//                    links.add("");
            }

        }

    }


    /*
    *       Start a new thread for get the wiki content for given word
    *
    * */
    public void StartSearchUrl(){
        Runnable rn=new Runnable() {
            @Override
            public void run() {
                Generateurl url1 = new Generateurl();
                try {
                    url1.GenerateWikiurl();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread t=new Thread(rn);
        t.start();




    }


    public void getdescription(){
        if(PagesResponse == null){
            listener.OnProcessFinished(null, null,null,null,null,false);
        }else {
            if (PagesResponse.getWikiEntity().IsConsistant == true) {
                if(MobileUrl.length() != 0)
                    MobileUrl = MobileUrl.substring(1, (MobileUrl.length() - 1));

                for(int i = 0; i < links.size();i++)
                {
                    if(links.get(i).length() != 0 )
                    links.set(i,links.get(i).substring(1,links.get(i).length()-1));


                }
                Log.e("mobile url form wikiparser (MobileUrl) ", MobileUrl);
                Log.e("from submit buttom", PagesResponse.getWikiEntity().getExtract().toString());
                listener.OnProcessFinished(PagesResponse.getWikiEntity().getExtract(),
                        MobileUrl,
                        PagesResponse.getWikiEntity().getTitle(),
                        PagesResponse.getWikiEntity().getLinks(),
                        links,
                        mobileResponse.getWikiEntity().isRedirect);
            } else {
                Log.e("from wikiparser", "some thing went wrong");
                /*
                    Pass then description and wiki link (MobileUrl) for creating and display a view (It's a callback)
                 */
                listener.OnProcessFinished(PagesResponse.getWikiEntity().getExtract(),
                        MobileUrl,
                        PagesResponse.getWikiEntity().getTitle(),
                        PagesResponse.getWikiEntity().getLinks(),
                        links,
                        mobileResponse.getWikiEntity().isRedirect);
            }
        }
    }
}