package itmo.gui.canvas;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Duration;
import lab6common.generatedclasses.Route;

import java.util.HashSet;

public class ResizableMapCanvas extends AbsResizableCanvas {

    private static final int SCREEN_START_MARGIN_ERROR_X = 10;
    private static final int SCREEN_START_MARGIN_ERROR_Y = 40;

    public HashSet<Route> routeHashSet;
    private double scale = 0;
    private GraphicsContext gc;
    private double min;
    private Pane wrapperMapPane;

    public ResizableMapCanvas(HashSet<Route> routeHashSet, Pane wrapperMapPane) {
        super();
        this.routeHashSet = routeHashSet;
        this.wrapperMapPane = wrapperMapPane;
    }

    @Override
    public Object findObj(double coordX, double coordY) throws NullPointerException {
        double min = Math.min(getWidth(), getHeight());
        double finalCoordX = (coordX-SCREEN_START_MARGIN_ERROR_X) * (scale / min) - scale / 2.0;
        double finalCoordY = scale / 2.0 - (coordY-SCREEN_START_MARGIN_ERROR_Y) * (scale / min);
        return routeHashSet.stream().filter(route ->
                Math.abs(route.getCoordinates().getX() - finalCoordX) < scale * 0.030)
                .filter(route ->
                        Math.abs(route.getCoordinates().getY() - finalCoordY) < scale * 0.030)
                .findAny().orElse(null);
    }

    @Override
    public void setObj(Object obj) {
        routeHashSet = (HashSet<Route>) obj;
    }

    @Override
    public Object getObj() {
        return routeHashSet;
    }

    @Override
    public void draw() {
        double width = getWidth();
        double height = getHeight();
        min = Math.min(width, height);

        gc = getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);

        // scale the map
        double maxx = routeHashSet.stream().mapToDouble(r-> r.getCoordinates().getX()).max().orElse(getWidth());
        double minx = routeHashSet.stream().mapToDouble(r-> r.getCoordinates().getX()).min().orElse(getHeight());
        double maxy = routeHashSet.stream().mapToDouble(r-> r.getCoordinates().getY()).max().orElse(0);
        double miny = routeHashSet.stream().mapToDouble(r-> r.getCoordinates().getY()).min().orElse(0);
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
        routeHashSet.forEach(this::drawRoutes);
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

        Circle circle = new Circle(64 * size + x, 66 * size + y, 25 * size,Color.valueOf(route.getId().toString()));
        Polygon polygon = new Polygon(45 * size + x,60 * size + y,
                85 * size + x,60 * size + y,65 * size + x,120 * size + y);
        polygon.setFill(Color.valueOf(route.getId().toString()));
        Circle circlew = new Circle(64 * size + x, 66 * size + y, 15 * size, Color.valueOf("white"));
        routeHashSet.remove(route);
        draw();
        wrapperMapPane.getChildren().addAll(polygon,circle,circlew);
        FadeTransition fadePolygon = new FadeTransition(Duration.seconds(1),polygon);
        FadeTransition fadeCircle = new FadeTransition(Duration.seconds(1),circle);
        FadeTransition fadeCircleW = new FadeTransition(Duration.seconds(1),circlew);

        fadePolygon.setFromValue(0);
        fadeCircle.setFromValue(0);
        fadeCircleW.setFromValue(0);

        fadePolygon.setToValue(1);
        fadeCircle.setToValue(1);
        fadeCircleW.setToValue(1);

        fadePolygon.setCycleCount(1);
        fadeCircle.setCycleCount(1);
        fadeCircleW.setCycleCount(1);
        fadePolygon.play();
        fadeCircle.play();
        fadeCircleW.play();
        fadeCircleW.setOnFinished(e -> {
            wrapperMapPane.getChildren().removeAll(circle,polygon,circlew);
            routeHashSet.add(route);
            drawRoutes(route);
        });
    }


    @Override
    public void animateUpdate(Route object, Route newRoute) {
        double x = ((object.getCoordinates().getX() + scale / 2.0) * (min / scale));
        double y = ((scale / 2.0 - object.getCoordinates().getY()) * (min / scale));
        double size = setSize(object);
        x = x - size*120/2D;
        y = y - size*120/2D;
        double xNew = ((newRoute.getCoordinates().getX() + scale / 2.0) * (min / scale));
        double yNew = ((scale / 2.0 - newRoute.getCoordinates().getY()) * (min / scale));
        double sizeNew = setSize(newRoute);
        xNew= xNew - sizeNew*120/2D;
        yNew = yNew - sizeNew*120/2D;


        Circle circle = new Circle(65 * size + x, 60 * size + y, 25 * size,Color.valueOf(object.getId().toString()));
        Circle circlew = new Circle(65 * size + x, 60 * size + y, 15 * size, Color.valueOf("white"));
        //////triangle
        MoveTo corner1 = new MoveTo(45 * size + x,60 * size + y);
        LineTo corner2 = new LineTo(85 * size + x,60 * size + y);
        LineTo corner3 = new LineTo(65 * size + x,120 * size + y);
        Path path = new Path(corner1, corner2, corner3, new ClosePath());
        path.setFill(Color.valueOf(object.getId().toString()));

        routeHashSet.remove(object);
            draw();
        wrapperMapPane.getChildren().addAll(path,circle,circlew);
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2),
                        new KeyValue(circle.radiusProperty(), 25 * sizeNew),
                        new KeyValue(circle.centerXProperty(), 65 * sizeNew + xNew),
                        new KeyValue(circle.centerYProperty(), 60 * sizeNew + yNew),
                        new KeyValue(circlew.centerXProperty(), 65 * sizeNew + xNew),
                        new KeyValue(circlew.centerYProperty(), 60 * sizeNew + yNew),
                        new KeyValue(circlew.radiusProperty(), 15 * sizeNew),
                        new KeyValue(corner1.xProperty(), 45 * sizeNew + xNew),
                        new KeyValue(corner1.yProperty(), 60 * sizeNew + yNew),
                        new KeyValue(corner2.xProperty(), 85 * sizeNew + xNew),
                        new KeyValue(corner2.yProperty(), 60 * sizeNew + yNew),
                        new KeyValue(corner3.xProperty(), 65 * sizeNew + xNew),
                        new KeyValue(corner3.yProperty(), 120 * sizeNew + yNew)));
        timeline.setCycleCount(1);
        timeline.play();

        timeline.setOnFinished(e -> {
            wrapperMapPane.getChildren().removeAll(circlew,path,circle);
            routeHashSet.add(newRoute);
            draw();
        });
    }

    @Override
    public void animateRemove(Route object) {
        double x = ((object.getCoordinates().getX() + scale / 2.0) * (min / scale));
        double y = ((scale / 2.0 - object.getCoordinates().getY()) * (min / scale));
        double size = setSize(object);
        x = x - size*120/2D;
        y = y - size*120/2D;

        Circle circle = new Circle(65 * size + x, 66 * size + y, 25 * size,Color.valueOf(object.getId().toString()));
        Polygon polygon = new Polygon(45 * size + x,60 * size + y,
                85 * size + x,60 * size + y,65 * size + x,120 * size + y);
        polygon.setFill(Color.valueOf(object.getId().toString()));
        Circle circlew = new Circle(65 * size + x, 66 * size + y, 15 * size, Color.valueOf("white"));;

        wrapperMapPane.getChildren().addAll(polygon,circle,circlew);
        routeHashSet.remove(object);
        draw();

        FadeTransition fadePolygon = new FadeTransition(Duration.seconds(2),polygon);
        FadeTransition fadeCircle = new FadeTransition(Duration.seconds(2),circle);
        FadeTransition fadeCircleW = new FadeTransition(Duration.seconds(2),circlew);

        fadePolygon.setFromValue(1);
        fadeCircle.setFromValue(1);
        fadeCircleW.setFromValue(1);

        fadePolygon.setToValue(0);
        fadeCircle.setToValue(0);
        fadeCircleW.setToValue(0);

        fadePolygon.setCycleCount(1);
        fadeCircle.setCycleCount(1);
        fadeCircleW.setCycleCount(1);
        fadePolygon.play();
        fadeCircle.play();
        fadeCircleW.play();
        fadeCircleW.setOnFinished(e -> {
            wrapperMapPane.getChildren().removeAll(circle,polygon,circlew);
        });
    }

}
