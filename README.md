# Simple Player List
Simple Player List is a Minecraft mod which introduces the ability to customize server's player list (Tab list) using
[Placeholder API](https://placeholders.pb4.eu/).
It supports colored text as well as server and player information.

## Configuration
Upon loading the mod for the first time, a default configuration file will be generated at
`config/simpleplayerlist.json`:
```json
{
  "header": [
    "<bold>Default Config</bold>",
    2.0,
    "<gray>To change this, edit",
    "config/simpleplayerlist.json</gray>",
    2.0,
    "<aqua><st>                                        </st></aqua>"
  ],
  "footer": [
    "<aqua><st>                                        </st></aqua>",
    2.0,
    "<gray>TPS: %server:tps_colored_alt%<dark_gray>/</dark_gray>%server:mspt_colored_alt% <dark_gray>-</dark_gray> Ping: %player:ping_colored_alt%</gray>"
  ],
  "tick_update_interval": 20
}
```

Each entry in the `header` and `footer` arrays can be either a text line or a number. Number how many blank lines to
insert.

This file uses Placeholder API's formatting system. Refer to:
- https://placeholders.pb4.eu/user/quicktext/
- https://placeholders.pb4.eu/user/default-placeholders/

This mod also adds three new alternatives to the default placeholders: `%server:tps_colored_alt`,
`%server:mspt_colored_alt%` and `%player:ping_colored_alt%`.
These variants provide improved color logic and presentation.

## Commands
To apply changes made to the configuration file, use the in-game command `/simpleplayerlist reload`.
Players can run `/simpleplayerlist` to toggle the mod on their end.

## Dependencies
- [Fabric API](https://modrinth.com/mod/fabric-api)
- [Placeholder API](https://modrinth.com/mod/placeholder-api)

## Download
- [Modrinth](https://modrinth.com/mod/simple-player-list)
- [CurseForge](https://www.curseforge.com/minecraft/mc-mods/simple-player-list)
- [GitHub Releases](https://github.com/fliplus/simple-player-list/releases)
