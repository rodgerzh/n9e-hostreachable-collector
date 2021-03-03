package n9e.hr.context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import n9e.hr.cfg.Cfg;
import n9e.hr.model.Endpoints;
import n9e.hr.monapi.MonApiCaller;

public class Context {
	private static final Logger logger = LogManager.getLogger();
	private static Cfg cfg;
	private static Object cfgLock = new Object();

	private static Endpoints endpoints;
	private static Object endpointsLock = new Object();

	public static void initCfg() {
		logger.info("begin to init Config");

		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(new File("config/cfg.json")), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			char[] cbuf = new char[1024];
			int readNum = -1;
			while ((readNum = reader.read(cbuf)) != -1) {
				sb.append(cbuf, 0, readNum);
			}
			String fileText = sb.toString();
			Gson gson = new Gson();

			synchronized (cfgLock) {
				Context.cfg = gson.fromJson(fileText, Cfg.class);
			}

		} catch (FileNotFoundException e) {
			System.err.println("config file not found");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("config file read error");
			System.exit(1);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		logger.info("finish init Config");
	}

	public static void syncEndpoints() {
		Cfg cfg = getCfg();
		if (cfg.getMonapiUrl() == null || cfg.getMonapiUrl().length() == 0) {
			logger.error("cfg.getMonapiUrl() is empty");
			return;
		}

		Endpoints newEndpoints = MonApiCaller.getEndpoints();
		
		if (newEndpoints != null) {
			synchronized (endpointsLock) {
				Context.endpoints = newEndpoints;
			}
		}
	}

	public static void loopSyncEndpoints() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					try {
						Thread.sleep(getCfg().getSyncEndpointsIntervalSeconds()*1000);
					} catch (InterruptedException e) {
						logger.warn(e.getMessage());
					}

					syncEndpoints();
				}
			}
		}).start();
	}

	public static Cfg getCfg() {
		synchronized (cfgLock) {
			return Context.cfg;
		}
	}

	public static Endpoints getEndpoints() {
		synchronized (endpointsLock) {
			return endpoints;
		}
	}
}
