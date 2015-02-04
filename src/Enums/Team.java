package Enums;

public enum Team {
	GOOD, BAD, NEUTRAL, ANY, ANY_NOT_GOOD, ANY_NOT_BAD, ANY_NOT_NEUTRAL;
	
	public Team opposite(){
		if(this == GOOD){
			return BAD;
		}
		else if(this == BAD){
			return GOOD;
		}
		
		else return NEUTRAL;
	}
	
	public boolean isTeam(Team t){
		if(t == ANY)
			return true;
		
		if(t == ANY_NOT_GOOD && this != GOOD)
			return true;
		
		if(t == ANY_NOT_BAD && this != BAD)
			return true;
		
		if(t == ANY_NOT_NEUTRAL && this != NEUTRAL)
			return true;
		
		else return this == t;
	}
}
