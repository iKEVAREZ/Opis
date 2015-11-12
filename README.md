This is a fork of ProfMobius' mod [Opis](http://minecraft.curseforge.com/projects/opis).
Opis is an important tool for profiling the client and server using a user-friendly GUI.
The main changes of this fork are:

* Removal of the integrated [MapWriter](minecraft.curseforge.com/projects/mapwriter-2) mod
* Removal of overlays that required MapWriter
* Integration of MobiusCore as opposed to building it as an external mod
* Fix for client profiling data "<ERROR>" strings
 
# Rationale

This fork was undertaken as an emergency fix for FTB Infinity, after Opis was removed
in 2.1.2 [for compatibility reasons](http://www.feed-the-beast.com/projects/ftb-infinity-evolved/files/2265972).
The incompatibility arises from a [MapWriter bug](https://bitbucket.org/ProfMobius/opis/issues/125/client-crash).

## Why remove MapWriter?

This is a personal choice. In a pack such as Infinity, mapping is already provided by
another mod such as JourneyMap. Subjectively, MapWriter is a less useful minimap. It
became an annoyance to attempt to disable it every pack (re)installation. I thought it OK
as I did not find any use for overlay functionality.

## Why integrate MobiusCore?

Subjectively, I found it too cumbersome in development to work with MobiusCore as a
separate mod and codebase whilst attempting to update Opis. I found that MobiusCore is
[not depended upon by any other mods](http://minecraft.curseforge.com/projects/mobiuscore/relations/dependents?filter-related-dependents=3),
so I thought it OK to integrate as a git submodule instead.

# Building

## Requirements

* [Gradle installation with gradle binary in PATH](http://www.gradle.org/installation).
Unlike the source package provided by Forge, this repository does not include a gradle
wrapper or distribution.

## Simple build

Execute `gradle setupCIWorkspace` in the root directory of this repository. Then execute
`gradle build`. If subsequent builds cause problems, do `gradle clean`.

## IntelliJ

* Open `build.gradle` as a project
* Execute the `setupDecompWorkspace` task
* Click the refresh button in the "Gradle" tab
* Execute the `genIntellijRuns` task
* For both the "Minecraft Client" and "Minecraft Server" run configurations, add the JVM
option `-Dfml.coreMods.load=mcp.mobius.mobiuscore.asm.CoreDescription`