package game.menu;

import com.jogamp.opengl.GL2;
import game.Game;
import game.graphics.Color4f;
import game.graphics.RenderUtils;
import game.input.Input;
import game.math.Vec2d;
import game.menu.element.Element;
import game.menu.element.SelectableElement;

import java.util.ArrayList;
import java.util.Stack;

public abstract class Menu {

    public static enum State {
        ENTERING, ACTIVE, EXITING
    }

    public static Menu mainMenu = new MainMenu();

    public static Stack<Menu> menuStack = new Stack<>();

    private State state = State.ACTIVE;
    private Menu nextMenu;

    protected ArrayList<Element> elements = new ArrayList<>();
    protected SelectableElement selection, previousSelection;
    protected long moveTime = 0;

    protected long enterTime, exitTime;

    public void enter() {
        state = State.ENTERING;
        setMenu(this);
        enterTime = System.currentTimeMillis();
    }

    public void exitTo(Menu menu) {
        state = State.EXITING;
        nextMenu = menu;
        exitTime = System.currentTimeMillis();
    }

    public static void setMenu(Menu menu) {
        menuStack.push(menu);
    }

    public void update() {
        Input input = Game.game.input;

        if(input.keyHit("down") && selection.down() != null) {
            previousSelection = selection;
            selection = selection.down();
            moveTime = System.currentTimeMillis();
        }
        if(input.keyHit("up") && selection.up() != null) {
            previousSelection = selection;
            selection = selection.up();
            moveTime = System.currentTimeMillis();
        }
        if(input.keyHit("left") && selection.left() != null) {
            previousSelection = selection;
            selection = selection.left();
            moveTime = System.currentTimeMillis();
        }
        if(input.keyHit("right") && selection.right() != null) {
            previousSelection = selection;
            selection = selection.right();
            moveTime = System.currentTimeMillis();
        }

        if(input.keyHit("select"))
            selection.select();
    }

    public final void render(GL2 gl) {
        elements.forEach(e -> e.render(gl));
        switch(state) {
            case ENTERING:
                renderEnter(gl);
                break;
            case EXITING:
                renderExit(gl);
                break;
            default:
        }
    }

    protected void renderEnter(GL2 gl) {
        float alpha = (1000 - Math.min(1000, System.currentTimeMillis() - enterTime)) / 1000f;
        RenderUtils.fillRect(new Vec2d(), Game.screen.getWidth(), Game.screen.getHeight(),
                new Color4f(0, 0, 0, alpha), gl);
    }

    protected void renderExit(GL2 gl) {
        float alpha = (Math.min(1000, System.currentTimeMillis() - enterTime)) / 1000f;
        RenderUtils.fillRect(new Vec2d(), Game.screen.getWidth(), Game.screen.getHeight(),
                new Color4f(0, 0, 0, alpha), gl);

    }

    public void addElement(Element element) {
        elements.add(element);
    }

    public void addElements(Element... elements) {
        for(Element element : elements) {
            addElement(element);
        }
    }
}
