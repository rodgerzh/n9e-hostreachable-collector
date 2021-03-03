package n9e.hr.model;

import java.util.HashMap;

public class Endpoints {

	private HashMap<String, Endpoint> endpointsMap=new HashMap<String, Endpoint>(); // <ident, Endpoint>

	public HashMap<String, Endpoint> getEndpointsMap() {
		return endpointsMap;
	}

	public void setEndpointsMap(HashMap<String, Endpoint> endpointsMap) {
		this.endpointsMap = endpointsMap;
	}

	public Endpoint getEndpoint(String ident) {
		return endpointsMap.get(ident);
	}

	public void putEndpoint(String ident, Endpoint endpoint) {
		endpointsMap.put(ident, endpoint);
	}
}
