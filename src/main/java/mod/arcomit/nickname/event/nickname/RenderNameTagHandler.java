package mod.arcomit.nickname.event.nickname;

import mod.arcomit.nickname.NicknameMod;
import mod.arcomit.nickname.init.NnAttachments;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderNameTagEvent;

/**
 * TODO：描述
 *
 * @author Arcomit
 * @since 2026-02-23
 */
@OnlyIn(Dist.CLIENT)
@EventBusSubscriber(modid = NicknameMod.MODID)
public class RenderNameTagHandler {

	@SubscribeEvent
	public static void onRenderNameTag(RenderNameTagEvent event) {
		if (!(event.getEntity() instanceof Player player)) {
			return;
		}
		Minecraft mc = Minecraft.getInstance();

		boolean isSneaking = mc.options.keyShift.isDown();

		String nick = player.getData(NnAttachments.NICKNAME);
		if (nick != null && !nick.isEmpty() && !isSneaking) {
			Component Nickname = Component.literal(nick);
			event.setContent(Nickname);
		}
	}
}
