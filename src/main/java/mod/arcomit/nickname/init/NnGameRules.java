package mod.arcomit.nickname.init;

import net.minecraft.world.level.GameRules;

public class NnGameRules {
	public static GameRules.Key<GameRules.BooleanValue> ALLOW_DUPLICATE_NICKNAME;
	public static GameRules.Key<GameRules.BooleanValue> DISALLOW_NICKNAME_CHANGE;

	public static void register() {
		ALLOW_DUPLICATE_NICKNAME = GameRules.register("allowDuplicateNickname", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
		DISALLOW_NICKNAME_CHANGE = GameRules.register("disallowNicknameChange", GameRules.Category.PLAYER, GameRules.BooleanValue.create(false));
	}
}

