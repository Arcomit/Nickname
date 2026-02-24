package mod.arcomit.nickname.event.nickname;

import mod.arcomit.nickname.NicknameMod;
import mod.arcomit.nickname.init.NnAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

/**
 * Tab列表显示名称处理器。
 *
 * @author Arcomit
 * @since 2026-02-23
 */
@EventBusSubscriber(modid = NicknameMod.MODID)
public class TabListNameHandler {

	@SubscribeEvent
	public static void onTabListFormat(PlayerEvent.TabListNameFormat event) {
		Player player = event.getEntity();
		String nick = player.getData(NnAttachments.NICKNAME);

		if (nick != null && !nick.isEmpty()) {
			// 格式: 昵称[原游戏ID]
			Component newName = Component.literal(nick)
				.append(Component.literal("[" + player.getGameProfile().getName() + "]").withStyle(ChatFormatting.GRAY));
			event.setDisplayName(newName);
		}
	}
}
