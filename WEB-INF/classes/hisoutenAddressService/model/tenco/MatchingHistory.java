package hisoutenAddressService.model.tenco;

import hisoutenAddressService.model.Id;

import java.util.Calendar;

/**
 * 
 * @author bngper
 * 
 */
public class MatchingHistory {

	public final Id Oponent;
	private final Calendar RemoveTime;

	public MatchingHistory(Id oponent, Calendar removeTime) {
		Oponent = oponent;
		RemoveTime = removeTime;
	}

	public Calendar getRemoveTime() {
		return (Calendar) RemoveTime.clone();
	}

	public MatchingHistory copy() {
		return new MatchingHistory(Oponent.copy(), (Calendar) RemoveTime.clone());
	}

	@Override
	public boolean equals(Object other) {
		if (other == null || !(other instanceof MatchingHistory)) {
			return false;
		}
		if (other == this) {
			return true;
		}

		MatchingHistory history = (MatchingHistory) other;

		return Oponent.equals(history.Oponent);
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 53 * hash + (this.Oponent != null ? this.Oponent.hashCode() : 0);
		return hash;
	}
}
