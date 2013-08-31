package hisoutenAddressService.model.tenco;

import hisoutenAddressService.model.Id;
import hisoutenAddressService.model.User;

import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * 
 * @author bngper
 * 
 */
public class TencoUser extends User {

	private static final int MIN_MATCHING_SPAN = 50;
	private static final int DEFAULT_MATCHING_SPAN = 150;
	private static final int MAX_MATCHING_SPAN = 250;
	public final String AccountName;
	public final boolean IsHostable;
	public final boolean IsRoomOnly;
	public final String Ip;
	public final int Port;
	public final String Comment;
	public final TencoRating Rating;
	public final int MatchingSpan;
	private final Collection<MatchingHistory> _histories;
	private boolean _unregistered;
	private Calendar _lastUpdate;

	@SuppressWarnings("unused")
	@Deprecated
	private TencoUser() {
		this(null, null, false, false, null, 10800, null, null, 150);
	}

	public TencoUser(Id id, String accountName, boolean isHostable, boolean isRoomOnly, String ip, int port, String comment, TencoRating rating, int matchingSpan) {
		this(id, accountName, isHostable, isRoomOnly, ip, port, comment, rating, matchingSpan, new LinkedList<MatchingHistory>());
	}

	public TencoUser(Id id, String accountName, boolean isHostable, boolean isRoomOnly, String ip, int port, String comment, TencoRating rating, int matchingSpan,
			Collection<MatchingHistory> histories) {
		super(id, new GregorianCalendar());
		AccountName = accountName;
		IsHostable = isHostable;
		IsRoomOnly = isRoomOnly;
		Ip = ip;
		Port = port;
		Comment = comment;
		Rating = rating;
		if (matchingSpan <= 0)
			MatchingSpan = DEFAULT_MATCHING_SPAN;
		else if (matchingSpan <= MIN_MATCHING_SPAN) {
			MatchingSpan = MIN_MATCHING_SPAN;
		} else if (MAX_MATCHING_SPAN <= matchingSpan) {
			MatchingSpan = MAX_MATCHING_SPAN;
		} else {
			MatchingSpan = matchingSpan;
		}

		_histories = histories;
		_lastUpdate = new GregorianCalendar();
	}

	public synchronized boolean containsHistory(Id oponent) {
		return _histories.contains(new MatchingHistory(oponent, null));
	}

	public boolean isMatch(TencoUser oponent) {
		int myMin = Rating.Value - MatchingSpan;
		int myMax = Rating.Value + MatchingSpan;
		int oponentMin = oponent.Rating.Value - oponent.MatchingSpan;
		int oponentMax = oponent.Rating.Value + oponent.MatchingSpan;

		if (oponent.Rating.Value < myMin || myMax < oponent.Rating.Value) {
			return false;
		}

		if (Rating.Value < oponentMin || oponentMax < Rating.Value) {
			return false;
		}

		return true;
	}

	public synchronized void addHistory(Id oponent, Calendar removeTime) {
		_histories.add(new MatchingHistory(oponent, removeTime));
		_lastUpdate = new GregorianCalendar();
	}

	@Override
	public synchronized TencoUser copy() {
		Collection<MatchingHistory> historiesCopy = new LinkedList<MatchingHistory>();
		for (MatchingHistory history : _histories) {
			historiesCopy.add(history.copy());
		}
		TencoUser copy = new TencoUser(Id.copy(), new String(AccountName), IsHostable, IsRoomOnly, new String(Ip), Port, new String(Comment), Rating.copy(), MatchingSpan, historiesCopy);
		copy._lastUpdate = (Calendar) _lastUpdate.clone();
		return copy;
	}

	public synchronized void refreshHistory() {
		Calendar now = new GregorianCalendar();
		Iterator<MatchingHistory> iterator = _histories.iterator();
		while (iterator.hasNext()) {
			MatchingHistory history = iterator.next();
			if (now.after(history.getRemoveTime())) {
				iterator.remove();
			}
		}
	}

	public synchronized void copyHistory(TencoUser dest) {
		dest._histories.clear();
		for (MatchingHistory history : _histories) {
			dest._histories.add(history.copy());
		}
	}

	/**
	 * @return the histories
	 */
	public Collection<MatchingHistory> getHistories() {
		return _histories;
	}

	/**
	 * @param unregistered the unregistered to set
	 */
	public void setUnregistered(boolean unregistered) {
		_unregistered = unregistered;
		_lastUpdate = new GregorianCalendar();
	}

	/**
	 * @return the unregistered
	 */
	public boolean isUnregistered() {
		return _unregistered;
	}

	/**
	 * @return the lastUpdate
	 */
	public Calendar getLastUpdate() {
		return _lastUpdate;
	}
}
