package n9e.hr.detec;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import n9e.hr.context.Context;
import n9e.hr.model.Endpoint;
import n9e.hr.model.Endpoints;
import n9e.hr.model.Node;
import n9e.hr.monapi.MonApiCaller;

public class ReachableDetec {
	private static final Logger logger = LogManager.getLogger();

	public static void loopDetecReachableAndReport() {
		while (true) {
			detecReachableAndRepot();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				logger.warn(e.getMessage());
			}
		}
	}

	public static void detecReachableAndRepot() {
		JsonArray endpointsMetricsJa = new JsonArray();

		Endpoints endpoints = Context.getEndpoints();
		HashMap<String, Endpoint> endpointsMap = endpoints.getEndpointsMap();

		String srcIp = null;
		try {
			srcIp = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			logger.error(e.getMessage());
		}
		if (srcIp == null || srcIp.length() == 0 || "127.0.0.1".equals(srcIp) || "::1".equals(srcIp) || "localhost".equalsIgnoreCase(srcIp)) {
			logger.warn("srcIp={}", srcIp);
			logger.info("set srcIp to collectorId: {}", Context.getCfg().getCollectorId());
			srcIp = Context.getCfg().getCollectorId();
		}

		for (Entry<String, Endpoint> endpointEntry : endpointsMap.entrySet()) {
			Endpoint endpoint = endpointEntry.getValue();
			String ident = endpoint.getIdent();

			if (!needDetec(endpoint)) {
				logger.debug("needDetec:false:{}",endpoint.getIdent());
				continue;
			}else{
				logger.debug("needDetec:true:{}",endpoint.getIdent());
			}

			JsonObject endpointMetricJo = new JsonObject();
			endpointMetricJo.addProperty("endpoint", ident);
			endpointMetricJo.addProperty("metric", Context.getCfg().getMeticName());
			endpointMetricJo.addProperty("tags", "srcIp=" + srcIp + ",collector=" + Context.getCfg().getCollectorId());
			endpointMetricJo.addProperty("timestamp", getCurrTimeSecondTs());
			endpointMetricJo.add("value", null);
			endpointMetricJo.addProperty("step", Context.getCfg().getReportStep());

			endpointsMetricsJa.add(endpointMetricJo);

			new Thread(new Runnable() {
				@Override
				public void run() {
					
					InetAddress addr;
					try {
						addr = InetAddress.getByName(ident);
					} catch (UnknownHostException e) {
						logger.warn("UnknownHostException:[{}]:[{}]", ident,e.getMessage());
						endpointMetricJo.add("value", null);
						return;
					}

					try {
						boolean reachable = addr.isReachable(Context.getCfg().getDetecHostReachableTimeout());
						endpointMetricJo.addProperty("value", reachable ? 1 : 0); 
					} catch (IOException e) {
						logger.warn("IOException:[{}]:[{}]", ident,e.getMessage());
						endpointMetricJo.add("value", null);
						return;
					}
				}
			}).start();

		}

		try {
			Thread.sleep(Context.getCfg().getBatchDetecReachableIntervalMilliSeconds());
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}

		logger.debug("endpointsMetricsJa.size={}", endpointsMetricsJa.size());
		String pushContent = endpointsMetricsJa.toString();
		logger.debug("pushContent:{}", pushContent);
		
		if(endpointsMetricsJa.size()>0){
			String pushResponseStr = MonApiCaller.pushToTransfer(pushContent);
			logger.info("pushResponse:{}", pushResponseStr);
		}else{
			logger.info("no push content need to push");
		}
			
	}

	private static boolean needDetec(Endpoint endpoint) {
		if(Context.getCfg().isUseEndpointMatch()){
			 String regex = Context.getCfg().getEndpointMatchRegExp();
		     boolean isMatch = Pattern.matches(regex, endpoint.getIdent());
		     if(!isMatch){
		    	 return false;
		     }
		}
		
		if(Context.getCfg().isUseNodeNameMatch()){
			 String regex = Context.getCfg().getNodeNameMatchRegExp();
			 
			 ArrayList<Node> nodes=endpoint.getNodes();
			 for(Node node:nodes){
				 boolean isMatch = Pattern.matches(regex, node.getPath());
			     if(isMatch){
			    	 return true;
			     }
			 }
			 return false;
		}
		
		return true;
	}

	private static long getCurrTimeSecondTs() {
		return System.currentTimeMillis() / 1000;
	}
}
