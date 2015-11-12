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