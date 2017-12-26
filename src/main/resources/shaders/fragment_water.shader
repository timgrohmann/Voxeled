#version 410

uniform sampler2D texture_diffuse;

in vec2 UV;
in float lightness;

out vec4 colorOut;
void main()
{
    vec4 tex = texture(texture_diffuse, UV);
    vec4 col = tex * lightness * 0.7;
    col.a = 0.9;
    colorOut = col;
}