package hisoutenAddressService.model.tournament;

import hisoutenAddressService.model.Id;

/**
 *
 * @author bngper
 */
public class Spectator extends TournamentUser {

    @SuppressWarnings("unused")
    private Spectator() {
        this(null);
    }

    public Spectator(Id id) {
        this(id, "");
    }

    public Spectator(Id id, String entryName) {
        super(id, entryName);
    }

    @Override
    public Spectator copy() {
        return new Spectator(Id.copy(), new String(_entryName));
    }
}
