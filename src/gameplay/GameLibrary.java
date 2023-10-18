package gameplay;

import java.util.HashMap;
import java.util.Map;

public class GameLibrary {
	private static Map<String, Game> games = new HashMap<>();

	public static boolean containsGame(String id) {
		if (games.keySet().contains(id)) {
			return true;
		}
		else {
			return false;
		}
	}

	public static void add(String id, Game game) {
		games.put(id, game);
	}
}
