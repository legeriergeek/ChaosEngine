package fr.chaos.engine.utils;

import fr.chaos.engine.core.ShaderProgram;
import fr.chaos.engine.graphics.Mesh;
import fr.chaos.engine.graphics.Camera;

public class RenderWithShader {

    public static void InitShader(ShaderProgram shader, Camera camera){
        shader.use();
        shader.setUniform("view", camera.getViewMatrix());
        shader.setUniform("projection", camera.getProjectionMatrix());
    }

    public static void Render(ShaderProgram shader, Mesh mesh){
        shader.setUniform("model", mesh.getModelMatrix());
        mesh.draw();
    }
}
