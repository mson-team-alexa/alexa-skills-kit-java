/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package fastmath;

import java.util.*;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;

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

/**
 * This sample shows how to create a simple speechlet for handling intent requests and managing
 * session interactions.
 */
public class FastMathSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(FastMathSpeechlet.class);

    private static final String STAGE_ID = "StageID";
    
    //Main Stage
    private static final int ASK_MODE_STAGE = 0;
    private static final int ASK_ANSWER_STAGE = 1;
    private static final int CONFIRM_STAGE = 2;
    
    //Survival Stages
    private static final String HAVE_ANSWER_ID = "HaveAnswerID";
    private static final int HAVE_ANSWER = 0;
    private static final int ASK_QUESTION = 1;
    
    //Practice Stages
    private static final String ASK_LEVEL_ID = "AskLevelID";
    private static final String LEVEL_ID = "CurrentLevelID";
    private static final int ASK_LEVEL_STAGE = 0;
    private static final int ANSWER_QUESTION_STAGE = 2;
    private static final int GENERATE_QUESTION_STAGE = 1;
    
    private static final String MODE_ID = "ModeID";
    private static final String SURVIVAL_MODE = "survival";
    private static final String PRACTICE_MODE = "practice";
    private static final String TIME_TRIALL_MODE = "timetrial";
    
    private static final int CORRECT_ANSWER_TO_BEAT_LEVEL = 5;
    
    private static final String ANSWERS_CORRECT_ID = "AnswersCorrectID";
    
    private static final String ANSWERS_WRONG_ID = "AnswersWrongID";
    
    private static final String CURRENT_QUESTION_ID = "CurrentQuestionID";
    
    private static final String CURRENT_LEVEL_ID = "CurrentLevelID";
    
    private static final String ASK_QUESTION_TIME_ID = "AskQuestionID";
    
    private static final String ANSWER_QUESTION_TIME_ID = "AnswerQuestionID";
    
    private static final int MAX_ANSWERS_WRONG = 5;
    
    private static Random RAND = new Random();

	private Object _;
 
    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        
        session.setAttribute(STAGE_ID, ASK_MODE_STAGE);
        
        return getWelcomeResponse();
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        // Get intent from the request object.
        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        // Note: If the session is started with an intent, no welcome message will be rendered;
        // rather, the intent specific response will be returned.
        if ("AnswerModeIntent".equals(intentName)) {
            return handleAnswerModeResponse(intent, session);
        } else if ("GiveAnswerIntent".equals(intentName)) {
        	
        	if(session.getAttributes().containsKey(MODE_ID)) {
        		if(((String)session.getAttribute(MODE_ID)).equals(SURVIVAL_MODE)) {
        			return setUpSurvivalStage(intent, session, false);
        		}else if(((String)session.getAttribute(MODE_ID)).equals(PRACTICE_MODE)) {
        			return setUpPracticeStage(intent, session, false, false);
        		}else {
        			return getHelp();
        		}
        	}else {
        		return getHelp();
        	}
            
        } else if ("ContinueIntent".equals(intentName)) {
        	
        	if(session.getAttributes().containsKey(STAGE_ID)) {
            	if((Integer)session.getAttribute(STAGE_ID) == CONFIRM_STAGE){
            		return handleContinueStageResponse(intent,  session);
            	}else if((Integer)session.getAttribute(STAGE_ID) == ASK_MODE_STAGE){
            		return getHelp();
            	}else {
            		if(session.getAttributes().containsKey(MODE_ID)){
            			if(((String)session.getAttribute(MODE_ID)).equals(SURVIVAL_MODE)) {
            				return setUpSurvivalStage(intent, session, true);
            			}else {
            				return getHelp();
            			}
            		}else {
            			return getHelp();
            		}
            	}
        	}else {
        		return getHelp();
        	}
        } else if("DoNotKnowIntent".equals(intentName)) { 
        	if(session.getAttributes().containsKey(STAGE_ID)) {
        		if((Integer)session.getAttribute(STAGE_ID) == ASK_ANSWER_STAGE) {
        			if(session.getAttributes().containsKey(MODE_ID)) {
        				if(((String)session.getAttribute(MODE_ID)).equals(PRACTICE_MODE)) {
        					return setUpPracticeStage(intent, session, false, true);
        				}else if(session.getAttribute(MODE_ID) == SURVIVAL_MODE) {
        					return getHelp(); // Add features for survival mode
        				}
        				else {
        					return getHelp();
        				}
        			}else {
        				return getHelp();
        			}
        		}else {
        			return getHelp();
        		}
        	}else {
        		return getHelp();
        	}
        } else if("AnswerLevelIntent".equals(intentName)) {
        	if(session.getAttributes().containsKey(STAGE_ID)) {
        		if((Integer)session.getAttribute(STAGE_ID) == ASK_ANSWER_STAGE) {
        			if(session.getAttributes().containsKey(MODE_ID)) {
        				if(((String)session.getAttribute(MODE_ID)).equals(PRACTICE_MODE)) {
        					return setUpPracticeStage(intent, session, true, false);
        				}else {
        					return getHelp();
        				}
        			}else {
        				return getHelp();
        			}
        		}else {
        			return getHelp();
        		}
        	}else {
        		return getHelp();
        	}
        }
        	else if ("CancelIntent".equals(intentName)) {
        	return handleCancelStageResponse(intent, session);
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
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
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }

    private void initializeAllComponents(Session session, boolean resetMode) {
    	
    	if(resetMode) {
    		if(session.getAttributes().containsKey(MODE_ID)) {
    			session.setAttribute(MODE_ID, null);
    		}
    	}
    	
    	if(session.getAttributes().containsKey(ASK_LEVEL_ID)){
    		session.setAttribute(ASK_LEVEL_ID, ASK_LEVEL_STAGE);
    	}
    	
    	if(session.getAttributes().containsKey(HAVE_ANSWER_ID)) {
    		session.setAttribute(HAVE_ANSWER_ID, ASK_QUESTION);
    	}
    	
    	if(session.getAttributes().containsKey(ANSWERS_WRONG_ID)) {
    		session.setAttribute(ANSWERS_WRONG_ID, 0);
    	}
		
    	if(session.getAttributes().containsKey(ANSWERS_CORRECT_ID)) {
    		session.setAttribute(ANSWERS_CORRECT_ID, 0);
    	}
		
    	if(session.getAttributes().containsKey(CURRENT_QUESTION_ID)) {
    		session.setAttribute(CURRENT_QUESTION_ID, null);
    	}
		
    	if(session.getAttributes().containsKey(CURRENT_LEVEL_ID)) {
    		session.setAttribute(CURRENT_LEVEL_ID, 1);
    	}
		
    	if(session.getAttributes().containsKey(STAGE_ID)) {
    		session.setAttribute(STAGE_ID, ASK_MODE_STAGE);
    	}
    }
    
    private SpeechletResponse handleCancelStageResponse(final Intent intent, final Session session) {
    	
    	String speechText = "";
    	String repromptText = "";
    	
    	if(session.getAttributes().containsKey(STAGE_ID)) {
    		if((Integer)session.getAttribute(STAGE_ID) == CONFIRM_STAGE){
    			speechText = "The mode you asked has been cancelled. Now, what would you like to play?";
    			
    			repromptText = "You can choose from Surival, Practice, and Time Trial modes";
    			
    			initializeAllComponents(session, true);
    		}else if((Integer)session.getAttribute(STAGE_ID) == ASK_MODE_STAGE){
    			speechText = "You have not chosen a mode yet! ";
    			
    			repromptText = "You can choose from Surival, Practice, and Time Trial modes";
    		}else {
    			speechText = "You exited the mode you are playing. What mode would you want to play now? ";
    			
    			repromptText = "You can choose from Surival, Practice, and Time Trial modes";
    			
    			initializeAllComponents(session, true);
    		}
    	}else {
    		speechText = "You have not chosen a mode yet! You can choose from Surival, Practice, and Time Trial modes";
			
			repromptText = "You can choose from Surival, Practice, and Time Trial modes";
			
			initializeAllComponents(session, true);
    	}
    	
    	return getSpeechletResponse(speechText, repromptText, true);
    }
    
    private SpeechletResponse handleContinueStageResponse(final Intent intent, final Session session) {
        // Create the welcome message.
    	
    	String speechText, repromptText;
    	
    	if(session.getAttributes().containsKey(STAGE_ID)) {
    		
    		if((Integer)session.getAttribute(STAGE_ID) != CONFIRM_STAGE) {
    			speechText = "You haven't chosen the stage yet!";
    			repromptText = "Please the mode you want to play. ";
    			
    			return getSpeechletResponse(speechText, repromptText, true);
    		}else {
    			if(((String)session.getAttribute(MODE_ID)).equals(SURVIVAL_MODE)) {
    				session.setAttribute(STAGE_ID, ASK_ANSWER_STAGE);
        			session.setAttribute(HAVE_ANSWER_ID, ASK_QUESTION);
        			return setUpSurvivalStage(intent, session, false);
        			
    			}else if(((String)session.getAttribute(MODE_ID)).equals(PRACTICE_MODE)) {
    				session.setAttribute(STAGE_ID, ASK_ANSWER_STAGE);
    				session.setAttribute(ASK_LEVEL_ID, ASK_LEVEL_STAGE);
    				return setUpPracticeStage(intent, session, false, false);
    			}else {
    				log.info("INVALID???");
    				
    				return null;
    			}
    		}
    	}else {
    		 return getHelp();
    	}
        
    }
    
    
    private SpeechletResponse getWelcomeResponse() {
        // Create the welcome message.
        String speechText =
                "Welcome to Fast Math Game! You can choose to play in three different modes:" +
                "Practice, Survival or Time Trial. Now, which one would you like to play?";
        String repromptText =
                "Please tell me the game mode that you would like to try.";

        return getSpeechletResponse(speechText, repromptText, true);
    }

    private SpeechletResponse handleAnswerModeResponse(final Intent intent, final Session session) {
    	
    	String speechText = "";
    	
    	String repromptText = "";

    	if(session.getAttributes().containsKey(STAGE_ID)) {
    		if((Integer)session.getAttribute(STAGE_ID) != ASK_MODE_STAGE) {
    			speechText = "You do not have to choose mode right now!";
    		}
    	}
    	
    	Slot GamemodeSlot = intent.getSlot("GameMode");
    	
    	if(GamemodeSlot != null && GamemodeSlot.getValue() != null && speechText == "") {
    		
    		log.info("NOT NULL");
    		
    		String modeSlot = GamemodeSlot.getValue();

    		if(modeSlot.toLowerCase().equals(SURVIVAL_MODE)) {
    			speechText = "Welcome to Survival Mode! Here you will be challenged with questions according to the level you are in. " +
    						"If you get five answers correct at your current level, you will advance to next level. " +
    						"For each question, you have at most eight seconds to answer. " +
    						"If you spend more than eight seconds, the question will be counted wrong. " +
    						"You can have at most 5 questions wrong. " + 
    						"Ready to start the game? Say Begin or Continue to proceed. ";
    			
    			repromptText = "Ready to start the game? Say Begin or Continue to go ahead. ";
    			
    			session.setAttribute(MODE_ID,  SURVIVAL_MODE);
    			
    			session.setAttribute(STAGE_ID, CONFIRM_STAGE);
    			
    		}else if(modeSlot.toLowerCase().equals(PRACTICE_MODE)) {
    			speechText = "Welcome to Practice Mode! In this mode, you can choose to practice math questions whatever the level you wanna be in. " +
    						 "You will not be given a time limit or wrong answers penalty. Have Fun! " +
						"Ready to start the game? Say Begin or Continue to proceed. ";
			
    			repromptText = "Ready to start the game? Say Begin or Continue to go ahead. ";
			
    			session.setAttribute(MODE_ID,  PRACTICE_MODE);
			
    			session.setAttribute(STAGE_ID, CONFIRM_STAGE);
    		}
    		return getSpeechletResponse(speechText, repromptText, true);
    		
    	}else if(speechText != "") {
    		repromptText = speechText;
    		
    		return getSpeechletResponse(speechText, repromptText, true);
    	}	
    	else {
    		return getHelp();
    	}
    }
    
    private SpeechletResponse getHelp() {
        String speechOutput =
                "You can ask to play Survival, Practice and Time Trial mode. Which mode would you want to play with?";
        String repromptText =
                "You can ask to play Survival, Practice and Time Trial mode. Which mode would you want to play with?";
        return newAskResponse(speechOutput,true, repromptText, false);
    }
    
    private Question generateQuestion(int level) {
    	switch(level) {
    	case 1:
    		int randX = RAND.nextInt(10);
    		
    		int randY = RAND.nextInt(50) + 10;
    		
    		int randZ = RAND.nextInt(1);
    		
    		if(randZ == 0) {
    			
    			int answer = randX + randY;
    			
    			String speech = "What is " + randX + " plus " + randY + " ? ";
    			
    			Question que = new Question(speech, answer);
    			
    			return que;
    		}else {
    			int answer  =  randY - randX;
    			
    			String speech = "What is " + randY + " minus " + randX + " ? ";
    			
    			Question que = new Question(speech, answer);
    			
    			return que;
    		}
    		
    		
    	case 2:
    		
    		randX = RAND.nextInt(10);
    		
    		randY = RAND.nextInt(15);
    		
    		randZ = RAND.nextInt(1);
    		
    		if(randZ == 0) {
    			
    			int answer = randX * randY;
    			
    			String speech = "What is " + randX + " multiply by " + randY + " ? ";
    			
    			Question que = new Question(speech, answer);
    			
    			return que;
    		}else {
    			int product = randX * randY;
    			
    			int answer  =  product / randX;
    			
    			String speech = "What is " + product + " divided by " + randX + " ? ";
    			
    			Question que = new Question(speech, answer);
    			
    			return que;
    		}
    	case 3:
    		return null;
    	default:
    		return null;
    	}
    }
    
    private SpeechletResponse setUpPracticeStage(final Intent intent, final Session session, boolean levelChanged, boolean doNotKnow) {
    	
    	String speechText = "", repromptText = "";
    	
    	if(session.getAttributes().containsKey(STAGE_ID)) {
    		if((Integer)session.getAttribute(STAGE_ID) != ASK_ANSWER_STAGE) {
    			speechText = "You have not chosen the mode yet! Please choose or confirm which mode you want to play first! ";
    			
        		repromptText = "You have not chosen the mode yet! Please choose or confirm which mode you want to play first! ";
    		}
    	}else {
    		speechText = "You have not chosen the mode yet! Please choose or confirm which mode you want to play first! ";
    		
    		repromptText = "You have not chosen the mode yet! Please choose or confirm which mode you want to play first! ";
    	}
    	
    	if((Integer)session.getAttribute(ASK_LEVEL_ID) == ANSWER_QUESTION_STAGE && doNotKnow) {
    		LinkedHashMap LHMQ = (LinkedHashMap)session.getAttribute(CURRENT_QUESTION_ID);
			
			Question que = getQuestionFromLinkedHashMap(LHMQ);
			
			speechText = "It's ok. The question was " + que.getQuestion() +
					"And the answer was " + que.getAnswer() + ". ";
		
			Question queN = generateQuestion((Integer)session.getAttribute(LEVEL_ID));
		
			speechText += "Let's try another one: " + queN.getQuestion();
		
			session.setAttribute(CURRENT_QUESTION_ID, queN);
    	}
    	
    	if((Integer)session.getAttribute(ASK_LEVEL_ID) != GENERATE_QUESTION_STAGE && levelChanged) {
    		
    		Slot levelSlot = intent.getSlot("Level");
			
			if(levelSlot != null && levelSlot.getValue() != null) {
				
				int levelAsked = Integer.parseInt(levelSlot.getValue());
				
				if(levelAsked == 1 || levelAsked == 2 || levelAsked == 3 || levelAsked == 4 || levelAsked == 5) {
					Question que = generateQuestion(levelAsked);
					
					session.setAttribute(LEVEL_ID, levelAsked);
					
					session.setAttribute(CURRENT_QUESTION_ID, que);
					
					speechText = "You have changed your level! Your first question chosen for level " + levelAsked + " is : " + que.getQuestion();
					
					repromptText = "Would you like to hear the question again? Your question is " + que.getQuestion();
					
					session.setAttribute(ASK_LEVEL_ID, ANSWER_QUESTION_STAGE);
					
				}else {
					speechText = "You did not ask for a correct level! ";
					
					repromptText = "You can ask questions from level one to five. ";
				}
			}else {
				speechText = "You did not ask for a correct level! ";
				
				repromptText = "You can ask questions from level one to five. ";
			}		
    				
    	}
    	
    	if(session.getAttributes().containsKey(ASK_LEVEL_ID) && speechText == "") {
    		if((Integer)session.getAttribute(ASK_LEVEL_ID) == ASK_LEVEL_STAGE) {
    			log.info("INVALID?????");
    			
    			speechText = "You can choose from level one to level 5, which one would you choose? Please answer in Level and number.";

    			repromptText = "You can choose from level one to level 5, which one would you choose?";
    			
    			session.setAttribute(ASK_LEVEL_ID, GENERATE_QUESTION_STAGE);
    			
    		}else if((Integer)session.getAttribute(ASK_LEVEL_ID) == ANSWER_QUESTION_STAGE) {
    			
    			LinkedHashMap LHMQ = (LinkedHashMap)session.getAttribute(CURRENT_QUESTION_ID);
    			
    			Question que = getQuestionFromLinkedHashMap(LHMQ);
    			
    			Slot answerSlot = intent.getSlot("Answer");
    			
    			if(answerSlot != null && answerSlot.getValue() != null) {
    				float answer = Float.parseFloat(answerSlot.getValue());
    				
    				if(que.checkAnswer(answer)) {
    					que = generateQuestion((Integer)session.getAttribute(LEVEL_ID));
    					
    					speechText = "Good job, you get the answer correct! Here is your next question: " + que.getQuestion();
    					
    					session.setAttribute(CURRENT_QUESTION_ID, que);
    					
    					repromptText = "Would you like to listen to the question again? Your question is " + que.getQuestion();
    				}else {
    					speechText = "You got the answer wrong, but it is ok. The question was " + que.getQuestion() +
    								"And the answer was " + que.getAnswer() + ". ";
    					
    					Question queN = generateQuestion((Integer)session.getAttribute(LEVEL_ID));
    					
    					speechText += "Let's try another one: " + queN.getQuestion();
    					
    					session.setAttribute(CURRENT_QUESTION_ID, queN);
    					
    					repromptText = "Would you like to listen to the question again? Your question is " + queN.getQuestion();
    				}
    			}else {
    				speechText = "Your answer is invalid! The question is " + que.getQuestion();
    				
    				repromptText = "Would you like to hear the questiona again? The question is: " + que.getQuestion();
    			}
    		}else {
    			Slot levelSlot = intent.getSlot("Level");
    			
    			if(levelSlot != null && levelSlot.getValue() != null) {
    				int levelAsked = Integer.parseInt(levelSlot.getValue());
    				
    				if(levelAsked == 1 || levelAsked == 2 || levelAsked == 3 || levelAsked == 4 || levelAsked == 5) {
    					Question que = generateQuestion(levelAsked);
    					
    					session.setAttribute(CURRENT_QUESTION_ID, que);
    					
    					session.setAttribute(LEVEL_ID, levelAsked);
    					
    					speechText = "Your first question chosen for level " + levelAsked + " is : " + que.getQuestion();
    					
    					repromptText = "Would you like to listen to the question again? Your question is " + que.getQuestion();
    					
    					session.setAttribute(ASK_LEVEL_ID, ANSWER_QUESTION_STAGE);
    					
    				}else {
    					speechText = "You did not ask for a correct level! ";
    					
    					repromptText = "You can ask questions from level one to five. ";
    				}
    			}else {
    				speechText = "You did not ask for a correct level! ";
					
					repromptText = "You can ask questions from level one to five. ";
    			}
    		}
    	} else {
    		if(speechText != "") {
    			speechText = speechText;
    		}else {
    			return getHelp();
    		}
    	}
    	
    	return getSpeechletResponse(speechText, repromptText, true);
    }
    
    private Question getQuestionFromLinkedHashMap(LinkedHashMap LHMQ) {
    	String question = LHMQ.get("question").toString();
		
		float answerT = Float.parseFloat(LHMQ.get("answer").toString());
		
		Question que = new Question(question, answerT);
		
		return que;
    }
    
    private SpeechletResponse setUpSurvivalStage(final Intent intent, final Session session, boolean exceededTime) {
        // Get the slots from the intent.
    	
    	String speechText, repromptText;
    	
    	speechText = "";
    	
    	if((Integer)session.getAttribute(STAGE_ID) != ASK_ANSWER_STAGE) {
    		speechText = "You have to choose or confirm which mode you want to play before you enter the play mode!" +
    				"There are three choices: Survival, practice and Time Trial. What is your choice? ";
    	}
    	
    	if(session.getAttributes().containsKey(HAVE_ANSWER_ID) && speechText == "") {
    		
    		if((Integer)session.getAttribute(HAVE_ANSWER_ID) == HAVE_ANSWER) {
    			
    			Slot answerSlot = intent.getSlot("Answer");
    			
    			if(answerSlot != null && answerSlot.getValue() != null) {
    				log.info(answerSlot.getValue() + "Answer in Float");
        			
        			float answer = Float.parseFloat(answerSlot.getValue());
        			
        			int level = (Integer)session.getAttribute(CURRENT_LEVEL_ID);
        			
        			if(session.getAttributes().containsKey(ASK_QUESTION_TIME_ID)) {
        				
        				LinkedHashMap LHM = (LinkedHashMap)session.getAttribute(ASK_QUESTION_TIME_ID);
        				
        				LocalDateTime now = LocalDateTime.now();
        				
        				LocalDateTime then, nowC;
        				
        				LocalDate lDT = LocalDate.now(), lDN = LocalDate.now();
        				
        				then = lDT.atTime(Integer.parseInt(LHM.get("hour").toString()), Integer.parseInt(LHM.get("minute").toString()), Integer.parseInt(LHM.get("second").toString()));
        				
        				nowC = lDN.atTime(now.getHour(), now.getMinute(), now.getSecond());
        				
        				Duration timeElapsed = Duration.between(then, nowC);
        		
        				if(timeElapsed.toMillis() < 25000 && !exceededTime) {
        					
        					LinkedHashMap LHMQ = (LinkedHashMap)session.getAttribute(CURRENT_QUESTION_ID);	
        					
        					String question = LHMQ.get("question").toString();
        					
        					float answerT = Float.parseFloat(LHMQ.get("answer").toString());
        					
        					Question que = new Question(question, answerT);
        					
        					if(que.checkAnswer(answer)) {
        						
        						if(session.getAttributes().containsKey(ANSWERS_CORRECT_ID)) {
        							
        							if((Integer)session.getAttribute(ANSWERS_CORRECT_ID) == 4) {
        								
        								if((Integer)session.getAttribute(CURRENT_LEVEL_ID) == 5) {
        									
        									speechText = "Wow! You are amazing! You just beat the hardest mathematic game in the history! " + 
        												"According to whoever's statistics, only 5 percent of our players can get through the game! " + 
        												"Congratulations! You are welcome to try the game again at any time! ";
        									
            								
            								session.setAttribute(HAVE_ANSWER_ID, ASK_QUESTION);
            								
            								session.setAttribute(ANSWERS_WRONG_ID, 0);
            								
            								session.setAttribute(ANSWERS_CORRECT_ID, 0);
            								
            								session.setAttribute(CURRENT_QUESTION_ID, null);
            								
            								session.setAttribute(CURRENT_LEVEL_ID, 1);
            								
        									session.setAttribute(STAGE_ID, ASK_MODE_STAGE);
        								}else {
        									speechText = "Great Job! You have beaten Level " + level + ". " +
        											"Now you will be challenged with questions from " + (level + 1) + ". " + 
        											"Prepare for it! Here is your question: ";
        								
        								session.setAttribute(CURRENT_LEVEL_ID, level + 1);
        								
        								Question queN = generateQuestion((Integer)session.getAttribute(CURRENT_LEVEL_ID));
        								
        								speechText += queN.getQuestion();
        								
        								session.setAttribute(CURRENT_QUESTION_ID, queN);
        								
        								LocalDateTime nowA = LocalDateTime.now();
        								
        			    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
        								}

        							}else {
        								int answersCorrectBefore = (Integer)session.getAttribute(ANSWERS_CORRECT_ID);
        								
        								session.setAttribute(ANSWERS_CORRECT_ID, answersCorrectBefore + 1);
        								
        								speechText = "Congratulations! You got the question correct. Let's continue to the next one.";
        								
        								Question queN = generateQuestion((Integer)session.getAttribute(CURRENT_LEVEL_ID));
        								
        								speechText += queN.getQuestion();
        								
        								session.setAttribute(CURRENT_QUESTION_ID, queN);
        								
        								LocalDateTime nowA = LocalDateTime.now();
        								
        			    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
        							}
        							
        						}else {
        							session.setAttribute(ANSWERS_CORRECT_ID, 1);
        							
        							speechText = "Great first try! Let's continue to the next question.";
        							
        							Question queN = generateQuestion(1);
        							
        							session.setAttribute(CURRENT_QUESTION_ID, queN);
        							
        							speechText += queN.getQuestion();
        							
        							LocalDateTime nowA = LocalDateTime.now();
    								
    			    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
        						}
        						
        					}else {
        						
        						if(session.getAttributes().containsKey(ANSWERS_WRONG_ID)) {
        							int answersWrong = (Integer)session.getAttribute(ANSWERS_WRONG_ID);
        							
        							if(answersWrong == 4) {
        								speechText = "Ah! Sorry, you have used all five chances of wrong answers. Try again!";
        								
        								session.setAttribute(HAVE_ANSWER_ID, ASK_QUESTION);
        								
        								session.setAttribute(ANSWERS_WRONG_ID, 0);
        								
        								session.setAttribute(ANSWERS_CORRECT_ID, 0);
        								
        								session.setAttribute(CURRENT_QUESTION_ID, null);
        								
        								session.setAttribute(CURRENT_LEVEL_ID, 1);
        								
        								session.setAttribute(STAGE_ID, ASK_MODE_STAGE);
        								
        							}else {
        								session.setAttribute(ANSWERS_WRONG_ID, answersWrong + 1);
        								
        								speechText = "Sorry, you got the answer Wrong. " +
        										"The question is " + que.getQuestion() +
        										"And the correct answer is " + que.getAnswer() + ". " +
        										"Better luck next Time! Here is the next question: ";
        								
        								Question queN = generateQuestion((Integer)session.getAttribute(CURRENT_LEVEL_ID));
        								
        								speechText += queN.getQuestion();
            							
            							session.setAttribute(CURRENT_QUESTION_ID, queN);
            							
            							LocalDateTime nowA = LocalDateTime.now();
        								
        			    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
        							}   							
        						}else {
        							session.setAttribute(ANSWERS_WRONG_ID, 1);
        							
        							speechText = "Sorry, you got the answer Wrong. " +
        										"The question is " + que.getQuestion() +
        										"And the correct answer is " + que.getAnswer() + ". " +
        										"Better luck next Time! Here is the next question: ";
        							
        							Question queN = generateQuestion(1);
        							
        							speechText += queN.getQuestion();
        							
        							session.setAttribute(CURRENT_QUESTION_ID, queN);
        							
        							LocalDateTime nowA = LocalDateTime.now();
    								
    			    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
        						}
        						
        					}
        					
        				}else {
        					log.info("The Time" + timeElapsed.toMillis());
        						
        					LinkedHashMap LHMQ = (LinkedHashMap)session.getAttribute(CURRENT_QUESTION_ID);	
        					
        					String question = LHMQ.get("question").toString();
        					
        					float answerT = Float.parseFloat(LHMQ.get("answer").toString());
        					
        					Question que = new Question(question, answerT);
        					
        					if(session.getAttributes().containsKey(ANSWERS_WRONG_ID)) {
    							int answersWrong = (Integer)session.getAttribute(ANSWERS_WRONG_ID);
    							
    							if(answersWrong == 4) {
    								speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/loser.mp3\"/>Ah! Sorry, you have used all five chances of wrong answers. Try again!";
    								
    								session.setAttribute(HAVE_ANSWER_ID, ASK_QUESTION);
    								
    								session.setAttribute(ANSWERS_WRONG_ID, 0);
    								
    								session.setAttribute(ANSWERS_CORRECT_ID, 0);
    								
    								session.setAttribute(CURRENT_QUESTION_ID, null);
    								
    								session.setAttribute(CURRENT_LEVEL_ID, 1);
    								
    								session.setAttribute(STAGE_ID, ASK_ANSWER_STAGE);
    								//End Game
    								//Initialize all Session Components
    								
    							}else {
    								session.setAttribute(ANSWERS_WRONG_ID, answersWrong + 1);
    								
    								speechText = "The question you missed is " + que.getQuestion() +
    										"And the correct answer is " + que.getAnswer() + ". " +
    										"Better luck next Time! Here is the next question: ";
    								
    								Question queN = generateQuestion((Integer)session.getAttribute(CURRENT_LEVEL_ID));
    								
    								session.setAttribute(CURRENT_QUESTION_ID, queN);
    								
    								speechText += queN.getQuestion();
    								
    								LocalDateTime nowA = LocalDateTime.now();
    								
    			    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
    							}   							
    						}else {
    							session.setAttribute(ANSWERS_WRONG_ID, 1);
    							
    							speechText = "The question you missed is " + que.getQuestion() +
    										"And the correct answer is " + que.getAnswer() + ". " +
    										"Better luck next Time! Here is the next question: ";
    							
    							Question queN = generateQuestion(1);
    							
    							session.setAttribute(CURRENT_QUESTION_ID, queN);
    							
    							speechText += queN.getQuestion();
    							
    							LocalDateTime nowA = LocalDateTime.now();
    							
    		    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
    						}
        					
        				}
        			}
    			}else {
    				speechText = "You answer is invalid! ";
    			}

    		}else {
    			session.setAttribute(HAVE_ANSWER_ID, HAVE_ANSWER);
        		
        		Question que = generateQuestion(1);
        		
        		session.setAttribute(CURRENT_QUESTION_ID, que);
        		
        		session.setAttribute(CURRENT_LEVEL_ID, 1);
        		
        		speechText = "You first question is: ";
        		
        		speechText += que.getQuestion();
        		
        		LocalDateTime nowA = LocalDateTime.now();
				
				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
    		}
    	}else if(speechText != "") {
    		speechText = speechText;
    	}else {
    		session.setAttribute(HAVE_ANSWER_ID, HAVE_ANSWER);
    		
    		Question que = generateQuestion(1);
    		
    		session.setAttribute(CURRENT_LEVEL_ID, 1);
    		
    		session.setAttribute(CURRENT_QUESTION_ID, que);
    		
    		speechText = "Your first question is: ";
    		
    		speechText += que.getQuestion();
    		
    		LocalDateTime nowA = LocalDateTime.now();
			
			session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
    	}
    	
        if((Integer)session.getAttribute(HAVE_ANSWER_ID) == ASK_QUESTION) {
        	repromptText = "Would you want to play again? ";
        }else if((Integer)session.getAttribute(STAGE_ID) == ASK_MODE_STAGE) {
        	repromptText = "Please choose the mode you want to play";
        }
        else {
        	repromptText = "You have exceeded the maximum amount of time to answer the question. Say \'continue\' or \'next question\' to continue";
        }

        return getSpeechletResponse(speechText, repromptText, true);
    }
    
	private SpeechletResponse getHelpResponse(Intent intent) {
    	String speechOutput =
                "With Fast math, you can get"
                        + " a better idea of how good you can do with your math skills."
                        + " Have fun! ";

        String repromptText = "What would you like to play?";

        return newAskResponse(speechOutput, false, repromptText, true);
	}
	
	private SpeechletResponse newAskResponse(String stringOutput, boolean isOutputSsml,
            String repromptText, boolean isRepromptSsml) {
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
	/**
     * Creates a {@code SpeechletResponse} when there is an error of any kind.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
	private SpeechletResponse getErrorResponse(Intent intent) {
    	
    	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText("I'm sorry... the fast math game seems to be down right now."
        						+ " Please try again later.");

        return SpeechletResponse.newTellResponse(outputSpeech);
		
	}

	/**
     * Creates a {@code SpeechletResponse} for the CancelIntent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
	private SpeechletResponse getCancelResponse(Intent intent) {
    	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText("Goodbye, have a good day.");

        return SpeechletResponse.newTellResponse(outputSpeech);
	}

	private SpeechletResponse getStopResponse(Intent intent) {
    	
    	PlainTextOutputSpeech outputSpeech = new PlainTextOutputSpeech();
        outputSpeech.setText("Goodbye, have a good day.");

        return SpeechletResponse.newTellResponse(outputSpeech);
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