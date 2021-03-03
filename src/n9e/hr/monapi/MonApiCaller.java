package n9e.hr.monapi;

import java.io.IOException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import n9e.hr.cfg.Cfg;
import n9e.hr.context.Context;
import n9e.hr.model.Endpoint;
import n9e.hr.model.Endpoints;

public class MonApiCaller {
	private static final Logger logger = LogManager.getLogger();
	private static JsonParser jsonParser = new JsonParser();
	private static Base64 base64 = new Base64();

	private static String endpointsUrl = "/api/portal/endpoint?field=ident&limit=1000000&p=1";
	private static String endpointsBindingsUrl = "/api/portal/endpoints/bindings?idents=";

	private static String pushTransferUrl = "/api/transfer/push";

	public static Endpoints getEndpoints() {
		Cfg cfg = Context.getCfg();
		if (cfg.getMonapiUrl() == null || cfg.getMonapiUrl().length() == 0) {
			logger.error("cfg.getMonapiUrl() is empty");
			return null;
		}
		try {
			logger.info("call http to get endpoints");
			String urlOfGetEndpointList = cfg.getMonapiUrl() + endpointsUrl;
			String responseStr = httpGet(urlOfGetEndpointList, cfg.getMonapiUserName(), cfg.getMonapiPassword());

			JsonObject responseJo = jsonParser.parse(responseStr).getAsJsonObject();
			JsonArray listJa = responseJo.get("dat").getAsJsonObject().get("list").getAsJsonArray();

			StringBuilder identsSb = new StringBuilder();
			logger.info("/api/portal/endpoint dat.list size={}", listJa.size());
			for (int i = 0; i < listJa.size(); i++) {
				identsSb.append(listJa.get(i).getAsJsonObject().get("ident").getAsString());
				if (i < listJa.size() - 1) {
					identsSb.append(',');
				}
			}

			Endpoints endpoints = new Endpoints();
			logger.info("call http to get endpoints-nodes");
			String urlOfGetEndpointsBindings = cfg.getMonapiUrl() + endpointsBindingsUrl + identsSb.toString();

			responseStr = httpGet(urlOfGetEndpointsBindings, cfg.getMonapiUserName(), cfg.getMonapiPassword());
			responseJo = jsonParser.parse(responseStr).getAsJsonObject();
			listJa = responseJo.get("dat").getAsJsonArray();
			Gson gson = new Gson();
			logger.info("/api/portal/endpoints/bindings dat size={}", listJa.size());
			for (int i = 0; i < listJa.size(); i++) {
				Endpoint endpoint = gson.fromJson(listJa.get(i).getAsJsonObject(), Endpoint.class);
				endpoints.putEndpoint(endpoint.getIdent(), endpoint);
			}
			logger.info("endpoints count={}", endpoints.getEndpointsMap().size());
			return endpoints;
		} catch (Exception e) {
			logger.error("Exception: {}", e.getMessage());
		}
		return null;
	}

	public static String pushToTransfer(String content) {
		Cfg cfg = Context.getCfg();
		if (cfg.getMonapiUrl() == null || cfg.getMonapiUrl().length() == 0) {
			logger.error("cfg.getMonapiUrl() is empty");
			return null;
		}
		String url = cfg.getMonapiUrl() + pushTransferUrl;
		logger.info("call http to push metrics");
		String responseStr = httpPost(url, cfg.getMonapiUserName(), cfg.getMonapiPassword(), content);
		logger.info("response of push metrics: {}", responseStr);
		return responseStr;
	}

	private static String httpGet(String url, String username, String password) {
		logger.info("call url: {}", url);
		CloseableHttpClient httpClient = HttpClients.createDefault();

		CloseableHttpResponse response = null;
		try {

			HttpGet httpGet = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(20000).setSocketTimeout(30000).build();

			httpGet.setConfig(requestConfig);

			String authStr = username + ":" + password;
			String authBase64Str = new String(base64.encode(authStr.getBytes("UTF-8")));
			httpGet.setHeader("Authorization", "Basic " + authBase64Str);

			response = httpClient.execute(httpGet);

			String responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			logger.debug(responseStr);
			return responseStr;
		} catch (Exception e) {
			logger.error("Exception: {} : {}", url, e.getMessage());
			return null;
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}

	private static String httpPost(String url, String username, String password, String content) {
		logger.info("call url: {}", url);
		CloseableHttpClient httpClient = HttpClients.createDefault();

		CloseableHttpResponse response = null;
		try {

			HttpPost httpPost = new HttpPost(url);
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(5000).setConnectionRequestTimeout(30000).setSocketTimeout(40000).build();
			httpPost.setConfig(requestConfig);

			String authStr = username + ":" + password;
			String authBase64Str = new String(base64.encode(authStr.getBytes("UTF-8")));
			httpPost.setHeader("Authorization", "Basic " + authBase64Str);

			StringEntity entity = new StringEntity(content, ContentType.APPLICATION_JSON);
			entity.setContentEncoding("UTF-8");
			httpPost.setEntity(entity);

			response = httpClient.execute(httpPost);

			String responseStr = EntityUtils.toString(response.getEntity(), "UTF-8");
			logger.debug(responseStr);
			return responseStr;
		} catch (Exception e) {
			logger.error("when call http url: {},exception: {}", url, e.getMessage());
			return null;
		} finally {
			try {
				if (response != null) {
					response.close();
				}
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
		}
	}
}
