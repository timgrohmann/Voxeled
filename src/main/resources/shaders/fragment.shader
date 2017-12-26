#version 410

uniform sampler2D texture_diffuse;

in vec2 UV;
in float lightness;

out vec4 colorOut;
void main()
{
    vec4 col = texture(texture_diffuse, UV) * lightness;
    col.a = 1;
    colorOut = col;
}
