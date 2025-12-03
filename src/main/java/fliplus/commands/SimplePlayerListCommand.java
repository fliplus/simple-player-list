package fliplus.commands;

import com.mojang.brigadier.CommandDispatcher;
import fliplus.SimplePlayerList;
import fliplus.config.SimplePlayerListConfig;
import fliplus.server.TabList;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.level.ServerPlayer;

public class SimplePlayerListCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("simpleplayerlist").executes(context -> {
            CommandSourceStack source = context.getSource();
            ServerPlayer player = source.getPlayer();

            if (player == null) {
                source.sendFailure(Component.literal("Only players can use this command"));
                return -1;
            }

            String playerName = player.getGameProfile().name();
            boolean isDisabled = TabList.disabledPlayers.contains(playerName);
            if (isDisabled) {
                TabList.disabledPlayers.remove(playerName);
                TabList.updateTabList(player);
            } else {
                TabList.disabledPlayers.add(playerName);
                player.connection.send(new ClientboundTabListPacket(
                    Component.literal(""),
                    Component.literal("")
                ));
            }

            String message = "Tab list information " + (isDisabled ? "enabled" : "disabled") + " on your end";
            source.sendSuccess(
                () -> Component.literal(message).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY),
                true
            );

            return 0;
        }).then(Commands.literal("reload")
            .requires(source -> source.hasPermission(2))
            .executes(context -> {
                SimplePlayerList.CONFIG = SimplePlayerListConfig.loadConfig(context.getSource());
                return 0;
            })
        ));
    }
}
