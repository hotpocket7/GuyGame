package game.graphics;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import game.Game;
import game.math.Vec2d;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class RenderUtils {

    public static HashMap<Font, TextRenderer> fontMap;

    public static Font trajan48;

    static {
        fontMap = new HashMap<>();
        loadFonts();
    }

    public static void loadFonts() {
        ClassLoader cl = RenderUtils.class.getClassLoader();
        try {
            File f1 = new File(cl.getResource("fonts/trajan.otf").getFile());
            trajan48 = Font.createFont(Font.TRUETYPE_FONT, f1).deriveFont(Font.PLAIN, 48);
        } catch(IOException | FontFormatException e) {
            e.printStackTrace();
        }

        fontMap.put(trajan48, new TextRenderer(trajan48, true, false));
    }

    public static void drawText(String string, Vec2d position, Color4f color, Font font) {
        TextRenderer r = fontMap.get(font);

        int stringWidth = 0;
        for(int i = 0; i < string.length(); i++) {
            stringWidth += r.getCharWidth(string.charAt(i));
        }


        r.beginRendering(Game.screen.getWidth(), Game.screen.getHeight());
        r.setColor(color.r, color.g, color.b, color.a);
        r.draw(string, (int) position.x - stringWidth / 2, Game.screen.getHeight() - (int) position.y);
        r.endRendering();
    }

    public static void fillRect(Vec2d position, double width, double height, Color4f color, GL2 gl) {
        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        gl.glBegin(GL2.GL_QUADS);

        gl.glColor4f(color.r, color.g, color.b, color.a);

        Vec2d pos = new Vec2d(position);
        pos.y = Game.screen.getHeight() - pos.y;

        gl.glVertex2d(pos.x, pos.y);
        gl.glVertex2d(pos.x + width, pos.y);
        gl.glVertex2d(pos.x + width, pos.y - height);
        gl.glVertex2d(pos.x, pos.y - height);

        gl.glEnd();
        gl.glDisable(GL2.GL_BLEND);
    }

}
