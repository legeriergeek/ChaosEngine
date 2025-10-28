package fr.chaos.engine.graphics;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

    private Vector3f position;
    private Vector3f rotation;

    private float fov;       
    private float aspect;    
    private float nearPlane; 
    private float farPlane;  

    // --- Constructeur ---
    public Camera(Vector3f givenPosition, Vector3f givenRotation, float givenFov, float givenAspect, float givenNP, float givenFP) {
        fov = (float) Math.toRadians(givenFov);
        position = givenPosition;
        rotation = givenRotation;
        aspect = givenAspect;
        nearPlane = givenNP;
        farPlane = givenFP;

    }

    public Matrix4f getViewMatrix(){
        return new Matrix4f()
            .rotateXYZ(-rotation.x, -rotation.y, -rotation.z)
            .translate(-position.x, -position.y, -position.z);
    }

    public Matrix4f getProjectionMatrix() {
        return new Matrix4f().perspective(fov,(float) aspect, nearPlane, farPlane);
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f newPosition) {
        this.position.set(newPosition);
    }


    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public float getFov() {
        return fov;
    }

    public void setFov(float fov) {
        this.fov = fov;
    }
}
