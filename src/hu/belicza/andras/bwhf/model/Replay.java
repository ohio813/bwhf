package hu.belicza.andras.bwhf.model;

import java.util.List;
import java.util.Map;

/**
 * 
 * @author Andras Belicza
 */
public class Replay {
	
	/** The players action map. The key is the player name, the value is the player's action list. */
	public final Map< String, List< Action > > playerNameActionListMap;
	
	public final Player[] players;
	
	public Replay( final Map< String, List< Action > >playerNameActionListMap ) {
		this.playerNameActionListMap = playerNameActionListMap;
		
		players = new Player[ playerNameActionListMap.size() ];
		
		int i = 0;
		for ( final Map.Entry< String, List< Action > > playerNameActionListEntry : playerNameActionListMap.entrySet() ) {
			final String         playerName       = playerNameActionListEntry.getKey();
			final List< Action > playerActionList = playerNameActionListEntry.getValue();
			final Action[]       playerActions    = playerActionList.toArray( new Action[ playerActionList.size() ] );
			
			players[ i++ ] = new Player( playerName, playerActions );
		}
	}
	
}
