package AbsurdRPG.CustomClasses.Inventory;

import java.util.ArrayList;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Weapons{

	private static final Logger log = LoggerFactory.getLogger(Weapons.class);

	public static ArrayList<Weapon> weapons;
	
	private static Random RAND = new Random();
	
	private final static String SWORD = "Sword";
	
	private final static String SPEAR = "Spear";
	
	private final static String DAGGER = "Dagger";
	
	private final static String QUALITY_GREEN = "Green";
	
	private final static String QUALITY_BLUE = "Blue";
	
	private final static String QUALITY_PURPLE = "Purple";
	
	private final static String QUALITY_LENGENDARY = "Lengendary";
	
	
	
	private Weapons() {

	}
	
	private static int lowDamageRange() {
		int damage = RAND.nextInt(6) + 10;
		
		return damage;
	}
	
	private static int middleDamageRange() {
		int damage = RAND.nextInt(13) + 18;
		
		return damage;
	}
	
	private static int goodDamageRange() {
		int damage = RAND.nextInt(26) + 27;
		
		return damage;
	}
	
	private static int lengendaryDamageRange() {
		int damage = RAND.nextInt(31) + 70;
		
		return damage;
	}
	
	private static int lowPriceRange() {
		int price = RAND.nextInt(31) + 50;
		
		return price;
	}
	
	public Weapon generateGreenWeapon() {
		ArrayList<Weapon> selected_weapons = new ArrayList<Weapon>();
		
		for(Weapon w : weapons) {
			if(w.quality.equals(QUALITY_GREEN)) {
				selected_weapons.add(w);
			}
		}
		
		int random = RAND.nextInt((selected_weapons.size()));
		
		return selected_weapons.get(random);
	}
	
	public static class Green_weapons {
		
		public static Weapon separater = new Weapon("Separater", 20, QUALITY_GREEN, 100, DAGGER);
		
		public static Weapon striker = new Weapon("Striker", 20, QUALITY_GREEN, 120, SPEAR);
		
		public static Weapon generateRandom() {
			int i = RAND.nextInt(3);
	
			if(i == 0) {
				Weapon beginnerSword = new Weapon("Beginner's sword", lowDamageRange() - 5, QUALITY_GREEN, lowPriceRange(), SWORD);
				
				return beginnerSword;
			}else if(i == 1){
				Weapon brutalCutter = new Weapon("Brutal Cuttter", lowDamageRange(), QUALITY_GREEN, lowPriceRange(), DAGGER);
				
				return brutalCutter;
			}else{
				Weapon spike = new Weapon("Spike", lowDamageRange() + 5, QUALITY_GREEN, lowPriceRange(), SPEAR);
				
				return spike;
			}
		}
	}
}


