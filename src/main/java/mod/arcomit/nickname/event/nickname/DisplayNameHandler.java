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
 * 聊天栏显示名字处理器
 *
 * @author Arcomit
 * @since 2026-02-23
 */
@EventBusSubscriber(modid = NicknameMod.MODID)
public class DisplayNameHandler {

	@SubscribeEvent
	public static void onNameFormat(PlayerEvent.NameFormat event) {
		Player player = event.getEntity();
		String nick = player.getData(NnAttachments.NICKNAME);

		if (nick != null && !nick.isEmpty()) {
			// 格式: 昵称[原游戏ID]
			Component newName = Component.literal(nick)
				.append(Component.literal("[" + player.getGameProfile().getName() + "]").withStyle(ChatFormatting.GRAY));
			event.setDisplayname(newName);
		}
	}
}
