package fr.chaos.engine.core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glEnable;

import org.joml.Vector3f;


import fr.chaos.engine.graphics.Mesh;
import fr.chaos.engine.graphics.Camera;
import fr.chaos.engine.graphics.Texture;



public class Renderer{
    public static ShaderProgram shader;
    public static Mesh cube;
    public static Mesh cube2;
    public static Mesh cube3;
    public static Mesh cube4;
    public static Mesh cube5;
    public static Mesh cube6;
    public static Camera camera;
    public static Texture texture;
    public static Texture texture2;
    public static void init(){    
        shader = new ShaderProgram("vertex.glsl", "fragment.glsl");
        texture = new Texture("wall.png");
        texture2 = new Texture("unportalable.jpg");
        cube = new Mesh(Mesh.cubeVertices, new Vector3f(0, -3f, 0), new Vector3f(0f, 0f, 0f), new Vector3f(10f, 0.5f, 10f), texture2);
        cube2 = new Mesh(Mesh.cubeVertices, new Vector3f(0f, 2.25f, -5f), new Vector3f(0f, 0f, 0f), new Vector3f(10f, 10f, 0.5f), texture);
        cube3 = new Mesh(Mesh.cubeVertices, new Vector3f(0f, 2.25f, 5f), new Vector3f(0f, 0f, 0f), new Vector3f(10f, 10f, 0.5f), texture);
        cube4 = new Mesh(Mesh.cubeVertices, new Vector3f(-5f, 2.25f, 0f), new Vector3f(0f, 90f, 0f), new Vector3f(10f, 10f, 0.5f), texture);
        cube5 = new Mesh(Mesh.cubeVertices, new Vector3f(5f, 2.25f, 0f), new Vector3f(0f, 90f, 0f), new Vector3f(10f, 10f, 0.5f), texture);
        cube6 = new Mesh(Mesh.cubeVertices, new Vector3f(0, 7.50f, 0), new Vector3f(0f, 0f, 0f), new Vector3f(10f, 0.5f, 10f), texture2);
        camera = new Camera(new Vector3f(0f, 0f, -3), new Vector3f(0f,-3f,0f), 70, (float) Engine.windowWidth/Engine.windowHeight, 0.1f, 100f);
    }
    public static void render(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        shader.use();
        texture.bind();
        texture2.bind();
        shader.setUniform("model", cube.getModelMatrix());
        shader.setUniform("view", camera.getViewMatrix());
        shader.setUniform("projection", camera.getProjectionMatrix());
        cube.draw();
        shader.setUniform("model", cube2.getModelMatrix());
        cube2.draw();
        shader.setUniform("model", cube3.getModelMatrix());
        cube3.draw();
        shader.setUniform("model", cube4.getModelMatrix());
        cube4.draw();
        shader.setUniform("model", cube5.getModelMatrix());
        cube5.draw();
        shader.setUniform("model", cube6.getModelMatrix());
        cube6.draw();
    }
    public static void update(long window) {
        float speed = 0.05f;
        float yaw = camera.getRotation().y;

        float forwardX = (float) Math.sin(yaw);
        float forwardZ = (float) -Math.cos(yaw);
        float rightX = (float) Math.cos(yaw);
        float rightZ = (float) Math.sin(yaw);

        Vector3f pos = camera.getPosition();

        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) {
            pos.x -= forwardX * speed;
            pos.z += forwardZ * speed;
        }
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) {
            pos.x += forwardX * speed;
            pos.z -= forwardZ * speed;
        }
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) {
            pos.x -= rightX * speed;
            pos.z -= rightZ * speed;
        }
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) {
            pos.x += rightX * speed;
            pos.z += rightZ * speed;
        }

        if (glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) {
            pos.y += speed;
        }
        if (glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS) {
            pos.y -= speed;
        }

        if (glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS) {
            camera.setRotation(new Vector3f(camera.getRotation().x, yaw - 0.05f, camera.getRotation().z));
        }
        if (glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS) {
            camera.setRotation(new Vector3f(camera.getRotation().x, yaw + 0.05f, camera.getRotation().z));
        }

        camera.setPosition(pos);
    }

}
