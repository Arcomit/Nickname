package mod.arcomit.nickname.event.nickname;

import mod.arcomit.nickname.NicknameMod;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.saveddata.SavedData;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 昵称映射表，用于全局查询昵称是否被占用（包括离线玩家）。
 *
 * @author Mitok
 * @since 2026-02-23
 */
public class NicknameMap extends SavedData {
	private static final String DATA_NAME = NicknameMod.MODID + "_map";
	private final Map<String, UUID> nicknameToUuid = new HashMap<>();

	@Override
	public @NotNull CompoundTag save(@NotNull CompoundTag compoundTag, HolderLookup.@NotNull Provider provider) {
		ListTag list = new ListTag();
		nicknameToUuid.forEach((nick, uuid) -> {
			CompoundTag entry = new CompoundTag();
			entry.putString("nick", nick);
			entry.putUUID("uuid", uuid);
			list.add(entry);
		});
		compoundTag.put("nicknames", list);
		return compoundTag;
	}

	public static NicknameMap load(CompoundTag tag, HolderLookup.Provider provider) {
		NicknameMap data = new NicknameMap();
		if (tag.contains("nicknames", Tag.TAG_LIST)) {
			ListTag list = tag.getList("nicknames", Tag.TAG_COMPOUND);
			for (int i = 0; i < list.size(); i++) {
				CompoundTag entry = list.getCompound(i);
				String nick = entry.getString("nick");
				UUID uuid = entry.getUUID("uuid");
				data.nicknameToUuid.put(nick, uuid);
			}
		}
		return data;
	}

	public static NicknameMap get(MinecraftServer server) {
		return server.overworld().getDataStorage().computeIfAbsent(new Factory<>(
			NicknameMap::new,
			NicknameMap::load,
			null // Upgrade Data Type
		), DATA_NAME);
	}

	/**
	 * 检查昵称是否已被占用
	 * @param nickname 昵称
	 * @return 占用该昵称的玩家 UUID，如果未被占用则返回 null
	 */
	public UUID getPlayerByNickname(String nickname) {
		return nicknameToUuid.get(nickname);
	}

	/**
	 * 设置玩家的昵称
	 * @param playerUuid 玩家 UUID
	 * @param nickname 新昵称
	 */
	public void setNickname(UUID playerUuid, String nickname) {
		// 先移除该玩家之前的昵称记录（如果存在）
		removeNickname(playerUuid);

		if (nickname != null && !nickname.isEmpty()) {
			nicknameToUuid.put(nickname, playerUuid);
		}
		setDirty();
	}

	/**
	 * 移除玩家的昵称记录
	 * @param playerUuid 玩家 UUID
	 */
	public void removeNickname(UUID playerUuid) {
		nicknameToUuid.values().removeIf(uuid -> uuid.equals(playerUuid));
		setDirty();
	}
}


