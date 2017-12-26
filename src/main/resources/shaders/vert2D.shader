#version 410

layout(location = 0) in vec2 in_Pos2D;

uniform mat4 mat;

void main(void) {
    gl_Position = mat * vec4(in_Pos2D, 0, 1.0);
}