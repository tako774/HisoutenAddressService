package hisoutenAddressService.model.tournament;

import hisoutenAddressService.model.Chat;
import hisoutenAddressService.model.Id;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * 
 * @author bngper
 */
public class Tournament {

	public final int No;
	public final Id RegisteredId;
	public final Calendar Time;
	public final int Type;
	public final String Rank;
	public final String Comment;
	public final List<String> Announces;
	public final List<Manager> Managers;
	public final List<Player> Players;
	public final List<Spectator> Spectators;
	public final List<Guest> Guests;
	public final List<Id> KickedIds;
	public final List<Chat> Chats;
	private int _userCount;
	private Calendar _lastDataUpdate;
	private Calendar _lastChatUpdate;
	private Calendar _lastAnnounceTime;
	private boolean _started = false;
	private boolean _deleted = false;
	private int _entryNo = 0;

	protected Tournament() {
		this(0, null, 0, 2, null, null);
	}

	public Tournament(int no, Id registeredId, int type, int userCount, String rank, String comment) {
		this(no, registeredId, new GregorianCalendar(), type, userCount, rank, comment);
	}

	protected Tournament(int no, Id registeredId, Calendar time, int type, int userCount, String rank, String comment) {
		No = no;
		RegisteredId = registeredId;
		Time = time;
		Type = type;
		Rank = rank;
		Comment = comment;
		Announces = new ArrayList<String>();
		Managers = new ArrayList<Manager>();
		Players = new ArrayList<Player>();
		Spectators = new ArrayList<Spectator>();
		Guests = new ArrayList<Guest>();
		KickedIds = new ArrayList<Id>();
		Chats = new ArrayList<Chat>();
		setUserCount(userCount);

		Calendar now = new GregorianCalendar();
		_lastDataUpdate = (Calendar) now.clone();
		_lastAnnounceTime = (Calendar) now.clone();
		_lastChatUpdate = (Calendar) now.clone();
	}

	public synchronized void addManager(Id operator, Id newManagerId) {
		if (!isManager(operator)) {
			return;
		}
		if (isManager(newManagerId)) {
			return;
		}
		if (isKickedId(operator) || isKickedId(newManagerId)) {
			return;
		}

		Manager newManager = new Manager(newManagerId);

		// player to manager
		Player activePlayer = null;
		Spectator spectator = null;
		Guest guest = null;

		do {
			activePlayer = findActivePlayer(newManagerId);
			if (activePlayer != null) {
				newManager.setEntryName(activePlayer.getEntryName());
				break;
			}

			spectator = findSpectator(newManagerId);
			if (spectator != null) {
				newManager.setEntryName(spectator.getEntryName());
				break;
			}

			guest = findGuest(newManagerId);
			if (guest != null) {
				newManager.setEntryName(guest.getEntryName());
				break;
			}
		} while (false);

		if (activePlayer != null || spectator != null || guest != null) {
			Managers.add(newManager);
			Spectators.remove(spectator);
			Guests.remove(guest);
			if (newManager.getEntryName().equals("")) {
				addSystemMessage("[" + newManagerId.getValue() + "] は運営に任命されました。");
			} else {
				addSystemMessage(newManager.getEntryName() + " は運営に任命されました。");
			}
			_lastDataUpdate = new GregorianCalendar();
		}
	}

	public synchronized void removeManager(Id operator, Id removeManagerId) {
		if (!isManager(operator)) {
			return;
		}
		if (isKickedId(operator) || isKickedId(removeManagerId)) {
			return;
		}
		if (RegisteredId.equals(removeManagerId)) {
			// can't remove register
			return;
		}

		Manager manager = findManager(removeManagerId);
		if (manager == null) {
			return;
		}

		// if only manager or retired player, to guest
		// if manager and player, new role is nothing
		Player player = findActivePlayer(removeManagerId);
		if (player == null || player.isRetired()) {
			Guests.add(new Guest(manager.Id, manager.getEntryName()));
		}
		Managers.remove(manager);
		if (manager.getEntryName().equals("")) {
			addSystemMessage("[" + manager.Id.getValue() + "] は運営から外されました。");
		} else {
			addSystemMessage(manager.getEntryName() + " は運営から外されました。");
		}
		_lastDataUpdate = new GregorianCalendar();
	}

	public synchronized void cancelEntryByManager(Id operator, Id playerId) {
		if (_started) {
			return;
		}
		if (!isManager(operator)) {
			return;
		}
		if (isKickedId(operator) || isKickedId(playerId)) {
			return;
		}

		Player player = findActivePlayer(playerId);
		if (player == null) {
			return;
		}

		Guests.add(new Guest(player.Id, player.getEntryName()));
		Players.remove(player);
		addSystemMessage(player.getEntryName() + " の参加が取り消されました。");
		_lastDataUpdate = new GregorianCalendar();
	}

	public synchronized void addDummyPlayer(Id operator) {
		if (_started) {
			return;
		}
		if (!isManager(operator)) {
			return;
		}
		if (isKickedId(operator)) {
			return;
		}
		if (_userCount <= Players.size()) {
			return;
		}

		String id_name = "XX" + Integer.toString(getNextDummyNo()) + "XX";
		Player dymmyPlayer = new Player(new Id(id_name), getNextEntryNo(), id_name, "127.0.0.1", 10800);
		Players.add(dymmyPlayer);
		_lastDataUpdate = new GregorianCalendar();
	}

	private int _dummyNo = 1000;

	private int getNextDummyNo() {
		_dummyNo += 1;
		if (9999 < _dummyNo) {
			_dummyNo = 1001;
		}
		return _dummyNo;
	}

	public synchronized void addSpectator(Id operator, Id spectatorId) {
		if (!isManager(operator)) {
			return;
		}
		if (isKickedId(operator) || isKickedId(spectatorId)) {
			return;
		}
		if (isActivePlayer(spectatorId) || isSpectator(spectatorId)) {
			return;
		}

		// only guest to spectator
		Guest guest = findGuest(spectatorId);
		if (guest == null) {
			return;
		}

		Spectators.add(new Spectator(guest.Id, guest.getEntryName()));
		Guests.remove(guest);
		if (guest.getEntryName().equals("")) {
			addSystemMessage("[" + guest.Id.getValue() + "] は観戦を許可されました。");
		} else {
			addSystemMessage(guest.getEntryName() + " は観戦を許可されました。");
		}
		_lastDataUpdate = new GregorianCalendar();
	}

	public synchronized void removeSpectator(Id operator, Id removeSpectatorId) {
		if (!isManager(operator)) {
			return;
		}
		if (isKickedId(operator) || isKickedId(removeSpectatorId)) {
			return;
		}

		Spectator spectator = findSpectator(removeSpectatorId);
		if (spectator == null) {
			return;
		}

		Guests.add(new Guest(spectator.Id, spectator.getEntryName()));
		Spectators.remove(spectator);
		if (spectator.getEntryName().equals("")) {
			addSystemMessage("[" + spectator.Id.getValue() + "] は観戦を禁止されました。");
		} else {
			addSystemMessage(spectator.getEntryName() + " は観戦を禁止されました。");
		}
		_lastDataUpdate = new GregorianCalendar();
	}

	public synchronized void setAnnounces(Id operator, List<String> announces) {
		if (!isManager(operator)) {
			return;
		}
		if (isKickedId(operator)) {
			return;
		}

		Announces.clear();
		if (announces == null) {
			return;
		}

		for (String announce : announces) {
			Announces.add(announce);
		}

		_lastAnnounceTime = new GregorianCalendar();
	}

	public synchronized void setUserCount(Id operator, int count) {
		if (!isManager(operator)) {
			return;
		}
		if (isKickedId(operator)) {
			return;
		}
		setUserCount(count);
	}

	public synchronized void start(Id operator, boolean shuffle) {
		if (!isManager(operator)) {
			return;
		}
		if (isKickedId(operator)) {
			return;
		}
		if (_started) {
			return;
		}

		if (shuffle) {
			List<Integer> numbers = new ArrayList<Integer>();
			for (int i = 0; i < Players.size(); i++) {
				numbers.add(i + 1);
			}
			Collections.shuffle(numbers);
			for (int i = 0; i < Players.size(); i++) {
				Players.get(i).setEntryNo(numbers.get(i));
			}
		}

		Collections.sort(Players);

		_started = true;
		addSystemMessage("大会が開始されました。");
		_lastDataUpdate = new GregorianCalendar();
	}

	public synchronized void retireByManager(Id operator, Id playerId) {
		if (!_started) {
			return;
		}
		if (!isManager(operator)) {
			return;
		}
		if (isKickedId(operator)) {
			return;
		}
		if (!isActivePlayer(playerId)) {
			return;
		}
		for (int i = 0; i < Players.size(); i++) {
			if (Players.get(i).Id.equals(playerId)) {
				if (!isManager(playerId)) {
					Guests.add(new Guest(Players.get(i).Id.copy(), Players.get(i).getEntryName()));
				}
				addSystemMessage(Players.get(i).getEntryName() + " がリタイアさせられました。");
				if (_started) {
					Players.get(i).setRetired(true);
				} else {
					Players.remove(i);
				}
				_lastDataUpdate = new GregorianCalendar();
				break;
			}
		}
	}

	public synchronized void setResultsByManager(Id operator, Id playerId, List<Integer> results) {
		if (!isManager(operator)) {
			return;
		}
		if (isKickedId(operator)) {
			return;
		}

		Player player = findActivePlayer(playerId);
		if (player == null) {
			// include already retired
			return;
		}

		try {
			player.MatchResults.clear();
			for (int result : results) {
				player.MatchResults.add(result);
			}
			_lastDataUpdate = new GregorianCalendar();
		} catch (IndexOutOfBoundsException e) {
		}
	}

	public synchronized void kick(Id operator, Id id) {
		if (!isManager(operator)) {
			return;
		}
		// can't kick managers
		if (isManager(id)) {
			return;
		}
		// already kicked
		if (isKickedId(id)) {
			return;
		}

		KickedIds.add(id);

		TournamentUser kickedUser = null;

		Player kickedPlayer = findPlayer(id);
		if (kickedPlayer != null) {
			if (_started) {
				kickedPlayer.setRetired(true);
			} else {
				Players.remove(kickedPlayer);
			}
			kickedUser = kickedPlayer;
		}

		if (kickedUser == null) {
			Spectator kickedSpectator = findSpectator(id);
			if (kickedSpectator != null) {
				Spectators.remove(kickedSpectator);
				kickedUser = kickedSpectator;
			}
		}

		if (kickedUser == null) {
			Guest kickedGuest = findGuest(id);
			if (kickedGuest != null) {
				Guests.remove(kickedGuest);
				kickedUser = kickedGuest;
			}
		}

		if (kickedUser != null) {
			if (kickedUser instanceof Guest || kickedUser.getEntryName().equals("")) {
				addSystemMessage("[" + id.getValue() + "] はキックされました。");
			} else {
				addSystemMessage(kickedUser.getEntryName() + " はキックされました。");
			}
			_lastDataUpdate = new GregorianCalendar();
		}
	}

	public synchronized Id entry(Id id, String entryName, String ip, int port) {
		if (isKickedId(id)) {
			return null;
		}

		Calendar now = new GregorianCalendar();

		Player player = findPlayer(id);
		if (player != null) {
			// if already player, update player information
			player.setEntryName(entryName);
			player.setIp(ip);
			player.setPort(port);
			_lastDataUpdate = (Calendar) now.clone();
			return player.Id;
		}

		// update entry name
		Manager manager = findManager(id);
		if (manager != null) {
			manager.setEntryName(entryName);
			_lastDataUpdate = (Calendar) now.clone();
		}
		Spectator spectator = findSpectator(id);
		Guest guest = findGuest(id);

		if (_started || _userCount <= Players.size()) {
			// already started or capacity over
			if (manager != null) {
				return manager.Id;
			} else if (spectator != null) {
				spectator.setEntryName(entryName);
				_lastDataUpdate = (Calendar) now.clone();
				return spectator.Id;
			} else if (guest != null) {
				guest.setEntryName(entryName);
				_lastDataUpdate = (Calendar) now.clone();
				return guest.Id;
			} else {
				return null;
			}
		}

		// new player
		Player newPlayer = new Player(id, getNextEntryNo(), entryName, ip, port);
		Players.add(newPlayer);
		if (spectator != null) {
			Spectators.remove(spectator);
		}
		if (guest != null) {
			Guests.remove(guest);
		}
		addSystemMessage(entryName + " さんが参加しました。");
		_lastDataUpdate = (Calendar) now.clone();
		return newPlayer.Id;
	}

	public synchronized Id guestEntry(Id id, String entryName) {
		// fail only kicked
		if (isKickedId(id)) {
			return null;
		}

		Manager manager = findManager(id);
		if (manager != null) {
			manager.setEntryName(entryName);
		}

		Player player = findPlayer(id);
		if (player != null) {
			player.setEntryName(entryName);
		}

		Spectator spectator = findSpectator(id);
		if (spectator != null) {
			spectator.setEntryName(entryName);
		}

		Guest guest = findGuest(id);
		if (guest != null) {
			guest.setEntryName(entryName);
		}

		if (manager == null && player == null && spectator == null && guest == null) {
			Guests.add(new Guest(id, entryName));
		}
		_lastDataUpdate = new GregorianCalendar();
		return id;
	}

	public synchronized void cancelEntry(Id operator) {
		if (_started) {
			return;
		}
		if (isKickedId(operator)) {
			return;
		}

		Player player = findPlayer(operator);
		if (player == null) {
			return;
		}

		if (!isManager(operator)) {
			Guests.add(new Guest(player.Id, player.getEntryName()));
		}
		Players.remove(player);
		addSystemMessage(player.getEntryName() + " さんが参加を取り消しました。");
		_lastDataUpdate = new GregorianCalendar();
	}

	public synchronized void retire(Id operator) {
		if (!_started) {
			return;
		}
		if (isKickedId(operator)) {
			return;
		}

		Player player = findActivePlayer(operator);
		if (player == null) {
			return;
		}
		if (!isManager(operator)) {
			Guests.add(new Guest(player.Id, player.getEntryName()));
		}
		player.setWaiting(false);
		player.setFighting(false);
		player.setRetired(true);
		addSystemMessage(player.getEntryName() + " さんがリタイアしました。");
		_lastDataUpdate = new GregorianCalendar();
	}

	public synchronized void setWaiting(Id operator, boolean waiting) {
		if (isKickedId(operator)) {
			return;
		}
		Player player = findActivePlayer(operator);
		if (player == null) {
			return;
		}
		if (player.isWaiting() != waiting) {
			player.setWaiting(waiting);
			_lastDataUpdate = new GregorianCalendar();
		}
	}

	public synchronized void setFighting(Id operator, boolean fighting) {
		if (isKickedId(operator)) {
			return;
		}
		Player player = findActivePlayer(operator);
		if (player == null) {
			return;
		}

		if (player.isFighting() != fighting) {
			player.setFighting(fighting);
			_lastDataUpdate = new GregorianCalendar();
		}
	}

	public synchronized void setResults(Id operator, List<Integer> results) {
		if (isKickedId(operator)) {
			return;
		}
		Player player = findActivePlayer(operator);
		if (player == null) {
			return;
		}

		player.MatchResults.clear();
		for (int result : results) {
			player.MatchResults.add(result);
		}
		_lastDataUpdate = new GregorianCalendar();
	}

	public synchronized void addChat(Id operator, String name, String contents) {
		if (isKickedId(operator)) {
			return;
		}

		if (isManager(operator)) {
			Chats.add(new Chat(new Id("☆運営☆"), name, contents));
		} else {
			Chats.add(new Chat(operator, name, contents));
		}
		_lastChatUpdate = new GregorianCalendar();
	}

	public synchronized void delete(Id operator) {
		if (_deleted) {
			return;
		}
		if (!RegisteredId.equals(operator)) {
			return;
		}
		_deleted = true;
		_lastDataUpdate = new GregorianCalendar();
	}

	public synchronized void delete() {
		if (_deleted) {
			return;
		}
		_deleted = true;
		_lastDataUpdate = new GregorianCalendar();
	}

	private Manager findManager(Id id) {
		for (Manager manager : Managers) {
			if (manager.Id.equals(id)) {
				return manager;
			}
		}
		return null;
	}

	private Player findActivePlayer(Id id) {
		for (Player player : Players) {
			if (player.Id.equals(id) && !player.isRetired()) {
				return player;
			}
		}
		return null;
	}

	private Player findPlayer(Id id) {
		for (Player player : Players) {
			if (player.Id.equals(id)) {
				return player;
			}
		}
		return null;
	}

	private Spectator findSpectator(Id id) {
		for (Spectator spectator : Spectators) {
			if (spectator.Id.equals(id)) {
				return spectator;

			}
		}
		return null;
	}

	private Guest findGuest(Id id) {
		for (Guest guest : Guests) {
			if (guest.Id.equals(id)) {
				return guest;
			}
		}
		return null;
	}

	private synchronized void addSystemMessage(String message) {
		Chats.add(new Chat(new Id("_system_"), message));
		_lastChatUpdate = new GregorianCalendar();
	}

	private synchronized boolean isManager(Id id) {
		for (Manager manager : Managers) {
			if (manager.Id.equals(id)) {
				return true;
			}
		}
		return false;
	}

	private synchronized boolean isKickedId(Id id) {
		for (Id kickedId : KickedIds) {
			if (kickedId.equals(id)) {
				return true;
			}
			break;
		}
		return false;
	}

	// private synchronized boolean isPlayer(Id id) {
	// for (Player player : Players) {
	// if (player.Id.equals(id)) {
	// return true;
	// }
	// }
	// return false;
	// }

	private synchronized boolean isActivePlayer(Id id) {
		for (Player player : Players) {
			if (player.Id.equals(id) && !player.isRetired()) {
				return true;
			}
		}
		return false;
	}

	private synchronized boolean isSpectator(Id id) {
		for (Spectator spectator : Spectators) {
			if (spectator.Id.equals(id)) {
				return true;
			}
		}
		return false;
	}

	// private synchronized boolean isGuest(Id id) {
	// for (Guest guest : Guests) {
	// if (guest.Id.equals(id)) {
	// return true;
	// }
	// }
	// return false;
	// }

	public synchronized void refreshGuests(Id operator, String entryName) {
		// already kicked
		if (isKickedId(operator)) {
			return;
		}

		Calendar now = new GregorianCalendar();

		if (isManager(operator)) {
			// remove old guests
			List<Guest> removeGuests = new ArrayList<Guest>();
			for (Guest guest : Guests) {
				Calendar removeTime = (Calendar) guest.getLastAccessTime().clone();
				removeTime.add(Calendar.SECOND, 30);
				if (now.after(removeTime)) {
					removeGuests.add(guest);
				}
			}
			if (0 < removeGuests.size()) {
				Guests.removeAll(removeGuests);
				_lastDataUpdate = (Calendar) now.clone();
			}
		} else {
			Guest guest = findGuest(operator);
			if (guest != null) {
				// update last access time
				guest.setLastAccessTime((Calendar) now.clone());
			} else if (!isActivePlayer(operator) && !isSpectator(operator)) {
				// add guest
				Guests.add(new Guest(operator, entryName));
				_lastDataUpdate = (Calendar) now.clone();
			}
		}
	}

	public synchronized Tournament getData(Id operator, Calendar lastChatTime) {
		// if kicked return dummy
		if (isKickedId(operator)) {
			Tournament dummy = new Tournament();
			dummy.KickedIds.add(operator);
			return dummy;
		}

		Tournament data = new Tournament(No, RegisteredId.copy(), (Calendar) Time.clone(), Type, _userCount, new String(Rank), new String(Comment));

		data.Announces.clear();
		for (String announce : Announces) {
			data.Announces.add(new String(announce));
		}

		data.Managers.clear();
		for (Manager manager : Managers) {
			data.Managers.add(manager.copy());
		}
		data.Players.clear();
		for (Player player : Players) {
			data.Players.add(player.copy());
		}
		data.Spectators.clear();
		for (Spectator spectator : Spectators) {
			data.Spectators.add(spectator.copy());
		}
		data.Guests.clear();
		for (Guest guest : Guests) {
			data.Guests.add(guest.copy());
		}
		data.KickedIds.clear();
		for (Id kickedId : KickedIds) {
			data.KickedIds.add(kickedId);
		}

		data.Chats.clear();
		for (Chat chat : Chats) {
			if (chat.Time.after(lastChatTime)) {
				data.Chats.add(chat);
			}
		}

		data._started = _started;
		data._deleted = _deleted;
		data._lastDataUpdate = (Calendar) _lastDataUpdate.clone();
		data._lastAnnounceTime = (Calendar) _lastAnnounceTime.clone();
		data._lastChatUpdate = (Calendar) _lastChatUpdate.clone();

		return data;
	}

	public synchronized Tournament copy() {
		Tournament copy = new Tournament(No, RegisteredId.copy(), (Calendar) Time.clone(), Type, _userCount, new String(Rank), new String(Comment));

		copy.Announces.clear();
		for (String announce : Announces) {
			copy.Announces.add(new String(announce));
		}

		copy.Managers.clear();
		for (Manager manager : Managers) {
			copy.Managers.add(manager.copy());
		}
		copy.Players.clear();
		for (Player player : Players) {
			copy.Players.add(player.copy());
		}
		copy.Spectators.clear();
		for (Spectator spectator : Spectators) {
			copy.Spectators.add(spectator.copy());
		}
		copy.Guests.clear();
		for (Guest guest : Guests) {
			copy.Guests.add(guest.copy());
		}
		copy.KickedIds.clear();
		for (Id kickedId : KickedIds) {
			copy.KickedIds.add(kickedId.copy());
		}
		copy.Chats.clear();
		for (Chat chat : Chats) {
			copy.Chats.add(chat.copy());
		}

		copy._started = _started;
		copy._deleted = _deleted;
		copy._lastDataUpdate = (Calendar) _lastDataUpdate.clone();
		copy._lastAnnounceTime = (Calendar) _lastAnnounceTime.clone();
		copy._lastChatUpdate = (Calendar) _lastChatUpdate.clone();

		return copy;
	}

	private int getNextEntryNo() {
		_entryNo += 1;
		if (999 < _entryNo) {
			_entryNo = 1;
		}

		return _entryNo;
	}

	public int getUserCount() {
		return _userCount;
	}

	public synchronized void setUserCount(int userCount) {
		if (_started) {
			return;
		}
		if (_userCount == userCount) {
			return;
		}

		if (userCount < 2) {
			_userCount = 2;
		} else {
			_userCount = userCount;
		}
		_lastDataUpdate = new GregorianCalendar();
	}

	public Calendar getLastDataUpdate() {
		return _lastDataUpdate;
	}

	@Deprecated
	public void setLastDataUpdate(Calendar lastUpdate) {
		_lastDataUpdate = lastUpdate;
	}

	public Calendar getLastAnnounceTime() {
		return _lastAnnounceTime;
	}

	public Calendar getLastChatUpdate() {
		return _lastChatUpdate;
	}

	@Deprecated
	public void setLastChatUpdate(Calendar lastChatUpdate) {
		_lastChatUpdate = lastChatUpdate;
	}

	public boolean isStarted() {
		return _started;
	}

	@Deprecated
	public void setStarted(boolean started) {
		_started = started;
	}

	public boolean isDeleted() {
		return _deleted;
	}

	@Deprecated
	public void setDeleted(boolean deleted) {
		_deleted = deleted;
	}
}
