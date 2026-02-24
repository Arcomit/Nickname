package mod.arcomit.nickname;

import com.mojang.logging.LogUtils;
import mod.arcomit.nickname.init.NnArguments;
import mod.arcomit.nickname.init.NnAttachments;
import mod.arcomit.nickname.init.NnGameRules;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

/**
 * 模组主类。
 *
 * @author Mitok
 * @since 2026-02-23
 */
@Mod(NicknameMod.MODID)
public class NicknameMod {
	public static final String MODID = "nickname";
	public static final Logger LOGGER = LogUtils.getLogger();

	public NicknameMod(IEventBus modEventBus, ModContainer modContainer) {
		NnGameRules.register();
		NnAttachments.register(modEventBus);
		NnArguments.register(modEventBus);
		LOGGER.info("Nickname mod initialized!");
	}

	public static ResourceLocation prefix(String path) {
		return ResourceLocation.fromNamespaceAndPath(MODID, path);
	}
}
