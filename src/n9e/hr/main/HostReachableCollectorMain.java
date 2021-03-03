package n9e.hr.main;

import n9e.hr.context.Context;
import n9e.hr.detec.ReachableDetec;

public class HostReachableCollectorMain {
	public static void main(String[] args) {
		Context.initCfg();

		Context.syncEndpoints();

		Context.loopSyncEndpoints();
		
		ReachableDetec.loopDetecReachableAndReport();
	}
}
