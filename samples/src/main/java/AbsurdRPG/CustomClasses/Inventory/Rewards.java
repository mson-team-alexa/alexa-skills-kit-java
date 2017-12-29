package AbsurdRPG.CustomClasses.Inventory;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Rewards{

	private static final Logger log = LoggerFactory.getLogger(Rewards.class);

	private Rewards() {
		
	}
	
	public static Reward generateGreenWeaponReward(int number) {
		Weapon w = Weapons.Green_weapons.generateRandom();
		
		Reward r = new Reward(w, number);
		
		return r;
	}
}


