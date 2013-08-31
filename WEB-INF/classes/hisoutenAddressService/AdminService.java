package hisoutenAddressService;

import hisoutenAddressService.model.Host;
import java.util.List;

/**
 * 
 * @author bngper
 */
public interface AdminService {

	void clearHosts(String keyword);

	void clearTournaments(String keyword);

	void clearChat(String keyword);

	void setAnnounces(String keyword, List<String> announces);

	void addAnnounce(String keyword, String contents);

	void removeAnnounce(String keyword, int number);

	void clearAnnounce(String keyword);

	void forceRegisterHost(String keyword, String ip, int port, String rank, String comment);

	void forceRegisterHostEx(String keyword, Host host);

	void forceUnregisterHost(String keyword, int no, String ip);

	void forceUnregisterTournament(String keyword, int no);

	void forceSetFighting(String keyword, int no, String ip, boolean isFighting);

	void addAdminChat(String keyword, String contents);

	String getAddressById(String keyword, String id);

	void setEnableRemoteAdmin(String keyword, boolean enable);
}
