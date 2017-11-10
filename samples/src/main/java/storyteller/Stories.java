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
				new AesopFable("The Ass and his Driver", 
        				"<speak>" + "An Ass was being driven along a road "
        						+ "leading down the mountain side, "
        						+ "when he suddenly took it into his silly head to "
        						+ "choose his own path. He could see his stall "
        						+ "at the foot of the mountain, and to him the "
        						+ "quickest way down seemed to be over the edge "
        						+ "of the nearest cliff. Just as he was about to "
        						+ "leap over, his master caught him by the tail "
        						+ "and tried to pull him back, but the stubborn "
        						+ "Ass would not yield and pulled with all his might." 
        						+ "\"Very well,\" said his master, \"go your way,"
        						+ " you willful beast, and see where it leads you.\"" 
        						+ "With that he let go, and the foolish Ass tumbled "
        						+ "head over heels down the mountain side. </speak>",
        						"<speak> They who will not listen to reason but stubbornly "
        						+ "go their own way against the friendly advice of those who "
        						+ "are wiser than they, are on the road to misfortune. </speak>"));
        
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


