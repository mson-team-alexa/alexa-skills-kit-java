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
        		new AesopFable("The Wolf and The Crane",
        				"<speak>" + "A Wolf had been feasting too greedily, and a bone had stuck crosswise in his throat. He could get it neither up nor down, and of course he could not eat a thing. Naturally that was an awful state of affairs for a greedy Wolf.\n" + 
        				"\n" + 
        				"So away he hurried to the Crane. He was sure that she, with her long neck and bill, would easily be able to reach the bone and pull it out.\n" + 
        				"\n" + 
        				"\"<prosody volume=\"loud\" pitch=\"x-low\">I will reward you very handsomely</prosody>, said the Wolf, \"<prosody volume=\"loud\" pitch=\"x-low\">if you pull that bone out for me.</prosody>" + 
        				"\n" + 
        				"The Crane, as you can imagine, was very uneasy about putting her head in a Wolf's throat. But she was grasping in nature, so she did what the Wolf asked her to do.\n" + 
        				"\n" + 
        				"When the Wolf felt that the bone was gone, he started to walk away.\n" + 
        				"\n" + 
        				"\"<prosody pitch = \"high\" volume=\"loud\">But what about my reward!</prosody>\" called the Crane anxiously." +
        				"\n" + 
        				"\"<prosody volume=\"loud\" pitch=\"x-low\">What!</prosody> snarled the Wolf, whirling around \"<prosody volume=\"medium\" pitch=\"x-low\" rate=\"slow\">Haven't you got it? Isn't it enough that I let you take your head out of my mouth without snapping it off?</prosody>\"\n" + 
        				"\n" + 
        				"</speak>", "<speak> <emphasis level=\"moderate\">Expect no reward for serving the wicked. </emphasis></speak>"));
        		
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


