//Default Scene

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
