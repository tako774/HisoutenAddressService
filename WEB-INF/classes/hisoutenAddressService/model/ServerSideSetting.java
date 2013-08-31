package hisoutenAddressService.model;

/**
 * 
 * @author bngper
 */
public class ServerSideSetting {

	public final String AdminPassword;
	public final String AdminId;
	private boolean _enableRemoteAdmin;
	public final int SubNetmask;
	public final int EnterMaxMinutes;
	public final int EnterMaxSeconds;
	public final int HostMaxCount;
	public final int HostMaxMinutes;
	public final int ChatMaxCount;
	public final int ChatMaxMinutes;
	public final int TournamentMaxCount;
	public final int AutoMatchingMaxMinutes;
	public final int AutoMatchingHisotyMaxMinutes;

	public ServerSideSetting(String adminPassword, String adminId, boolean enableRemoteAdmin, int subNetmask, int enterMaxMinutes, int enterMaxSeconds, int hostMaxCount, int hostMaxMinutes,
			int chatMaxCount, int chatMaxMinutes, int tournamentMaxCount, int autoMatchingMaxMinutes, int autoMatchingHisotyMaxMinutes) {
		AdminPassword = adminPassword;
		AdminId = adminId;
		_enableRemoteAdmin = enableRemoteAdmin;
		SubNetmask = subNetmask;
		EnterMaxMinutes = enterMaxMinutes;
		EnterMaxSeconds = enterMaxSeconds;
		HostMaxCount = hostMaxCount;
		HostMaxMinutes = hostMaxMinutes;
		ChatMaxCount = chatMaxCount;
		ChatMaxMinutes = chatMaxMinutes;
		TournamentMaxCount = tournamentMaxCount;
		AutoMatchingMaxMinutes = autoMatchingMaxMinutes;
		AutoMatchingHisotyMaxMinutes = autoMatchingHisotyMaxMinutes;
	}

	/**
	 * @return the enableRemoteAdmin
	 */
	public boolean isEnableRemoteAdmin() {
		return _enableRemoteAdmin;
	}

	/**
	 * @param enableRemoteAdmin the enableRemoteAdmin to set
	 */
	public void setEnableRemoteAdmin(boolean enableRemoteAdmin) {
		_enableRemoteAdmin = enableRemoteAdmin;
	}
}
