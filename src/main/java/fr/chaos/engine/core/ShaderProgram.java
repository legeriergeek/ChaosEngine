package fr.chaos.engine.core;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*; 

public class ShaderProgram {

    private int programId; 

    public static CharSequence openShader(String shaderName) {
        StringBuilder shader = new StringBuilder();
        
        try (InputStream is = ShaderProgram.class.getResourceAsStream("/" + shaderName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            
            String line;
            while ((line = br.readLine()) != null) {
                shader.append(line).append("\n");
            }
            
        } catch (IOException | NullPointerException e) {
            System.out.println("Error reading shader: " + shaderName);
            e.printStackTrace();
        }
        
        return shader;
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
