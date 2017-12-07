package fastmath;

public class Question {

	private static String QUESTION_SPEECH;
	
	private static float ANSWER;
	
	Question(String questionSpeech, int answer){
		
		QUESTION_SPEECH = questionSpeech;
		
		ANSWER = answer;
		
	}
	
	public String getQuestion() {
		return QUESTION_SPEECH;
	}
	
	public float getAnswer() {
		return ANSWER;
	}
	
	public boolean checkAnswer(float answer) {
		return answer == ANSWER;
	}
}
