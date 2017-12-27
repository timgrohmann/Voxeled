#version 410

uniform sampler2DArray texture_array;

in vec3 UVW;
in vec3 blendColor;
in float lightness;

out vec4 colorOut;
void main()
{
    vec4 tex = texture(texture_array, UVW);
    vec4 col = tex * vec4(blendColor, 1.0) * lightness * 0.7;
    col.a = 0.9;
    colorOut = col;
}