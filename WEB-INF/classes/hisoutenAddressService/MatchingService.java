package hisoutenAddressService;

import hisoutenAddressService.model.Id;
import hisoutenAddressService.model.tenco.MatchingResult;
import hisoutenAddressService.model.tenco.TencoUser;

/**
 * 
 * @author bngper
 * 
 */
public interface MatchingService {

	void registerMatching(TencoUser user);

	void unregisterMatching();

	void addMatchingHistory(Id oponent);

	void setPrepared(Id oponent);

	void skipMatching(Id oponent);

	MatchingResult getMatchingResult();
}
