
package ChooseAdventure;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import storyteller.AesopFable;




public final class Stories {

	private static final Logger log = LoggerFactory.getLogger(Stories.class);

	private static int loki = 0;
	private static final ArrayList<Adventures> ADVENTURES_COLLECTION = new ArrayList<Adventures>();
	
    public Stories() {
    }

   
public static Adventures getName(String name) {
		
		for (Adventures adse : ADVENTURES_COLLECTION) {
			if (adse.getStoryName().equalsIgnoreCase(name)) {
				loki = ADVENTURES_COLLECTION.indexOf(adse);
				return adse;
			}
		}
		return null;
	}

    static {
    	ArrayList<Scenario> scenarios = new ArrayList<Scenario>();
 
//<<<<<<< HEAD
    	
    	//Very first choice
    	scenarios.add(new Scenario("Follow" , "My Way" ,  
    			
    			//If user says Follow (Choice A)
    			"You set the rocket to autopilot, heading towards the coordinates. It takes a few days, "
    			+" but eventually you reach the coordinates and see that your boss’s guess was right: "
    			+ "there is a block hole here. You decide to play it safe before jumping into the hole, "
    			+"but you’re not sure what to do first. Should you see if there’s any planets or life nearby?" 
    			+ "The choice is yours, commander:" + 
    			"	To scan the black hole for life, say Scan." + 
    			"	To orbit the black hole and search for nearby planets, say Search", 
    			
    			//If user says My Way (Choice B)
    			"You stick with your gut and turn around the ship, piloting it into the abyss. "
    			+"Your boss calls you, furious, ordering you to turn around, but you ignore him, "
    			+"following your instinct. You’re the one in space anyway- what say does he have? "
    			+ "As you fly into the unknown, your ship suddenly loses power. You stand up, "
    			+"heading to fix the power, but one monitor flickers on, displaying a message in a "
    			+"language you’ve never seen before. Your curiosity spikes- you want to translate the message, "
    			+"but you know you need to fix the power before your oxygen runs out…”" + 
    			"The choice is yours, commander:" + 
    			"	To translate the message, say “Translate”" + 
    			"	To fix the power, say “Fix"
    			));
    	
    	//if Choice A is selected
    	scenarios.add(new Scenario("Scan ", "Search", 
    			
    			//If user says Scan (Choice Aa)
    			"You scan the black hole. The results come up negative- "
    			+"there is no life in the hole. As soon as you get the results, "
    			+"the power goes out and you see a message saying that the engine has lost power. "
    			+"Your gut begins to sink as you feel the ship being pulled into black hole."
    			+"Without the engines, you will surely die, but you wonder what caused the engines to fail…" + 
    			"The choice is yours, commander:" + 
    			"	To fix the engines, say Fix" + 
    			"	To figure out why the engines lost power, say Engines", 
    			
    			//If user says Search (Choice Ab)
    			"You orbit the black hole, looking for nearby planets. After a few hours, "
    			+"you see a big purple sphere in the distance- could it be a new planet? "
    			+"You steer towards the object and realize that you have found a new planet! "
    			+"As you record the planet’s coordinates in your log, you begin to wonder- "
    			+"could there be life forms on this planet? Could there be more planets out there?" + 
    			"The choice is yours, commander:" + 
    			"	To scan the planet for life, say Scan" + 
    			"	To search for more planets, say Search"
    			));
   
    	//if Choice B is selected
    	scenarios.add(new Scenario("Translate", "Fix",  
    			
    			//If user says Translate (Choice Ba)
    			"You decide to translate the message- contact with extraterrestrial life "
    			+"is far more important than the ship’s power. It’s takes some time, "
    			+"but eventually you think you’ve got some of the message. You can make out two words: "
    			+"AIM and DANGER. Your gut sinks- are the aliens hostile? Are they threatening you, "
    			+"or are they warning you? You realize you have two options: fix the power and escape from the aliens, "
    			+"or find out who sent the message and let them know you only want peace." + 
    			"The choice is yours, commander:" + 
    			"	To fix the power and escape, say Fix" + 
    			"	To send a peace letter to the aliens, say Letter", 
    			
    			//If user says Fix (Choice Bb)
    			"You decide to ignore the message and fix the power. It takes some time, "
    			+"but eventually the lights turn back on and you feel the engine shaking the ship. "
    			+"However, seconds after you fix the power, the ship’s alarm goes off. You’re under attack! "
    			+"You begin to panic- do you try and find the threat or use an escape pod to head back to Earth?" + 
    			"The choice is yours, commander:" + 
    			"	To find the threat, say Find" + 
    			"	To leave in the escape pod, say Leave"
    			));
    	
    	//if Choice Aa is selected
    	scenarios.add(new Scenario("Fix", "Engines", 
    			
    			//if Fix is selected (Ending Aaa)
    			"You successfully fix the engines, but it's too late: while you were working,  "
    			+"your ship was pulled into the black hole. You quickly realize there is no"
    			+" escape as you sink deeper into nothingness."
    			+ "GAME OVER. Thank you for playing!", 
    			
    			//if Engines is selected (Ending Aab)
    			"Just as you start to invesitagte the power loss, you see a bright light coming through the windows. " + 
    			"Just as quickly, a loud message begins to play: Do not be alarmed human, the voice says, "
    			+"we know you seek the answers behind the black holes, we are docking your ship now. "
    			+"Come aboard, and we will give you shelter and share our knowledge. "
    			+"Congratulations, commander! You've made contact with a friendly alien species and are learning their knowledge." + 
    			"The mission was an amazing success! "
    			+"GAME OVER. Thank you for playing!"
    			));
    	
    	//if choice Ab is selected
    	scenarios.add(new Scenario("Scan", "Search",  
    			
    			//If Scan is selected (Ending Aba)
    			"You scan the planet and the scans come back positive! However, you realize the planet is slowly"
    			+"sinking into the black hole- you have to help them before the aliens are consumed by the black hole! "
    			+"You quickly land on the planet, open your doors, and let in every living alien in sight. "
    			+"Just before the planet is absorbed into the black hole, you launch back into space." + 
    			"Congratulations, commander! You discovered and saved an alien species! In honor of your heroicness, "
    			+"there is a statue of you on Earth and on the new aliens’ planet!" + 
    			"GAME OVER. Thank you for playing!", 
    			
    			//If search is selected (Ending Abb)
    			"You fail to find any planets. You turn around to go back to investigate the one you found earlier, "
    			+"but when you get there, the planet has vanished. You begin to think...could it have been swallowed by the black hole?"
    			+"Finding nothing, you steer away from the black hole, going back out into space to wait for the next orders."
    			+ "	GAME OVER. Thank you for playing!"  
    			));
    	
    	//if Choice Ba is selected
    	scenarios.add(new Scenario("Fix", "Letter",  
    			
    			//If Fix is selected (Ending Baa)
    			"outcome", 
    			
    			//If Letter is selected (Ending Bab)
    			"outcome"));
    	
    	//if Choice Bb is selected
    	scenarios.add(new Scenario("Find", "Leave",  
    			
    			//if Find is selected (ending Bba)
    			"outcome", 
    			
    			//if Leave is selected (ending Bbb)
    			"outcome"));
    	
/*
    	scenarios.add(new Scenario("If you would like to follow the coordinates, simply say “Follow”\n" , "If you wish to follow your gut and go another way, say “My Way”\n" ,  "outcome" , "outcmoe"));
    	
    	scenarios.add(new Scenario("option 1", "option 2",  "outcome",  "outcome"));
    	
    	scenarios.add(new Scenario("option 1", "option 2",  "outcome",  "outcome"));
    	
    	scenarios.add(new Scenario("option 1", "option 2",  "outcome",  "outcome"));
*/
    	
    	Adventures a = new Adventures("Ajaz's Adventure" , scenarios );
    	
    	ADVENTURES_COLLECTION.add(a);
    }	
     
	
}



