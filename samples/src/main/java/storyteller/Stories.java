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
        		new AesopFable("Belling the Cat", "<speak>" + "The Mice once called a meeting to decide on a plan to free themselves of their enemy, the Cat."
        		+ "At least they wished to find some way of knowing when she was coming, so they might have time to run away. Indeed, something had to be done, "
        		+ "for they lived in such constant fear of her claws that they hardly dared stir from their dens by night or day."

        		+"Many plans were discussed, but none of them was thought good enough. At last a very young Mouse got up and said:"

        		+"\"I have a plan that seems very simple, but I know it will be successful."

        		+"All we have to do is to hang a bell about the Cat's neck. When we hear the bell ringing we will know immediately that our enemy is coming.\""

        		+"All the Mice were much surprised that they had not thought of such a plan before. But in the midst of the rejoicing over their good fortune, "
        		+"an old Mouse arose and said:"

        		+"\"I will say that the plan of the young Mouse is very good. But let me ask one question: Who will bell the Cat?\"</speak>",

        		"<speak><emphasis level=\"moderate\">It is one thing to say that something should be done, but quite a different matter to do it.</emphasis></speak>"));
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


