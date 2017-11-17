package storyteller;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Stories {

	private static final Logger log = LoggerFactory.getLogger(Stories.class);

	private static final ArrayList<AesopFable> STORY_COLLECTION = new ArrayList<AesopFable>();
	private static int loki = 0;
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
				new AesopFable("The Fisherman and The Little Fish",
						"<speak>" 
								+ "A poor Fisherman, who lived on the fish he caught, had bad luck one day  and caught nothing but <emphasis level=\"strong\">a very small fry. </emphasis> "
								+ "The Fisherman was about to put it in his basket when the little Fish said: \n" 
								+ "\" <prosody pitch=\\\"high\\\"><prosody volume=\\\"soft\\\"> <say-as interpret-as=\"interjection\"> Please spare me, Mr. Fisherman. </say-as>   I am so small it is not worth while to carry me home.   When I am bigger,  I shall make you a much better meal. </prosody> </prosody>" 
								+ " But the Fisherman quickly put the fish into his basket. <prosody pitch=\"x-low\"> How foolish I should be, </prosody> he said, <prosody pitch=\"x-low\"> to throw you back. </prosody>  <prosody pitch=\"x-low\"> However small you may be, you are better than nothing at all. </prosody>\" " + "</speak>",
															"<speak> <emphasis level=\"moderate\"> A small gain is worth more than a large promise.</emphasis> </speak>"));
        
    } 
    
	public static AesopFable getFable(int id) {
        return STORY_COLLECTION.get(id);
    }

	
	public static AesopFable getFableWithName(String name) {
		
		for (AesopFable fable : STORY_COLLECTION) {
			if (fable.getStoryName().equalsIgnoreCase(name)) {
				loki = STORY_COLLECTION.indexOf(fable);
				return fable;
			}
		}
		return null;
	}
	public static int getFableloc()
	{
		return loki;
	}
	
	public static int getCollectionSize() {
		return STORY_COLLECTION.size();
	}
	
}


