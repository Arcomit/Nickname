package mod.arcomit.nickname.event.nickname;

import mod.arcomit.nickname.NicknameMod;
import mod.arcomit.nickname.init.NnAttachments;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * 监听玩家登录事件，确保全局昵称映射表是最新的。
 *
 * @author Mitok
 * @since 2026-02-23
 */
@EventBusSubscriber(modid = NicknameMod.MODID)
public class NicknameMapHandler {
	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer player)) {
			return;
		}
		String nick = player.getData(NnAttachments.NICKNAME);
		if (!nick.isEmpty() && player.getServer() != null) {
			NicknameMap.get(player.getServer()).setNickname(player.getUUID(), nick);
		}
	}
}


