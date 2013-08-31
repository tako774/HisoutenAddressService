package hisoutenAddressService.model.tournament;

import hisoutenAddressService.model.Id;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bngper
 */
public class Player extends TournamentUser implements Comparable<Player> {

    private int _entryNo;
    private String _ip;
    private int _port;
    public final List<Integer> MatchResults;
    private boolean _waiting;
    private boolean _fighting;
    private boolean _retired;

    @SuppressWarnings("unused")
    @Deprecated
    private Player() {
        this(null, "", "", 0);
    }

    public Player(Id id, String entryName, String ip, int port) {
        this(id, 0, entryName, ip, port);
    }

    public Player(Id id, int entryNo, String entryName, String ip, int port) {
        super(id, entryName);
        _entryNo = entryNo;
        _entryName = entryName;
        _ip = ip;
        _port = port;

        MatchResults = new ArrayList<Integer>();
        _retired = false;
    }

    public boolean isWaiting() {
        return _waiting;
    }

    public void setWaiting(boolean waiting) {
        _waiting = waiting;
    }

    public boolean isFighting() {
        return _fighting;
    }

    public void setFighting(boolean fighting) {
        _fighting = fighting;
    }

    public boolean isRetired() {
        return _retired;
    }

    public void setRetired(boolean retired) {
        _retired = retired;
    }

    @Override
    public Player copy() {
        Player copy = new Player(Id.copy(), _entryNo, new String(_entryName), new String(_ip), _port);

        for (int result : MatchResults) {
            copy.MatchResults.add(result);
        }
        copy.setWaiting(_waiting);
        copy.setFighting(_fighting);
        copy.setRetired(_retired);

        return copy;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof Player)) {
            return false;
        }
        if (other == this) {
            return true;
        }

        Player player = (Player) other;

        return Id.equals(player.Id);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.Id != null ? this.Id.hashCode() : 0);
        hash = 23 * hash + (this.MatchResults != null ? this.MatchResults.hashCode() : 0);
        return hash;
    }

    public int getEntryNo() {
        return _entryNo;
    }

    public void setEntryNo(int entryNo) {
        _entryNo = entryNo;
    }

    public String getIp() {
        return _ip;
    }

    public void setIp(String ip) {
        _ip = ip;
    }

    public int getPort() {
        return _port;
    }

    public void setPort(int port) {
        _port = port;
    }

    @Override
    public int compareTo(Player other) {
        return new Integer(_entryNo).compareTo(other.getEntryNo());
    }
}
