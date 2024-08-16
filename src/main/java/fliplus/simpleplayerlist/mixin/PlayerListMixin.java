package fliplus.simpleplayerlist.mixin;

import fliplus.simpleplayerlist.config.SimplePlayerListConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTickRateManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.TimeUtil;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(PlayerList.class)
public class PlayerListMixin {
	@Shadow
	@Final
	private MinecraftServer server;

	@Unique
	private int headerUpdateTimer;

	@Inject(at = @At("HEAD"), method = "tick")
	private void tick(CallbackInfo info) {
		SimplePlayerListConfig config = SimplePlayerListConfig.INSTANCE;

		if (config.enableMod && ++this.headerUpdateTimer > config.tickUpdateInterval) {
			for (ServerPlayer player : server.getPlayerList().getPlayers()) {
				if (SimplePlayerListConfig.disabledPlayers.contains(player)) continue;

				player.connection.send(new ClientboundTabListPacket(
						replacePlaceholders(player, config.header),
						replacePlaceholders(player, config.footer)
				));

				this.headerUpdateTimer = 0;
			}
		}
	}

	@Unique
	private MutableComponent replacePlaceholders(ServerPlayer player, String text) {
		double MSPT = ((double) server.getAverageTickTimeNanos()) / TimeUtil.NANOSECONDS_PER_MILLISECOND;
		ServerTickRateManager tickManager = server.tickRateManager();

		double TPS = 1000.0 / Math.max(tickManager.isSprinting() ? 0.0 : tickManager.millisecondsPerTick(), MSPT);
		if (tickManager.isFrozen()) TPS = 0;

		int ping = player.connection.latency();

		int day = (int) (server.overworld().getDayTime() / 24000);

		String formatedMSPT = heatmapColor(formatDecimal(MSPT), MSPT, tickManager.millisecondsPerTick());
		String formatedTPS = heatmapColor(formatDecimal(TPS), MSPT, tickManager.millisecondsPerTick());
		String formatedPing = heatmapColor(ping + "ms", ping, 200);

		text = text
				.replaceAll("(?i)%mspt%", formatedMSPT)
				.replaceAll("(?i)%tps%", formatedTPS)
				.replaceAll("(?i)%ping%", formatedPing)
				.replaceAll("(?i)%day%", String.valueOf(day));

		return parseHexColor(text);
	}

	@Unique
	private String formatDecimal(double value) {
		return String.valueOf(Math.round(value * 10) / 10.0);
	}

	@Unique
	private String heatmapColor(String text, double actual, double reference) {
		String color = "";
		if (actual >= 0.0D) color = "§a";
		if (actual > 0.5D * reference) color = "§e";
		if (actual > 0.8D * reference) color = "§c";
		if (actual > reference) color = "§d";
		return "§r" + color + text + "§r";
	}

	@Unique
	private MutableComponent parseHexColor(String text) {
		MutableComponent result = Component.literal("");

		Matcher textMatcher = Pattern.compile("§#[0-9a-fA-F]{6}[^§]*").matcher(text);

		int lastEnd = 0;
		while (textMatcher.find()) {
			if (textMatcher.start() > lastEnd) {
				result.append(text.substring(lastEnd, textMatcher.start()));
			}

			Matcher hexMatcher = Pattern.compile("#[0-9a-fA-F]{6}").matcher(textMatcher.group());
			if (hexMatcher.find()) {
				TextColor color = TextColor.fromRgb(Integer.parseInt(hexMatcher.group().substring(1), 16));
				result.append(Component.literal(textMatcher.group().substring(8)).setStyle(Style.EMPTY.withColor(color)));
			}

			lastEnd = textMatcher.end();
		}

		if (lastEnd < text.length()) {
			result.append(text.substring(lastEnd));
		}

		return result;
	}
}