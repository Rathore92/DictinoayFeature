
package fragmentssample.fragment;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class WikiEntity{
    boolean IsConsistant = true;
    boolean isRedirect = false;
   	private Number ns;
   	private Number pageid;
   	private String extract;
   	private String title;
    private String fullurl;
    private String multipleResultUrl;
    private ArrayList<String> links = new ArrayList<String>();



 	public Number getNs(){
		return this.ns;
	}
	public void setNs(Number ns){
		this.ns = ns;
	}
 	public Number getPageid(){
		return this.pageid;
	}
	public void setPageid(Number pageid){
		this.pageid = pageid;
	}
 	public String getExtract(){
		return this.extract;
	}
	public void setExtract(String revisions){
		this.extract = revisions;
	}
 	public String getTitle(){
		return this.title;
	}
	public void setTitle(String title){
		this.title =title;
	}
    public void setFullurl(String fullurl){
        this.fullurl = fullurl;
    }
    public String getFullurl() {
        return this.fullurl;
    }
    public void setLinks(String ele){ this.links.add(ele);}
    public ArrayList getLinks(){return this.links;}
    public void setMultipleResultUrl(String multipleResultUrl) { this.multipleResultUrl = multipleResultUrl;}
    public String getMultipleResultUrl() {return this.multipleResultUrl;}

}
