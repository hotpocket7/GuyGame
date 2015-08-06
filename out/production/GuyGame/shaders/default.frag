uniform sampler2D texture1;

void main(void)
{
    gl_FragColor = texture2D(texture1, gl_TexCoord[0].st);
}
