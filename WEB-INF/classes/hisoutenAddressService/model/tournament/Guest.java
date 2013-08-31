package hisoutenAddressService.model.tournament;

import hisoutenAddressService.model.Id;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author bngper
 */
public class Guest extends TournamentUser {

    private Calendar _lastAccessTime;

    @SuppressWarnings("unused")
    private Guest() {
        this(null);
    }

    public Guest(Id id) {
        this(id, "");
    }

    public Guest(Id id, String entryName) {
        this(id, entryName, new GregorianCalendar());
    }

    public Guest(Id id, String entryName, Calendar lastAccessTime) {
        super(id, entryName);
        _lastAccessTime = lastAccessTime;
    }

    @Override
    public Guest copy() {
        return new Guest(Id.copy(), new String(_entryName), (Calendar) _lastAccessTime.clone());
    }

    public Calendar getLastAccessTime() {
        return _lastAccessTime;
    }

    public void setLastAccessTime(Calendar lastAccessTime) {
        this._lastAccessTime = lastAccessTime;
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof TournamentUser)) {
            return false;
        }
        if (other == this) {
            return true;
        }

        TournamentUser user = (TournamentUser) other;

        return Id.equals(user.Id);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
