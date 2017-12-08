/**
    Copyright 2014-2015 Amazon.com, Inc. or its affiliates. All Rights Reserved.

    Licensed under the Apache License, Version 2.0 (the "License"). You may not use this file except in compliance with the License. A copy of the License is located at

        http://aws.amazon.com/apache2.0/

    or in the "license" file accompanying this file. This file is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */
package fastmath;


import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.time.Duration;
import java.time.Instant;

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
    private static final Logger log = LoggerFactory.getLogger(TestSurvivalModeCode.class);

    private static final String STAGE_ID = "StageID";
    private static final int ASK_MODE_STAGE = 0;
    private static final int ASK_ANSWER_STAGE = 1;
    
    private static final String HAVE_ANSWER_ID = "HaveAnswerID";
    private static final int HAVE_ANSWER = 0;
    private static final int ASK_QUESTION = 1;
    
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
            return setUpSurvivalStage(intent, session);
        }else if ("AMAZON.HelpIntent".equals(intentName)) {
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
    	
    	Slot GamemodeSlot = intent.getSlot("GameMode");
    	
    	String speechText = "";
    	
    	String repromptText = "";
    	
    	if(GamemodeSlot != null && GamemodeSlot.getValue() != null) {
    		
    		String modeSlot = GamemodeSlot.getValue();

    		switch(modeSlot) {
    		case "Survival":
    			speechText = "Welcome to Survival Mode! Here you will be challenged with questions according to the level you are in. " +
    						"If you get five answers correct at your current level, you will advance to next level. " +
    						"For each question, you have at most eight seconds to answer. " +
    						"If you spend more than eight seconds, the question will be counted wrong. " +
    						"You can have at most 5 questions wrong. " + 
    						"Ready to start the game? Say Begin or Continue to proceed. ";
    			
    			repromptText = "Ready to start the game? Say Begin or Continue to go ahead. ";
    			
    			session.setAttribute(STAGE_ID, ASK_ANSWER_STAGE);
    			
    			break;
    		
    		case "Practice":
    			break;
    		
    		case "Time Trial":
    			break;
    		
    		default:
    			break;
    		}
    		
    		return getSpeechletResponse(speechText, repromptText, true);
    	}else {
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
    		
    		int randY = RAND.nextInt(100) + 10;
    		
    		int randZ = RAND.nextInt(1);
    		
    		if(randZ == 0) {
    			
    			int answer = randX + randY;
    			
    			String speech = "What is " + randX + " plus " + randY + " ? ";
    			
    			Question que = new Question(speech, answer);
    			
    			return que;
    		}else {
    			int answer  =  randX - randY;
    			
    			String speech = "What is " + randX + " minus " + randY + " ? ";
    			
    			Question que = new Question(speech, answer);
    			
    			return que;
    		}
    		
    		
    	case 2:
    		return null;
    		
    	default:
    		return null;
    	}
    }
    
    
    
    private SpeechletResponse setUpSurvivalStage(final Intent intent, final Session session) {
        // Get the slots from the intent.
    	
    	String speechText, repromptText;
    	
    	speechText = "";
    	
    	if((Integer)session.getAttribute(STAGE_ID) == ASK_MODE_STAGE) {
    		speechText = "You have to choose which mode you want to play before you enter the play mode!" +
    				"There are three choices: Survival, practice and Time Trial. What is your choice? ";
    	}
    	
    	if(session.getAttributes().containsKey(HAVE_ANSWER_ID) && speechText == "") {
    		
    		if((Integer)session.getAttribute(HAVE_ANSWER_ID) == HAVE_ANSWER) {
    			
    			Slot answerSlot = intent.getSlot("Answer");
    			
    			float answer = Float.parseFloat(answerSlot.getValue());
    			
    			int level = (Integer)session.getAttribute(CURRENT_LEVEL_ID);
    			
    			if(session.getAttributes().containsKey(ASK_QUESTION_TIME_ID)) {
    				
    				Instant now = Instant.now();
    				Duration timeElapsed = Duration.between((Instant)session.getAttribute(ASK_QUESTION_TIME_ID), now);
    		
    				if(timeElapsed.toMillis() < 8000) {
    					
    					Question que = (Question)session.getAttribute(CURRENT_QUESTION_ID);
    					
    					if(que.checkAnswer(answer)) {
    						
    						if(session.getAttributes().containsKey(ANSWERS_CORRECT_ID)) {
    							
    							if((Integer)session.getAttribute(ANSWERS_CORRECT_ID) == 4) {
    								
    								if((Integer)session.getAttribute(CURRENT_LEVEL_ID) == 5) {
    									
    									speechText = "Wow! You are amazing! You just beat the hardest mathematic game in the history! " + 
    												"According to whoever's statistics, only 5 percent of our players can get through the game! " + 
    												"Congratulations! You are welcome to try the game again at any time! ";
    									
    									session.setAttribute(STAGE_ID, ASK_MODE_STAGE);
    								}else {
    									speechText = "Great Job! You have beaten Level " + level + ". " +
    											"Now you will be challenged with questions from " + (level + 1) + ". " + 
    											"Prepare for it! Here is your question: ";
    								
    								session.setAttribute(CURRENT_LEVEL_ID, level + 1);
    								
    								Question queN = generateQuestion((Integer)session.getAttribute(CURRENT_LEVEL_ID));
    								
    								speechText += queN.getQuestion();
    								
    								session.setAttribute(CURRENT_QUESTION_ID, queN);
    								
    								Instant nowA = Instant.now();
    								
    			    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
    								}

    							}else {
    								int answersCorrectBefore = (Integer)session.getAttribute(ANSWERS_CORRECT_ID);
    								
    								session.setAttribute(ANSWERS_CORRECT_ID, answersCorrectBefore + 1);
    								
    								speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/correct.mp3\"/>Congratulations! You got the question correct. Let's continue to the next one.";
    								
    								Question queN = generateQuestion((Integer)session.getAttribute(CURRENT_LEVEL_ID));
    								
    								speechText += queN.getQuestion();
    								
    								session.setAttribute(CURRENT_QUESTION_ID, queN);
    								
    								Instant nowA = Instant.now();
    								
    			    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
    							}
    							
    						}else {
    							session.setAttribute(ANSWERS_CORRECT_ID, 1);
    							
    							speechText = "Great first try! Let's continue to the next question.";
    							
    							Question queN = generateQuestion(1);
    							
    							session.setAttribute(CURRENT_QUESTION_ID, queN);
    							
    							speechText += queN.getQuestion();
    							
								Instant nowA = Instant.now();
								
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
    								
    							}else {
    								session.setAttribute(ANSWERS_WRONG_ID, answersWrong + 1);
    								
    								speechText = "<audio src=\"https://s3.amazonaws.com/cschool0/incorrect.mp3\"/>Sorry, you got the answer Wrong. " +
    										"The question is " + que.getQuestion() +
    										"And the correct answer is " + que.getAnswer() + ". " +
    										"Better luck next Time! Here is the next question: ";
    								
    								Question queN = generateQuestion((Integer)session.getAttribute(CURRENT_LEVEL_ID));
    								
    								speechText += queN.getQuestion();
        							
        							session.setAttribute(CURRENT_QUESTION_ID, queN);
        							
    								Instant nowA = Instant.now();
    								
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
    							
								Instant nowA = Instant.now();
								
			    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
    						}
    						
    					}
    					
    				}else {
    						
    					Question que = (Question)session.getAttribute(CURRENT_QUESTION_ID);
    					
    					if(session.getAttributes().containsKey(ANSWERS_WRONG_ID)) {
							int answersWrong = (Integer)session.getAttribute(ANSWERS_WRONG_ID);
							
							if(answersWrong == 4) {
								speechText = "Ah! Sorry, you have used all five chances of wrong answers. Try again!";
								
								session.setAttribute(HAVE_ANSWER_ID, ASK_QUESTION);
								
								session.setAttribute(ANSWERS_WRONG_ID, 0);
								
								session.setAttribute(ANSWERS_CORRECT_ID, 0);
								
								session.setAttribute(CURRENT_QUESTION_ID, null);
								
								session.setAttribute(CURRENT_LEVEL_ID, 1);
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
								
								Instant nowA = Instant.now();
								
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
							
							Instant nowA = Instant.now();
							
		    				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
						}
    					
    				}
    			}
    		}else {
    			session.setAttribute(HAVE_ANSWER_ID, HAVE_ANSWER);
        		
        		Question que = generateQuestion(1);
        		
        		session.setAttribute(CURRENT_QUESTION_ID, que);
        		
        		session.setAttribute(CURRENT_LEVEL_ID, 1);
        		
        		speechText = "You first question is: ";
        		
        		speechText += que.getQuestion();
        		
				Instant nowA = Instant.now();
				
				session.setAttribute(ASK_QUESTION_TIME_ID, nowA);
    		}
    	}else {
    		session.setAttribute(HAVE_ANSWER_ID, HAVE_ANSWER);
    		
    		Question que = generateQuestion(1);
    		
    		session.setAttribute(CURRENT_LEVEL_ID, 1);
    		
    		session.setAttribute(CURRENT_QUESTION_ID, que);
    		
    		speechText = "Your first question is: ";
    		
    		speechText += que.getQuestion();
    		
			Instant nowA = Instant.now();
			
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
