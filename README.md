# Minecraft Plugin - Idea Submission System

This plugin allows players to submit ideas for review via the `/idea` command. Depending on their permissions, players can submit a limited number of requests, and after approval, a cooldown is applied before they can submit another idea.

## Features

- **Command**: `/idea` - Used to submit ideas for review.
- **Permissions**:
  - `eidea.default` - Players with this permission can submit **1 idea**. After approval, a cooldown is applied.
  - `eidea.premium` - Players with this permission can submit **3 ideas**. After approval, a cooldown is applied.
  - `eidea.admin` - Admins have access to a special menu for managing ideas.

## Installation

1. Download the plugin and place it in the `plugins` folder of your Minecraft server.
2. Restart the server.
3. Make sure the permissions are properly configured using your preferred permission plugin (e.g., [PermissionsEx](https://www.spigotmc.org/resources/permissionsex.726/) or [LuckPerms](https://www.spigotmc.org/resources/luckperms.28104/)).

## Setting Permissions

To use the plugin, set the following permissions in your permission plugin:

- `eidea.default` — for regular players, allowing them to submit 1 idea and set cooldown from config.
- `eidea.premium` — for premium players, allowing them to submit 3 ideas.
- `eidea.admin` — for admins, granting access to the idea management menu.

## Usage

### `/idea` Command

- Players with the `eidea.default` permission can submit one idea.
- Players with the `eidea.premium` permission can submit up to three ideas.
- After each idea is approved, a cooldown is applied (configurable).
- Admins with `eidea.admin` permission have access to a special menu for managing all ideas.

### Admin Menu

Admins (with the `eidea.admin` permission) can access a menu with the following features:

- View all submitted ideas.
- Approve or reject ideas.
- Configure the cooldown time for players.

## Configuration

All settings, such as the number of allowed submissions, cooldown time, and other features, can be customized in the plugin's configuration file. After making changes, restart the server for the settings to take effect.

## License

This plugin is released under the MIT License. You are free to use, modify, and distribute it.
