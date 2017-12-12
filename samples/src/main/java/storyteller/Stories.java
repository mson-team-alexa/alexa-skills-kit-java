package storyteller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Stories {

	private static final Logger log = LoggerFactory.getLogger(Stories.class);

	private static final ArrayList<AesopFable> STORY_COLLECTION = new ArrayList<AesopFable>();

    private Stories() {
    }

    static {
        
        STORY_COLLECTION.add(
				new AesopFable("The Gnat and the Bull", 
        				"<speak>" + "A Gnat flew over the meadow with much buzzing for so small a creature and settled on the tip of one "
        						+ "of the horns of a Bull. After he had rested a short time, he made ready to fly away. But before he left "
        						+ "he begged the Bull's pardon for having used his horn for a resting place. "
        						+ "<emphasis level=\"reduced\">You must be very glad to have me go now </emphasis>"
        						+ "he said. "
        						+ "<emphasis level=\"reduced\">It's all the same to me </emphasis>"
        						+ "replied the Bull. "
        						+ "<emphasis level=\"reduced\">I did not even know you were there.</emphasis> </speak>", 
        				"<speak> <emphasis level=\"moderate\">We are often of greater importance in our own eyes than in the eyes of our neighbor. "
        				+ "The smaller the mind, the greater the conceit.</emphasis> </speak>"));
        STORY_COLLECTION.add(
				new AesopFable("The Ant and the Dove", 
        				"<speak>" + "<emphasis level=\"moderate\"> A Dove saw an Ant fall into a brook. The Ant struggled in vain to reach the bank, "
        						+ "and in pity, the Dove dropped a blade of straw close beside it. Clinging to the straw "
        						+ "like a shipwrecked sailor to a broken spar, the Ant floated safely to shore. "
        						+ "Soon after, the Ant saw a man getting ready to kill the Dove with a stone. "
        						+ "But just as he cast the stone, </emphasis> the Ant stung him in the heel, "
        						+ "so that the pain made him miss his aim, and the startled Dove flew to safety in a distant wood. </speak>" , 
        				"<speak> <emphasis level=\"moderate\">" + "A kindness is never wasted." + "</emphasis> </speak>"));
        
        STORY_COLLECTION.add(
        		new AesopFable("The Stag and His Reflection",
        				"<speak> <emphasis level = \"moderate\">  " 
        				+ "A Stag, drinking from a crystal spring, saw himself mirrored in the clear water. " 
        				+ "He greatly admired the graceful arch  of his antlers, but he was very much ashamed of his spindling legs. "
        				+ "<prosody pitch =\"-33.3%\"> "+ "How can it be, " + "</prosody>"
        				+  " he sighed, "
        				+ "<prosody pitch =\"-33.3%\"> "+ " that I should be cursed with such legs when I have so magnificent a crown. " + "</prosody>"
        				+ "At that moment he scented a panther, and in an instant was bounding away through the forest. "
        				+ "But as he ran, his wide-spreading antlers caught in the branches of the trees, and soon the Panther overtook him. "
        				+ "Then the Stag perceived that the legs of which he was so ashamed would have saved him, "
        				+ "had it not been for the useless ornaments on his head. </emphasis> </speak> ",
        			"<speak> <emphasis level=\"strong\"> " + "We often make much of the ornamental and despise the useful." 
        	+ " </emphasis> </speak>"	));
    } 
    
	public static AesopFable getFable(int id) {
        return STORY_COLLECTION.get(id);
    }
	
	public static AesopFable getFableWithName(String name) {
		
		for (AesopFable fable : STORY_COLLECTION) {
			if (fable.getStoryName().equalsIgnoreCase(name)) {
				return fable;
			}
		}
		return null;
	}
	
	public static int getCollectionSize() {
		return STORY_COLLECTION.size();
	}
	
}


