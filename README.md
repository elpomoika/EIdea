# Minecraft Plugin - Idea Submission System

This plugin allows players to submit feedbacks for review via the `/feedback` command. Depending on their permissions, players can submit a limited number of requests, and after approval, a cooldown is applied before they can submit another feedback.

## Features

- **Command**: `/idea` - Used to submit feedbacks for review.
- **Permissions**:
  - `feedback.default` - Players with this permission can submit **1 feedback**. After approval, a cooldown is applied.
  - `feedback.premium` - Players with this permission can submit **3 feedbacks**. After approval, a cooldown is applied.
  - `feedback.admin` - Admins have access to a special menu for managing feedbacks.

- **Command**: `/bugs` - Used to submit feedbacks for review.
- **Permissions**:
  - `feedback.default` - Players with this permission can submit **1 feedback**. After approval, a cooldown is applied.
  - `feedback.premium` - Players with this permission can submit **3 feedbacks**. After approval, a cooldown is applied.
  - `feedback.admin` - Admins have access to a special menu for managing feedbacks.

## Installation

1. Download the plugin and place it in the `plugins` folder of your Minecraft server.
2. Restart the server.
3. Make sure the permissions are properly configured using your preferred permission plugin (e.g., [PermissionsEx](https://www.spigotmc.org/resources/permissionsex.726/) or [LuckPerms](https://www.spigotmc.org/resources/luckperms.28104/)).

## Setting Permissions

To use the plugin, set the following permissions in your permission plugin:

- `feedback.default` — for regular players, allowing them to submit 1 feedback and set cooldown from config.
- `feedback.premium` — for premium players, allowing them to submit 3 feedbacks.
- `feedback.admin` — for admins, granting access to the feedback management menu.

## Usage

### `/feedback` Command

- Players with the `feedback.default` permission can submit one feedback.
- Players with the `feedback.premium` permission can submit up to three feedbacks.
- After each feedback is approved, a cooldown is applied (configurable).
- Admins with `feedback.admin` permission have access to a special menu for managing all feedbacks.

### Admin Menu

Admins (with the `feedback.admin` permission) can access a menu with the following features:

- View all submitted feedbacks.
- Approve or reject feedbacks.
- Configure the cooldown time for players.

## Configuration

All settings, such as the number of allowed submissions, cooldown time, and other features, can be customized in the plugin's configuration file. After making changes, restart the server for the settings to take effect.

## License

This plugin is released under the MIT License. You are free to use, modify, and distribute it.
