#version 410

layout(location = 0) in vec3 in_Position;
layout(location = 1) in vec3 in_UVW;

layout(location = 2) in vec3 in_Normal;
layout(location = 3) in vec3 in_BlendColor;

uniform mat4 mat;
uniform vec3 light_dir;

out vec3 blendColor;
out vec3 UVW;
out float lightness;

float diff_light = 0.5;

void main(void) {
    gl_Position = mat * vec4(in_Position, 1.0);

    //col = (clamp(dot(normalize(in_Normal), normalize(light_dir - in_Position)), 0, 1) * 0.8 + 0.2) * color;

    lightness = (clamp(dot(normalize(in_Normal), normalize(light_dir)), 0, 1) * (1-diff_light) + diff_light);
    UVW = in_UVW;
    blendColor = in_BlendColor;
}