package barthelper;

import java.util.*;

public class Stations {
	private static final Map<String, String> stations = new HashMap<String, String>();
	
		private Stations() {
		}
	
		static {
			stations.put("12th street oakland city center", "12th");
			stations.put("16th street mission", "16th");
			stations.put("19th street oakland", "19th");
			stations.put("24th street mission", "24th");
			stations.put("ashby", "ashb");
			stations.put("balboa park", "balb");
			stations.put("bay fair", "bayf");
			stations.put("castro valley", "cast");
			stations.put("civic center", "civc");
			stations.put("coliseum", "cols");
			stations.put("colma", "colm");
			stations.put("concord", "conc");
			stations.put("daly city", "daly");
			stations.put("downtown berkeley", "dbrk");
			stations.put("dublin pleasanton", "dubl");
			stations.put("el cerrito del norte", "deln");
			stations.put("del norte", "deln");
			stations.put("el cerrito plaza", "plza");
			stations.put("embarcadero", "embr");
			stations.put("fremont", "frmt");
			stations.put("fruitvale", "ftvl");
			stations.put("glen park", "glen");
			stations.put("hayward", "hayw");
			stations.put("lafayette", "lafy");
			stations.put("lake merritt", "lake");
			stations.put("macarthur", "mcar");
			stations.put("millbrae", "mlbr");
			stations.put("montgomery street", "mont");
			stations.put("north berkeley", "nbrk");
			stations.put("north concord martinez", "ncon");
			stations.put("oakland airport", "oakl");
			stations.put("orinda", "orin");
			stations.put("pittsburg bay point", "pitt");
			stations.put("pleasant hill", "phil");
			stations.put("powell street", "powl");
			stations.put("richmond", "rich");
			stations.put("rockridge", "rock");
			stations.put("san bruno", "sbrn");
			stations.put("san francisco airport", "sfia");
			stations.put("san leandro", "sanl");
			stations.put("south hayward", "shay");
			stations.put("south san francisco", "ssan");
			stations.put("union city", "ucty");
			stations.put("walnut creek", "wcrk");
			stations.put("west dublin pleasanton", "wdub");
			stations.put("west oakland", "woak");
		}
	
		public static String get(String item) {
			return stations.get(item);
		}
}
/* 

 * 
 */