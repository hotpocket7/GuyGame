package game.menu;

public interface Selectable {
    public void select();

    public Selectable getLeft();
    public Selectable getRight();
    public Selectable getUp();
    public Selectable getDown();
}
