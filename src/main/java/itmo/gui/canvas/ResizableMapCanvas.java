package itmo.gui.canvas;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.util.Duration;
import lab6common.generatedclasses.Route;

import java.util.ArrayList;

public class ResizableMapCanvas extends AbsResizableCanvas {

    private static final int SCREEN_START_MARGIN_ERROR_X = 10;
    private static final int SCREEN_START_MARGIN_ERROR_Y = 40;

    public ArrayList<Route> routeArrayList ;
    private double scale = 0;
    private GraphicsContext gc;
    private double min;
    private Pane wrapperMapPane;

    public ResizableMapCanvas(ArrayList<Route> routeArrayList, Pane wrapperMapPane) {
        super();
        this.routeArrayList = routeArrayList;
        this.wrapperMapPane = wrapperMapPane;
    }

    @Override
    public Object findObj(double coordX, double coordY) throws NullPointerException {
        double min = Math.min(getWidth(), getHeight());
        double finalCoordX = (coordX-SCREEN_START_MARGIN_ERROR_X) * (scale / min) - scale / 2.0;
        double finalCoordY = scale / 2.0 - (coordY-SCREEN_START_MARGIN_ERROR_Y) * (scale / min);
        gc.setFill(Color.BLACK);
        gc.strokeLine(coordX-SCREEN_START_MARGIN_ERROR_X- min * 0.030, coordY-SCREEN_START_MARGIN_ERROR_Y, coordX-SCREEN_START_MARGIN_ERROR_X+ min * 0.030, coordY-SCREEN_START_MARGIN_ERROR_Y);
        gc.strokeLine(coordX-SCREEN_START_MARGIN_ERROR_X, coordY-SCREEN_START_MARGIN_ERROR_Y- min * 0.030, coordX-SCREEN_START_MARGIN_ERROR_X, coordY-SCREEN_START_MARGIN_ERROR_Y+ min * 0.030);
        return routeArrayList.stream().filter(route ->
                Math.abs(route.getCoordinates().getX() - finalCoordX) < scale * 0.030)
                .filter(route ->
                        Math.abs(route.getCoordinates().getY() - finalCoordY) < scale * 0.030)
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
        if (route.getDistance()<50.0f) return route.getDistance()*min/30_000d;
        if (route.getDistance() > 1000.0f) {
            return route.getDistance()*min/500_000d;
        }
        return route.getDistance()*min/250_000D;
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
        double size = setSize(route);
        x = x - size*120/2D;
        y = y - size*120/2D;
        Circle circle = new Circle(65 * size + x, 60 * size + y, 50 * size, Color.valueOf(route.getId().toString()));
        Polygon polygon = new Polygon(15 * size + x,60 * size + y,
                115 * size + x,60 * size + y,65 * size + x,150 * size + y);
        Circle circlew = new Circle(65 * size + x, 60 * size + y, 30 * size, Color.valueOf("white"));
        wrapperMapPane.getChildren().addAll(circle,polygon,circlew);
        FadeTransition fadeOut = new FadeTransition();
        fadeOut.setNode(circle);fadeOut.setNode(polygon);fadeOut.setNode(circlew);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setCycleCount(1);
        fadeOut.play();

        fadeOut.setOnFinished(e -> {
            wrapperMapPane.getChildren().removeAll(circle,polygon,circlew);
            drawRoutes(route);
        });
    }
    @Override
    public void animateMove(Route object, Route newRoute) {
        double x = ((object.getCoordinates().getX() + scale / 2.0) * (min / scale));
        double y = ((scale / 2.0 - object.getCoordinates().getY()) * (min / scale));
        double size = setSize(object);
        x = x - size*120/2D;
        y = y - size*120/2D;
        double xNew = ((object.getCoordinates().getX() + scale / 2.0) * (min / scale));
        double yNew = ((scale / 2.0 - object.getCoordinates().getY()) * (min / scale));
        double sizeNew = setSize(object);
        xNew= xNew - sizeNew*120/2D;
        yNew = yNew - sizeNew*120/2D;
        Circle circle = new Circle(65 * size + x, 60 * size + y, 50 * size, Color.valueOf(object.getId().toString()));
        Polygon polygon = new Polygon(15 * size + x,60 * size + y,
                115 * size + x,60 * size + y,65 * size + x,150 * size + y);
        Circle circlew = new Circle(65 * size + x, 60 * size + y, 30 * size, Color.valueOf("white"));

        KeyValue circleX = new KeyValue(circle.centerXProperty(),65 * sizeNew + xNew);
        KeyValue circleY = new KeyValue(circle.centerYProperty(),60 * sizeNew + yNew);
        KeyValue circlewX = new KeyValue(circlew.centerXProperty(),65 * sizeNew + xNew);
        KeyValue circlewY = new KeyValue(circlew.centerYProperty(),60 * sizeNew + yNew);

        KeyFrame keyFrameW = new KeyFrame(Duration.seconds(1),circlewX,circlewY);
        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1),circleX,circleY);
        wrapperMapPane.getChildren().addAll(circle,polygon,circlew);
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().addAll(keyFrame,keyFrameW);
        timeline.play();

        timeline.setOnFinished(e -> {
            wrapperMapPane.getChildren().removeAll(circle,polygon,circlew);
        });
    }

    @Override
    public void animateRemove(Route object) {
        double x = ((object.getCoordinates().getX() + scale / 2.0) * (min / scale));
        double y = ((scale / 2.0 - object.getCoordinates().getY()) * (min / scale));
        double size = setSize(object);
        x = x - size*120/2D;
        y = y - size*120/2D;

        Circle circle = new Circle(65 * size + x, 60 * size + y, 50 * size, Color.valueOf(object.getId().toString()));
        Polygon polygon = new Polygon(15 * size + x,60 * size + y,
                115 * size + x,60 * size + y,65 * size + x,150 * size + y);
        Circle circlew = new Circle(65 * size + x, 60 * size + y, 30 * size, Color.valueOf("white"));
        wrapperMapPane.getChildren().addAll(circle,polygon,circlew);
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(4));
        fadeOut.setNode(circle);fadeOut.setNode(polygon);fadeOut.setNode(circlew);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setCycleCount(1);
        fadeOut.play();

        fadeOut.setOnFinished(e -> {
            wrapperMapPane.getChildren().removeAll(circle,polygon,circlew);
        });
    }

    @Override
    public void animateUpdate(Route object, Route newRoute) {

    }
}
