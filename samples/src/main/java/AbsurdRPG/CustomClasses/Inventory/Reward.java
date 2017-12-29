package AbsurdRPG.CustomClasses.Inventory;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Reward{

	private static final Logger log = LoggerFactory.getLogger(Reward.class);

	private String type;
	
	private Object reward;
	
	private Integer quantity;
	
	public Reward(Object R, Integer Q) {

		if(R instanceof Armor) {
			reward = (Armor)R;
		}else if(R instanceof Restoration) {
			reward = (Restoration)R;
		}else if(R instanceof Weapon) {
			reward = (Weapon)R;
		}else if(R instanceof Wing) {
			reward = (Wing)R;
		}else {
			reward = R;
		}
		
		quantity = Q;
	}
	
	
	
	public Object return_instance() {
		if(reward instanceof Armor) {
			reward = (Armor)reward;
		}else if(reward instanceof Restoration) {
			reward = (Restoration)reward;
		}else if(reward instanceof Weapon) {
			reward = (Weapon)reward;
		}else if(reward instanceof Wing) {
			reward = (Wing)reward;
		}else {
			reward = reward;
		}
		
		return reward;
	}
	
}


