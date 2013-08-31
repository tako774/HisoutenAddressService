package hisoutenAddressService;

import hisoutenAddressService.model.Host;

/**
 * 
 * @author bngper
 */
public interface HostService {

	void registerHostInformation(String ip, int port, String rank, String comment);

	boolean registerHost(String ip, int port, String rank, String comment);

	void registerHostEx(Host host);

	void unregisterHost(String ip);

	void setFighting(String ip, boolean isFighting);
}
