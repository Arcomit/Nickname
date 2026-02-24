package mod.arcomit.nickname.init;

import mod.arcomit.nickname.NicknameMod;
import mod.arcomit.nickname.arguments.NicknameArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

/**
 * TODO：描述
 *
 * @author Arcomit
 * @since 2026-02-23
 */
public class NnArguments {
	public static final DeferredRegister<ArgumentTypeInfo<?, ?>> ARGUMENT_TYPES =
		DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, NicknameMod.MODID);

	public static final Supplier<ArgumentTypeInfo<NicknameArgument, ?>> NICKNAME_ARG =
		ARGUMENT_TYPES.register(
			"nickname_arg",
			() -> ArgumentTypeInfos.registerByClass(NicknameArgument.class, SingletonArgumentInfo.contextFree(NicknameArgument::nickname))
		);

	public static void register(IEventBus bus) {
		ARGUMENT_TYPES.register(bus);
	}
}
