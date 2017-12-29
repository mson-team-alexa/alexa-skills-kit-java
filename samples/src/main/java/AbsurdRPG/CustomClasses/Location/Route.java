package AbsurdRPG.CustomClasses.Location;

import java.util.ArrayList;
import java.util.Map;
import java.util.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Route {

	private static final Logger logC = LoggerFactory.getLogger(Route.class);
	
	public Map<String, Integer> distance_X;
	
	public Map<String, Integer> distance_Y;
	
	public Route(Vector<Integer> v) {
		distance_X.put("X_dir", v.get(0));
		
		distance_Y.put("Y_dir", v.get(1));
	}
	
	public ArrayList<Vector<String>> specificRouteToDestination(Map<String, Integer> X_Dir, Map<String, Integer> Y_Dir){
		
		ArrayList<Vector<String>> map_Dirs = new ArrayList<Vector<String>>();
		
		for(int i = 0; i < X_Dir.get("X_dir"); i++) {
			Vector<String> v = new Vector<String>(2);		
			
			v.add("X_dir");
			
			Integer unit = X_Dir.get("X_dir") / Math.abs(X_Dir.get("X_dir"));
			
			v.add(unit.toString());
			
			map_Dirs.add(v);
		}
		
		for(int i = 0; i < X_Dir.get("Y_dir"); i++) {
			Vector<String> v = new Vector<String>(2);		
			
			v.add("Y_dir");
			
			Integer unit = X_Dir.get("Y_dir") / Math.abs(X_Dir.get("Y_dir"));
			
			v.add(unit.toString());
			
			map_Dirs.add(v);
		}
		
		return map_Dirs;
	}
}
