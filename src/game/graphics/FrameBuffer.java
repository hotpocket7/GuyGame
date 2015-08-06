package game.graphics;

import com.jogamp.opengl.GL2;

import java.util.ArrayList;
import java.util.Stack;

public final class FrameBuffer {

    public static FrameBuffer lightMap, portal;

    private static ArrayList<FrameBuffer> frameBuffers = new ArrayList<FrameBuffer>();
    private static Stack<FrameBuffer> frameBufferStack = new Stack<>();

    public static void initFrameBuffers(GL2 gl) {
        frameBuffers.add(null);
        frameBufferStack.push(null);

        lightMap = new FrameBuffer(gl, 800, 608);
        portal = new FrameBuffer(gl, 800, 608);
    }

    private int width, height;

    private int texture[];
    private int fbo[];

    private FrameBuffer(GL2 gl, int width, int height) {
        this.width = width;
        this.height = height;

        texture = new int[1];
        fbo = new int[1];

        init(gl);
    }

    private void init(GL2 gl) {
        gl.glGenTextures(1, texture, 0);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture[0]);
        gl.glTexImage2D(GL2.GL_TEXTURE_2D, 0, GL2.GL_RGBA, width, height, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, null);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, GL2.GL_NEAREST);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, GL2.GL_NEAREST);

        gl.glGenFramebuffers(1, fbo, 0);
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, fbo[0]);
        gl.glFramebufferTexture2D(GL2.GL_FRAMEBUFFER, GL2.GL_COLOR_ATTACHMENT0, GL2.GL_TEXTURE_2D, texture[0], 0);

        frameBuffers.add(this);
    }

    /**
     * Binds the framebuffer to be rendered to and pushes it to the top of the stack
     */
    public void bind(GL2 gl) {
        gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, fbo[0]);
        gl.glLoadIdentity();
        frameBufferStack.push(this);
    }

    /**
     * Pops the framebuffer from the stack and binds the next one (default one if null)
     * @see #bind
     */
    public static void unbindCurrentFramebuffer(GL2 gl) {
        frameBufferStack.pop();

        FrameBuffer nextFrameBuffer = frameBufferStack.peek();
        if(nextFrameBuffer != null)
            gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, frameBufferStack.peek().fbo[0]);
        else
            gl.glBindFramebuffer(GL2.GL_FRAMEBUFFER, 0);
    }

    /**
     * Binds the texture, allowing you to render the framebuffer to the screen
     * Make sure you call glEnable(GL2.GL_TEXTURE_2D) first
     */
    public void bindTexture(GL2 gl) {
        gl.glEnable(GL2.GL_TEXTURE_2D);
        gl.glBindTexture(GL2.GL_TEXTURE_2D, texture[0]);
    }

    /**
     * Unbinds the texture
     * @see #bindTexture
     */
    public void unbindTexture(GL2 gl) {
        gl.glDisable(GL2.GL_TEXTURE_2D);
    }

}
