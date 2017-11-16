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
        		new AesopFable("The Frogs and the Ox",
        				"<speak><prosody rate=\"slow\">" + "An Ox came down to a reedy pool to drink. As he splashed heavily into the water, he crushed a young Frog into the mud. "
        						+ "The old Frog soon missed the little one and asked his brothers and sisters what had become of him."
        						+ "<prosody pitch=\"high\" rate=\"slow\">A great big monster</prosody>, said one of them, <prosody pitch=\"high\" rate=\"slow\">stepped on little brother with one of his huge feet!</prosody>"
        						+ "<prosody pitch=\"low\" rate=\"slow\">Big, was he?</prosody> said the old Frog, puffing herself up. <prosody pitch=\"low\" rate=\"slow\">Was he as big as this?</prosody>" 
        						+ "<prosody pitch=\"high\" rate=\"slow\">Oh, much bigger!</prosody> they cried. " 
        						+ "The Frog puffed up still more. <prosody pitch=\"low\" rate=\"slow\">He could not have been bigger than this!</prosody> she said. But the little Frogs all declared that the monster was much, "
        						+ "much bigger. and the old Frog kept puffing herself out more and more until, all at once, she burst."
        						+ "</prosody></speak>", "<speak><emphasis level=\"moderate\">" + "Do not attempt the impossible." + "</emphasis></speak>"));
        
       STORY_COLLECTION.add(
       		new AesopFable("The Hare and the Tortoise", 
       				"<speak><prosody rate=\"slow\">" + "A Hare was making fun of the Tortoise one day for being so slow."
       						+ "<audio src=\"https://s3.amazonaws.com/jacks-alexa-sounds/hare_voice.mp3\"/>  he asked with a mocking laugh."
       						+ "<audio src=\"https://s3.amazonaws.com/jacks-alexa-sounds/yes_turtle.mp3\"/> replied the Tortoise, <audio src=\"https://s3.amazonaws.com/jacks-alexa-sounds/turtle_audio_pt2.mp3\"/>"
       						+ "The Hare was much amused at the idea of running a race with the Tortoise, but for the fun of the thing he agreed. "
       						+ "So the Fox, who had consented to act as judge, marked the distance and started the runners off. "
       						+ "The Hare was soon far out of sight, and to make the Tortoise feel very deeply how ridiculous it was for him to try a race with a Hare, he lay down beside the course to take a nap until the Tortoise should catch up. "
       						+ "The Tortoise meanwhile kept going slowly but steadily, and, after a time, passed the place where the Hare was sleeping. "
       						+ "But the Hare slept on very peacefully; and when at last he did wake up, the Tortoise was near the goal. "
       						+ "The Hare now ran his swiftest, but he could not overtake the Tortoise in time. "
       						+ "</prosody></speak>", "<speak><emphasis level=\"moderate\">" + "The race is not always to the swift." + "</emphasis></speak>"));
       		
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


