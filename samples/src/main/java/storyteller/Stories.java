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
        				"A Gnat flew over the meadow with much buzzing for so small a creature and settled on the tip of one "
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
        		new AesopFable("The Miser",
        				"<p><prosody volume=\"soft\">A Miser had buried his gold in a <amazon:effect name=\"whispered\"><prosody rate=\"slow\">secret place</prosody></amazon:effect> in his garden.</prosody> " +
        							"<emphasis level=\"strong\">Every day</emphasis> he went to the spot, dug up the treasure and counted it <prosody rate=\"x-slow\">piece by piece</prosody> to make sure <emphasis level=\"moderate\">it was all there</emphasis>.</p> " +
        							"He made <emphasis level=\"moderate\">so many trips</emphasis> that a Thief,<break time=\".5s\"/>  who had been observing him,<break time=\".8s\"/> guessed what it was the Miser had hidden," + 
        							"and one night, <amazon:effect name=\"whispered\">quietly dug up the treasure and made off with it.</amazon:effect> " +
        							"When the Miser discovered his loss, he was overcome with <prosody rate=\"slow\">grief and despair.</prosody> " + 
        							"<emphasis level=\"strong\">He groaned and cried and tore his hair</emphasis>. " +
        							"A passerby heard his cries and asked what had happened. " + 
        							
        							//Miser							
        							"<prosody volume=\"loud\"><emphasis level=\"strong\">My gold! O my gold!</emphasis></prosody> " + 
        							"Cried the Miser,<prosody volume=\"loud\"><emphasis level=\"moderate\"> wildly</emphasis></prosody>, " +
        							"<prosody volume=\"x-loud\"><prosody rate=\"medium\">Someone has robbed me</prosody></prosody>! " +				
        							//Miser
        							
        							//Passby						
        							"<prosody volume=\"x-loud\">Your gold</prosody>! <prosody volume=\"loud\">There in that hole? Why did you put it there?</prosody>" +
        							"Why did you not keep it in the house where you could easily get it when you had to buy things?" +
        							//Passby
        							
        							//Miser
        							"<prosody volume=\"loud\"><emphasis level=\"moderate\">Buy!</emphasis></prosody>" +
        							"Screamed the Miser angrily. " +
        							"Why, I never touched the gold. <emphasis level=\"reduced\">I couldn't think of spending any of it</emphasis>. " +
        							//Miser
        							
        							"<p>The stranger picked up a large stone and<prosody rate=\"fast\"> threw it into the hole.</prosody></p> " +
        							"If that is the case, " +
        							"he said," +
        							"cover up that stone.<break time=\".5s\"/> <prosody volume=\"loud\">It is worth just as much to you as the treasure you lost</prosody>!" +	
        							"</speak>",
        							
        							//Moral
        							"<speak><emphasis level=\"moderate\">A possession is worth no more than the use we make of it</emphasis>. </speak>"
        							)
        		);
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


