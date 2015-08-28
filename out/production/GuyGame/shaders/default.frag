uniform sampler2D texture1;
uniform float wtff;

void main(void)
{
    vec4 tex = texture2D(texture1, gl_TexCoord[0].st);
    gl_FragColor = tex;
    gl_FragColor.a *= wtff;
}
