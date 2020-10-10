package itmo.gui.canvas;

import javafx.animation.FadeTransition;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import lab6common.generatedclasses.Route;

import java.util.ArrayList;

public class ResizableMapCanvas extends AbsResizableCanvas {

    private static final int SCREEN_START_MARGIN_ERROR_X = 10;
    private static final int SCREEN_START_MARGIN_ERROR_Y = 90;

    public ArrayList<Route> routeArrayList ;
    private double scale = 0;
    private GraphicsContext gc;
    private double min;
    private final Pane wrapperMapPane;

    public ResizableMapCanvas(ArrayList<Route> routeArrayList, Pane wrapperMapPane) {
        super();
        this.routeArrayList = routeArrayList;
        this.wrapperMapPane = wrapperMapPane;
    }

    @Override
    public Object findObj(double coordX, double coordY) throws NullPointerException {
        double min = Math.min(getWidth(), getHeight());
        double finalCoordX = (coordX - SCREEN_START_MARGIN_ERROR_X) * (scale / min) - scale / 2.0;
        double finalCoordY = scale / 2.0 - (coordY - SCREEN_START_MARGIN_ERROR_Y) * (scale / min);

        return routeArrayList.stream().filter(route ->
                Math.abs(route.getCoordinates().getX() - finalCoordX) < scale * 0.018)
                .filter(route ->
                        Math.abs(route.getCoordinates().getY() - finalCoordY) < scale * 0.018)
                .findAny().orElse(null);
    }

    @Override
    public void setObj(Object obj) {
        routeArrayList = (ArrayList<Route>) obj;
    }

    @Override
    public Object getObj() {
        return routeArrayList;
    }

    @Override
    public void draw() {
        double width = getWidth();
        double height = getHeight();
        min = Math.min(width, height);

        gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        // scale the map
        double maxx = routeArrayList.stream().mapToDouble(r-> r.getCoordinates().getX()).max().orElse(getWidth());
        double minx = routeArrayList.stream().mapToDouble(r-> r.getCoordinates().getX()).min().orElse(getHeight());
        double maxy = routeArrayList.stream().mapToDouble(r-> r.getCoordinates().getY()).max().orElse(0);
        double miny = routeArrayList.stream().mapToDouble(r-> r.getCoordinates().getY()).min().orElse(0);
        scale = 2 * Math.max(maxx, Math.max(-Math.min(minx, miny), maxy));

        //draw background
        gc.setFill(Color.DARKGREY);
        gc.fillRect(0, 0, width, height);

        //draw axis
        gc.setFill(Color.BLACK);
        gc.strokeLine(0, min / 2, min, min / 2);
        gc.strokeLine(min / 2, 0, min / 2, min);
        gc.fillText("0.0", min / 2, min / 2 + 20);
        gc.fillText(String.valueOf((int)(-scale*2/2.2 / 4)), min / 4, min / 2 + 20);
        gc.fillText(String.valueOf((int)(scale*2/2.2 / 4)), min * 3.0 / 4.0, min / 2 + 20);

        // Draw dragons
        routeArrayList.forEach(this::drawRoutes);
    }

    private void drawRoutes(Route route) {
        drawRoute(((route.getCoordinates().getX() + scale / 2.0) * (min / scale)),
                ((scale / 2.0 - route.getCoordinates().getY()) * (min / scale)), gc, route);
    }

    public void drawRoute(double x, double y, GraphicsContext gc, Route route) {
        double size = setSize(route);
        x = x - size*120/2D;
        y = y - size*120/2D;
        gc.setFill(Color.valueOf(route.getId().toString()));
        drawShape(gc, route, size, x, y);
    }

    private double setSize(Route route) {
        if (route.getDistance()<50) return 0.05D*min/400;
        if (route.getDistance() > 1000) {
            return 1D*min/400;
        }
        return route.getDistance()*min/400000D;
    }



    private void drawShape(GraphicsContext gc, Route route, double size, double x, double y) {
        gc.fillPolygon(new double[]{45 * size + x, 85 * size + x, 65 * size + x},
                new double[]{70 * size + y, 70 * size + y, 120 * size + y}, 3);
        gc.fillOval(40 * size + x, 40 * size + y, 50 * size, 50 * size);
        gc.setFill(Color.WHITE);
        gc.fillOval(50 * size + x, 50 * size + y, 30 * size, 30 * size);

    }

    @Override
    public void animateEntry(Route route) {
        double x = ((route.getCoordinates().getX() + scale / 2.0) * (min / scale));
        double y = ((scale / 2.0 - route.getCoordinates().getY()) * (min / scale));
        Circle circle = new Circle(x, y, setSize(route) * 120, Color.valueOf(route.toString()));
        wrapperMapPane.getChildren().add(circle);
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(4), circle);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setCycleCount(1);
        fadeOut.play();

        fadeOut.setOnFinished(e -> {
            wrapperMapPane.getChildren().remove(circle);
            drawRoutes(route);
        });
    }
}
