package AbsurdRPG.CustomClasses.Inventory;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Weapon{

	private static final Logger log = LoggerFactory.getLogger(Weapon.class);

	public String quality;
	
	public Integer damage;
	
    public String name;
    
    public Integer price;
    
    public String catogory;
    
    public Weapon(String weapon_name, Integer weapon_damage, String weapon_quality, Integer weapon_price, String weapon_catogory) {
    	name = weapon_name;
    	
    	damage = weapon_damage;
    	
    	quality = weapon_quality;
    	
    	price = weapon_price;
    	
    	catogory = weapon_catogory;
    }
}


