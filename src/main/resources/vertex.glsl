#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec2 aTexCoord;

uniform mat4 model;
uniform mat4 view;
uniform mat4 projection;
uniform vec3 lightPos;

out vec2 TexCoord;
out float diff;

void main()
{
    gl_Position = projection * view * model * vec4(aPos, 1.0);
    vec3 worldPos = vec3(model * vec4(aPos, 1.0));
    TexCoord = aTexCoord;
    diff = length(lightPos - worldPos);
}
