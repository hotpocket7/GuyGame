
#define PI 3.1415926535897932384626

uniform float time;

void main(void)
{
    vec4 v = vec4(gl_Vertex);
    float yOffset = 1.2f * sin(2 * PI / 1000 * (time + 2 * v.xy));
    gl_Position = gl_ModelViewProjectionMatrix * vec4(v.x, v.y + yOffset, v.zw);
    gl_TexCoord[0] = gl_MultiTexCoord0;
}
