package AbsurdRPG.CustomClasses.Inventory;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Inventory{

	private static final Logger log = LoggerFactory.getLogger(Inventory.class);

	public static ArrayList<Weapon> weapons = new ArrayList<Weapon>();
	
	public static ArrayList<Restoration> restorations = new ArrayList<Restoration>();
	
	public static ArrayList<Armor> armors = new ArrayList<Armor>();

	private Inventory() {
		
	}
	
    public void addWeapon(Weapon w) {
    	weapons.add(w);
    }

    public void addArmor(Armor a) {
    	armors.add(a);
    }
	
	public void addRestoration(Restoration r) {
		restorations.add(r);
	}
}


