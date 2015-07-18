
varying float distance;
uniform vec4 lightPos;
void main(void)
{
    vec4 v = gl_Vertex;
    distance = sqrt(pow(lightPos.x - v.x, 2) + pow(lightPos.y - v.y, 2));
    gl_Position = gl_ModelViewProjectionMatrix * v;
}