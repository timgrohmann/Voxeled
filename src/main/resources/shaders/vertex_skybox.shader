#version 410

layout(location = 0) in vec3 in_Position;

uniform vec3 player_Pos;

uniform mat4 mat;
uniform mat4 rotMat;

out vec3 UV;

void main(void) {

    gl_Position = mat * rotMat * vec4(in_Position, 1.0);

    UV = in_Position;
}