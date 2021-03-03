package n9e.hr.cfg;

public class Cfg {
	private String collectorId;
	private String monapiUrl;
	private String monapiUserName;
	private String monapiPassword;
	private long syncEndpointsIntervalSeconds=120;
	private int detecHostReachableTimeout=5000;
	private String meticName="host.net.reachable";
	private int batchDetecReachableIntervalMilliSeconds=60000;
	private boolean useEndpointMatch = false;
	private String endpointMatchRegExp;
	private boolean useNodeNameMatch = false;
	private String nodeNameMatchRegExp;
	private int reportStep=60;

	public boolean isUseEndpointMatch() {
		return useEndpointMatch;
	}

	public void setUseEndpointMatch(boolean useEndpointMatch) {
		this.useEndpointMatch = useEndpointMatch;
	}

	public String getEndpointMatchRegExp() {
		return endpointMatchRegExp;
	}

	public void setEndpointMatchRegExp(String endpointMatchRegExp) {
		this.endpointMatchRegExp = endpointMatchRegExp;
	}

	public boolean isUseNodeNameMatch() {
		return useNodeNameMatch;
	}

	public void setUseNodeNameMatch(boolean useNodeNameMatch) {
		this.useNodeNameMatch = useNodeNameMatch;
	}

	public String getNodeNameMatchRegExp() {
		return nodeNameMatchRegExp;
	}

	public void setNodeNameMatchRegExp(String nodeNameMatchRegExp) {
		this.nodeNameMatchRegExp = nodeNameMatchRegExp;
	}

	public String getMonapiUrl() {
		return monapiUrl;
	}

	public void setMonapiUrl(String monapiUrl) {
		this.monapiUrl = monapiUrl;
	}

	public String getMonapiUserName() {
		return monapiUserName;
	}

	public void setMonapiUserName(String monapiUserName) {
		this.monapiUserName = monapiUserName;
	}

	public String getMonapiPassword() {
		return monapiPassword;
	}

	public void setMonapiPassword(String monapiPassword) {
		this.monapiPassword = monapiPassword;
	}


	public String getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(String collectorId) {
		this.collectorId = collectorId;
	}

	public long getSyncEndpointsIntervalSeconds() {
		return syncEndpointsIntervalSeconds;
	}

	public void setSyncEndpointsIntervalSeconds(long syncEndpointsIntervalSeconds) {
		this.syncEndpointsIntervalSeconds = syncEndpointsIntervalSeconds;
	}

	public int getDetecHostReachableTimeout() {
		return detecHostReachableTimeout;
	}

	public void setDetecHostReachableTimeout(int detecHostReachableTimeout) {
		this.detecHostReachableTimeout = detecHostReachableTimeout;
	}

	public int getBatchDetecReachableIntervalMilliSeconds() {
		return batchDetecReachableIntervalMilliSeconds;
	}

	public void setBatchDetecReachableIntervalMilliSeconds(int batchDetecReachableIntervalMilliSeconds) {
		this.batchDetecReachableIntervalMilliSeconds = batchDetecReachableIntervalMilliSeconds;
	}

	public int getReportStep() {
		return reportStep;
	}

	public void setReportStep(int reportStep) {
		this.reportStep = reportStep;
	}

	public String getMeticName() {
		return meticName;
	}

	public void setMeticName(String meticName) {
		this.meticName = meticName;
	}



}
