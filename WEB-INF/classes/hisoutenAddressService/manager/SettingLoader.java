package hisoutenAddressService.manager;

import hisoutenAddressService.model.ServerSetting;
import hisoutenAddressService.model.ServerSideSetting;

import java.util.Collection;
import java.util.LinkedList;

import javax.servlet.ServletContext;

/**
 * 
 * @author bngper
 */
public class SettingLoader {

	private ServletContext servletContext;

	public SettingLoader(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	public ServerSideSetting loadServerSideSetting() {
		String adminPassword = new String(servletContext.getInitParameter("AdminKeyword"));
		String adminId = new String(servletContext.getInitParameter("AdminId"));
		if (adminId.isEmpty()) {
			adminId = " 管理者 ";
		}
		boolean enableRemoteAdmin = Boolean.parseBoolean(servletContext.getInitParameter("EnebleRemoteAdmin"));
		int subNetmask = Integer.parseInt(servletContext.getInitParameter("SubNetmask"));

		String enterMaxMinutesValue = servletContext.getInitParameter("EnterMaxMinutes");
		int enterMaxMinutes;
		int enterMaxSeconds = 0;
		if (!enterMaxMinutesValue.contains(".")) {
			enterMaxMinutes = Integer.parseInt(servletContext.getInitParameter("EnterMaxMinutes"));
			if (enterMaxMinutes <= 0) {
				enterMaxMinutes = 1;
			}
		} else {
			enterMaxMinutes = Integer.parseInt(enterMaxMinutesValue.split("\\.")[0]);
			enterMaxSeconds = 60 * Integer.parseInt(enterMaxMinutesValue.split("\\.")[1]) / 10;
		}

		int hostMaxCount = Integer.parseInt(servletContext.getInitParameter("ListMaxCount"));
		int hostMaxMinutes = Integer.parseInt(servletContext.getInitParameter("ListMaxMinutes"));

		int chatMaxCount = Integer.parseInt(servletContext.getInitParameter("ChatMaxCount"));
		int chatMaxMinutes = Integer.parseInt(servletContext.getInitParameter("ChatMaxMinutes"));

		int tournamentMaxCount = Integer.parseInt(servletContext.getInitParameter("TournamentMaxCount"));

		int autoMatchingMaxMinutes = Integer.parseInt(servletContext.getInitParameter("AutoMatchingMaxMinutes"));
		int autoMatchingHisotyMaxMinutes = Integer.parseInt(servletContext.getInitParameter("AutoMatchingHistoryMaxMinutes"));

		return new ServerSideSetting(adminPassword, adminId, enableRemoteAdmin, subNetmask, enterMaxMinutes, enterMaxSeconds, hostMaxCount, hostMaxMinutes, chatMaxCount, chatMaxMinutes,
				tournamentMaxCount, autoMatchingMaxMinutes, autoMatchingHisotyMaxMinutes);
	}

	public ServerSetting loadServerSetting() {
		int rankMaxLength = Integer.parseInt(servletContext.getInitParameter("RankMaxLength"));
		int commentMaxLength = Integer.parseInt(servletContext.getInitParameter("CommentMaxLength"));
		boolean enableChat = Boolean.parseBoolean(servletContext.getInitParameter("EnableChat"));
		boolean enableChatPrefix = Boolean.parseBoolean(servletContext.getInitParameter("EnableChatPrefix"));
		int chatMaxLength = Integer.parseInt(servletContext.getInitParameter("ChatMaxLength"));

		boolean enableTournament = Boolean.parseBoolean(servletContext.getInitParameter("EnableTournament"));
		int tournamentMaxMinutes = Integer.parseInt(servletContext.getInitParameter("TournamentMaxMinutes"));
		int tournamentMaxUsers = Integer.parseInt(servletContext.getInitParameter("TournamentMaxUsers"));

		String th105WindowCaption = servletContext.getInitParameter("HisoutenWindowCaption");
		String th105ClassName = servletContext.getInitParameter("HisoutenClassName");
		String th105SceneIdAddress = servletContext.getInitParameter("HisoutenSceneIdAddress");
		String th105FightingScenes = servletContext.getInitParameter("HisoutenFightingScenes");

		boolean enableAutoMatching = Boolean.parseBoolean(servletContext.getInitParameter("EnableAutoMatching"));

		String gameInformationsText = servletContext.getInitParameter("GameInformations");
		String[] gameInformations = gameInformationsText.split("\\;");
		Collection<String> gameInformationList = new LinkedList<String>();
		for (String gameInformation : gameInformations) {
			String text = gameInformation.replaceAll("\\r|\\n", "");
			gameInformationList.add(text.trim());
		}

		return new ServerSetting(rankMaxLength, commentMaxLength, enableChat, enableChatPrefix, chatMaxLength, enableTournament, tournamentMaxMinutes, tournamentMaxUsers,
				th105WindowCaption, th105ClassName, th105SceneIdAddress, th105FightingScenes, gameInformationList.toArray(new String[] {}), enableAutoMatching);
	}
}
