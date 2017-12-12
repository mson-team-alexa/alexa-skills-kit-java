package ChooseAdventure;

public class Scenario {
	private final String option1;
	private final String option2;
	private final String outcome2;
	private final String outcome;

	
   public Scenario (String one, String two, String outcome, String out2) {
	   
       this.option1 = one;
       this.option2 = two;
       this.outcome = outcome;
       this.outcome2 = out2;
    }
    
    public String getResponse()
    {
    		return outcome;
    }
    public String getResponse2()
    {
    		return outcome2;
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
