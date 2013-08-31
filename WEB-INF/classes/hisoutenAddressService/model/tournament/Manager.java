package hisoutenAddressService.model.tournament;

import hisoutenAddressService.model.Id;

/**
 *
 * @author bngper
 */
public class Manager extends TournamentUser {

    @SuppressWarnings("unused")
    private Manager() {
        this(null);
    }

    public Manager(Id id) {
        this(id, "");
    }

    public Manager(Id id, String entryName) {
        super(id, entryName);
    }

    @Override
    public Manager copy() {
        return new Manager(Id.copy(), new String(_entryName));
    }
}
