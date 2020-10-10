package itmo.gui.canvas;

import javafx.scene.canvas.Canvas;
import lab6common.generatedclasses.Route;


public abstract class AbsResizableCanvas extends Canvas {
    public AbsResizableCanvas() {
        // Redraw canvas when size changes.
        widthProperty().addListener(evt -> draw());
        heightProperty().addListener(evt -> draw());
    }

    public abstract void draw();

    public abstract Object findObj(double coordX, double coordY);

    public abstract void setObj(Object obj);
    public abstract Object getObj();

    public abstract void animateEntry(Route object);
    public abstract void animateMove(Route object,Route newRoute);
    public abstract void animateRemove(Route object);
    public abstract void animateUpdate(Route object, Route newRoute);

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
}
