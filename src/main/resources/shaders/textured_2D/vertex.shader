#version 410

layout(location = 0) in vec2 in_Pos2D;
layout(location = 1) in vec2 in_UV;

out vec2 UV;

uniform float aspect;

void main(void) {
    vec4 pos = vec4(in_Pos2D, 0, 1.0);
    pos.x /= aspect;
    gl_Position = pos;
    UV = in_UV;
}