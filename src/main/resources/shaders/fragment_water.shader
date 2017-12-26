#version 410

uniform sampler2DArray texture_array;

in vec3 UVW;
in float lightness;

out vec4 colorOut;
void main()
{
    vec4 tex = texture(texture_array, UVW);
    vec4 col = tex * lightness * 0.7;
    col.a = 0.9;
    colorOut = col;
}