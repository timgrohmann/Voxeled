#version 410

uniform sampler2DArray texture_array;

in vec3 UVW;
in float lightness;

out vec4 colorOut;
void main()
{
    vec4 col = texture(texture_array, UVW) * lightness;
    col.a = 1;
    colorOut = col;
}
