package hisoutenAddressService.model.tournament;

import hisoutenAddressService.model.Id;
import java.util.GregorianCalendar;

/**
 * 削除されたトーナメントを意味します。
 *
 * @author bngper
 */
public class DeletedTournament extends Tournament {

    @SuppressWarnings("unused")
    private DeletedTournament() {
        this(0, null, 0, 2, null, null);
    }

    public DeletedTournament(int no, Id registeredId, int type, int userCount, String rank, String comment) {
        super(no, registeredId, new GregorianCalendar(), type, userCount, rank, comment);
        super.delete();
    }
}
