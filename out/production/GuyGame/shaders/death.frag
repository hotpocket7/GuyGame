
#define PI 3.1415926535897932384626

uniform sampler2D texture1;
uniform float time;

void main()
{
    vec4 color = vec4((sin((time - 1000.0/8.0) * 2 * PI / 1000.0) + 1.0) / 2.0, 0.0, 0.0, 0.0);
    gl_FragColor = texture2D(texture1, gl_TexCoord[0].st) + color;
}