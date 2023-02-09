package Problem;

import java.util.HashMap;

class BiPlayer extends Player {
    public enum PlayerCharacter {
        MAX,
        MIN
    }

    public PlayerCharacter character;
    public static HashMap<String, BiPlayer> AvailiablePlayerList = new HashMap<>() {{
        put("MAX", new BiPlayer(PlayerCharacter.MAX));
        put("MIN", new BiPlayer(PlayerCharacter.MIN));
    }};

    private BiPlayer(final PlayerCharacter character) {
        this.character = character;
    }
}

public abstract class BiPlayerGame {
    private final BiPlayer MAX_PLAYER;
    private final BiPlayer MIN_PLAYER;

    public BiPlayerGame() {
        MAX_PLAYER = BiPlayer.AvailiablePlayerList.get("MAX");
        MIN_PLAYER = BiPlayer.AvailiablePlayerList.get("MIN");
    }
}
