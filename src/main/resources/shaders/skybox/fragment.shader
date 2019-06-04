#version 410

uniform samplerCube texture_cube;

in vec3 UV;

uniform float timeAngle;

out vec4 colorOut;

void main()
{
    float time = (cos(timeAngle) / 2 + 0.5) * 0.9 + 0.1;

    float upness = dot(normalize(UV), vec3(0.0, 1.0, 0.0));

    float borderLightness = (1-upness) * 0.1 + 0.9;

    vec3 skyBlue = time * vec3(135.0/255,206.0/255,250.0/255);

    vec3 col = skyBlue * borderLightness;
    //colorOut = vec4(col, 1.0);
    colorOut = texture(texture_cube,UV);
}