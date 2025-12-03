package fliplus.server;

import eu.pb4.placeholders.api.ParserContext;
import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import eu.pb4.placeholders.api.node.TextNode;
import eu.pb4.placeholders.api.parsers.NodeParser;
import fliplus.SimplePlayerList;
import fliplus.config.Settings;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerTickRateManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class TabList {
    public static List<String> disabledPlayers = new ArrayList<>();
    private static int tick = 0;

    public static void tick(MinecraftServer server) {
        if (tick-- > 0) return;
        tick = SimplePlayerList.CONFIG.TickUpdateInterval;

        List<ServerPlayer> players = server.getPlayerList().getPlayers();
        for (ServerPlayer player : players) updateTabList(player);
    }

    public static void updateTabList(ServerPlayer player) {
        if (disabledPlayers.contains(player.getGameProfile().name())) return;

        Settings config = SimplePlayerList.CONFIG;

        TextNode header = TextNode.of(joinLines(config.Header));
        TextNode footer = TextNode.of(joinLines(config.Footer));
        ParserContext context = PlaceholderContext.of(player).asParserContext();

        player.connection.send(new ClientboundTabListPacket(
            parseText(header, context),
            parseText(footer, context)
        ));
    }

    private static String joinLines(List<Object> list) {
        StringBuilder builder = new StringBuilder();

        for (Object element : list) {
            if (element instanceof Number number) {
                builder.append("\n".repeat(Math.max(0, number.intValue())));
            } else {
                builder.append(element).append("\n");
            }
        }

        if (!list.isEmpty()) builder.setLength(builder.length() - 1);

        return builder.toString();
    }

    private static Component parseText(TextNode text, ParserContext parserContext) {
        return NodeParser.builder()
            .quickText()
            .globalPlaceholders()
            .build()
            .parseText(text, parserContext);
    }

    public static void registerPlaceholders() {
        Placeholders.register(ResourceLocation.fromNamespaceAndPath("server", "mspt_colored_alt"), (ctx, arg) -> {
            MinecraftServer server = ctx.server();
            ServerTickRateManager tickRateManager = server.tickRateManager();

            double MSPT = ((double) server.getAverageTickTimeNanos()) / TimeUtil.NANOSECONDS_PER_MILLISECOND;

            String formatedMSPT = String.format("%.1f", MSPT);

            return PlaceholderResult.value(heatmapColor(formatedMSPT, MSPT, tickRateManager.millisecondsPerTick()));
        });

        Placeholders.register(ResourceLocation.fromNamespaceAndPath("server", "tps_colored_alt"), (ctx, arg) -> {
            MinecraftServer server = ctx.server();
            ServerTickRateManager tickRateManager = server.tickRateManager();

            double MSPT = ((double) server.getAverageTickTimeNanos()) / TimeUtil.NANOSECONDS_PER_MILLISECOND;

            double TPS = TimeUtil.MILLISECONDS_PER_SECOND / Math.max(tickRateManager.isSprinting() ? 0 : tickRateManager.millisecondsPerTick(), MSPT);
            if (tickRateManager.isFrozen()) TPS = 0;

            String formatedTPS = String.format("%.1f", TPS);

            return PlaceholderResult.value(heatmapColor(formatedTPS, MSPT, tickRateManager.millisecondsPerTick()));
        });

        Placeholders.register(ResourceLocation.fromNamespaceAndPath("player", "ping_colored_alt"), (ctx, arg) -> {
            if (ctx.hasPlayer()) {
                int ping = ctx.player().connection.latency();
                return PlaceholderResult.value(heatmapColor(String.valueOf(ping), ping, 200));
            } else {
                return PlaceholderResult.invalid("No player!");
            }
        });
    }

    private static Component heatmapColor(String text, double actual, double reference) {
        ChatFormatting color = ChatFormatting.GREEN;
        if (actual > 0.5D * reference) color = ChatFormatting.YELLOW;
        if (actual > 0.8D * reference) color = ChatFormatting.RED;
        if (actual > reference) color = ChatFormatting.LIGHT_PURPLE;

        return Component.literal(text).withColor(color.getColor());
    }
}
