# Simple Player List

Simple Player List is a **server-side** mod which introduces the ability to customize server's player list through a
configuration file.

## Features

- Modify the player list appearance using a JSON configuration file.
- Show real-time server statistics directly in the player list.

## Configuration

Upon loading the mod for the first time, a default configuration file will be generated
(`config/simpleplayerlist.json`):

```json
{
  "enable_mod": true,
  "header": "§lDefault Config§r\n\n§7To change this, edit\nconfig/simpleplayerlist.json\n\n§r§b§m                                        §r",
  "footer": "§r§b§m                                        §r\n\n§7TPS: %tps%§7/%mspt% §8-§7 Ping: %ping%\n§7Day: §b%day%",
  "tick_update_interval": 20
}
```

This file supports the [§-based formatting system](https://minecraft.wiki/w/Formatting_codes) and hex colors
(e.g., `§#FFFFFF`).

The following placeholders will be dynamically replaced with real-time server information at the interval specified by
`tick_update_interval`:

- `%mspt%`: Displays server the server's MSPT (Milliseconds Per Tick).
- `%tps%`: Displays the server's TPS (Ticks Per Second).
- `%ping%`: Shows the player's ping in milliseconds.
- `%day%`: Indicates the current in-game day.

`%mspt%`, `%tps%` and `%ping%` are heatmap colored for easier interpretation.

## Commands

To apply changes made to the configuration file, you can use the in-game command `/playerlist reload`. You can also
enable or disable the mod with `/playerlist toggle`.

Players without operator status can run `/playerlist` to toggle the mod on their end.

## Support and Contributions

Feel free to report issues or suggest features through the mod's
[issue tracker](https://github.com/fliplus/simple-player-list/issues). Contributions are welcome - simply fork the
repository and submit a pull request with your improvements.

---

[Fabric API](https://modrinth.com/mod/fabric-api) is required to run this mod.