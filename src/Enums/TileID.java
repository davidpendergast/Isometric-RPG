package Enums;

import java.awt.Color;

public enum TileID {
	//these labels are order specific. They must match up with sprite sheet.
	DIRT, OCEAN, OCEAN_ROCKY, STONE, BRIDGE_LEFT, BRIDGE_CENTER, GRASS, SAND, HOLE, SWAMP, BRIDGE_RIGHT;
	
	public static TileID getRandomID(){
		TileID[] id_list = TileID.values();
		int rand = (int)(Math.random()*id_list.length);
		return id_list[rand];
	}
	
	/**
	 * @return
	 */
	public boolean isTraversable(){
		switch(this){
			case OCEAN:
			case OCEAN_ROCKY:
			case HOLE:
				return false;
			default:
				return true;
		}
		
	}
	
	/**
	 * Converts given string to enum of type TileID.
	 * @return TileID with equivalent name (non-case specific), or TileID.GRASS if none exists.
	 */
	public TileID getTileID(String name){
		TileID[] values = TileID.values();
		for(TileID id : values){
			if(name.equalsIgnoreCase(id.toString())){
				return id;
			}
		}
		
		return GRASS;
	}
	
	public static Color getIDColor(TileID id){
		
		if(!id.isTraversable())
			return Color.DARK_GRAY;
		
		switch(id){
		case GRASS:
			return Color.GREEN;
		case DIRT:
			return Color.ORANGE;
		case OCEAN_ROCKY:
		case OCEAN:
			return Color.BLUE;
		case SAND:
			return Color.YELLOW;
		case BRIDGE_RIGHT:
		case BRIDGE_LEFT:
		case BRIDGE_CENTER:
			return Color.MAGENTA;
		case SWAMP:
			return Color.GREEN;
		case STONE:
			return Color.LIGHT_GRAY;
		default:
			return Color.WHITE;
		}
	}
}
