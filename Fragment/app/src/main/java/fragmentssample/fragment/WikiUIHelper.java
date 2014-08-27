package fragmentssample.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


/**
 * Created by lal.chand on 23/07/14.
 */
public class WikiUIHelper implements  WordSearchInterface{

        private  String lastAttemptedWord = "";
        private Context activityContext ;
        private ProgressDialog wordsearchLookupProgressDialog;
        private WordViewInterface viewlistner;
        private Activity activity;

        WikiUIHelper(WordViewInterface viewlistner,Activity activity){
            if(viewlistner == null)
            {
                throw new NullPointerException();
            }
            this.viewlistner = viewlistner;
            this.activity = activity;
        }


    /*
            Getting a calback and creating a view
     */

        @Override
        public void OnProcessFinished(final String description, final String MobileUrl, final String title,
                                      final ArrayList list, final ArrayList<String> links,final  boolean redirect) {
            Log.e("from WikiUIHepler OnprocessFinished", description);
            if (description == null || MobileUrl == null) {
                viewlistner.OnGenerateViewFinished(null, null,false,null);
            } else {
                /*
                        Creating a view (using updateUI) so need to run in UI thread
                 */
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            updateUI(description, MobileUrl, title,list,links,redirect);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
        }

    private void createHyperlinks(ArrayList<String> links, ArrayList<String> list, View view)
    {
        TextView txtlinks = (TextView) view.findViewById(R.id.link1);
        String linkedText = new String();
        StringBuilder linkedtextbuilder = new StringBuilder();
        for(int i = 0; i <links.size() && i <10 ;i++)
        {
            linkedText = linkedText + String.format("<a href=\"" + links.get(i).toString() + "\">" + list.get(i).toString() + ">> </a>");
      //      linkedtextbuilder.append(linkedText);
            linkedText = linkedText + '\n';
//            linkedtextbuilder.append(linkedText);
//            linkedtextbuilder.append(System.getProperty());
            //linkedtextbuilder.append(System.getProperty("line.separator")).append(linkedText);

        }
        txtlinks.setText(Html.fromHtml(String.valueOf(linkedText)));
        txtlinks.setMovementMethod(LinkMovementMethod.getInstance());


    }


        private void updateUI(String description, String MobileUrl, String title, ArrayList list,
                              final ArrayList<String> links, boolean redirect) throws IOException {
            View view = generateView(description);
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.word_view_layout, null);
            if(description != null) {
                     if(links.size() != 0){
                             Log.e("list content",list.get(0).toString());
                                    createHyperlinks(links,list,view);
                     }
                    TextView tv1 = (TextView) view.findViewById(R.id.WVL1);
                    tv1.setMovementMethod(ScrollingMovementMethod.getInstance());
                    tv1.setText(description);
                if(MobileUrl.equalsIgnoreCase("")){
                    Log.e("sdfasdfsafsfsasdsfds","illegal word so not go to generate mobile url");
                }
                else {
                    TextView mobileUrl = (TextView) view.findViewById(R.id.WVL2);
                    mobileUrl.setText(Html.fromHtml("<a href=\"" + MobileUrl + "\">see more >> </a>"));
                    mobileUrl.setMovementMethod(LinkMovementMethod.getInstance());
                }

            }
            viewlistner.OnGenerateViewFinished(view,title,redirect,MobileUrl);
        }

        //additions
    /*
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });
    */
            //view ready for consumption

        private View generateView(String description) {
            return null;
        }

        public void lookupWordAsync(String word)
        {
            this.lastAttemptedWord = word;
            WikiParser wikiParser = new WikiParser(this);
            wikiParser.message = word;
            wikiParser.StartSearchUrl();

        }



}
