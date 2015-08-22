package game.menu;

import com.sun.org.apache.bcel.internal.generic.Select;
import game.Game;
import game.Screen;
import game.graphics.Color4f;
import game.graphics.RenderUtils;
import game.graphics.Sprite;
import game.graphics.SpriteSheet;
import game.math.Vec2d;
import game.menu.element.*;

public class MainMenu extends Menu {

    RectangularElement selectionIndicator;

    public MainMenu() {
        SelectableElement playButton, optionsButton, exitButton;

        playButton = (SelectableElement) new SelectableElement.Builder()
                .element(
                        new TextElement.Builder().text("Play").color(Color4f.WHITE).font(RenderUtils.trajan48)
                                .position(Game.screen.getWidth() / 2, Game.screen.getHeight() / 2 + 64).build()
                )
//              .onSelect(() -> Game.game.screen.getScene().exitTo(Game.game.screen.playScene))
                .onSelect(() -> Game.screen.getScene().exitTo(Game.screen.playScene))
                .build();

        optionsButton = (SelectableElement) new SelectableElement.Builder()
                .element(
                    new TextElement.Builder().text("Options").color(Color4f.WHITE).font(RenderUtils.trajan48)
                    .position(Game.screen.getWidth()/2, Game.screen.getHeight()/2 + 64 * 2).build()
                )
                .onSelect(() -> System.exit(0))
                .build();

        exitButton = (SelectableElement) new SelectableElement.Builder()
                .element(
                    new TextElement.Builder().text("Exit").color(Color4f.WHITE).font(RenderUtils.trajan48)
                    .position(Game.screen.getWidth()/2, Game.screen.getHeight()/2 + 64 * 3).build()
                )
                .onSelect(() -> System.exit(0))
                .build();
        selection = playButton;
        previousSelection = selection;

        playButton.setDown(optionsButton);

        optionsButton.setUp(playButton);
        optionsButton.setDown(exitButton);

        exitButton.setUp(optionsButton);

        selectionIndicator = (RectangularElement)
                new RectangularElement.Builder()
                        .size(252, 48).color(0x22/255f, 0x22/255f, 0x22/255f, 0.5f)
                        .position(selection.position.subtract(126, 41))
                        .build();

        addElements(
                new SpriteElement.Builder().sprite(Sprite.menuBG).position(0, 0).build(),
                new SpriteElement.Builder().sprite(Sprite.menuTitle)
                    .position(new Vec2d((Game.screen.getWidth() - SpriteSheet.menuTitle.getWidth()) / 2.0, 32))
                    .build(),
                new RectangularElement.Builder()
                    .size(256, 220).color(190/255f, 190/255f, 190/255f, 60/255f)
                    .borderThickness(2).borderColor(1, 1, 1, 1f)
                    .position((Game.screen.getWidth() - 256) / 2, Game.screen.getHeight()/2)
                    .build(),
                selectionIndicator,
                playButton,
                optionsButton,
                exitButton
        );
    }

    public void update() {
        super.update();

        Vec2d goalPos = selection.position.subtract(126, 41);
        Vec2d dist = previousSelection == null ? new Vec2d(0) : selection.position.subtract(previousSelection
                .position);
        if(!selectionIndicator.position.equals(goalPos)) {
            selectionIndicator.position.plusEquals(
                    dist.multiply(0.25)
            );
        }
    }
}
