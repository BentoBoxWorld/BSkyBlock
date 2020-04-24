BSkyBlock
==========
[![Build Status](https://ci.codemc.org/buildStatus/icon?job=BentoBoxWorld/BSkyBlock)](https://ci.codemc.org/job/BentoBoxWorld/job/BSkyBlock/)
[![Lines Of Code](https://sonarcloud.io/api/project_badges/measure?project=world.bentobox%3Abskyblock&metric=ncloc)](https://sonarcloud.io/component_measures?id=world.bentobox%3Abskyblock&metric=ncloc)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=world.bentobox%3Abskyblock&metric=sqale_rating)](https://sonarcloud.io/component_measures?id=world.bentobox%3Abskyblock&metric=Maintainability)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=world.bentobox%3Abskyblock&metric=reliability_rating)](https://sonarcloud.io/component_measures?id=world.bentobox%3Abskyblock&metric=Reliability)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=world.bentobox%3Abskyblock&metric=security_rating)](https://sonarcloud.io/component_measures?id=world.bentobox%3Abskyblock&metric=Security)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=world.bentobox%3Abskyblock&metric=bugs)](https://sonarcloud.io/project/issues?id=world.bentobox%3Abskyblock&resolved=false&types=BUG)

# NOTE
BSkyBlock requires BentoBox, so to run BSkyBlock, you must have BentoBox installed and place BSkyBlock in BentoBox's addon folder.

## About
BSkyBlock provides a skyblock-type Minecraft game for players that supports a default set of 3 islands (overworld, nether and end), protection of islands, team and coop play and other features. Please see the config.yml for all the settings.

## Installation

1. Install BentoBox and run it on the server at least once to create its data folders.
2. Place this jar in the addons folder of the BentoBox plugin.
3. Restart the server.
4. The addon will create worlds and a data folder and inside the folder will be a config.yml.
5. Stop the server .
6. Edit the config.yml how you want.
7. Delete any worlds that were created by default if you made changes that would affect them.
8. Restart the server.

## Config.yml

The config.yml is similar to ASkyBlock but *not the same*. Note that distance between islands and protection range are **radius values** so the island size will be twice these values in blocks! Also, the distance between islands will be set automatically to a chunk boundary (a multiple of 16 blocks).


### Other Add-ons

BSkyBlock is an add-on that uses the BentoBox API. Here are some other ones that you may be interested in:

* [**Addons**](https://github.com/BentoBoxWorld/BentoBox/blob/develop/ADDON.md)

Bugs and Feature requests
=========================
File bug and feature requests here: https://github.com/BentoBoxWorld/BSkyBlock/issues
