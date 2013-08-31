package hisoutenAddressService.model;

import hisoutenAddressService.model.tournament.TournamentInformation;
import java.util.List;

/**
 *
 * @author bngper
 */
public class AllData {

    public final int UserCount;
    public final List<String> Announces;
    public final List<Host> Hosts;
    public final boolean GetHost;
    public final List<Chat> Chats;
    public final boolean GetChat;
    public final List<TournamentInformation> Tournaments;
    public final boolean GetTournament;

    @SuppressWarnings("unused")
    @Deprecated
    private AllData() {
        this(0, null, null, false, null, false, null, false);
    }

    public AllData(int userCount, List<String> announces, List<Host> hosts, boolean getHost, List<Chat> chats, boolean getChat) {
        this(userCount, announces, hosts, getChat, chats, getChat, null, false);
    }

    public AllData(int userCount, List<String> announces, List<Host> hosts, boolean getHost, List<Chat> chats, boolean getChat, List<TournamentInformation> tournaments, boolean getTournament) {
        UserCount = userCount;
        Announces = announces;
        Hosts = hosts;
        GetHost = getHost;
        Chats = chats;
        GetChat = getChat;
        Tournaments = tournaments;
        GetTournament = getTournament;
    }
}
