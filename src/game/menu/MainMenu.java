package game.menu;

import com.jogamp.opengl.GL2;
import game.Game;
import game.graphics.Sprite;
import game.graphics.SpriteSheet;
import game.math.Vec2d;
import game.menu.element.SpriteElement;

public class MainMenu extends Menu {

    public MainMenu() {
        addElements(
                new SpriteElement.Builder().sprite(Sprite.menuBG).position(0, 0).build(),
                new SpriteElement.Builder().sprite(Sprite.menuTitle)
                        .position(new Vec2d((Game.screen.getWidth() - SpriteSheet.menuTitle.getWidth()) / 2.0, 32))
                        .build()
        );
    }

    public void update() {

    }
}
