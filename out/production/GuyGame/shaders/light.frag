
varying float distance;
uniform vec4 lightColor;
uniform vec4 ambientColor;
uniform float radius;

void main(void)
{
    float a = 0;
    float minLight = 0.1;
    float b = 1.0 / (radius*radius * minLight);
    float att = 1.0 / ((1.0 + a*distance) * (1.0 + b*distance*distance));

    gl_FragColor = att*(lightColor + ambientColor);
}