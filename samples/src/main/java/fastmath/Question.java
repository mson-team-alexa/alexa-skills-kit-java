package fastmath;

public class Question {

	private static String QUESTION_SPEECH;
	
	private static float ANSWER;
	
	public Question(String questionSpeech, float f){
		
		QUESTION_SPEECH = questionSpeech;
		
		ANSWER = f;
		
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
