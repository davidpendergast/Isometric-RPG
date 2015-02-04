package Main;

import Enums.RenderStrategy;
import Enums.Team;

public class RenderSettings {
	
	private static RenderStrategy[] render_strategies;
	private static int render_strat_num = 0;
	private static boolean show_enemy_health = true;
	private static boolean show_friendly_health = true;
	private static boolean show_player_health = true;
	
	static{
		render_strategies = RenderStrategy.values();
	}
	
	public static RenderStrategy getRenderStrategy(){
		return render_strategies[render_strat_num];
	}
	
	public static void incrementRenderStrategy(){
		render_strat_num = (render_strat_num + 1) % render_strategies.length;
	}
	
	public static void toggleShowEnemyHealth(){
		show_enemy_health = !show_enemy_health;
	}
	
	public static boolean showEnemyHealth(){
		return show_enemy_health;
	}
	
	public static void toggleShowFriendlyHealth(){
		show_friendly_health = !show_friendly_health;
	}
	
	public static boolean showFriendlyHealth(){
		return show_friendly_health;
	}
	
	public static void toggleShowPlayerHealth(){
		show_player_health = !show_player_health;
	}
	
	public static boolean showPlayerHealth(){
		return show_player_health;
	}
	
	public static boolean showHealthBar(Team t){
		if(t == Team.BAD)
			return showEnemyHealth();
		else if(t == Team.GOOD)
			return showFriendlyHealth();
		else
			return showEnemyHealth();
	}

}
