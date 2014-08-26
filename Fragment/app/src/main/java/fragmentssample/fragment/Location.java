
package fragmentssample.fragment;

import java.util.List;

public class Location{
   	private Number lat;
   	private Number lng;
    public boolean isConsistant = true;
    public boolean isGeomatry = false;

 	public Number getLat(){
		return this.lat;
	}
	public void setLat(Number lat){
		this.lat = lat;
	}
 	public Number getLng(){
		return this.lng;
	}
	public void setLng(Number lng){
		this.lng = lng;
	}
}
