#version 410

in vec3 in_Position;

uniform mat4 matGUI;

void main(void) {
    gl_Position = matGUI * vec4(in_Position, 1);
}