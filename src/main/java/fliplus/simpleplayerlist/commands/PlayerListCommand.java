package fliplus.simpleplayerlist.commands;

import com.mojang.brigadier.CommandDispatcher;
import fliplus.simpleplayerlist.config.SimplePlayerListConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class PlayerListCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("playerlist").executes(context -> {
            ServerPlayer player = context.getSource().getPlayer();
            assert player != null;

            boolean isDisabled = SimplePlayerListConfig.disabledPlayers.contains(player);
            if (!isDisabled) {
                SimplePlayerListConfig.disabledPlayers.add(player);
                player.connection.send(new ClientboundTabListPacket(Component.literal(""), Component.literal("")));
            } else {
                SimplePlayerListConfig.disabledPlayers.remove(player);
            }

            String message = isDisabled ? "Player list re-enabled on your end" : "Player list disabled on your end";
            context.getSource().sendSuccess(() -> Component.literal(message).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY), false);

            return 0;
        }).then(Commands.literal("reload").requires(source -> source.hasPermission(2)).executes(context -> {
            ServerPlayer player = context.getSource().getPlayer();
            assert player != null;

            SimplePlayerListConfig.INSTANCE.load(context.getSource());

            return 0;
        })).then(Commands.literal("toggle").requires(source -> source.hasPermission(2)).executes(context -> {
            MinecraftServer server = context.getSource().getServer();
            ServerPlayer player = context.getSource().getPlayer();
            assert player != null;

            SimplePlayerListConfig.INSTANCE.enableMod = !SimplePlayerListConfig.INSTANCE.enableMod;

            for (ServerPlayer playerEntity : server.getPlayerList().getPlayers()) {
                playerEntity.connection.send(new ClientboundTabListPacket(Component.literal(""), Component.literal("")));
            }

            String message = player.getName().getString() + (SimplePlayerListConfig.INSTANCE.enableMod ? " enabled player list" : " disabled player list");
            context.getSource().sendSuccess(() -> Component.literal(message), true);

            return 0;
        })));
    }
}
