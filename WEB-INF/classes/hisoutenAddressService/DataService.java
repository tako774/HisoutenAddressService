package hisoutenAddressService;

import hisoutenAddressService.model.AllData;
import java.util.Calendar;

/**
 * 
 * @author bngper
 */
public interface DataService {

	AllData getAllDataEx(boolean getHost, Calendar lastHostTime, boolean getChat, Calendar lastChatTime, boolean getTournament, Calendar lastTournamentTime);

	AllData getAllData(boolean getHost, boolean getChat, Calendar lastHostTime, Calendar lastChatTime);
}
