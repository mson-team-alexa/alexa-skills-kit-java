package AbsurdRPG.CustomClasses.NPCs;

import java.util.Random;

import AbsurdRPG.CustomClasses.Location.Location;

public class NPC {

	public String name;
	
	public int damage;
	
	public Location location;
	
	public boolean friendly;
	
	private boolean marriable;
	private Integer enemy_level;
	
	public int intimacy_level;
	
	public NPC(boolean whether_friendly, int boss_level, Location lc) {
		marriable = false;
		
		if(whether_friendly) {
			friendly = true;
		}else {
			friendly = false;
			
			if(boss_level == 1) {
				damage = generateDamageInRange(10, 20);
				
				enemy_level = generateDamageInRange(5, 10);
			}else if(boss_level == 2){
				damage = generateDamageInRange(20, 30);
				
				enemy_level = generateDamageInRange(10, 20);
			}else if(boss_level == 3) {
				damage = generateDamageInRange(40, 60);
				
				enemy_level = boss_level;
				
				enemy_level = generateDamageInRange(20, 30);
			}else if(boss_level == 4) {
				damage = generateDamageInRange(80, 100);
				
				enemy_level = boss_level;
				
				enemy_level = generateDamageInRange(30, 40);
			}else if(boss_level == 5) {
				damage = 120;
				
				enemy_level = 50;
			}else {
				damage = 200;
				
				enemy_level = 100;
			}
		}
	}
	
	public Integer getLevel() {
		return enemy_level;
	}
	
	public void set_marriable() {
		marriable = true;
	}
	
	public void setIdentity(String N) {
		name = N;
	}
	
	public boolean is_marriable() {
		return marriable;
	}
	
	private Integer generateDamageInRange(int A, int B) {
		Integer C;
		
		Random RAND = new Random();
		
		C = RAND.nextInt(B-A) + A;
		
		return C;
	}
	
}
