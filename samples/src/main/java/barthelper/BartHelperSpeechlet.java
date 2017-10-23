package barthelper;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import java.util.Map;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.LaunchRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.SpeechletException;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.OutputSpeech;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazon.speech.ui.SsmlOutputSpeech;
import com.amazonaws.util.json.JSONArray;
import com.amazonaws.util.json.JSONException;
import com.amazonaws.util.json.JSONObject;



public class BartHelperSpeechlet implements Speechlet {

	private static final Logger log = LoggerFactory.getLogger(BartHelperSpeechlet.class);

	private static final String URL_PREFIX = "https://api.bart.gov/api/sched.aspx?json=y&";

	private static final String ST_SLOT = "traincenter";

	private static final String API_KEY = "MW9S-E7SL-26DU-VV8V";

	private static final String URL_PREFIX2 = "https://api.bart.gov/api/bsa.aspx?json=y&";

	private static final String API_KEY2 = "MW9S-E7SL-26DU-VV8V";
	
	private static final String URL_PREFIX3= "http://bartjsonapi.elasticbeanstalk.com/api/departures/";
	private static final String ST_KEY = "STATION";


	private static final int MAX_HOLIDAYS = 3;
   

	@Override
	public void onSessionStarted(final SessionStartedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		// any initialization logic goes here
	}

	@Override
	public SpeechletResponse onLaunch(final LaunchRequest request, final Session session) throws SpeechletException {
		log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		return getWelcomeResponse();
	}



	@Override
	public SpeechletResponse onIntent(final IntentRequest request, final Session session) throws SpeechletException {
		log.info("onIntent requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		 Intent intent = request.getIntent();
	      String intentName = (intent != null) ? intent.getName() : null;
	      
	    		
	    	
	       
			 if ("GetHolidaysIntent".equals(intentName)) {
			try {
				return getBARTHolidays(intent);
			} catch (IOException e) {
				log.error("Holidays IO Error");
				e.printStackTrace();
				return getErrorResponse(intent);
			} catch (JSONException e) {
				log.error("Holidays JSON Error");
				e.printStackTrace();
				return getErrorResponse(intent);
			}}
	      
//		} else if ("GetElevatorStatus".equals(intentName)) {
//			try {
//				return getElevatorStatus(intent);
//			} catch (IOException e) {
//				log.error("Elevator IO Error");
//				e.printStackTrace();
//				return getErrorResponse(intent);
//			} catch (JSONException e) {
//				log.error("Elevator JSON Error");
//				e.printStackTrace();
//				return getErrorResponse(intent);
//			}}
			 if ("WhatsMyStationIntent".equals(intentName)) {
		    	    return getStationFromSession(intent, session);
		    	} else if ("MyStationIsIntent".equals(intentName)) {
		    	    return setStationInSession(intent, session);
		    	} else if ("GetTrainTimesIntent".equals(intentName)) {
		    		try {
		    				return GetTrainTimes(intent);
		    			} catch (IOException e) {
		    				log.error("Train Times IO Error");
		    				e.printStackTrace();
		    				return getErrorResponse(intent);
		    			} catch (JSONException e) {
		    				log.error("Train Times JSON Error");
		    				e.printStackTrace();
		    				return getErrorResponse(intent);
		    			}
		    	}

		   
		 
		 else if ("AMAZON.HelpIntent".equals(intentName)) {
			return getHelpResponse(intent);
		} else if ("AMAZON.StopIntent".equals(intentName)) {
			return getStopResponse(intent);
		} else if ("AMAZON.CancelIntent".equals(intentName)) {
			return getCancelResponse(intent);
		} else {
			throw new SpeechletException("Invalid Intent");
		}
		
     

	}
	

	@Override
	public void onSessionEnded(final SessionEndedRequest request, final Session session) throws SpeechletException {
		log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(), session.getSessionId());

		// any session cleanup logic would go here
	}

	/**
	 * Creates a {@code SpeechletResponse} for the GetHolidaysIntent.
	 *
	 * @param intent
	 *            intent for the request
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */

	    
//	    private SpeechletResponse getNameFromSession(final Intent intent, final Session session) {
//	       	    }
	private SpeechletResponse getBARTHolidays(Intent intent) throws IOException, JSONException {

		String command = "holiday";
		String holidayURL = URL_PREFIX + "key=" + API_KEY + "&cmd=" + command;
		log.info("BART Holidays URL: " + holidayURL);

		URL url = new URL(holidayURL);
		Scanner scan = new Scanner(url.openStream());
		String holidayOutput = new String();
		while (scan.hasNext()) {
			holidayOutput += scan.nextLine();
		}
		// scan.close();

		// build a JSON object
		JSONObject output = new JSONObject(holidayOutput);

		// get the results
		JSONObject root = output.getJSONObject("root");

		JSONArray holidays = root.getJSONArray("holidays");

		JSONObject list = holidays.getJSONObject(0);

		JSONArray holidayList = list.getJSONArray("holiday");

		String speechOutput = "The upcoming " + MAX_HOLIDAYS + " holidays are: ";
		for (int i = 0; i < MAX_HOLIDAYS; i++) {
			JSONObject o = (JSONObject) holidayList.get(i);

				if (i == MAX_HOLIDAYS - 1)
					speechOutput = speechOutput + "and " + o.getString("name") + " on " + o.getString("date") + ".";
				else {

					// now we can use the current date , and compare the o.getstring(date) with
					// current date;
					speechOutput = speechOutput + o.getString("name") + " on " + o.getString("date") + ", ";
				}

			}
		

		PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
		outputSpeech.setText(speechOutput);

		SimpleCard card = new SimpleCard();
		card.setTitle("Upcoming BART Holidays");
		card.setContent(speechOutput);

		return SpeechletResponse.newTellResponse(outputSpeech, card);

	}

	/**
	 * Creates a {@code SpeechletResponse} for the HelpIntent.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getHelpResponse(Intent intent) {
		String speechOutput = "With Bart Helper, you can get" + " information about the Bay Area Rapid Transit system."
				+ " For example, you could say what are the upcoming BART holidays?"
				+ " Now, what would you like to know?";

		String repromptText = "What would you like to know?";

		return newAskResponse(speechOutput, false, repromptText, false);
	}

//	private SpeechletResponse getElevatorStatus(Intent intent) throws IOException, JSONException {
//
//		String command = "elev";
//		String elevatorURL = URL_PREFIX2 + "key=" + API_KEY2 + "&cmd=" + command;
//		log.info("BART elevators URL: " + elevatorURL);
//
//		URL url = new URL(elevatorURL);
//		Scanner scan = new Scanner(url.openStream());
//		String ElevOutput = new String();
//		while (scan.hasNext()) {
//			ElevOutput += scan.nextLine();
//		}
//		scan.close();
//
//		// build a JSON object
//		JSONObject output = new JSONObject(ElevOutput);
//
//		// get the results
//		JSONObject boot = output.getJSONObject("root");
//
//		JSONArray elevators = boot.getJSONArray("bsa");
//
//		JSONObject list = elevators.getJSONObject(0);
//
//		JSONArray elevatorList = list.getJSONArray("description");
//
//		String speechOutput = "The elevators that are out: " ;
//		for (int i = 0; i < elevatorList.length(); i++) {
//			JSONObject o = (JSONObject) elevatorList.get(i);
//
//			speechOutput = speechOutput + o.getString("#cdata-section") + ".";
//		}
//
//		PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
//		outputSpeech.setText(speechOutput);
//
//		SimpleCard card = new SimpleCard();
//		card.setTitle("Elevators Out");
//		card.setContent(speechOutput);
//
//		return SpeechletResponse.newTellResponse(outputSpeech, card);
//
//	}

	private SpeechletResponse setStationInSession(final Intent intent, final Session session) {
	    // Get the slots from the intent.
	    Map<String, Slot> slots = intent.getSlots();
	
	    // Get the station slot from the list of slots.
	    Slot myStationSlot = slots.get(ST_SLOT);
	    String speechText, repromptText;
	
	    // Check for station and create output to user.
	    if (myStationSlot != null) {
	        // Store the user's station in the Session and create response.
	        String myStation = myStationSlot.getValue();
	        session.setAttribute(ST_KEY, myStation);
	        speechText =
	                String.format("I now know that your station is %s. You can ask me your "
	                        + "station by saying, what's my station?", myStation);
	        repromptText =
	                "You can ask me your station by saying, what's my station?";
	
	    } else {
	        // Render an error since we don't know what the users favorite color is.
	        speechText = "I'm not sure what your station is, please try again";
	        repromptText =
	                "I'm not sure what your station is. You can tell me your "
	                        + "station by saying, my station is Civic Center";
	    }
	
	    return getSpeechletResponse(speechText, repromptText, true);
	}

	String myStation = "";
	private SpeechletResponse getStationFromSession(final Intent intent, final Session session) {
       String speechText;
       boolean isAskResponse = false;



       // Get the user's station from the session.
        myStation = (String) session.getAttribute(ST_KEY);



       // Check to make sure user's station is set in the session.
       if (StringUtils.isNotEmpty(myStation)) {
           speechText = String.format("Your station is %s. Goodbye.", myStation);
       } else {
           // Since the user's station is not set render an error message.
           speechText =
                   "I'm not sure what your station is. You can say, my station is "
                           + "lafayette";
           isAskResponse = true;
       }



       return getSpeechletResponse(speechText, speechText, isAskResponse);
   }


	private SpeechletResponse GetTrainTimes(Intent intent ) throws IOException, JSONException {

    
    	
   

		 String command ="";
		HashMap<String, String> hash = new HashMap<String, String>();
		 hash.put("12th st. oakland city center", "12th");
	     hash.put("16th st. mission", "16th");
	     hash.put("19th st. mission", "19th");
	     hash.put("24th st. mission", "24th");
	     hash.put("12th street oakland city center", "12th");
	     hash.put("16th street mission", "16th");
	     hash.put("19th street mission", "19th");
	     hash.put("24th street mission", "24th");
	     hash.put("ashby", "ashb");
	     hash.put("balboa park", "balb");
	     hash.put("bay fair", "bayf");
	     hash.put("castro valley", "cast");
	     hash.put("coliseum", "cols");
	     hash.put("colma", "colm");
	     hash.put("concord", "conc");
	     hash.put("daly city", "daly");
	     hash.put("downtown berkeley", "dbrk");
	     hash.put("dublin pleasanton", "dubl");
	     hash.put("el cerrito del norte", "deln");
	     hash.put("del norte", "deln");
	     hash.put("el cerrito plaza", "plza");
	     hash.put("embarcadero", "embr");
	     hash.put("fremont", "frmt");
	     hash.put("fruitvale", "ftvl");
	     hash.put("glen park", "glen");
	     hash.put("hayward", "hayw");
	     hash.put("Lafayette", "lafy");
	     hash.put("lake merritt", "lake");
	     hash.put("macarthur", "mcar");
	     hash.put("millbrae", "mlbr");
	     hash.put("montgomery street", "mont");
	     hash.put("north berkeley", "nbrk");
	     hash.put("north concord martinez", "ncord");
	     hash.put("oakland airport", "oakl");
	     hash.put("orinda", "orin");
	     hash.put("pittsburg bay point", "pitt");
	     hash.put("pleasant hill", "phil");
	     hash.put("powell street", "powl");
	     hash.put("richmond", "rich");
	     hash.put("rockridge", "rock");
	     hash.put("san bruno", "sbrn");
	     hash.put("san francisco airport", "sfia");
	     hash.put("san leandro", "sanl");
	     hash.put("south hayward", "shay");
	     hash.put("south san francisco", "ssan");
	     hash.put("union city", "ucty");
	     hash.put("walnut creek", "wcrk");
	     hash.put("west dublin pleasanton", "wdub");
	     hash.put("west oakland", "woak");
	     hash.put("civic center", "civc");
	     Map<String, Slot> slots = intent.getSlots();

	        // Get the color slot from the list of slots.
	        Slot itemSlot = slots.get(ST_SLOT);

	        // Check for favorite color and create output to user.
	    
	            
	        


	        // Get the user's favorite color from the session.
	        

	      

    
     String itemName = "";
	        if (itemSlot != null && itemSlot.getValue() != null) {
	             itemName = itemSlot.getValue();
	             command = hash.get(itemName);
	             log.info("ItemName " + itemName);
	             log.info("Command " + command);
	             log.info("stat: "+ myStation);
	           

	        }

	         

		String TrainTimesURL = URL_PREFIX3 + command;
		log.info("BART TrainTimes URL: " + TrainTimesURL);

		URL url = new URL(TrainTimesURL);
		Scanner scan = new Scanner(url.openStream());
		String DepOut = new String();
		int a =0;
		while (scan.hasNext()) {
			DepOut += scan.nextLine();
		}

		// build a JSON object
		JSONObject etd = new JSONObject(DepOut);
		
		JSONArray name = etd.getJSONArray("etd");
		


		JSONObject destination = name.getJSONObject(0);


		String speechOutput = " ";
		
		String now = "";
		for(int j=0; j<name.length();j++)
		{
			
			destination = name.getJSONObject(j);
			JSONArray Dlist = destination.getJSONArray("estimate");

		

		for (int i = 0; i < Dlist.length(); i++) {
			
			JSONObject o = (JSONObject) Dlist.get(i);
		


			now = o.getString("minutes");
			if(o.getString("minutes").equals("Leaving")) {
				
			speechOutput = speechOutput + "The train going to " + name.getJSONObject(j).getString("destination") + " leaves now from platform " + o.getString("platform") + ". ";}
			else 
				speechOutput = speechOutput + "The train going to " +name.getJSONObject(j).getString("destination") + " leaves in " + o.getString("minutes") + " minutes at platform " + o.getString("platform") + ". ";
			
		}
		}
		PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
		outputSpeech.setText(speechOutput);

		SimpleCard card = new SimpleCard();
		card.setTitle("departures");
		card.setContent(speechOutput);

		return SpeechletResponse.newTellResponse(outputSpeech, card);

	
	
	}
	
	
	
	// 
	

	/**
	 * Creates a {@code SpeechletResponse} when there is an error of any kind.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getErrorResponse(Intent intent) {

		PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
		outputSpeech.setText(
				"I'm sorry... the BART information system seems to be down right now." + " Please try again later." );

		return SpeechletResponse.newTellResponse(outputSpeech);
		

	}

	/**
	 * Creates a {@code SpeechletResponse} for the CancelIntent.
	 *
	 * @return SpeechletResponse spoken and visual response for the given intent
	 */
	private SpeechletResponse getCancelResponse(Intent intent) {
		PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
		outputSpeech.setText("Goodbye");

		return SpeechletResponse.newTellResponse(outputSpeech);
	}

	private SpeechletResponse getStopResponse(Intent intent) {

		PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
		outputSpeech.setText("Goodbye");

		return SpeechletResponse.newTellResponse(outputSpeech);
	}

	/**
	 * Function to handle the onLaunch skill behavior.
	 * 
	 * @return SpeechletResponse object with voice/card response to return to the
	 *         user
	 */
	private SpeechletResponse getWelcomeResponse() {
		String speechOutput = "Welcome to the BART Helper! What would you like to know?";
		// If the user either does not reply to the welcome message or says something
		// that is not
		// understood, they will be prompted again with this text.
		String repromptText = "With BART Helper, you can get information about the Bay Area Rapid Transit system."
				+ " For example, you could say what are the upcoming BART holidays?" + " Or what elevators are out, or what trains are departing from Civic Center"
				+ " Now, what would you like to know?";

		return newAskResponse(speechOutput, false, repromptText, false);
	}

	/**
	 * Wrapper for creating the Ask response from the input strings.
	 * 
	 * @param stringOutput
	 *            the output to be spoken
	 * @param isOutputSsml
	 *            whether the output text is of type SSML
	 * @param repromptText
	 *            the reprompt for if the user doesn't reply or is misunderstood.
	 * @param isRepromptSsml
	 *            whether the reprompt text is of type SSML
	 * @return SpeechletResponse the speechlet response
	 */
	private SpeechletResponse newAskResponse(String stringOutput, boolean isOutputSsml, String repromptText,
			boolean isRepromptSsml) {
		
		OutputSpeech outputSpeech, repromptOutputSpeech;
		if (isOutputSsml) {
			outputSpeech = new SsmlOutputSpeech();
			((SsmlOutputSpeech) outputSpeech).setSsml(stringOutput);
		} else {
			outputSpeech = new PlainTextOutputSpeech();
			((PlainTextOutputSpeech) outputSpeech).setText(stringOutput);
		}

		if (isRepromptSsml) {
			repromptOutputSpeech = new SsmlOutputSpeech();
			((SsmlOutputSpeech) repromptOutputSpeech).setSsml(repromptText);
		} else {
			repromptOutputSpeech = new PlainTextOutputSpeech();
			((PlainTextOutputSpeech) repromptOutputSpeech).setText(repromptText);
		}
		Reprompt reprompt = new Reprompt();
		reprompt.setOutputSpeech(repromptOutputSpeech);
		return SpeechletResponse.newAskResponse(outputSpeech, reprompt);


		}
    private SpeechletResponse getSpeechletResponse(String speechText, String repromptText,
            boolean isAskResponse) {
        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Session");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        if (isAskResponse) {
            // Create reprompt
            PlainTextOutputSpeech repromptSpeech = new PlainTextOutputSpeech();
            repromptSpeech.setText(repromptText);
            Reprompt reprompt = new Reprompt();
            reprompt.setOutputSpeech(repromptSpeech);

            return SpeechletResponse.newAskResponse(speech, reprompt, card);

        } else {
            return SpeechletResponse.newTellResponse(speech, card);
        }
    }
}




