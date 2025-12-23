# ChaosEngine
Barebones 3D GameEngine made in Java using LWJGL

### How to run?
**You can go 2 ways:**
* Run the premade `.jar` file.
* Download the Code and run it yourself

**For the downloading `.jar` file, here's how you shall proceed:**
* Download the jar of of the release tab
* Ensure you have a recent version of java installed (Java 21+)
* double click on the jar (or open your terminal and type `java -jar Chaos_Engine_0.X.jar`)\

**Please note that the premade jar is only released for demonstration purposes only. Unless you like seeing a cube spinning this is not gonna be useful to you.**

**And to run the code yourself:**
* Make sure you have maven & a recent version (21+) of the JDK installed on your computer
* run the command `mvn exec:java -e -Dexec.mainClass="fr.chaos.engine.core.Engine"` in your terminal, in the root folder of the project.

### How to code with it
The place you are excepted to write your code (when making "games" with the engine) is core/Renderer.java .
