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

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    
    //Challenger Stages
    private static final String ASK_RANGE_ID = "AskRangeID";
    private static final String LEVEL_A_ID = "LevelAID";
    private static final String LEVEL_B_ID = "LevelBID";
    private static final int ASK_RANGE_STAGE = 1;
    private static final int HAVE_RANGE_STAGE = 0;
    private static final int HAVE_RANGE_ANSWER_STAGE = 2;
    
    private static final String MODE_ID = "ModeID";
    private static final String SURVIVAL_MODE = "survival";
    private static final String PRACTICE_MODE = "practice";
    private static final String CHALLENGER_MODE = "challenger";
    private static final String SURVIVAL_MODE_FULL = "survival mode";
    private static final String PRACTICE_MODE_FULL = "practice mode";
    private static final String CHALLENGER_MODE_FULL = "challenger mode";
    
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
        	
        	if(session.getAttributes().containsKey(STAGE_ID)) {
        		if((Integer)session.getAttribute(STAGE_ID) != ASK_ANSWER_STAGE) {
        			
        			GetHelp gH = new GetHelp();
        			
        			return gH.notHavingAnswer();
        		}
        	}else {
        		GetHelp gH = new GetHelp();
    			
    			return gH.notAskingAnything();
        	}
        	if(session.getAttributes().containsKey(MODE_ID)) {
        		if(((String)session.getAttribute(MODE_ID)).equals(SURVIVAL_MODE)) {
        			return setUpSurvivalStage(intent, session, false, false);
        		}else if(((String)session.getAttribute(MODE_ID)).equals(PRACTICE_MODE)) {
        			return setUpPracticeStage(intent, session, false, false, false);
        		}else if(((String)session.getAttribute(MODE_ID)).equals(CHALLENGER_MODE)) {
        			return setUpChallengerStage(intent, session, false, false);
        		}else {
        			GetHelp gH = new GetHelp();
        			
        			return gH.noSuchMode();
        		}
        	}else {
        		GetHelp gH = new GetHelp();
    			
    			return gH.notAskingAnything();
        	}
            
        } else if ("ContinueIntent".equals(intentName)) {
        	
        	if(session.getAttributes().containsKey(STAGE_ID)) {
            	if((Integer)session.getAttribute(STAGE_ID) == CONFIRM_STAGE){
            		return handleContinueStageResponse(intent,  session);
            	}else if((Integer)session.getAttribute(STAGE_ID) == ASK_MODE_STAGE){
            		GetHelp gH = new GetHelp();
        			
        			return gH.notAskingMode();
            	}else {
            		if(session.getAttributes().containsKey(MODE_ID)){
            			if(((String)session.getAttribute(MODE_ID)).equals(SURVIVAL_MODE)) {
            				return setUpSurvivalStage(intent, session, true, false);
            			}else {
            				GetHelp gH = new GetHelp();
                			
                			return gH.notNeedingContinue();
            			}
            		}else {
            			GetHelp gH = new GetHelp();
            			
            			return gH.notAskingMode();
            		}
            	}
        	}else {
        		GetHelp gH = new GetHelp();
    			
    			return gH.notAskingAnything();
        	}
        } else if("AnswerRangeIntent".equals(intentName)) {
        	if(session.getAttributes().containsKey(STAGE_ID)) {
        		if((Integer)session.getAttribute(STAGE_ID) == ASK_ANSWER_STAGE) {
        			if(session.getAttributes().containsKey(MODE_ID)) {
        				if(((String)session.getAttribute(MODE_ID)).equals(CHALLENGER_MODE)) {
        					return setUpChallengerStage(intent, session, false, false);
        				}else {
        					GetHelp gH = new GetHelp();
                			
                			return gH.notNeedingAskingRange();
        				}
        			}else {
        				GetHelp gH = new GetHelp();
            			
            			return gH.notAskingMode();
        			}
        		}else {
        			GetHelp gH = new GetHelp();
        			
        			return gH.notHavingAnswer();
        		}
        	}else {
        		GetHelp gH = new GetHelp();
    			
    			return gH.notAskingAnything();
        	}
        } else if("RepeatQuestionIntent".equals(intentName)) { 
        	if(session.getAttributes().containsKey(STAGE_ID)) {
        		if((Integer)session.getAttribute(STAGE_ID) == ASK_ANSWER_STAGE) {
        			if(session.getAttributes().containsKey(MODE_ID)) {
        				if(((String)session.getAttribute(MODE_ID)).equals(PRACTICE_MODE)) {
        					return setUpPracticeStage(intent, session, false, false, true);
        				}else if(((String)session.getAttribute(MODE_ID)).equals(CHALLENGER_MODE)) {
        					return setUpChallengerStage(intent, session, false, true);
        				}else {
        					GetHelp gH = new GetHelp();
        	    			
        	    			return gH.cantAskRepeat();
        				}
        			}else {
        				GetHelp gH = new GetHelp();
            			
            			return gH.notAskingMode();
        			}
        		}else {
        			GetHelp gH = new GetHelp();
        			
        			return gH.notHavingAnswer();
        		}
        	}else {
        		GetHelp gH = new GetHelp();
    			
    			return gH.notAskingAnything();
        	}
        } else if("DoNotKnowIntent".equals(intentName)) { 
        	if(session.getAttributes().containsKey(STAGE_ID)) {
        		if((Integer)session.getAttribute(STAGE_ID) == ASK_ANSWER_STAGE) {
        			if(session.getAttributes().containsKey(MODE_ID)) {
        				if(((String)session.getAttribute(MODE_ID)).equals(PRACTICE_MODE)) {
        					return setUpPracticeStage(intent, session, false, true, false);
        				}else if(((String)session.getAttribute(MODE_ID)).equals(SURVIVAL_MODE)) {
        					return setUpSurvivalStage(intent, session, false, true);
        				}else if(((String)session.getAttribute(MODE_ID)).equals(CHALLENGER_MODE)) {
        					return setUpChallengerStage(intent, session, true, false);
        				}else {
        					GetHelp gH = new GetHelp();
        	    			
        	    			return gH.notAskingAnything();
        				}
        			}else {
        				GetHelp gH = new GetHelp();
            			
            			return gH.notAskingMode();
        			}
        		}else {
        			GetHelp gH = new GetHelp();
        			
        			return gH.notHavingAnswer();
        		}
        	}else {
        		GetHelp gH = new GetHelp();
    			
    			return gH.notAskingAnything();
        	}
        } else if("AnswerLevelIntent".equals(intentName)) {
        	if(session.getAttributes().containsKey(STAGE_ID)) {
        		if((Integer)session.getAttribute(STAGE_ID) == ASK_ANSWER_STAGE) {
        			if(session.getAttributes().containsKey(MODE_ID)) {
        				if(((String)session.getAttribute(MODE_ID)).equals(PRACTICE_MODE)) {
        					return setUpPracticeStage(intent, session, true, false, false);
        				}else {
        					GetHelp gH = new GetHelp();
        	    			
        	    			return gH.cantAskLevel();
        				}
        			}else {
        				GetHelp gH = new GetHelp();
            			
            			return gH.notAskingMode();
        			}
        		}else {
        			GetHelp gH = new GetHelp();
        			
        			return gH.notHavingAnswer();
        		}
        	}else {
        		GetHelp gH = new GetHelp();
    			
    			return gH.notAskingAnything();
        	}
        }
        	else if ("CancelIntent".equals(intentName)) {
        	return handleCancelStageResponse(intent, session);
        } else if ("AMAZON.HelpIntent".equals(intentName)) {
            return GetHelpResponse(intent);
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
    	
    	if(session.getAttributes().containsKey(ASK_RANGE_ID)) {
    		session.setAttribute(ASK_RANGE_ID, ASK_RANGE_STAGE);
    	}
    	
    	if(session.getAttributes().containsKey(LEVEL_A_ID)) {
    		session.setAttribute(LEVEL_A_ID, null);
    	}
    	
    	if(session.getAttributes().containsKey(LEVEL_B_ID)) {
    		session.setAttribute(LEVEL_B_ID, null);
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
    			
    			repromptText = "You can choose from Surival, Practice, and Challenger modes";
    			
    			initializeAllComponents(session, true);
    		}else if((Integer)session.getAttribute(STAGE_ID) == ASK_MODE_STAGE){
    			speechText = "You have not chosen a mode yet! ";
    			
    			repromptText = "You can choose from Surival, Practice, and Challenger modes";
    		}else {
    			speechText = "You exited the mode you are playing. What mode would you want to play now? ";
    			
    			repromptText = "You can choose from Surival, Practice, and Challenger modes";
    			
    			initializeAllComponents(session, true);
    		}
    	}else {
    		speechText = "You have not chosen a mode yet! You can choose from Surival, Practice, and Challenger modes";
			
			repromptText = "You can choose from Surival, Practice, and Challenger modes";
			
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
        			return setUpSurvivalStage(intent, session, false, false);
        			
    			}else if(((String)session.getAttribute(MODE_ID)).equals(PRACTICE_MODE)) {
    				session.setAttribute(STAGE_ID, ASK_ANSWER_STAGE);
    				session.setAttribute(ASK_LEVEL_ID, ASK_LEVEL_STAGE);
    				return setUpPracticeStage(intent, session, false, false, false);
    			}else {
    				session.setAttribute(STAGE_ID, ASK_ANSWER_STAGE);
    				session.setAttribute(ASK_RANGE_ID, ASK_RANGE_STAGE);
    				return setUpChallengerStage(intent, session, false,false);
    			}
    		}
    	}else {
    		 GetHelp gH = new GetHelp();
    		 
    		 return gH.notAskingMode();
    	}
        
    }
    
    private SpeechletResponse getWelcomeResponse() {
        // Create the welcome message.
        String speechText =
                "Welcome to Fast Math Game! You can choose to play in three different modes:    " +
                "Practice   ,   Survival,    or   Challenger. Notice that these levels and modes can be extremely challenging. " + 
                		"But anyway, practice makes perfect.    Now, which one would you like to play?";
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

    		String modeSlot = GamemodeSlot.getValue();

    		if(modeSlot.toLowerCase().equals(SURVIVAL_MODE) || modeSlot.toLowerCase().equals(SURVIVAL_MODE_FULL)) {
    			speechText = "Welcome to Survival Mode! Here you will be challenged with questions according to the level you are in. " +
    						"Please answer the question loudly and clearly, or your voice may result in a misunderstanding for me. " +
    						"If you get five answers correct at your current level, you will advance to next level. " +
    						"For each question, you have at most eight seconds to answer. " +
    						"If you spend more than eight seconds, the question will be counted wrong. " +
    						"You can have at most 5 questions wrong. You can exit the mode whenever you want by saying Quit or Cancel. " +
    						"You will not be able to change mode unlesss you quit the game mode. " +
    						"You will also not be able to ask me to repeat the question in survival mode. " +
    						"If you get stuck or do not know the question, just say I don't know. " +
    						"Ready to start the game? Say Begin or Continue to proceed. ";
    			
    			repromptText = "Ready to start the game? Say Begin or Continue to go ahead. ";
    			
    			session.setAttribute(MODE_ID,  SURVIVAL_MODE);
    			
    			session.setAttribute(STAGE_ID, CONFIRM_STAGE);
    			
    		}else if(modeSlot.toLowerCase().equals(PRACTICE_MODE) || modeSlot.toLowerCase().equals(PRACTICE_MODE_FULL)) {
    			speechText = "Welcome to Practice Mode! In this mode, you can choose to practice math questions whatever the level you wanna be in. " +
    						 "You will not be given a time limit or wrong answers penalty. You can exit the mode whenever you want by saying Quit or Cancel. Have Fun! " +
    						 "If you get stuck or do not know the question, just say I don't know. " +
    						 "If you need me to repeat the question, just say repeat the question or repeat. " +
    						 "Ready to start the game? Say Begin or Continue to proceed. ";
			
    			repromptText = "Ready to start the game? Say Begin or Continue to go ahead. ";
			
    			session.setAttribute(MODE_ID,  PRACTICE_MODE);
			
    			session.setAttribute(STAGE_ID, CONFIRM_STAGE);
    		}else if(modeSlot.toLowerCase().equals(CHALLENGER_MODE) || modeSlot.toLowerCase().equals(CHALLENGER_MODE_FULL)) {
    			speechText = "Welcome to the Challenger Mode! In this mode, you can pick a range of two different levels, by saying,     for example,      " +
    						"Level one to level two. You can also stay in the same level by just saying the same level. " +
    						"However, if you miss one question, the game is over. Prepare for it! " +
    						"If you need me to repeat the question, just say repeat the question or repeat. " +
    						"You can exit the mode whenever you want by saying Quit or Cancel, but you can not change the level when you are playing. " +
    						"Ready to start the game? Say Begin or Continue to proceed. ";
    			
    			repromptText = "Ready to start the game? Say Begin or Continue to go ahead. ";
    			
    			session.setAttribute(MODE_ID,  CHALLENGER_MODE);
    			
    			session.setAttribute(STAGE_ID, CONFIRM_STAGE);
    		}
    		return getSpeechletResponse(speechText, repromptText, true);
    		
    	}else if(speechText != "") {
    		repromptText = speechText;
    		
    		return getSpeechletResponse(speechText, repromptText, true);
    	}else {
    		speechText = "Sorry, we do not have such mode! You can choose from survival, practice, or challenger mode. ";
    		
    		repromptText = "You can choose from survival, practice, or challenger mode. ";
    		
    		return getSpeechletResponse(speechText, repromptText, true);
    	}
    }
    

    private SpeechletResponse getHelp() {
        String speechOutput =
                "There is something wrong with your answer. Please keep in mind that you have to say continue every time you enter a new mode. " +
                "You will have to provide one level by saying level and level number for the practice mode, and you will have to provide a level range " +
                "of two numbers within five for the challenger stage. You will not need to say any decimal answers so please do not include point " +
                "in your answer. If you want to quit playing the current mode, just say quit mode or cancel. You are free to change your level in " +
                "practice mode, but you won't be able to do this in survival or challenger mode unless you quit and reenter.You can ask me to repeat " + 
                "the question in Practice and Challenger mode, but you can not do this in survival mode.   What would you want to do now?";
        String repromptText =
        		"There is something wrong with your answer. Please keep in mind that you have to say continue every time you enter a new mode. " +
                        "You will have to provide one level by saying level and level number for the practice mode, and you will have to provide a level range " +
                        "of two numbers within five for the challenger stage. You will not need to say any decimal answers so please do not include point " +
                        "in your answer. If you want to quit playing the current mode, just say quit mode or cancel. You are free to change your level in " +
                        "practice mode, but you won't be able to do this in survival or challenger mode unless you quit and reenter. " +  " "
                        		+ "You can ask me to repeat the question in Practice and Challenger mode, but you can not do this in survival mode.  What would you want to do now?";
        return newAskResponse(speechOutput,true, repromptText, false);
    }

    
    private Question generateQuestion(int level) {
    	switch(level) {
    	case 1:
    		int randX = RAND.nextInt(11);
    		
    		int randY = RAND.nextInt(41) + 10;
    		
    		float randZ = RAND.nextFloat();
    		
    		if(randZ >= 0.5f) {
    			
    			int answer = randX + randY;
    			
    			String speech = "What is.     " + randX + ".     plus.      " + randY + " ? ";
    			
    			Question que = new Question(speech, answer);
    			
    			return que;
    		}else {
    			int answer  =  randY - randX;
    			
    			String speech = "What is.    " + randY + ".      minus.     " + randX + " ? ";
    			
    			Question que = new Question(speech, answer);
    			
    			return que;
    		}
    		
    		
    	case 2:
    		
    		randX = RAND.nextInt(11);
    		
    		randY = RAND.nextInt(11);
    		
    		randZ = RAND.nextFloat();

    		if(randZ >= 0.5f) {
    			
    			int answer = randX * randY;
    			
    			String speech = "What is.     " + randX + ".    multiply by.      " + randY + " ? ";
    			
    			Question que = new Question(speech, answer);
    			
    			return que;
    		}else {
    			while(randX == 0) {
    				randX = RAND.nextInt(11);
    			}
	
    			int product = randX * randY;
    			
    			int answer  =  product / randX;
    			
    			String speech = "What is.    " + product + ".     divided by.      " + randX + " ? ";
    			
    			Question que = new Question(speech, answer);
    			
    			return que;
    		}
    		
    	case 3:
    		randX = RAND.nextInt(11);
    		
    		randY = RAND.nextInt(11);
    		
    		int randI = RAND.nextInt(30);
    		
    		if((randX * randY) >= randI) {
    			String speech = "What is.    " + randX + ",    multiplied by,      " + randY + ".        minus.       " + randI + " ? ";
    			
    			float answer = randX * randY - randI;
    			
    			Question que = new Question(speech, answer);
        		
        		return que;
    		}else {
    			String speech = "What is.   " + randI + ".    minus the product of.    " + randX + ",   and,    " + randY + " ? ";
    			
    			float answer = randI - (randX * randY);
    			
    			Question que = new Question(speech, answer);
    			
        		return que;
    		}
    	
    	case 4:
    		
    		randX = RAND.nextInt(51) + 10;
    		
    		if(randX > 50) {
    			randX -= 10;
    		}
    		
    		float randYf = RAND.nextFloat() * 10;
    		
    		BigDecimal bd = new BigDecimal(Float.toString(randYf));
    		
    		bd = bd.setScale(1, RoundingMode.HALF_UP);
    		
    		randYf = bd.floatValue();

    		randZ = RAND.nextFloat();
    		
    		if(randZ >= 0.5f) { 
    			float anotherFloat = randX - randYf;
    			
    			BigDecimal bdA = new BigDecimal(Float.toString(anotherFloat));
        		
        		bdA = bdA.setScale(1, RoundingMode.HALF_UP);
        		
        		anotherFloat = bdA.floatValue();
    			
    			String speech = "What is.  " + anotherFloat + ".  plus.  " + randYf + " ? ";
    			
    			float answer = randX;
    			
    			Question que =  new Question(speech, answer);
    			
    			return que;
    		}else {
    			float newFloat = randX + randYf;
    			
    			BigDecimal bdN = new BigDecimal(Float.toString(newFloat));
        		
        		bdN = bdN.setScale(1, RoundingMode.HALF_UP);
        		
        		newFloat = bdN.floatValue();

    			String speech = "What is.      " + newFloat + ".         minus.        " + randYf + " ? ";
    			
    			float answer = randX;
    			
    			Question que = new Question(speech, answer);
    			
    			return que;
    		}
    	
    	case 5:
    		randY = RAND.nextInt(11) + 10;
    		
    		randX = RAND.nextInt(3);
		
    		switch(randX) {
    		case 0:
    			float randZC;
    			
    			randZ = RAND.nextFloat();
    			
    			bd = new BigDecimal(Float.toString(randZ));
        		
        		bd = bd.setScale(1, RoundingMode.HALF_UP);
        		
        		randZ = bd.floatValue();
        		
        		if(randZ * 2 >= 1) {
        			randZC = 2 - randZ * 2;
        		}else {
        			randZC = 1 - randZ * 2;
        		}
    			
    			if(randZ >= 0.5) {
    				randZ += 1;
    			}else {
    				randZ += 2;
    			}

    			float anotherRand = RAND.nextInt(7) + randZC;
    			
    			float answer = anotherRand + randZ * 2;
    			
    			String speech = "What is. " + randZ + ", multiplies by, two, plus, " + anotherRand + " ?";
    			
    			Question que = new Question(speech, answer);
    			
    			return que;	

    		case 1:
    			randZ = RAND.nextFloat();
			
    			bd = new BigDecimal(Float.toString(randZ));
    		
    			bd = bd.setScale(1, RoundingMode.HALF_UP);
    		
    			randZ = bd.floatValue();
    		
    			if(randZ * 3 >= 1) {
    				if(randZ * 3 > 2) {
    					randZC = 3 - 3 * randZ;
    				}else {
    					randZC = 2 - 3 * randZ;
    				}
    			}else {
    				randZC = 1 - randZ * 3;
    			}
    			
    			if(randZ >= 0.5) {
    				randZ += 1;
    			}else {
    				randZ += 2;
    			}

    			anotherRand = RAND.nextInt(7) + randZC;
			
    			answer = anotherRand + randZ * 3;
			
    			speech = "What is. " + randZ + ", multiplies by, three, plus, " + anotherRand + " ?";
			
    			que = new Question(speech, answer);
			
    			return que;
    			
    		case 2:
    			randZ = RAND.nextFloat();
    			
    			bd = new BigDecimal(Float.toString(randZ));
    		
    			bd = bd.setScale(1, RoundingMode.HALF_UP);
    		
    			randZ = bd.floatValue();
    		
    			if(randZ * 4 >= 1) {
    				if(randZ * 4 > 3) {
    					randZC = 4 - 4 * randZ;
    				}else if(randZ * 4 > 2) {
    					randZC = 3 - 4 * randZ;
    				}else {
    					randZC = 2 - 4 * randZ;
    				}
    			}else {
    				randZC = 1 - randZ * 4;
    			}
    			
    			if(randZ >= 0.5) {
    				randZ += 1;
    			}else {
    				randZ += 2;
    			}

    			anotherRand = RAND.nextInt(7) + randZC;
			
    			answer = anotherRand + randZ * 4;
			
    			speech = "What is. " + randZ + ", multiplies by, four, plus, " + anotherRand + " ?";
			
    			que = new Question(speech, answer);
			
    			return que;
    		}
    		
    	default:
    		return null;
    	}
    }
    
    private int pickRandomNumberInRange(int A, int B) {
    	
    	if(A == B) {
    		return A;
    	}
    	
    	int random = RAND.nextInt(B - A + 1);
    	
    	random += A;
    	
    	log.info("The Random Choice level is " + random);
    	
    	return random;
    }
    
    private SpeechletResponse setUpChallengerStage(final Intent intent, final Session session, boolean doNotKnow, boolean repeatQuestion) {
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
    	
    	if(session.getAttributes().containsKey(ASK_RANGE_ID)) {
    		if((Integer)session.getAttribute(ASK_RANGE_ID) == HAVE_RANGE_ANSWER_STAGE && doNotKnow) {
    			Question que = getQuestionFromLinkedHashMap((LinkedHashMap)session.getAttribute(CURRENT_QUESTION_ID));
    		    			
    			speechText = "It's ok. The question you missed is.      " + que.getQuestion();
    			
    			speechText += "The answer is.     " + que.getAnswer() + " . ";
    			
    			speechText += "Better luck next time! You are back in the main menu. Which mode would you want to play? ";
				
				repromptText = "You can choose from Survival, Practice and Challenger mode. Which mode would you want to play? ";
				
				initializeAllComponents(session, true);
    		}else if((Integer)session.getAttribute(ASK_RANGE_ID) == HAVE_RANGE_ANSWER_STAGE && repeatQuestion) {
    			Question que = getQuestionFromLinkedHashMap((LinkedHashMap)session.getAttribute(CURRENT_QUESTION_ID));
    			
    			speechText = "Sure, The question is.    " + que.getQuestion();
    			
    			repromptText = "The question is    " + que.getQuestion();
    		}
    	}
    	
    	if(session.getAttributes().containsKey(ASK_RANGE_ID) && speechText == "") {
    		if((Integer)session.getAttribute(ASK_RANGE_ID) == ASK_RANGE_STAGE) {
    			speechText = "Please choose the level range from the five levels that you want to play with. ";
    			
    			repromptText = "Please choose the level range from the five levels that you want to play with. ";
    			
    			session.setAttribute(ASK_RANGE_ID, HAVE_RANGE_STAGE);
    		}else if((Integer)session.getAttribute(ASK_RANGE_ID) == HAVE_RANGE_STAGE){
    			Slot levelASlot = intent.getSlot("LevelA");
    			
    			Slot levelBSlot = intent.getSlot("LevelB");
    			
    			if(levelASlot != null && levelBSlot != null && levelASlot.getValue() != null && levelBSlot.getValue() != null) {
    				
    				int levelA = Integer.parseInt(levelASlot.getValue());
    				
    				int levelB = Integer.parseInt(levelBSlot.getValue());
    				
    				if(levelA <= levelB && (levelA > 0 && levelA <= 5 && levelB > 0 && levelB <= 5)) {
    					int levelDecided = pickRandomNumberInRange(levelA, levelB);
    					
    					Question que = generateQuestion(levelDecided);
    					

    					session.setAttribute(LEVEL_A_ID, levelA);
    					
    					session.setAttribute(LEVEL_B_ID, levelB);
    					
    					session.setAttribute(ASK_RANGE_ID, HAVE_RANGE_ANSWER_STAGE);
    					
    					session.setAttribute(CURRENT_QUESTION_ID, que);
    					
    					repromptText = "Would you like to hear the question again? The question is " + que.getQuestion();
    				}else {
    					speechText = "You did not say the range correctly! Please say any number between one and five. ";
    					
    					repromptText = "You did not say the range correctly! Please say any number between one and five. ";
    				}
    				
    			}else {
    				speechText = "The range you picked was invalid! Please say any range between one and five";
    				
    				repromptText = "The range you picked was invalid! Please say any range between one and five";
    			}
    		}else {
    			Slot answerSlot =intent.getSlot("Answer");
    			
    			if(answerSlot != null && answerSlot.getValue() != null) {
    				float answer = Float.parseFloat(answerSlot.getValue());
    				
    				Question que = getQuestionFromLinkedHashMap((LinkedHashMap)session.getAttribute(CURRENT_QUESTION_ID));
        			
    				if(que.checkAnswer(answer)) {
    					speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/correct.mp3\"/> Nice! You got the question correct! Here is the next question:     ";
    					
    					int levelDecided = pickRandomNumberInRange((Integer)session.getAttribute(LEVEL_A_ID), (Integer)session.getAttribute(LEVEL_B_ID));
    					
    					Question queN = generateQuestion(levelDecided);
    					
    					speechText += queN.getQuestion();
    					
    					session.setAttribute(CURRENT_QUESTION_ID, queN);
    					
    					repromptText = "Would you like to hear the question again? The question is.       " + queN.getQuestion();
    				}else {
    					speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/incorrect.mp3\"/> Oops! You got the answer wrong! The question is.       " + que.getQuestion();
    					
    					speechText += "And the answer is.     " + que.getAnswer() + "   . ";
    					
    					initializeAllComponents(session, true);
    					
    					speechText += "<audio src=\"https://s3.amazonaws.com/cschool0/loser.mp3\"/> Better luck next time! You are back in the main menu. Which mode would you want to play? ";
    					
    					repromptText = "You can choose from Survival, Practice and Challenger mode. Which mode would you want to play? ";
    				}
    			}else {
    				speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/incorrect.mp3\"/> Your answer is invalid! Please try again! ";
    				
    				repromptText = "Your answer is invalid! Please try again! ";
    			}		
    		}
    	}else if(speechText != "") {
    		speechText = speechText;
    	}
    	
    	return getSpeechletResponse(speechText, repromptText, true);
    }
    
    private SpeechletResponse setUpPracticeStage(final Intent intent, final Session session, boolean levelChanged, boolean doNotKnow, boolean repeatQuestion) {
    	
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
			
			speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/incorrect.mp3\"/> It's ok. The question is.       " + que.getQuestion() +
					"And the answer is.      " + que.getAnswer() + ". ";
		
			Question queN = generateQuestion((Integer)session.getAttribute(LEVEL_ID));
		
			speechText += "Let's try another one:     " + queN.getQuestion();
		
			session.setAttribute(CURRENT_QUESTION_ID, queN);
			
			repromptText  = "The question is. " + que.getQuestion(); 
    	}else if((Integer)session.getAttribute(ASK_LEVEL_ID) == ANSWER_QUESTION_STAGE && repeatQuestion) {
    		
    		LinkedHashMap LHMQ = (LinkedHashMap)session.getAttribute(CURRENT_QUESTION_ID);
			
			Question que = getQuestionFromLinkedHashMap(LHMQ);
    		
    		speechText = "Sure, the question is       " +  que.getQuestion();
    		
    		repromptText = "The question is       " + que.getQuestion();
    	}
    	
    	if((Integer)session.getAttribute(ASK_LEVEL_ID) != GENERATE_QUESTION_STAGE && levelChanged) {
    		
    		Slot levelSlot = intent.getSlot("Level");
			
			if(levelSlot != null && levelSlot.getValue() != null) {
				
				int levelAsked = Integer.parseInt(levelSlot.getValue());
				
				if(levelAsked == 1 || levelAsked == 2 || levelAsked == 3 || levelAsked == 4 || levelAsked == 5) {
					Question que = generateQuestion(levelAsked);
					
					session.setAttribute(LEVEL_ID, levelAsked);
					
					session.setAttribute(CURRENT_QUESTION_ID, que);
					
					speechText = "You have changed your level! Your first question chosen for level " + levelAsked + " is.        " + que.getQuestion();
					
					repromptText = "Would you like to hear the question again? Your question is.      " + que.getQuestion();
					
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
    					
    					speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/correct.mp3\"/> Good job, you get the answer correct! Here is your next question:       " + que.getQuestion();
    					
    					session.setAttribute(CURRENT_QUESTION_ID, que);
    					
    					repromptText = "Would you like to listen to the question again? Your question is.        " + que.getQuestion();
    				}else {
    					speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/incorrect.mp3\"/> You got the answer wrong, but it is ok. The question is.        " + que.getQuestion() +
    								"And the answer was    " + que.getAnswer() + ". ";
    					
    					Question queN = generateQuestion((Integer)session.getAttribute(LEVEL_ID));
    					
    					speechText += "Let's try another one:      " + queN.getQuestion();
    					
    					session.setAttribute(CURRENT_QUESTION_ID, queN);
    					
    					repromptText = "Would you like to listen to the question again? Your question is.       " + queN.getQuestion();
    				}
    			}else {
    				speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/incorrect.mp3\"/> Your answer is invalid! The question is.      " + que.getQuestion();
    				
    				repromptText = "Would you like to hear the question again? The question is.        " + que.getQuestion();
    			}
    		}else {
    			Slot levelSlot = intent.getSlot("Level");
    			
    			if(levelSlot != null && levelSlot.getValue() != null) {
    				int levelAsked = Integer.parseInt(levelSlot.getValue());
    				
    				if(levelAsked == 1 || levelAsked == 2 || levelAsked == 3 || levelAsked == 4 || levelAsked == 5) {
    					Question que = generateQuestion(levelAsked);
    					
    					session.setAttribute(CURRENT_QUESTION_ID, que);
    					
    					session.setAttribute(LEVEL_ID, levelAsked);
    					
    					speechText = "Your first question chosen for level " + levelAsked + " is :       " + que.getQuestion();
    					
    					repromptText = "Would you like to listen to the question again? Your question is.        " + que.getQuestion();
    					
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
    			speechText = "You level has not been properly set up. Please quit the mode and reset it! ";
    			
    			repromptText = speechText;
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
    
    private SpeechletResponse setUpSurvivalStage(final Intent intent, final Session session, boolean exceededTime, boolean doNotKnow) {
        // Get the slots from the intent.
    	
    	String speechText, repromptText;
    	
    	speechText = "";
    	
    	if((Integer)session.getAttribute(STAGE_ID) != ASK_ANSWER_STAGE) {
    		speechText = "You have to choose or confirm which mode you want to play before you enter the play mode!" +
    				"There are three choices: Survival, practice and Challenger. What is your choice? ";
    	}else {
    		if(session.getAttributes().containsKey(HAVE_ANSWER_ID)) {
    			if(((Integer)session.getAttribute(HAVE_ANSWER_ID)) == HAVE_ANSWER && doNotKnow) {
    				Question que = getQuestionFromLinkedHashMap((LinkedHashMap)session.getAttribute(CURRENT_QUESTION_ID));
    				
    				if(session.getAttributes().containsKey(ANSWERS_WRONG_ID)) {
						int answersWrong = (Integer)session.getAttribute(ANSWERS_WRONG_ID);
						
						if(answersWrong == 4) {
							speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/loser.mp3\"/>Ah! Sorry, you have used all five chances of wrong answers. Try again later! You are back in the main menu, which mode would you like to play?";
							
							session.setAttribute(HAVE_ANSWER_ID, ASK_QUESTION);
							
							session.setAttribute(ANSWERS_WRONG_ID, 0);
							
							session.setAttribute(ANSWERS_CORRECT_ID, 0);
							
							session.setAttribute(CURRENT_QUESTION_ID, null);
							
							session.setAttribute(CURRENT_LEVEL_ID, 1);
							
							session.setAttribute(STAGE_ID, ASK_MODE_STAGE);
							
						}else {
							session.setAttribute(ANSWERS_WRONG_ID, answersWrong + 1);
							
							speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/incorrect.mp3\"/> Sorry, you got the answer Wrong. " +
									"The question is.       " + que.getQuestion() +
									"And the correct answer is.       " + que.getAnswer() + ". " +
									"Better luck next Time! Here is the next question: ";
							
							Question queN = generateQuestion((Integer)session.getAttribute(CURRENT_LEVEL_ID));
							
							speechText += queN.getQuestion();
							
							session.setAttribute(CURRENT_QUESTION_ID, queN);
							
							LocalDateTime nowA = LocalDateTime.now();
							
		    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
						}   							
					}else {
						session.setAttribute(ANSWERS_WRONG_ID, 1);
						
						speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/incorrect.mp3\"/> Sorry, you got the answer Wrong. " +
									"The question is.       " + que.getQuestion() +
									"And the correct answer is.      " + que.getAnswer() + ". " +
									"Better luck next Time! Here is the next question: ";
						
						Question queN = generateQuestion(1);
						
						speechText += queN.getQuestion();
						
						session.setAttribute(CURRENT_QUESTION_ID, queN);
						
						LocalDateTime nowA = LocalDateTime.now();
						
	    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
					}
    			
    			}else if(((Integer)session.getAttribute(HAVE_ANSWER_ID)) == HAVE_ANSWER && exceededTime) {
    				
    				Question que = getQuestionFromLinkedHashMap((LinkedHashMap)session.getAttribute(CURRENT_QUESTION_ID));
    				
    				if(session.getAttributes().containsKey(ANSWERS_WRONG_ID)) {
						int answersWrong = (Integer)session.getAttribute(ANSWERS_WRONG_ID);
						
						if(answersWrong == 4) {
							speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/loser.mp3\"/> Ah! Sorry, you have used all five chances of wrong answers. Try again later! You are back in the main menu, which mode would you like to play?";
							
							session.setAttribute(HAVE_ANSWER_ID, ASK_QUESTION);
							
							session.setAttribute(ANSWERS_WRONG_ID, 0);
							
							session.setAttribute(ANSWERS_CORRECT_ID, 0);
							
							session.setAttribute(CURRENT_QUESTION_ID, null);
							
							session.setAttribute(CURRENT_LEVEL_ID, 1);
							
							session.setAttribute(STAGE_ID, ASK_MODE_STAGE);
							
						}else {
							session.setAttribute(ANSWERS_WRONG_ID, answersWrong + 1);
							
							speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/incorrect.mp3\"/> Mind the timing!  " +
									"The question you missed is.      " + que.getQuestion() +
									"And the correct answer is       " + que.getAnswer() + ". " +
									"Better luck next Time! Here is the next question:      ";
							
							Question queN = generateQuestion((Integer)session.getAttribute(CURRENT_LEVEL_ID));
							
							speechText += queN.getQuestion();
							
							session.setAttribute(CURRENT_QUESTION_ID, queN);
							
							LocalDateTime nowA = LocalDateTime.now();
							
		    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
						}   							
					}else {
						session.setAttribute(ANSWERS_WRONG_ID, 1);
						
						speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/incorrect.mp3\"/> Mind the timing! " +
									"The question you missed is.     " + que.getQuestion() +
									"And the correct answer is.      " + que.getAnswer() + ". " +
									"Better luck next Time! Here is the next question:      ";
						
						Question queN = generateQuestion(1);
						
						speechText += queN.getQuestion();
						
						session.setAttribute(CURRENT_QUESTION_ID, queN);
						
						LocalDateTime nowA = LocalDateTime.now();
						
	    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
					}
    			}
    				
    		}
    	}
    	
    	
    	
    	if(session.getAttributes().containsKey(HAVE_ANSWER_ID) && speechText == "") {
    		
    		if((Integer)session.getAttribute(HAVE_ANSWER_ID) == HAVE_ANSWER) {
    			
    			Slot answerSlot = intent.getSlot("Answer");
    			
    			if(answerSlot != null && answerSlot.getValue() != null) {

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
        		
        				if(timeElapsed.toMillis() < 30000) {
        					
        					LinkedHashMap LHMQ = (LinkedHashMap)session.getAttribute(CURRENT_QUESTION_ID);	
        					
        					String question = LHMQ.get("question").toString();
        					
        					float answerT = Float.parseFloat(LHMQ.get("answer").toString());
        					
        					Question que = new Question(question, answerT);
        					
        					if(que.checkAnswer(answer)) {
        						
        						if(session.getAttributes().containsKey(ANSWERS_CORRECT_ID)) {
        							
        							if((Integer)session.getAttribute(ANSWERS_CORRECT_ID) == 4) {
        								
        								if((Integer)session.getAttribute(CURRENT_LEVEL_ID) == 5) {
        									
        									speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/winner.mp3\"/> Wow! You are amazing! You just beat the hardest mathematics game in the history! " + 
        												"According to whoever's statistics, only 0.11 percent of our players can get through the game! " + 
        												"Congratulations! You are welcome to try the game again at any time! ";
        									
            								session.setAttribute(HAVE_ANSWER_ID, ASK_QUESTION);
            								
            								session.setAttribute(ANSWERS_WRONG_ID, 0);
            								
            								session.setAttribute(ANSWERS_CORRECT_ID, 0);
            								
            								session.setAttribute(CURRENT_QUESTION_ID, null);
            								
            								session.setAttribute(CURRENT_LEVEL_ID, 1);
            								
        									session.setAttribute(STAGE_ID, ASK_MODE_STAGE);
        								}else {
        									speechText = "Great Job! You have beaten Level " + level + ". " +
        											"Now you will be challenged with questions from level " + (level + 1) + ". " + 
        											"Prepare for it! Here is your question:        ";
        								
        								session.setAttribute(CURRENT_LEVEL_ID, level + 1);
        								
        								Question queN = generateQuestion((Integer)session.getAttribute(CURRENT_LEVEL_ID));
        								
        								speechText += queN.getQuestion();
        								
        								session.setAttribute(ANSWERS_CORRECT_ID, 0);
        								
        								session.setAttribute(CURRENT_QUESTION_ID, queN);
        								
        								LocalDateTime nowA = LocalDateTime.now();
        								
        			    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
        								}

        							}else {
        								int answersCorrectBefore = (Integer)session.getAttribute(ANSWERS_CORRECT_ID);
        								
        								session.setAttribute(ANSWERS_CORRECT_ID, answersCorrectBefore + 1);
        								
        								speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/correct.mp3\"/> Congratulations! You got the question correct. Let's continue to the next one.       ";
        								
        								if((Integer)session.getAttribute(ANSWERS_CORRECT_ID) >= 3) {
        									if((Integer)session.getAttribute(ANSWERS_CORRECT_ID) == 1) {
        										speechText += "Only one more to go! Keep up! Here is the question. ";
        									}else {
        										speechText += "Only " + (5 - (Integer)session.getAttribute(ANSWERS_CORRECT_ID)) + " questions to go! Here is the new question: ";
        									}
        								}
        								
        								Question queN = generateQuestion((Integer)session.getAttribute(CURRENT_LEVEL_ID));
        								
        								speechText += queN.getQuestion();
        								
        								session.setAttribute(CURRENT_QUESTION_ID, queN);
        								
        								LocalDateTime nowA = LocalDateTime.now();
        								
        			    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
        							}
        							
        						}else {
        							session.setAttribute(ANSWERS_CORRECT_ID, 1);
        							
        							speechText = "Great first try! Let's continue to the next question.           ";
        							
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
        								speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/incorrect.mp3\"/> Ah! Sorry, you have used all five chances of wrong answers. Try again!";
        								
        								session.setAttribute(HAVE_ANSWER_ID, ASK_QUESTION);
        								
        								session.setAttribute(ANSWERS_WRONG_ID, 0);
        								
        								session.setAttribute(ANSWERS_CORRECT_ID, 0);
        								
        								session.setAttribute(CURRENT_QUESTION_ID, null);
        								
        								session.setAttribute(CURRENT_LEVEL_ID, 1);
        								
        								session.setAttribute(STAGE_ID, ASK_MODE_STAGE);
        								
        							}else {
        								session.setAttribute(ANSWERS_WRONG_ID, answersWrong + 1);
        								
        								speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/incorrect.mp3\"/> Sorry, you got the answer Wrong. " +
        										"The question is       " + que.getQuestion() +
        										"And the correct answer is        " + que.getAnswer() + ". " +
        										"Better luck next Time! Here is the next question:        ";
        								
        								Question queN = generateQuestion((Integer)session.getAttribute(CURRENT_LEVEL_ID));
        								
        								speechText += queN.getQuestion();
            							
            							session.setAttribute(CURRENT_QUESTION_ID, queN);
            							
            							LocalDateTime nowA = LocalDateTime.now();
        								
        			    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
        							}   							
        						}else {
        							session.setAttribute(ANSWERS_WRONG_ID, 1);
        							
        							speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/incorrect.mp3\"/> Sorry, you got the answer Wrong. " +
        										"The question is        " + que.getQuestion() +
        										"And the correct answer is         " + que.getAnswer() + ". " +
        										"Better luck next Time! Here is the next question:        ";
        							
        							Question queN = generateQuestion(1);
        							
        							speechText += queN.getQuestion();
        							
        							session.setAttribute(CURRENT_QUESTION_ID, queN);
        							
        							LocalDateTime nowA = LocalDateTime.now();
    								
    			    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
        						}
        						
        					}
        					
        				}else {
        					
        					LinkedHashMap LHMQ = (LinkedHashMap)session.getAttribute(CURRENT_QUESTION_ID);	
        					
        					String question = LHMQ.get("question").toString();
        					
        					float answerT = Float.parseFloat(LHMQ.get("answer").toString());
        					
        					Question que = new Question(question, answerT);
        					
        					if(session.getAttributes().containsKey(ANSWERS_WRONG_ID)) {
    							int answersWrong = (Integer)session.getAttribute(ANSWERS_WRONG_ID);
    							
    							if(answersWrong == 4) {
    								speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/loser.mp3\"/> Ah! Sorry, you have used all five chances of wrong answers. Try again later! You are back in the main menu, which mode would you like to play?";
    								
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
    								
    								speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/incorrect.mp3\"/> Mind the Timing! The question you missed is   	 " + que.getQuestion() +
    										"And the correct answer is		 " + que.getAnswer() + ". " +
    										"Better luck next Time! Here is the next question:		 ";
    								
    								Question queN = generateQuestion((Integer)session.getAttribute(CURRENT_LEVEL_ID));
    								
    								session.setAttribute(CURRENT_QUESTION_ID, queN);
    								
    								speechText += queN.getQuestion();
    								
    								LocalDateTime nowA = LocalDateTime.now();
    								
    			    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
    							}   							
    						}else {
    							session.setAttribute(ANSWERS_WRONG_ID, 1);
    							
    							speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/incorrect.mp3\"/> Mind the timing! The question you missed is      " + que.getQuestion() +
    										"And the correct answer is      " + que.getAnswer() + ". " +
    										"Better luck next Time! Here is the next question:        ";
    							
    							Question queN = generateQuestion(1);
    							
    							session.setAttribute(CURRENT_QUESTION_ID, queN);
    							
    							speechText += queN.getQuestion();
    							
    							LocalDateTime nowA = LocalDateTime.now();
    							
    		    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
    						}
        					
        				}
        			}
    			}else {
    				speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/incorrect.mp3\"/> You answer is invalid! ";
    			}

    		}else {
    			session.setAttribute(HAVE_ANSWER_ID, HAVE_ANSWER);
        		
        		Question que = generateQuestion(1);
        		
        		session.setAttribute(CURRENT_QUESTION_ID, que);
        		
        		session.setAttribute(CURRENT_LEVEL_ID, 1);
        		
        		speechText = "Your first question is.           ";
        		
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
    		
    		speechText = "Your first question is. 		 ";
    		
    		speechText += que.getQuestion();
    		
    		LocalDateTime nowA = LocalDateTime.now();
			
			session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
    	}
    	
        if((Integer)session.getAttribute(HAVE_ANSWER_ID) == ASK_QUESTION) {
        	repromptText = "Would you like to play again? ";
        }else if((Integer)session.getAttribute(STAGE_ID) == ASK_MODE_STAGE) {
        	repromptText = "Please choose the mode you want to play";
        }
        else {
        	repromptText = "You have exceeded the maximum amount of time to answer the question. Say \'continue\' or \'next question\' to continue";
        }

        return getSpeechletResponse(speechText, repromptText, true);
    }
    
	private SpeechletResponse GetHelpResponse(Intent intent) {
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
