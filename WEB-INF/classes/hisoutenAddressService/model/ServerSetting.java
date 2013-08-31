package hisoutenAddressService.model;

import java.util.ArrayList;
import java.util.List;

/**
 * サーバー設定
 * 
 * @author bngper
 */
public class ServerSetting {

	private int _rankMaxLength = 20;
	private int _commentMaxLength = 100;
	private boolean _enableChat = true;
	private boolean _enableChatPrefix = false;
	private int _chatMaxLength = 100;
	private boolean _enableTournament = false;
	private int _tournamentMaxMinutes = 180;
	private int _tournamentMaxUsers = 16;
	private String _hisoutenWindowCaption = "東方緋想天 Ver1.06";
	private String _hisoutenClassName = "th105_106";
	private String _hisoutenSceneIdAddress = "0x006ECE78";
	private String _hisoutenFightingScenes = "8,9,10,11,13,14";
	private String[] _gameInformations = new String[] { "東方緋想天 Ver1.06, th105_106, 0x006ECE788, 9:10:11:13:14", "東方非想天則 ～ 超弩級ギニョルの謎を追え Ver1.03, th123_103, 0x00867BC8, 9:10:11:13:14" };
	private boolean _enableAutoMatching = false;

	@SuppressWarnings("unused")
	@Deprecated
	private ServerSetting() {
	}

	public ServerSetting(int rankMaxLength, int commentMaxLength, boolean enableChat, boolean enableChatPrefix, int chatMaxLength, boolean enableTournament, int tournamentMaxMinutes,
			int tournamentMaxUsers, String th105WindowCaption, String th105ClassName, String th105SceneIdAddress, String th105FightingScenes, String[] gameInformations,
			boolean enableAutoMatching) {
		_rankMaxLength = rankMaxLength;
		_commentMaxLength = commentMaxLength;

		_enableChat = enableChat;
		_enableChatPrefix = enableChatPrefix;
		_chatMaxLength = chatMaxLength;

		_enableTournament = enableTournament;
		_tournamentMaxMinutes = tournamentMaxMinutes;
		_tournamentMaxUsers = tournamentMaxUsers;

		_hisoutenWindowCaption = th105WindowCaption;
		_hisoutenClassName = th105ClassName;
		_hisoutenSceneIdAddress = th105SceneIdAddress;
		_hisoutenFightingScenes = th105FightingScenes;

		_enableAutoMatching = enableAutoMatching;

		_gameInformations = gameInformations;
	}

	public int getRankMaxLength() {
		return _rankMaxLength;
	}

	public void setRankMaxLength(int rankMaxLength) {
		_rankMaxLength = rankMaxLength;
	}

	public int getCommentMaxLength() {
		return _commentMaxLength;
	}

	public void setCommentMaxLength(int commentMaxLength) {
		_commentMaxLength = commentMaxLength;
	}

	public boolean isEnableChat() {
		return _enableChat;
	}

	public void setEnableChat(boolean enableChat) {
		_enableChat = enableChat;
	}

	public boolean isEnableChatPrefix() {
		return _enableChatPrefix;
	}

	public void setEnableChatPrefix(boolean enableChatPrefix) {
		_enableChatPrefix = enableChatPrefix;
	}

	public int getChatMaxLength() {
		return _chatMaxLength;
	}

	public void setChatMaxLength(int chatMaxLength) {
		_chatMaxLength = chatMaxLength;
	}

	public boolean isEnableTournament() {
		return _enableTournament;
	}

	public void setEnableTournament(boolean enableTournament) {
		_enableTournament = enableTournament;
	}

	public int getTournamentMaxMinutes() {
		return _tournamentMaxMinutes;
	}

	public void setTournamentMaxMinutes(int tournamentMaxMinutes) {
		_tournamentMaxMinutes = tournamentMaxMinutes;
	}

	public int getTournamentMaxUsers() {
		return _tournamentMaxUsers;
	}

	public void setTournamentMaxUsers(int tournamentMaxUsers) {
		_tournamentMaxUsers = tournamentMaxUsers;
	}

	public String getHisoutenWindowCaption() {
		return _hisoutenWindowCaption;
	}

	public void setHisoutenWindowCaption(String HisoutenWindowCaption) {
		_hisoutenWindowCaption = HisoutenWindowCaption;
	}

	public String getHisoutenClassName() {
		return _hisoutenClassName;
	}

	public void setHisoutenClassName(String hisoutenClassName) {
		_hisoutenClassName = hisoutenClassName;
	}

	public String getHisoutenSceneIdAddress() {
		return _hisoutenSceneIdAddress;
	}

	public void setHisoutenSceneIdAddress(String HisoutenSceneIdAddress) {
		_hisoutenSceneIdAddress = HisoutenSceneIdAddress;
	}

	public String getHisoutenFightingScenes() {
		return _hisoutenFightingScenes;
	}

	public void setHisoutenFightingScenes(String hisoutenFightingScenes) {
		_hisoutenFightingScenes = hisoutenFightingScenes;
	}

	public String[] getGameInformations() {
		return _gameInformations;
	}

	public void setGameInformations(String[] gameInformations) {
		_gameInformations = gameInformations;
	}

	public boolean isEnableAutoMatching() {
		return _enableAutoMatching;
	}

	public void setEnableAutoMatching(boolean enableAutoMatching) {
		_enableAutoMatching = enableAutoMatching;
	}

	@Deprecated
	public List<String> toStringList() {
		List<String> settings = new ArrayList<String>();
		settings.add("RANK_MAX_LENGTH" + ":" + Integer.toString(this.getRankMaxLength()));
		settings.add("COMMENT_MAX_LENGTH" + ":" + Integer.toString(this.getCommentMaxLength()));
		settings.add("ENABLE_CHAT" + ":" + Boolean.toString(this.isEnableChat()));
		settings.add("ENABLE_CHAT_PREFIX" + ":" + Boolean.toString(this.isEnableChatPrefix()));
		settings.add("CHAT_MAX_LENGTH" + ":" + Integer.toString(this.getChatMaxLength()));
		settings.add("HISOUTEN_WINDOW_CAPTION" + ":" + this.getHisoutenWindowCaption());
		settings.add("HISOUTEN_CLASS_NAME" + ":" + this.getHisoutenClassName());
		settings.add("HISOUTEN_SCENE_ID_ADDRESS" + ":" + this.getHisoutenSceneIdAddress());
		return settings;
	}
}
