#version 330

layout (location = 0) in vec3 position;
layout (location = 1) in vec2 texCoord;
layout (location = 2) in vec3 normal;

out vec2 outTexCoord;

uniform mat4 world;
uniform mat4 projection;
uniform mat4 view;

void main()
{
    gl_Position = projection * view * world * vec4(position, 1.0);
}