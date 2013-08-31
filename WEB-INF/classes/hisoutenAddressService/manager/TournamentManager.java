package hisoutenAddressService.manager;

import hisoutenAddressService.model.Id;
import hisoutenAddressService.model.tournament.DeletedTournament;
import hisoutenAddressService.model.tournament.Manager;
import hisoutenAddressService.model.tournament.Tournament;
import hisoutenAddressService.model.tournament.TournamentInformation;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author bngper
 */
public class TournamentManager {

    private int _no;
    private final int _tournamentMaxCount;
    private final int _tournamentMaxMinutes;
    private final int _tournamentMaxUsers;
    private final List<Tournament> _tournaments;

    public TournamentManager(int tournamentMaxCount, int tournamentMaxMinutes, int tournamentMaxUsers) {
        _no = 0;
        _tournamentMaxCount = tournamentMaxCount;
        _tournamentMaxMinutes = tournamentMaxMinutes;
        _tournamentMaxUsers = tournamentMaxUsers;
        _tournaments = new ArrayList<Tournament>();
    }

    public synchronized List<TournamentInformation> getTournamentInformations(Calendar lastDataTime) {
        refresh();

        List<TournamentInformation> tournaments = new ArrayList<TournamentInformation>();
        for (Tournament tournament : _tournaments) {
            if (tournament.getLastDataUpdate().after(lastDataTime)) {
                tournaments.add(TournamentInformation.fromTournamentBase(tournament));
            }
        }
        return tournaments;
    }

    public synchronized void clear() {
        _no = 0;

        for (Tournament tournament : _tournaments) {
            tournament.delete();
        }
    }

    public synchronized void deleteTournament(int no) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }
        tournament.delete();
    }

    public synchronized void addTournament(Id operator, int userCount, int type, String rank, String comment) {
        int correctUserCount;
        if (userCount <= _tournamentMaxUsers) {
            correctUserCount = userCount;
        } else {
            correctUserCount = _tournamentMaxUsers;
        }

        for (Tournament tournament : _tournaments) {
            if (tournament.RegisteredId.equals(operator) && !tournament.isDeleted()) {
                tournament.delete();
                break;
            }
        }

        Tournament tournament = new Tournament(getNextNo(), operator, type, correctUserCount, rank, comment);
        tournament.Managers.add(new Manager(operator));
        _tournaments.add(tournament);

        deleteOverSizeTournaments();
    }

    public synchronized void deleteTournament(Id operator) {
        for (Tournament tournament : _tournaments) {
            if (!tournament.isDeleted() && tournament.RegisteredId.equals(operator)) {
                tournament.delete();
                return;
            }
        }
    }

    public synchronized void addManager(int no, Id operator, Id manager) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }
        tournament.addManager(operator, manager);
    }

    public synchronized void removeManager(int no, Id operator, Id manager) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }
        tournament.removeManager(operator, manager);
    }

    public synchronized void cancelEntryByManager(int no, Id operator, Id player) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }
        tournament.cancelEntryByManager(operator, player);
    }

    public synchronized void addDummyPlayer(int no, Id operator) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }
        tournament.addDummyPlayer(operator);
    }

    public synchronized void addSpectator(int no, Id operator, Id spectator) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }
        tournament.addSpectator(operator, spectator);
    }

    public synchronized void removeSpectator(int no, Id operator, Id spectator) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }
        tournament.removeSpectator(operator, spectator);
    }

    public synchronized void setAnnounces(int no, Id operator, List<String> announces) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }

        tournament.setAnnounces(operator, announces);
    }

    public synchronized void setUserCount(int no, Id operator, int userCount) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }

        int correctUserCount;
        if (userCount <= _tournamentMaxUsers) {
            correctUserCount = userCount;
        } else {
            correctUserCount = _tournamentMaxUsers;
        }
        tournament.setUserCount(operator, correctUserCount);
    }

    public synchronized void start(int no, int minCount, int maxCount, Id operator, boolean shuffle) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }
        if (tournament.Players.size() < minCount || maxCount < tournament.Players.size()) {
            return;
        }
        tournament.start(operator, shuffle);
    }

    public synchronized void retireByManager(int no, Id operator, Id player) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }
        tournament.retireByManager(operator, player);
    }

    public synchronized void kick(int no, Id operator, Id kickId) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }
        tournament.kick(operator, kickId);
    }

    public synchronized void setResultsByManager(int no, Id operator, Id player, List<Integer> results) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }
        tournament.setResultsByManager(operator, player, results);
    }

    public void cancelEntry(int no, Id operator) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }
        tournament.cancelEntry(operator);
    }

    public synchronized void retire(int no, Id operator) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }
        tournament.retire(operator);
    }

    public synchronized void setWaiting(int no, Id operator, boolean waiting) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }
        tournament.setWaiting(operator, waiting);
    }

    public synchronized void setFighting(int no, Id operator, boolean fighting) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }
        tournament.setFighting(operator, fighting);
    }

    public synchronized void setResults(int no, Id operator, List<Integer> results) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }
        tournament.setResults(operator, results);
    }

    public synchronized Id entry(int no, Id id, String entryName, String ip, int port) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return null;
        }
        return tournament.entry(id, entryName, ip, port);
    }

    public synchronized Id guestEntry(int no, Id operator, String entryName) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return null;
        }
        return tournament.guestEntry(operator, entryName);
    }

    public synchronized void addChat(int no, Id id, String name, String contents) {
        Tournament tournament = getActiveTournament(no);
        if (tournament == null) {
            return;
        }
        tournament.addChat(id, name, contents);
    }

    public synchronized Tournament getTournamentData(int no, Id operator, String entryName, Calendar lastDataTime, Calendar lastChatTime) {

        Tournament activeTournament = getActiveTournament(no);
        if (activeTournament == null) {
            // if already deleted return DeletedTournament
            return new DeletedTournament(no, null, 0, 0, null, null);
        }

        // refresh guests
        activeTournament.refreshGuests(operator, entryName);

        // if no update return null
        if (!activeTournament.getLastDataUpdate().after(lastDataTime) && !activeTournament.getLastChatUpdate().after(lastChatTime) && !activeTournament.getLastAnnounceTime().after(lastDataTime) && !activeTournament.getLastAnnounceTime().after(lastChatTime)) {
            return null;
        }

        return activeTournament.getData(operator, lastChatTime);
    }

    private synchronized Tournament getActiveTournament(int no) {
        for (Tournament tournament : _tournaments) {
            if (tournament.No == no && !tournament.isDeleted()) {
                return tournament;
            }
        }
        return null;
    }

    private synchronized void refresh() {
        Calendar now = new GregorianCalendar();

        List<Tournament> removeTournaments = new ArrayList<Tournament>();

        for (Tournament tournament : _tournaments) {
            if (!tournament.isDeleted()) {
                // まだ削除されていない大会の場合
                // 削除する時間を計算（登録から _tournamentMaxMinutes 分後）
                Calendar deleteTime = (Calendar) tournament.Time.clone();
                deleteTime.add(Calendar.MINUTE, _tournamentMaxMinutes);

                // 削除する時間がきていたら削除
                if (now.after(deleteTime)) {
                    tournament.delete();
                }
            } else {
                // 既に削除されているホストの場合
                // 除去する時間を計算（削除された時間から10分後）
                Calendar removeTime = (Calendar) tournament.getLastDataUpdate().clone();
                removeTime.add(Calendar.MINUTE, 10);

                // 除去する時間がきていたら除去（するリストに追加）
                if (now.after(removeTime)) {
                    removeTournaments.add(tournament);
                }
            }
        }

        if (0 < removeTournaments.size()) {
            _tournaments.removeAll(removeTournaments);
        }
    }

    private synchronized void deleteOverSizeTournaments() {
        int notDeletedTournamentCount = 0;
        for (Tournament tournament : _tournaments) {
            if (!tournament.isDeleted()) {
                notDeletedTournamentCount += 1;
            }
        }

        while (_tournamentMaxCount < notDeletedTournamentCount) {
            for (int i = 0; i < _tournaments.size(); i++) {
                if (!_tournaments.get(i).isDeleted()) {
                    _tournaments.get(i).delete();
                    notDeletedTournamentCount -= 1;
                    break;
                }
            }
        }
    }

    private synchronized int getNextNo() {
        _no += 1;
        if (99 < _no) {
            _no = 1;
        }
        return _no;
    }
}
