# BetterDays — Project Context

## What This Is
A mod that extends and customizes the Minecraft day-night cycle length, provides
smooth sleep transitions in multiplayer, and adds optional time effects (crop growth,
weather, potion effects, furnaces, etc.).

Originally a 1.15.2 mod. When it went unmaintained, [Hourglass](https://github.com/DuckyCrayfish/hourglass)
was created as a successor. When Hourglass also went unmaintained, BetterDays was
revived for 1.19+ as a fork of Hourglass — with one critical difference: Hourglass
is Forge-only. This fork expanded to a multi-loader model adding Fabric support.
BetterDays is one of the only mods of this type available for Fabric, making Fabric
support a key reason for its popularity and an important feature to maintain.
See README.md for full differences and configuration.

License: LGPL v3

## Project Structure
Multi-loader: `Common/` + `NeoForge/` + `Fabric/`

## Branch Convention
| Branch | Modloaders        |
|--------|-------------------|
| 1.18.2 | Forge + Fabric    |
| 1.19.2 | Forge + Fabric    |
| 1.20.1 | Forge + Fabric    |
| 1.21.1 | NeoForge + Fabric |
| 26.1   | NeoForge + Fabric |

Maintained: 1.20.1, 1.21.1, 26.1

## Dependencies
- WhiteNoise (jarJar/include)

## Notable Compatibility
Homeostatic, ReadyPlayerFun, Serene Seasons, Comforts. Several sleep mods
(Quark, Sleep Warp, Sleep Tight) require disabling overlapping features.
See README.md for the full compatibility matrix.

## Distribution
Side: both (clientRequired = true, serverRequired = true)

## Release Process
Follow the standard wendall911 release process in
`../docs/minecraft/MINECRAFT_DEVELOPMENT_NOTES.md`.
