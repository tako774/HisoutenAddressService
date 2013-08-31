package hisoutenAddressService;

import hisoutenAddressService.model.Id;
import hisoutenAddressService.model.tournament.Tournament;
import java.util.Calendar;
import java.util.List;

/**
 * 
 * @author bngper
 */
public interface TournamentService {

	void registerTournament(int userCount, int type, String rank, String comment);

	void unregisterTournament();

	void addTournamentManager(int no, String id);

	void removeTournamentManager(int no, String id);

	void addTournamentDummyPlayer(int no);

	void cancelTournamentEntryByManager(int no, String id);

	void addTournamentSpectator(int no, String id);

	void removeTournamentSpectator(int no, String id);

	void setTournamentAnnounces(int no, List<String> announces);

	void setTournamentUserCount(int no, int userCount);

	void startTournament(int no, int minCount, int maxCount, boolean shuffle);

	void retireTournamentByManager(int no, String playerId);

	void kickTournamentUser(int no, String id);

	void setTournamentResultsByManager(int no, String id, List<Integer> results);

	void cancelTournamentEntry(int no);

	void setTournamentPlayerWaiting(int no, boolean waiting);

	void setTournamentPlayerFighting(int no, boolean fighting);

	void setTournamentResults(int no, List<Integer> results);

	void retireTournament(int no);

	Tournament getTournamentData(int no, String entryName, Calendar lastDataTime, Calendar lastChatTime);

	Id entryTournament(int no, String entryName, String ip, int port);

	Id guestEntryTournament(int no, String entryName);

	void addTournamentChat(int no, String name, String contents);
}
