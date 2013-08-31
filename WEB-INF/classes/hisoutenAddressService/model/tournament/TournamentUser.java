package hisoutenAddressService.model.tournament;

import hisoutenAddressService.model.Id;

/**
 *
 * @author bngper
 */
public class TournamentUser {

    public final Id Id;
    protected String _entryName;

    @SuppressWarnings("unused")
    private TournamentUser() {
        this(null, "");
    }

    public TournamentUser(Id id) {
        this(id, "");
    }

    public TournamentUser(Id id, String entryName) {
        Id = id;
        _entryName = entryName;
    }

    public String getEntryName() {
        return _entryName;
    }

    public void setEntryName(String entryName) {
        this._entryName = entryName;
    }

    public TournamentUser copy() {
        return new TournamentUser(Id.copy(), new String(_entryName));
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
        int hash = 3;
        hash = 53 * hash + (this.Id != null ? this.Id.hashCode() : 0);
        return hash;
    }
}
