#version 330 core

out vec4 FragColor;
in vec2 TexCoord;
in float diff;
uniform sampler2D texture0;
uniform float intensity;
uniform float spread;

void main()
{
    float brightness = exp(-diff * spread);
    FragColor = texture(texture0, TexCoord) * brightness * intensity;
}
