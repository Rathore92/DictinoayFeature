
package fragmentssample.fragment;

import java.util.List;

public class Geometry{
   	private Location location = new Location();
   	private String location_type;
   	private Viewport viewport;

 	public Location getLocation(){
		return this.location;
	}
	public void setLocation(Location location){
		this.location = location;
	}
 	public String getLocation_type(){
		return this.location_type;
	}
	public void setLocation_type(String location_type){
		this.location_type = location_type;
	}
 	public Viewport getViewport(){
		return this.viewport;
	}
	public void setViewport(Viewport viewport){
		this.viewport = viewport;
	}
}
