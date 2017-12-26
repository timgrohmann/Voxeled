#version 410

uniform samplerCube texture_cube;

in vec3 UV;

out vec4 colorOut;

void main()
{
    colorOut = texture(texture_cube, UV);
    //colorOut = vec4(UV / 100,1.0);
}