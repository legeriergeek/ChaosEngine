package fr.chaos.engine.core;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwGetCursorPos;
import static org.lwjgl.glfw.GLFW.glfwGetKey;
import static org.lwjgl.glfw.GLFW.glfwGetMouseButton;
import static org.lwjgl.glfw.GLFW.glfwSetInputMode;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import fr.chaos.engine.graphics.Camera;
import fr.chaos.engine.graphics.Light;
import fr.chaos.engine.graphics.Mesh;
import fr.chaos.engine.graphics.Texture;
import fr.chaos.engine.prefab_behavior.CarController;
import fr.chaos.engine.utils.OBJLoader;
import fr.chaos.engine.utils.RenderWithShader;
public class Renderer{
    public static ShaderProgram shader;
    public static ShaderProgram unlitShader;
    public static Mesh cube;
    public static Mesh cubeFollowingLight;
    public static Mesh Truck;
    public static Mesh Skybox;
    public static Camera camera;
    public static Texture texture;
    public static Texture texture2;
    public static Texture skyboxTexture;
    public static Light light;
    public static float[] truckModel;
    public static float[] skyboxModel;
    public static CarController car = new CarController();
    
    public static void init(){    
        OBJLoader loader = new OBJLoader();
        shader = new ShaderProgram("vertex.glsl", "fragment.glsl");
        unlitShader = new ShaderProgram("vertex.glsl", "unlit_frag.glsl");
        texture2 = new Texture("unportalable.jpg");
        skyboxTexture = new Texture("skybox.jpg");
        cube = new Mesh(Mesh.cubeVertices, new Vector3f(0, -3f, 0), new Vector3f(0f, 0f, 0f), new Vector3f(10f, 0.5f, 10f), texture2);
        cubeFollowingLight = new Mesh(Mesh.cubeVertices, new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0f, 0f), new Vector3f(1f, 1f, 1f), texture2);
        truckModel = loader.load("sphere_detailed.obj");
        Truck = new Mesh(truckModel, new Vector3f(0, 10f, 0), new Vector3f(0f, 0f, 0f), new Vector3f(1f, 1f, 1f), texture2);
        light = new Light(new Vector3f(0f, -2f, 0f));
        camera = new Camera(new Vector3f(0f, 0f, -3), new Vector3f(0f,-3f,0f), 70, (float) Engine.windowWidth/Engine.windowHeight, 0.1f, 99999f);
        skyboxModel = loader.load("sphere_detailed.obj");
        Skybox = new Mesh(skyboxModel, new Vector3f(0f, 0f, 0f), new Vector3f(0f, 0f, 0f), new Vector3f(10000f, 10000f, 10000f), skyboxTexture);
    }
    
    public static void render(){
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
        glEnable(GL_DEPTH_TEST);
        
        RenderWithShader.InitShader(shader, camera);
        texture2.bind();
        shader.setUniformV3f("lightPos", light.position);
        shader.setUniformFloat("intensity", 10.0f);
        shader.setUniformFloat("spread", 0.1f);
        RenderWithShader.Render(shader, cube);
        RenderWithShader.Render(shader, Truck);
        RenderWithShader.Render(shader, cubeFollowingLight);
        RenderWithShader.InitShader(unlitShader, camera);
        skyboxTexture.bind();
        RenderWithShader.Render(unlitShader, Skybox);
    }

    static boolean rmbWasDown;
    static double lastMx, lastMy;
    static final float sens = 0.12f; // deg/pixel
    static final float maxPitch = 89f;
    static private boolean lightState = true;
    static private int lightCounter = 0;

    public static void update(long window) {
        float deltaTime = 0.016f;
        float speed = 0.1f;

        if(lightState){
            light.position = new Vector3f(light.position.x, light.position.y + (1f * 0.016f), light.position.z);
            lightCounter += 1;
        } else {
            light.position = new Vector3f(light.position.x, light.position.y - (1f * 0.016f), light.position.z);
            lightCounter -= 1;
        }
        if(lightCounter == 600){
            lightState = false;
        } else if (lightCounter == 0){
            lightState = true;
        }
        cubeFollowingLight.position = light.position;

        Vector3f pos = camera.getPosition();
        Vector3f rot = camera.getRotation(); 

        //yes the character controller is vibecoded. i have no want to code that LMFAO
        boolean rmbDown = glfwGetMouseButton(window, GLFW_MOUSE_BUTTON_RIGHT) == GLFW_PRESS; 
        if (rmbDown) {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED); 

            double[] mx = new double[1], my = new double[1];
            glfwGetCursorPos(window, mx, my); 

            if (!rmbWasDown) {
                lastMx = mx[0];
                lastMy = my[0];
            } else {
                double dx = mx[0] - lastMx;
                double dy = my[0] - lastMy;
                lastMx = mx[0];
                lastMy = my[0];

                rot.y -= (float) dx * sens;   
                rot.x -= (float) dy * sens;   

                if (rot.x >  maxPitch) rot.x =  maxPitch;
                if (rot.x < -maxPitch) rot.x = -maxPitch;
            }
        } else if (rmbWasDown) {
            glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
        }
        rmbWasDown = rmbDown;

        Matrix4f view = camera.getViewMatrix(); 

        Vector3f right   = new Vector3f(view.m00(), view.m10(), view.m20()).normalize();
        Vector3f forward = new Vector3f(-view.m02(), -view.m12(), -view.m22()).normalize();

        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) { pos.x += forward.x * speed; pos.y += forward.y * speed; pos.z += forward.z * speed; }
        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) { pos.x -= forward.x * speed; pos.y -= forward.y * speed; pos.z -= forward.z * speed; }
        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) { pos.x += right.x   * speed; pos.z += right.z   * speed; }
        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) { pos.x -= right.x   * speed; pos.z -= right.z   * speed; }

        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) pos.y += speed;
        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) pos.y -= speed;
    }

}
