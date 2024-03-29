package ca.ubc.cs.cpsc210.translink.ui;

import android.content.Context;
import android.graphics.Color;
import ca.ubc.cs.cpsc210.translink.BusesAreUs;
import ca.ubc.cs.cpsc210.translink.model.*;
import ca.ubc.cs.cpsc210.translink.util.Geometry;
import ca.ubc.cs.cpsc210.translink.util.LatLon;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.overlays.Polyline;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// A bus route drawer
public class BusRouteDrawer extends MapViewOverlay {
    /** overlay used to display bus route legend text on a layer above the map */
    private BusRouteLegendOverlay busRouteLegendOverlay;
    /** overlays used to plot bus routes */
    private List<Polyline> busRouteOverlays;

    /**
     * Constructor
     * @param context   the application context
     * @param mapView   the map view
     */
    public BusRouteDrawer(Context context, MapView mapView) {
        super(context, mapView);
        busRouteLegendOverlay = createBusRouteLegendOverlay();
        busRouteOverlays = new ArrayList<>();
    }

    /**
     * Plot each visible segment of each route pattern of each route going through the selected stop.
     */
    public void plotRoutes(int zoomLevel) {
        updateVisibleArea();

        busRouteLegendOverlay.clear();
        busRouteOverlays.clear();


        Stop stop = StopManager.getInstance().getSelected();
        if(stop!=null) {
            for (Route route : stop.getRoutes()) {
                busRouteLegendOverlay.add(route.getNumber());
                for (RoutePattern rp : route.getPatterns()) {

                    List<LatLon> latlons = rp.getPath();
                    for (int i =0;i<latlons.size()-1;i++) {
                        LatLon l1 = latlons.get(i);
                        LatLon l2 = latlons.get(i+1);
                        if (Geometry.rectangleIntersectsLine(northWest,southEast,l1,l2)){
                            Polyline segment = new Polyline(context);
                            ArrayList lop = new ArrayList<GeoPoint>();
                            lop.add(Geometry.gpFromLL(l1));
                            lop.add(Geometry.gpFromLL(l2));
                            segment.setPoints(lop);
                            segment.setVisible(true);
                            busRouteOverlays.add(segment);
                            segment.setColor(busRouteLegendOverlay.getColor(route.getNumber()));
                        }
                    }
                }
            }
        }
    }

    public List<Polyline> getBusRouteOverlays() {
        return Collections.unmodifiableList(busRouteOverlays);
    }

    public BusRouteLegendOverlay getBusRouteLegendOverlay() {
        return busRouteLegendOverlay;
    }


    /**
     * Create text overlay to display bus route colours
     */
    private BusRouteLegendOverlay createBusRouteLegendOverlay() {
        ResourceProxy rp = new DefaultResourceProxyImpl(context);
        return new BusRouteLegendOverlay(rp, BusesAreUs.dpiFactor());
    }

    /**
     * Get width of line used to plot bus route based on zoom level
     * @param zoomLevel   the zoom level of the map
     * @return            width of line used to plot bus route
     */
    private float getLineWidth(int zoomLevel) {
        if(zoomLevel > 14)
            return 7.0f * BusesAreUs.dpiFactor();
        else if(zoomLevel > 10)
            return 5.0f * BusesAreUs.dpiFactor();
        else
            return 2.0f * BusesAreUs.dpiFactor();
    }
}
