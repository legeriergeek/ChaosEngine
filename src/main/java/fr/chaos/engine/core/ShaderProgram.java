package fr.chaos.engine.core;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*; 

public class ShaderProgram {

    private int programId; 

    public static CharSequence openShader(String shaderName)
    {
        CharSequence shader = new StringBuffer("");
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/" + shaderName))) {
            String line;
            while ((line = br.readLine()) != null) {
                shader = new StringBuilder(shader + "\n" +line);
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }

        return(shader);
    }
    // --- Constructeur ---
    public ShaderProgram(String vertexPath, String fragmentPath) {
        CharSequence vertShaderSource = openShader("vertex.glsl");
        CharSequence fragShaderSource = openShader("fragment.glsl");
        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertShaderSource);
        glCompileShader(vertexShader);
        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragShaderSource);
        glCompileShader(fragmentShader);
        programId = glCreateProgram();
        glAttachShader(programId, vertexShader);
        glAttachShader(programId, fragmentShader);
        glLinkProgram(programId);
        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    // --- Activation du shader ---
    public void use() {
        glUseProgram(programId);
    }

    // --- Setter d’uniforms (comme les matrices) ---
    public void setUniform(String name, Matrix4f value) {
        FloatBuffer fb = BufferUtils.createFloatBuffer(16);
        value.get(fb);
        int loc = glGetUniformLocation(programId, name);
        glUniformMatrix4fv(loc, false, fb);
    }

    // --- Getter facultatif ---
    public int getId() {
        return programId;
    }

    // --- Nettoyage ---
    public void cleanup() {
        glDeleteProgram(programId);
    }
}
