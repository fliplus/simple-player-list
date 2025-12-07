package fliplus.simpleplayerlist.config;

import java.util.List;

public class Settings {
    public List<Object> Header = List.of(
        "<bold>Default Config</bold>",
        2.0,
        "<gray>To change this, edit",
        "config/simpleplayerlist.json</gray>",
        2.0,
        "<aqua><st>                                        </st></aqua>"
    );

    public List<Object> Footer = List.of(
        "<aqua><st>                                        </st></aqua>",
        2.0,
        "<gray>TPS: %server:tps_colored_alt%<dark_gray>/</dark_gray>%server:mspt_colored_alt% <dark_gray>-</dark_gray> Ping: %player:ping_colored_alt%</gray>"
    );

    public int TickUpdateInterval = 20;
}
