package fr.chaos.engine.core;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;


import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Engine {
    private long window;
    public static int windowHeight = 720;
    public static int windowWidth = 1280;

    public void run() {
        System.out.println("Starting ChaosEngine...");
        init();
        loop();

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    private void init() {
        if (!glfwInit())
            throw new IllegalStateException("Impossible d'initialiser GLFW");

        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(windowWidth, windowHeight, "ChaosEngine", NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Échec de création de la fenêtre!");

        // Centre la fenêtre
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if (vidmode != null)
            glfwSetWindowPos(window, (vidmode.width() - 1280) / 2, (vidmode.height() - 720) / 2);

        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        glfwShowWindow(window);

        GL.createCapabilities();


        glClearColor(0.1f, 0.1f, 0.3f, 1.0f);
        Renderer.init();
    }

    private void loop() {
        double lastTime = glfwGetTime();
        int frames = 0;
        while (!glfwWindowShouldClose(window)) {
            double currentTime = glfwGetTime();
            frames++;

            if (currentTime - lastTime >= 1.0) { 
                System.out.println("FPS: " + frames);
                frames = 0;
                lastTime = currentTime;
            }
            glClear(GL_COLOR_BUFFER_BIT);
            Renderer.update(window);
            Renderer.render();
            glfwSwapBuffers(window);
            glfwPollEvents();


            if (glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS)
                glfwSetWindowShouldClose(window, true);
        }
    }

    public static void main(String[] args) {
        new Engine().run();
    }
}
