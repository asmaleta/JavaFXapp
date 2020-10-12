package itmo.utils;


import lab6common.generatedclasses.Route;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@Data
public class ClientCollectionManager {

    private List<Route> routeList;
    public ClientCollectionManager() {
        routeList = new ArrayList<>();
    }

    public ClientCollectionManager(List<Route> response) {
        routeList = response;
    }

    public synchronized List<Route> getRouteList() {
        return routeList;
    }
    public synchronized HashSet<Route> getRouteHashSet() {
        return new HashSet<>(routeList);
    }




    public Route getByID(int id) {
        return  routeList.stream()
                .filter(route-> route.getId() == id)
                .findAny()
                .orElse(null);
    }

    public double getSumNumFields() {
        return routeList.stream()
                .mapToDouble(e ->
                        (double) (e.getCorX()
                                    +e.getCorY()
                                    +e.getFromX()
                                    +e.getFromY()
                                    +e.getToX()
                                    +e.getToY()
                                    + e.getDistance()))
                .sum();
    }

    public long getSumNames() {
        return routeList.stream()
                .mapToLong(e ->
                        (long) (e.getName().length())
                                + e.getFromName().length()
                                + e.getToName().length())
                .sum();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ClientCollectionManager that = (ClientCollectionManager) o;
        return Math.abs(this.getSumNumFields() - that.getSumNumFields()) < 0.000001d &&
                this.getSumNames() == that.getSumNames();
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeList);}


}
