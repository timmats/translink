package ca.ubc.cs.cpsc210.translink.providers;

import ca.ubc.cs.cpsc210.translink.model.Stop;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Wrapper for Translink Bus Location Data Provider
 */
public class HttpBusLocationDataProvider extends AbstractHttpDataProvider {
    private Stop stop;

    public HttpBusLocationDataProvider(Stop stop) {
        super();
        this.stop = stop;
    }

    @Override
    /**
     * Produces URL used to query Translink web service for locations of buses serving
     * the stop specified in call to constructor.
     *
     * @returns URL to query Translink web service for arrival data
     */
    protected URL getURL() throws MalformedURLException {

        String stopnum = String.valueOf(stop.getNumber());
        String spec = "http://api.translink.ca/rttiapi/v1/buses?apikey=MGXUwmzY9dwsoUPtsqQM&stopNo="+stopnum;
        return new URL(spec);
    }

    @Override
    public byte[] dataSourceToBytes() throws IOException {
        return new byte[0];
    }
}
