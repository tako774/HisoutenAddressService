package hisoutenAddressService.model.tournament;

import hisoutenAddressService.model.Id;
import java.util.Calendar;

/**
 *
 * @author bngper
 */
public class TournamentInformation {

    public final int No;
    public final Id Id;
    public final Calendar Time;
    public final int Type;
    public final int UserCount;
    public final int PlayersCount;
    public final String Rank;
    public final String Comment;
    public final boolean Started;
    public final boolean Deleted;
    public final Calendar LastUpdate;

    @SuppressWarnings("unused")
    private TournamentInformation() {
        this(0, null, null, 0, 4, 0, null, null, false, false, null);
    }

    public TournamentInformation(int no, Id id, Calendar time, int type, int userCount, int playersCount, String rank, String comment, boolean started, boolean deleted, Calendar lastUpdate) {
        No = no;
        Id = id;
        Time = time;
        Type = type;
        UserCount = userCount;
        PlayersCount = playersCount;
        Rank = rank;
        Comment = comment;
        Started = started;
        Deleted = deleted;
        LastUpdate = lastUpdate;
    }

    public static TournamentInformation fromTournamentBase(Tournament tournament) {
        TournamentInformation info = new TournamentInformation(tournament.No, tournament.RegisteredId.copy(), (Calendar) tournament.Time.clone(), tournament.Type, tournament.getUserCount(),
                tournament.Players.size(), new String(tournament.Rank), new String(tournament.Comment), tournament.isStarted(), tournament.isDeleted(), tournament.getLastDataUpdate());

        return info;
    }
}
