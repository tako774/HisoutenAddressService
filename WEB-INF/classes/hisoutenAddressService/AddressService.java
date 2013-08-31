package hisoutenAddressService;

import hisoutenAddressService.manager.*;
import hisoutenAddressService.model.*;
import hisoutenAddressService.model.tenco.MatchingResult;
import hisoutenAddressService.model.tenco.TencoUser;
import hisoutenAddressService.model.tournament.Tournament;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;
import javax.jws.Oneway;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;

/**
 * 
 * @author bngper
 * 
 */
@WebService(serviceName = "AddressService")
public class AddressService implements AdminService, UserService, HostService, ChatService, TournamentService, MatchingService, DataService {

	private static final String SERVER_VERSION = "9.2.0.0";
	@Resource
	private WebServiceContext _context;
	private ServerSetting _serverSetting;
	private ServerSideSetting _serverSideSetting;
	private IdManager _idManager;
	private UserManager _userManager;
	private AnnounceManager _announceManager;
	private HostManager _hostManager;
	private ChatManager _chatManager;
	private MatchingManager _matchingManager;
	private TournamentManager _tournamentManager;

	/**
	 * 初期化されたかどうか
	 */
	private static boolean Initialized = false;

	/**
	 * 初期化（コンテキストパラメータ取得）
	 */
	private synchronized void initialize() {

		if (Initialized) {
			return;
		}

		MessageContext messageContext = _context.getMessageContext();
		ServletContext servletContext = (ServletContext) messageContext.get(MessageContext.SERVLET_CONTEXT);

		SettingLoader settingLoader = new SettingLoader(servletContext);

		_serverSetting = settingLoader.loadServerSetting();
		_serverSideSetting = settingLoader.loadServerSideSetting();

		_idManager = new IdManager(messageContext);
		_userManager = new UserManager(_serverSideSetting.EnterMaxMinutes, _serverSideSetting.EnterMaxSeconds);
		_announceManager = new AnnounceManager();
		_hostManager = new HostManager(_serverSideSetting.HostMaxCount, _serverSideSetting.HostMaxMinutes);
		_chatManager = new ChatManager(_serverSideSetting.ChatMaxCount, _serverSideSetting.ChatMaxMinutes);
		_matchingManager = new MatchingManager(_serverSideSetting.AutoMatchingMaxMinutes, _serverSideSetting.AutoMatchingHisotyMaxMinutes);
		_tournamentManager = new TournamentManager(_serverSideSetting.TournamentMaxCount, _serverSetting.getTournamentMaxMinutes(), _serverSetting.getTournamentMaxUsers());

		Initialized = true;
	}

	/**
	 * サーバーバージョン取得
	 * 
	 * @return バージョン
	 */
	@WebMethod(operationName = "getServerVersion")
	public String getServerVersion() {
		initialize();
		return SERVER_VERSION;
	}

	/**
	 * サーバー設定取得
	 * 
	 * @return サーバー設定
	 */
	@WebMethod(operationName = "getServerSetting")
	public ServerSetting getServerSetting() {
		initialize();
		return _serverSetting;
	}

	/**
	 * サーバー設定取得（ver7.3からはgetServerSettingを推奨）
	 * 
	 * @return サーバー設定
	 */
	@Deprecated
	@WebMethod(operationName = "getServerSettings")
	public List<String> getServerSettings() {
		initialize();
		return _serverSetting.toStringList();
	}

	/**
	 * ホスト一覧クリア
	 * 
	 * @param keyword 管理者キーワード
	 */
	@WebMethod(operationName = "clearHosts")
	@Oneway
	@Override
	public void clearHosts(@WebParam(name = "keyword") String keyword) {
		initialize();
		if (isEmptyString(keyword) || !keyword.equals(_serverSideSetting.AdminPassword)) {
			return;
		}
		if (!_serverSideSetting.isEnableRemoteAdmin() && !isInLocalNetwork()) {
			return;
		}
		_hostManager.clear();
	}

	/**
	 * 大会のクリア
	 * 
	 * @param keyword 管理者キーワード
	 */
	@WebMethod(operationName = "clearTournaments")
	@Override
	public void clearTournaments(@WebParam(name = "keyword") String keyword) {
		initialize();
		if (isEmptyString(keyword) || !keyword.equals(_serverSideSetting.AdminPassword)) {
			return;
		}
		if (!_serverSideSetting.isEnableRemoteAdmin() && !isInLocalNetwork()) {
			return;
		}
		_tournamentManager.clear();
	}

	/**
	 * チャットクリア
	 * 
	 * @param keyword 管理者キーワード
	 */
	@WebMethod(operationName = "clearChat")
	@Oneway
	@Override
	public void clearChat(@WebParam(name = "keyword") String keyword) {
		initialize();
		if (isEmptyString(keyword) || !keyword.equals(_serverSideSetting.AdminPassword)) {
			return;
		}
		if (!_serverSideSetting.isEnableRemoteAdmin() && !isInLocalNetwork()) {
			return;
		}
		_chatManager.clear();
	}

	/**
	 * アナウンスをセットする
	 * 
	 * @param keyword 管理者キーワード
	 * @param announces アナウンス
	 */
	@WebMethod(operationName = "setAnnounces")
	@Oneway
	@Override
	public void setAnnounces(@WebParam(name = "keyword") String keyword, @WebParam(name = "announces") List<String> announces) {
		initialize();
		if (isEmptyString(keyword) || !keyword.equals(_serverSideSetting.AdminPassword)) {
			return;
		}
		if (!_serverSideSetting.isEnableRemoteAdmin() && !isInLocalNetwork()) {
			return;
		}
		_announceManager.setAnnounces(announces);
	}

	/**
	 * アナウンスを追加する
	 * 
	 * @param keyword 管理者キーワード
	 * @param announce アナウンス内容
	 */
	@WebMethod(operationName = "addAnnounce")
	@Oneway
	@Override
	@Deprecated
	public void addAnnounce(@WebParam(name = "keyword") String keyword, @WebParam(name = "contents") String contents) {
		initialize();
		if (isEmptyString(keyword) || !keyword.equals(_serverSideSetting.AdminPassword)) {
			return;
		}
		if (!_serverSideSetting.isEnableRemoteAdmin() && !isInLocalNetwork()) {
			return;
		}
		if (isEmptyString(contents)) {
			return;
		}
		_announceManager.addAnnounce(contents);
	}

	/**
	 * アナウンスを削除する
	 * 
	 * @param keyword 管理者キーワード
	 * @param number アナウンス番号(１～)
	 */
	@WebMethod(operationName = "removeAnnounce")
	@Oneway
	@Override
	@Deprecated
	public void removeAnnounce(@WebParam(name = "keyword") String keyword, @WebParam(name = "number") int number) {
		initialize();
		if (isEmptyString(keyword) || !keyword.equals(_serverSideSetting.AdminPassword)) {
			return;
		}
		if (!_serverSideSetting.isEnableRemoteAdmin() && !isInLocalNetwork()) {
			return;
		}
		_announceManager.removeAnnounce(number - 1);
	}

	/**
	 * アナウンスをクリアする
	 * 
	 * @param keyword 管理者キーワード
	 */
	@WebMethod(operationName = "clearAnnounce")
	@Oneway
	@Override
	@Deprecated
	public void clearAnnounce(@WebParam(name = "keyword") String keyword) {
		initialize();
		if (isEmptyString(keyword) || !keyword.equals(_serverSideSetting.AdminPassword)) {
			return;
		}
		if (!_serverSideSetting.isEnableRemoteAdmin() && !isInLocalNetwork()) {
			return;
		}
		_announceManager.clear();
	}

	/**
	 * 強制ホスト登録
	 * 
	 * @param keyword 管理者キーワード
	 * @param ip IP
	 * @param port ポート
	 * @param rank ランク
	 * @param comment コメント
	 */
	@WebMethod
	@Oneway
	@Override
	public void forceRegisterHost(@WebParam(name = "keyword") String keyword, @WebParam(name = "ip") String ip, @WebParam(name = "port") int port, @WebParam(name = "rank") String rank,
			@WebParam(name = "comment") String comment) {
		initialize();
		if (isEmptyString(keyword) || !keyword.equals(_serverSideSetting.AdminPassword)) {
			return;
		}
		if (!_serverSideSetting.isEnableRemoteAdmin() && !isInLocalNetwork()) {
			return;
		}
		_hostManager.addHost(createId(), ip, port, rank, comment);
	}

	/**
	 * 強制ホスト登録
	 * 
	 * @param keyword 管理者キーワード
	 * @param host ホスト
	 */
	@WebMethod(operationName = "forceRegisterHostEx")
	@Oneway
	@Override
	public void forceRegisterHostEx(@WebParam(name = "keyword") String keyword, @WebParam(name = "host") Host host) {
		forceRegisterHost(keyword, host.Ip, host.Port, host.Rank, host.Comment);
	}

	/**
	 * 強制登録解除
	 * 
	 * @param keyword 管理者キーワード
	 * @param no No
	 * @param ip IP
	 */
	@WebMethod(operationName = "forceUnregisterHost")
	@Oneway
	@Override
	public void forceUnregisterHost(@WebParam(name = "keyword") String keyword, @WebParam(name = "no") int no, @WebParam(name = "ip") String ip) {
		initialize();
		if (isEmptyString(keyword) || !keyword.equals(_serverSideSetting.AdminPassword)) {
			return;
		}
		if (!_serverSideSetting.isEnableRemoteAdmin() && !isInLocalNetwork()) {
			return;
		}
		_hostManager.forceDeleteHost(no, ip);
	}

	/**
	 * 強制大会削除
	 * 
	 * @param keyword 管理者キーワード
	 * @param no 大会番号
	 */
	@WebMethod(operationName = "forceUnregisterTournament")
	@Oneway
	@Override
	public void forceUnregisterTournament(@WebParam(name = "keyword") String keyword, @WebParam(name = "no") int no) {
		initialize();
		if (isEmptyString(keyword) || !keyword.equals(_serverSideSetting.AdminPassword)) {
			return;
		}
		if (!_serverSideSetting.isEnableRemoteAdmin() && !isInLocalNetwork()) {
			return;
		}
		_tournamentManager.deleteTournament(no);
	}

	/**
	 * 強制対戦状態設定
	 * 
	 * @param keyword 管理者キーワード
	 * @param no No
	 * @param ip IP
	 * @param isFighting 対戦状態
	 */
	@WebMethod(operationName = "forceSetFighting")
	@Oneway
	@Override
	public void forceSetFighting(@WebParam(name = "keyword") String keyword, @WebParam(name = "no") int no, @WebParam(name = "ip") String ip,
			@WebParam(name = "isFighting") boolean isFighting) {
		initialize();
		if (isEmptyString(keyword) || !keyword.equals(_serverSideSetting.AdminPassword)) {
			return;
		}
		if (!_serverSideSetting.isEnableRemoteAdmin() && !isInLocalNetwork()) {
			return;
		}
		_hostManager.forceSetFighting(no, ip, isFighting);
	}

	/**
	 * 管理者発言をする
	 * 
	 * @param keyword 管理者キーワード
	 * @param contents 発言内容
	 */
	@WebMethod(operationName = "addAdminChat")
	@Oneway
	@Override
	public void addAdminChat(@WebParam(name = "keyword") String keyword, @WebParam(name = "contents") String contents) {
		initialize();
		if (!_serverSetting.isEnableChat()) {
			return;
		}
		if (isEmptyString(keyword) || !keyword.equals(_serverSideSetting.AdminPassword)) {
			return;
		}
		if (!_serverSideSetting.isEnableRemoteAdmin() && !isInLocalNetwork()) {
			return;
		}
		if (isEmptyString(contents)) {
			return;
		}
		String contentsString = new String(contents);
		if (_serverSetting.getChatMaxLength() < contents.length()) {
			contentsString = contents.substring(0, _serverSetting.getChatMaxLength());
		}
		_chatManager.AddChat(new Chat(new Id(_serverSideSetting.AdminId), contentsString));
	}

	/**
	 * IDからアドレスを取得
	 * 
	 * @param keyword 管理者キーワード
	 * @param id ID
	 * @return アドレス
	 */
	@WebMethod(operationName = "getAddressById")
	@Override
	public String getAddressById(@WebParam(name = "keyword") String keyword, @WebParam(name = "id") String id) {
		initialize();
		if (isEmptyString(keyword) || !keyword.equals(_serverSideSetting.AdminPassword)) {
			return "";
		}
		if (!_serverSideSetting.isEnableRemoteAdmin() && !isInLocalNetwork()) {
			return "";
		}
		return _idManager.decrypt(id);
	}

	/**
	 * リモート管理を許可するかどうかを設定
	 * 
	 * @param keyword 管理者キーワード
	 * @param enable 許可するかどうか
	 */
	@WebMethod(operationName = "setEnableRemoteAdmin")
	@Oneway
	@Override
	public void setEnableRemoteAdmin(@WebParam(name = "keyword") String keyword, @WebParam(name = "enable") boolean enable) {
		initialize();
		if (isEmptyString(keyword) || !keyword.equals(_serverSideSetting.AdminPassword)) {
			return;
		}
		if (!_serverSideSetting.isEnableRemoteAdmin() && !isInLocalNetwork()) {
			return;
		}
		_serverSideSetting.setEnableRemoteAdmin(enable);
	}

	/**
	 * 接続人数取得
	 * 
	 * @return 接続人数
	 */
	@WebMethod(operationName = "getUserCount")
	@Override
	public int getUserCount() {
		initialize();
		return _userManager.getUserCount();
	}

	/**
	 * 入室
	 */
	@Override
	public void enter() {
		_userManager.enter(createId());
	}

	/**
	 * 退室
	 */
	@WebMethod(operationName = "leave")
	@Oneway
	@Override
	public void leave() {
		initialize();
		_userManager.leave(createId());
	}

	/**
	 * ホスト情報登録
	 * 
	 * @param ip IP(書式: AAA.BBB.CCC.DDD)
	 * @param port ポート(0～65535)
	 * @param rank ランク
	 * @param comment コメント
	 */
	@WebMethod(operationName = "registerHostInformation")
	@Oneway
	public void registerHostInformation(@WebParam(name = "ip") String ip, @WebParam(name = "port") int port, @WebParam(name = "rank") String rank, @WebParam(name = "comment") String comment) {
		initialize();

		String ipString = null;
		if (ip == null || ip.equals("") || ip.equals("127.0.0.1")) {
			ipString = getRemoteIp();
		} else {
			ipString = ip;
		}

		if (!ipString.matches("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})")) {
			return;
		}

		if (port < 0 || 65535 < port) {
			return;
		}

		String rankString = new String(rank);
		if (rank == null) {
			rankString = "";
		}
		if (_serverSetting.getRankMaxLength() < rank.length()) {
			rankString = rank.substring(0, _serverSetting.getRankMaxLength());
		}

		String commentString = new String(comment);
		if (comment == null) {
			commentString = "";
		}
		if (_serverSetting.getCommentMaxLength() < comment.length()) {
			commentString = comment.substring(0, _serverSetting.getCommentMaxLength());
		}

		if (isEmptyString(rankString) && isEmptyString(commentString)) {
			return;
		}

		_hostManager.addHost(createId(), ipString, port, rankString, commentString);
	}

	/**
	 * ホスト登録
	 * 
	 * @param ip IP(書式: AAA.BBB.CCC.DDD)
	 * @param port ポート(0～65535)
	 * @param rank ランク
	 * @param comment コメント
	 * @return true:成功 / false:失敗
	 */
	@WebMethod(operationName = "registerHost")
	@Override
	public boolean registerHost(@WebParam(name = "ip") String ip, @WebParam(name = "port") int port, @WebParam(name = "rank") String rank, @WebParam(name = "comment") String comment) {
		initialize();

		String ipString = null;
		if (ip == null || ip.equals("") || ip.equals("127.0.0.1")) {
			ipString = getRemoteIp();
		} else {
			ipString = new String(ip);
		}

		if (!ipString.matches("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})")) {
			return false;
		}

		if (port < 0 || 65535 < port) {
			return false;
		}

		String rankString = new String(rank);
		if (rank == null) {
			rankString = "";
		}
		if (_serverSetting.getRankMaxLength() < rank.length()) {
			rankString = rank.substring(0, _serverSetting.getRankMaxLength());
		}

		String commentString = new String(comment);
		if (comment == null) {
			commentString = "";
		}
		if (_serverSetting.getCommentMaxLength() < comment.length()) {
			commentString = comment.substring(0, _serverSetting.getCommentMaxLength());
		}

		if (isEmptyString(rankString) && isEmptyString(commentString))
			return false;

		_hostManager.addHost(createId(), ipString, port, rankString, commentString);
		return true;
	}

	/**
	 * ホスト登録
	 * 
	 * @param host ホスト
	 * @return true:成功 / false:失敗
	 */
	@WebMethod(operationName = "registerHostEx")
	@Override
	public void registerHostEx(@WebParam(name = "host") Host host) {
		initialize();

		registerHost(host.Ip, host.Port, host.Rank, host.Comment);
	}

	/**
	 * ホスト登録解除
	 * 
	 * @param ip IP
	 */
	@WebMethod(operationName = "unregisterHost")
	@Oneway
	@Override
	public void unregisterHost(@WebParam(name = "ip") String ip) {
		initialize();

		String ipString = null;
		if (ip == null || ip.equals("") || ip.equals("127.0.0.1")) {
			ipString = getRemoteIp();
		} else {
			ipString = new String(ip);
		}
		if (!ipString.matches("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})")) {
			return;
		}

		_hostManager.deleteHost(createId(), ipString);
	}

	/**
	 * 対戦状態を設定する
	 * 
	 * @param ip IP
	 * @param isFighting 対戦状態
	 */
	@WebMethod(operationName = "setFighting")
	@Oneway
	@Override
	public void setFighting(@WebParam(name = "ip") String ip, @WebParam(name = "isFighting") boolean isFighting) {
		initialize();

		String ipString = null;
		if (ip == null || ip.equals("") || ip.equals("127.0.0.1")) {
			ipString = getRemoteIp();
		} else {
			ipString = new String(ip);
		}
		if (!ipString.matches("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})")) {
			return;
		}

		_hostManager.setFighting(createId(), ipString, isFighting);
	}

	/**
	 * 発言する
	 * 
	 * @param contents 内容
	 */
	@WebMethod(operationName = "addChat")
	@Oneway
	@Override
	public void addChat(@WebParam(name = "contents") String contents) {
		addChatEx(null, contents);
	}

	/**
	 * 発言する
	 * 
	 * @param name 名前
	 * @param contents 内容
	 */
	@WebMethod(operationName = "addChatEx")
	@Oneway
	@Override
	public void addChatEx(@WebParam(name = "name") String name, @WebParam(name = "contents") String contents) {
		initialize();
		if (!_serverSetting.isEnableChat()) {
			return;
		}
		if (isEmptyString(contents)) {
			return;
		}
		String contentsString = new String(contents);
		if (_serverSetting.getChatMaxLength() < contents.length()) {
			contentsString = contents.substring(0, _serverSetting.getChatMaxLength());
		}

		if (_serverSetting.isEnableChatPrefix()) {
			_chatManager.AddChat(new Chat(createId(), name, contentsString));
		} else {
			_chatManager.AddChat(new Chat(createId(), contentsString));
		}
	}

	/**
	 * 大会情報取得
	 * 
	 * @param no 大会番号
	 * @param entryName エントリー名
	 * @param lastDataTime 最終データ取得日時
	 * @param lastChatTime 最終チャット取得日時
	 * @return
	 */
	@WebMethod(operationName = "getTournamentData")
	@Override
	public Tournament getTournamentData(@WebParam(name = "no") int no, @WebParam(name = "entryName") String entryName, @WebParam(name = "lastDataTime") Calendar lastDataTime,
			@WebParam(name = "lastChatTime") Calendar lastChatTime) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return null;
		}
		if (entryName == null) {
			entryName = "";
		}
		return _tournamentManager.getTournamentData(no, createId(), entryName, lastDataTime, lastChatTime);
	}

	/**
	 * 大会登録
	 * 
	 * @param userCoount 人数
	 * @param type 種別(トーナメント・総当たり)
	 * @param rank ランク
	 * @param comment コメント
	 */
	@WebMethod(operationName = "registerTournament")
	@Oneway
	@Override
	public void registerTournament(@WebParam(name = "userCount") int userCount, @WebParam(name = "type") int type, @WebParam(name = "rank") String rank,
			@WebParam(name = "comment") String comment) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.addTournament(createId(), userCount, type, rank, comment);
	}

	/**
	 * 大会削除
	 * 
	 * @param no トーナメントNo
	 */
	@WebMethod(operationName = "unregisterTournament")
	@Oneway
	@Override
	public void unregisterTournament() {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.deleteTournament(createId());
	}

	/**
	 * 運営追加
	 * 
	 * @param no 大会番号
	 * @param id 追加するID
	 */
	@WebMethod(operationName = "addTournamentManager")
	@Oneway
	@Override
	public void addTournamentManager(@WebParam(name = "no") int no, @WebParam(name = "id") String id) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.addManager(no, createId(), new Id(id));
	}

	/**
	 * 運営追放
	 * 
	 * @param no 大会番号
	 * @param id 追放するID
	 */
	@WebMethod(operationName = "removeTournamentManager")
	@Oneway
	@Override
	public void removeTournamentManager(@WebParam(name = "no") int no, @WebParam(name = "id") String id) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.removeManager(no, createId(), new Id(id));
	}

	/**
	 * ダミーの参加者を追加
	 * 
	 * @param no 大会番号
	 */
	@WebMethod(operationName = "addTournamentDummyPlayer")
	@Oneway
	@Override
	public void addTournamentDummyPlayer(@WebParam(name = "no") int no) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.addDummyPlayer(no, createId());
	}

	/**
	 * 参加を取り消させる
	 * 
	 * @param no 大会番号
	 * @param id 取り消させる参加者のID
	 */
	@WebMethod(operationName = "cancelTournamentEntryByManager")
	@Oneway
	@Override
	public void cancelTournamentEntryByManager(@WebParam(name = "no") int no, @WebParam(name = "id") String id) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.cancelEntryByManager(no, createId(), new Id(id));
	}

	/**
	 * 観戦追加
	 * 
	 * @param no 大会番号
	 * @param id 追加するID
	 */
	@WebMethod(operationName = "addTournamentSpectator")
	@Oneway
	@Override
	public void addTournamentSpectator(@WebParam(name = "no") int no, @WebParam(name = "id") String id) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.addSpectator(no, createId(), new Id(id));
	}

	/**
	 * 観戦追放
	 * 
	 * @param no 大会番号
	 * @param id 追放するID
	 */
	@WebMethod(operationName = "removeTournamentSpectator")
	@Oneway
	@Override
	public void removeTournamentSpectator(@WebParam(name = "no") int no, @WebParam(name = "id") String id) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.removeSpectator(no, createId(), new Id(id));
	}

	/**
	 * アナウンスの設定
	 * 
	 * @param no 大会番号
	 * @param announces アナウンス
	 */
	@WebMethod(operationName = "setTournamentAnnounces")
	@Oneway
	@Override
	public void setTournamentAnnounces(@WebParam(name = "no") int no, @WebParam(name = "announces") List<String> announces) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.setAnnounces(no, createId(), announces);
	}

	/**
	 * 人数再設定
	 * 
	 * @param no 大会番号
	 * @param count 人数
	 */
	@WebMethod(operationName = "setTournamentUserCount")
	@Oneway
	@Override
	public void setTournamentUserCount(@WebParam(name = "no") int no, @WebParam(name = "userCount") int userCount) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.setUserCount(no, createId(), userCount);
	}

	/**
	 * トーナメント開始
	 * 
	 * @param no 大会番号
	 * @param minCount 開始できる最少人数
	 * @param maxCount 開始できる最大人数
	 * @param shuffle 開始時に順番をシャッフルするかどうか
	 */
	@WebMethod(operationName = "startTournament")
	@Oneway
	@Override
	public void startTournament(@WebParam(name = "no") int no, @WebParam(name = "minCount") int minCount, @WebParam(name = "maxCount") int maxCount,
			@WebParam(name = "shuffle") boolean shuffle) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.start(no, minCount, maxCount, createId(), shuffle);
	}

	/**
	 * 参加を取り消させる
	 * 
	 * @param no 大会番号
	 * @param playerId 取り消させるプレイヤー
	 */
	@WebMethod(operationName = "retireTournamentByManager")
	@Oneway
	@Override
	public void retireTournamentByManager(@WebParam(name = "no") int no, @WebParam(name = "id") String playerId) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.retireByManager(no, createId(), new Id(playerId));
	}

	/**
	 * キック
	 * 
	 * @param no 大会番号
	 * @param id キックするID
	 */
	@WebMethod(operationName = "kickTournamentUser")
	@Oneway
	@Override
	public void kickTournamentUser(@WebParam(name = "no") int no, @WebParam(name = "targetId") String targetId) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.kick(no, createId(), new Id(targetId));
	}

	/**
	 * 結果設定(運営用)
	 * 
	 * @param no 大会番号
	 * @param id 報告対象のID
	 * @param results 結果
	 */
	@WebMethod(operationName = "setTournamentResultsByManager")
	@Oneway
	@Override
	public void setTournamentResultsByManager(@WebParam(name = "no") int no, @WebParam(name = "id") String id, @WebParam(name = "results") List<Integer> results) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.setResultsByManager(no, createId(), new Id(id), results);
	}

	/**
	 * エントリー
	 * 
	 * @param no 大会番号
	 * @param entryName エントリー名
	 * @param ip IP
	 * @param port ポート
	 * @return true:成功 / false:失敗
	 */
	@WebMethod(operationName = "entryTournament")
	@Override
	public Id entryTournament(@WebParam(name = "no") int no, @WebParam(name = "entryName") String entryName, @WebParam(name = "ip") String ip, @WebParam(name = "port") int port) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return null;
		}

		String ipString = null;
		if (ip == null || ip.equals("") || ip.equals("127.0.0.1")) {
			ipString = getRemoteIp();
		} else {
			ipString = new String(ip);
		}
		if (!ipString.matches("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})")) {
			return null;
		}
		if (port < 0 || 65535 < port) {
			return null;
		}
		return _tournamentManager.entry(no, createId(), entryName, ipString, port);
	}

	/**
	 * 観戦で入る
	 * 
	 * @param no 大会番号
	 */
	@WebMethod(operationName = "guestEntryTournament")
	@Override
	public Id guestEntryTournament(@WebParam(name = "no") int no, @WebParam(name = "entryName") String entryName) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return null;
		}

		String nameString = "";
		if (!isEmptyString(entryName)) {
			nameString = entryName;
		}
		return _tournamentManager.guestEntry(no, createId(), nameString);
	}

	/**
	 * エントリー取り消し
	 * 
	 * @param no 大会番号
	 */
	@WebMethod(operationName = "cancelTournamentEntry")
	@Oneway
	@Override
	public void cancelTournamentEntry(@WebParam(name = "no") int no) {
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		initialize();
		_tournamentManager.cancelEntry(no, createId());
	}

	/**
	 * リタイアする
	 * 
	 * @param no 大会番号
	 */
	@WebMethod(operationName = "retireTournament")
	@Oneway
	@Override
	public void retireTournament(@WebParam(name = "no") int no) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.retire(no, createId());
	}

	/**
	 * 待機中かどうか設定する
	 * 
	 * @param no 大会番号
	 * @param waiting 待機中かどうか
	 */
	@WebMethod(operationName = "setTournamentPlayerWaiting")
	@Oneway
	@Override
	public void setTournamentPlayerWaiting(@WebParam(name = "no") int no, @WebParam(name = "waiting") boolean waiting) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.setWaiting(no, createId(), waiting);
	}

	/**
	 * 対戦中かどうか設定する
	 * 
	 * @param no 大会番号
	 * @param fighting 対戦中かどうか
	 */
	@WebMethod(operationName = "setTournamentPlayerFighting")
	@Oneway
	@Override
	public void setTournamentPlayerFighting(@WebParam(name = "no") int no, @WebParam(name = "fighting") boolean fighting) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.setFighting(no, createId(), fighting);
	}

	/**
	 * 結果報告
	 * 
	 * @param no 大会番号
	 * @param results 結果
	 */
	@WebMethod(operationName = "setTournamentResults")
	@Oneway
	@Override
	public void setTournamentResults(@WebParam(name = "no") int no, @WebParam(name = "results") List<Integer> results) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}
		_tournamentManager.setResults(no, createId(), results);
	}

	/**
	 * 大会チャット
	 * 
	 * @param no 大会番号
	 * @param name 名前
	 * @param contents 内容
	 */
	@WebMethod(operationName = "addTournamentChat")
	@Oneway
	@Override
	public void addTournamentChat(@WebParam(name = "no") int no, @WebParam(name = "name") String name, @WebParam(name = "contents") String contents) {
		initialize();
		if (!_serverSetting.isEnableTournament()) {
			return;
		}

		if (isEmptyString(contents)) {
			return;
		}
		String nameString = "";
		if (!isEmptyString(name)) {
			nameString = name;
		}
		_tournamentManager.addChat(no, createId(), nameString, contents);
	}

	/**
	 * 自動マッチ登録
	 * 
	 * @param user 自分の情報
	 */
	@WebMethod(operationName = "registerMatching")
	@Oneway
	@Override
	public void registerMatching(@WebParam(name = "user") TencoUser user) {
		initialize();
		if (!_serverSetting.isEnableAutoMatching()) {
			return;
		}

		if (user.AccountName == null || user.AccountName.equals("")) {
			return;
		}

		String ip = null;
		if (user.Ip == null || user.Ip.trim().equals("")) {
			ip = getRemoteIp();
		} else {
			ip = user.Ip;
		}
		if (!ip.matches("(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})\\.(\\d{1,3})")) {
			return;
		}

		if (user.Port < 0 || 65535 < user.Port) {
			return;
		}

		String comment = "";
		if (user.Comment != null)
			comment = user.Comment;

		TencoUser u = new TencoUser(createId(), user.AccountName, user.IsHostable, user.IsRoomOnly, ip, user.Port, comment, user.Rating, user.MatchingSpan);

		_matchingManager.register(u);
	}

	/**
	 * 自動マッチ登録解除
	 */
	@WebMethod(operationName = "unregisterMatching")
	@Oneway
	@Override
	public void unregisterMatching() {
		initialize();
		if (!_serverSetting.isEnableAutoMatching()) {
			return;
		}

		_matchingManager.unregister(createId());
	}

	/**
	 * 対戦履歴追加
	 * 
	 * @param oponent 対戦相手
	 */
	@WebMethod(operationName = "addMatchingHistory")
	@Oneway
	@Override
	public void addMatchingHistory(@WebParam(name = "oponent") Id oponent) {
		initialize();
		if (!_serverSetting.isEnableAutoMatching()) {
			return;
		}

		_matchingManager.addHistory(createId(), oponent);
	}

	/**
	 * 自動マッチング準備OK状態にする
	 * 
	 * @param oponent 相手のID
	 */
	@WebMethod(operationName = "setPrepared")
	@Oneway
	@Override
	public void setPrepared(@WebParam(name = "oponent") Id oponent) {
		initialize();
		if (!_serverSetting.isEnableAutoMatching()) {
			return;
		}

		_matchingManager.setPrepared(createId(), oponent);
	}

	/**
	 * マッチングをスキップ
	 * 
	 * @param oponent 相手のID
	 */
	@WebMethod(operationName = "skipMatching")
	@Oneway
	@Override
	public void skipMatching(@WebParam(name = "oponent") Id oponent) {
		initialize();
		if (!_serverSetting.isEnableAutoMatching()) {
			return;
		}

		_matchingManager.skip(createId(), oponent);
	}

	/**
	 * マッチング結果を取得
	 */
	@WebMethod(operationName = "getMatchingResult")
	@Override
	public MatchingResult getMatchingResult() {
		initialize();
		enter();
		if (!_serverSetting.isEnableAutoMatching()) {
			return null;
		}
		
		return _matchingManager.getMatchingResult(createId());
	}

	/**
	 * データ取得
	 * 
	 * @param getHost ホスト情報を取得するかどうか
	 * @param lastHostTime 最後にホスト情報を取得した日時
	 * @param getChat チャット情報を取得するかどうか
	 * @param lastChatTime 最後にチャット情報を取得した日時
	 * @param getTournament 大会情報を取得するかどうか
	 * @param lastTournamentTime 最後に大会情報を取得した日時
	 */
	@WebMethod(operationName = "getAllDataEx")
	@Override
	public AllData getAllDataEx(@WebParam(name = "getHost") boolean getHost, @WebParam(name = "lastHostTime") Calendar lastHostTime, @WebParam(name = "getChat") boolean getChat,
			@WebParam(name = "lastChatTime") Calendar lastChatTime, @WebParam(name = "getTournament") boolean getTournament,
			@WebParam(name = "lastTournamentTime") Calendar lastTournamentTime) {
		initialize();
		enter();

		return new AllData(getUserCount(), _announceManager.getAnnounces(), _hostManager.getHosts(lastHostTime), getHost, _chatManager.getChats(lastChatTime), getChat, _tournamentManager
				.getTournamentInformations(lastTournamentTime), getTournament);
	}

	/**
	 * 接続人数・アナウンス・ホスト一覧・チャット取得
	 * 
	 * @param getHost ホスト一覧を取得するかどうか
	 * @param getChat チャットを取得するかどうか
	 * @param lastChatTime 最後に取得したChatの日時
	 * @return 接続人数・アナウンス・一覧・チャット
	 */
	@WebMethod(operationName = "getAllData")
	@Override
	public AllData getAllData(@WebParam(name = "getHost") boolean getHost, @WebParam(name = "getChat") boolean getChat, @WebParam(name = "lastHostTime") Calendar lastHostTime,
			@WebParam(name = "lastChatTime") Calendar lastChatTime) {

		initialize();
		enter();

		return new AllData(getUserCount(), _announceManager.getAnnounces(), _hostManager.getHosts(lastHostTime), getHost, _chatManager.getChatsOld(lastChatTime), getChat, null, false);
	}

	/**
	 * 空の文字列かどうか
	 * 
	 * @param value 文字列
	 * @return true:空 / false:空でない
	 */
	private boolean isEmptyString(String value) {
		return value == null || value.isEmpty() || value.trim().isEmpty() || value.replace("　", "").replace(" ", "").trim().isEmpty();
	}

	/**
	 * たぶんローカルネットワーク内
	 * 
	 * @return true:ローカル内 / false:違う
	 */
	private boolean isInLocalNetwork() {
		try {
			String remoteIp = getRemoteIp();
			if (remoteIp.equals("127.0.0.1")) {
				return true;
			}

			String localIp = Inet4Address.getLocalHost().getHostAddress();

			String[] remoteIpList = remoteIp.split("\\.");
			String[] localIpList = localIp.split("\\.");

			StringBuffer remoteIpString = new StringBuffer();
			for (int i = 0; i < remoteIpList.length; i++) {
				remoteIpString.append(toBinaryDigit(Integer.parseInt(remoteIpList[i])));
			}

			StringBuffer localIpString = new StringBuffer();
			for (int i = 0; i < localIpList.length; i++) {
				localIpString.append(toBinaryDigit(Integer.parseInt(localIpList[i])));
			}

			boolean isLocal = true;
			for (int i = 0; i < _serverSideSetting.SubNetmask; i++) {
				if (localIpString.charAt(i) != remoteIpString.charAt(i)) {
					isLocal = false;
					break;
				}
			}

			return isLocal;
		} catch (UnknownHostException e) {
			return false;
		}

	}

	// 10進数を2進数表記文字列へ変換するメソッド
	private String toBinaryDigit(int value) {
		StringBuffer buf = new StringBuffer();
		for (int i = 31; i >= 0; i--) {
			buf.append((value & (int) Math.pow(2, i)) >>> i);
		}
		return buf.toString();
	}

	private synchronized String getRemoteIp() {
		MessageContext messageContext = _context.getMessageContext();
		HttpServletRequest request = (HttpServletRequest) messageContext.get(MessageContext.SERVLET_REQUEST);
		return request.getRemoteAddr();
	}

	private Id createId() {
		return _idManager.create(getRemoteIp());
	}
}
