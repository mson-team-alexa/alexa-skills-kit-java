package ChooseAdventure;

public class Scenario {
	private final String option1;
	private final String option2;
	private final String outcome;

	
   public Scenario (String one, String two, String outcome) {
       this.option1 = one;
       this.option2 = two;
       this.outcome = outcome;
    }
    
    public String getResponse()
    {
    		return outcome;
    }
    public String getop1()
    {
    		return option1;
    }
    public String getop2()
    {
    		return option2;
    }
   
}
