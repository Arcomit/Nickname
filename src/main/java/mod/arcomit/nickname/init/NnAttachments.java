package mod.arcomit.nickname.init;

import com.mojang.serialization.Codec;
import mod.arcomit.nickname.NicknameMod;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentSyncHandler;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * 数据附件注册。
 *
 * @author Arcomit
 * @since 2026-02-23
 */
public class NnAttachments {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
		DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, NicknameMod.MODID);

	public static final Supplier<AttachmentType<String>> NICKNAME = ATTACHMENT_TYPES.register(
		"nickname",
		() -> AttachmentType.builder(() -> "")
			.serialize(Codec.STRING)
			.sync(new AttachmentSyncHandler<String>() {
				@Override
				public void write(RegistryFriendlyByteBuf buf, String attachment, boolean initialSync) {
					ByteBufCodecs.STRING_UTF8.encode(buf, attachment);
				}

				@Override
				@OnlyIn(Dist.CLIENT)
				public @Nullable String read(IAttachmentHolder holder, RegistryFriendlyByteBuf buf, @Nullable String previousValue) {
					String value = ByteBufCodecs.STRING_UTF8.decode(buf);
					// 客户端收到昵称数据后，刷新对应玩家的显示名字
					if (holder instanceof Player player) {
						Minecraft.getInstance().execute(() -> {
							// 同时刷新该玩家的显示名称
							if (player != null) {
								player.refreshDisplayName();
							}
						});
					}
					return value;
				}
			})
			.copyOnDeath()
			.build()
	);

	public static void register(IEventBus bus) {
		ATTACHMENT_TYPES.register(bus);
	}
}
