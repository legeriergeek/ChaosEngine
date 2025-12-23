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

**Here is a quick documentation to get you started on making stuff with this engine**

First, let's take look at a simple example of code (the one provided already in core/Renderer by default). It shows a great usage of most of the features of this engine.

```java
//100K Cubes - 2025 legeriergeek
//Spawns 100k cubes by default. Warning: gets pretty laggy. You can reduce the number of cubes by changing the "cubeNumber" variable.

package fr.chaos.engine.core;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import fr.chaos.engine.graphics.Mesh;
import fr.chaos.engine.graphics.Camera;
import fr.chaos.engine.graphics.Texture;
import java.util.ArrayList;
import java.util.Random;

public class Renderer{
    public static ShaderProgram shader;
    public static Mesh cube;
    public static Camera camera;
    public static Texture texture1;
    public static int cubeNumber = 100000;
    public static void init(){    
        shader = new ShaderProgram("vertex.glsl", "fragment.glsl");
        texture1 = new Texture("unportalable.jpg");
        Random r = new Random();
        cube = new Mesh(Mesh.cubeVertices, new Vector3f(-5 + r.nextFloat() * (5 - -5), -2 + r.nextFloat() * (5 - -5), -5 + r.nextFloat() * (5 - -5)), new Vector3f(0f, 0f, 0f), new Vector3f(0.5f, 0.5f, 0.5f), texture1);
        camera = new Camera(new Vector3f(0f, 0f, 3), new Vector3f(0f,0f,0f), 70, (float) Engine.windowWidth/Engine.windowHeight, 0.1f, 100f);
    }
    
    public static void render(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        shader.use();
        texture1.bind();
        shader.setUniform("view", camera.getViewMatrix());
        shader.setUniform("projection", camera.getProjectionMatrix());
        shader.setUniform("model", cube.getModelMatrix());
        cube.draw();
    }

    public static void update(long window) {
        float deltaTime = 0.016f;
        cube.rotation = new Vector3f(cube.rotation.x + 1f * deltaTime, cube.rotation.y + 1f * deltaTime, cube.rotation.z + 1f * deltaTime);
    }

}
```
I think this is pretty straight forward but i'll explain it anyways.\
To put an object on the screen you need to:
* Declare it's Variable (`public static Mesh cube;`)
* In the `init()` function, set all it's parameters, which come in this order: Mesh
* * Mesh: You can either use the inculded `Mesh.cubeVertices`, which will make a cube, or use `OBJLoader.load("file.obj")` (import `fr.chaos.engine.utils.OBJLoader`) to load a `.obj` model
* * Position, in a Vector3f. It uses OpenGL default units.
* * Rotation, in a Vector3f. Supply angles in degrees.
* * Scale, in a Vector3f. It uses OpenGL default units.
