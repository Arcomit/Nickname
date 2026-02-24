package mod.arcomit.nickname.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.network.chat.Component;

/**
 * TODO：描述
 *
 * @author Arcomit
 * @since 2026-02-23
 */
public class NicknameArgument implements ArgumentType<String> {
	private static final SimpleCommandExceptionType ERROR_TOO_LONG = new SimpleCommandExceptionType(
		Component.translatable("nickname.error.too_long")
	);
	private static final SimpleCommandExceptionType ERROR_EMPTY = new SimpleCommandExceptionType(
		Component.translatable("nickname.error.empty")
	);

	public static NicknameArgument nickname() {
		return new NicknameArgument();
	}

	@Override
	public String parse(StringReader reader) throws CommandSyntaxException {
		int start = reader.getCursor();

		String text = reader.getRemaining();
		reader.setCursor(reader.getTotalLength());

		String trimmed = text.trim();

		if (trimmed.isEmpty()) {
			reader.setCursor(start);
			throw ERROR_EMPTY.createWithContext(reader);
		}

		if (trimmed.length() > 8) {
			reader.setCursor(start + 8);
			throw ERROR_TOO_LONG.createWithContext(reader);
		}

		return trimmed;
	}
}