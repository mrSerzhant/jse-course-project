package database.datamain;
import database.datamain.utils.ListRoutesUtils;
import java.util.HashMap;

public class MapBuilder extends HashMap<String, Route> {

    public MapBuilder() {
        ListRoutes listRoute = new ListRoutes();
        for(Route route: listRoute.getRouteList()){
            super.put(ListRoutesUtils.routeNameMaker(route.getStops()),route);
        }
    }
}
