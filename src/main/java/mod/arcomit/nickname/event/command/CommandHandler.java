package mod.arcomit.nickname.event.command;

import mod.arcomit.nickname.NicknameMod;
import mod.arcomit.nickname.arguments.NicknameArgument;
import mod.arcomit.nickname.init.NnAttachments;
import mod.arcomit.nickname.init.NnGameRules;
import mod.arcomit.nickname.event.nickname.NicknameMap;
import java.util.UUID;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

/**
 * 指令处理器
 *
 * @author Mitok
 * @since 2026-01-01
 */
@EventBusSubscriber(modid = NicknameMod.MODID)
public class CommandHandler {

	@SubscribeEvent
	public static void onRegisterCommands(RegisterCommandsEvent event) {
		event.getDispatcher().register(Commands.literal("nickname")
			// ==================== SET 分支 ====================
			.then(Commands.literal("set")
				.then(Commands.argument("name", NicknameArgument.nickname())
					.executes(context -> {
						ServerPlayer player = context.getSource().getPlayerOrException();
						// 获取参数输入
						String newNick = context.getArgument("name", String.class);
						// 检查重复
						if (!context.getSource().getLevel().getGameRules().getBoolean(NnGameRules.ALLOW_DUPLICATE_NICKNAME)) {
							// 优先从全局映射表中检查
							NicknameMap map = NicknameMap.get(context.getSource().getServer());
							UUID existingOwner = map.getPlayerByNickname(newNick);
							if (existingOwner != null && !existingOwner.equals(player.getUUID())) {
								context.getSource().sendFailure(Component.translatable("nickname.set.duplicate", newNick));
								return 0;
							}
						}
						if (context.getSource().getLevel().getGameRules().getBoolean(NnGameRules.DISALLOW_NICKNAME_CHANGE)) {
							context.getSource().sendFailure(Component.translatable("nickname.set.change_disallowed"));
							return 0;
						}

						// 保存数据（attachment sync 会自动同步给客户端）
						player.setData(NnAttachments.NICKNAME, newNick);
						// 更新全局映射表
						NicknameMap.get(context.getSource().getServer()).setNickname(player.getUUID(), newNick);

						// 刷新服务端名字
						player.refreshDisplayName();
						player.refreshTabListName();

						context.getSource().sendSuccess(() -> Component.translatable("nickname.set.success", newNick), false);

						return 1;
					})
				)
			)
			// ==================== CLEAR 分支 ====================
			.then(Commands.literal("clear")
				.executes(context -> {
					if (context.getSource().getLevel().getGameRules().getBoolean(NnGameRules.DISALLOW_NICKNAME_CHANGE)) {
						context.getSource().sendFailure(Component.translatable("nickname.set.change_disallowed"));
						return 0;
					}
					ServerPlayer player = context.getSource().getPlayerOrException();
					// 清除数据（attachment sync 会自动同步给客户端）
					player.setData(NnAttachments.NICKNAME, "");
					// 更新全局映射表
					NicknameMap.get(context.getSource().getServer()).removeNickname(player.getUUID());

					// 刷新服务端名字
					player.refreshDisplayName();
					player.refreshTabListName();

					context.getSource().sendSuccess(() -> Component.translatable("nickname.clear.success"), false);
					return 1;
				})
				// 管理员清除指定玩家的昵称
				.then(Commands.argument("target", EntityArgument.player())
					.requires(source -> source.hasPermission(2))
					.executes(context -> {
						ServerPlayer target = EntityArgument.getPlayer(context, "target");
						// 清除数据（attachment sync 会自动同步给客户端）
						target.setData(NnAttachments.NICKNAME, "");
						// 更新全局映射表
						NicknameMap.get(context.getSource().getServer()).removeNickname(target.getUUID());

						// 刷新服务端名字
						target.refreshDisplayName();
						target.refreshTabListName();

						context.getSource().sendSuccess(() -> Component.translatable("nickname.clear.other.success", target.getName()), false);
						return 1;
					})
				)
			)
		);
	}
}