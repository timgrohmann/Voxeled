#version 410

uniform sampler2D texture_diffuse;
in vec2 UV;

out vec4 colorOut;
void main()
{
    colorOut = texture(texture_diffuse, UV);
}