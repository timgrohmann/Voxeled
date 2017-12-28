#version 410

uniform sampler2DArray texture_array;

in vec3 UVW;
in vec3 blendColor;
in float lightness;

out vec4 colorOut;
void main()
{
    vec4 col = texture(texture_array, UVW) * vec4(blendColor, 1.0);
    col.rgb = col.rgb * lightness;
    if (col.a == 0) discard;
    //col.a = 1;
    colorOut = col;
}
