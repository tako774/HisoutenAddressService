package hisoutenAddressService.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author bngper
 */
public class Host {

    public final int No;
    public final Id Id;
    public final Calendar Time;
    public final String Ip;
    public final int Port;
    public final String Rank;
    public final String Comment;
    private Calendar _lastUpdate;
    @Deprecated
    public boolean IsFighting;
    private boolean _isDeleted;

    @SuppressWarnings("unused")
    @Deprecated
    private Host() {
        this(0, null, "", 0, "", "");
    }

    public Host(int no, Id id, String ip, int port, String rank, String comment) {
        this(no, id, new GregorianCalendar(), ip, port, rank, comment, false, false, new GregorianCalendar());
    }

    private Host(int no, Id id, Calendar time, String ip, int port, String rank, String comment, boolean isFighting, boolean isDeleted, Calendar lastUpdate) {
        No = no;
        Id = id;
        Time = time;
        Ip = ip;
        Port = port;
        Rank = rank;
        Comment = comment;
        IsFighting = isFighting;
        _isDeleted = isDeleted;
        _lastUpdate = lastUpdate;
    }

    public void delete() {
        setIsDeleted(true);
    }

    public Host copy() {
        return new Host(No, Id.copy(), (Calendar) Time.clone(), new String(Ip), Port, new String(Rank), new String(Comment), IsFighting, _isDeleted, (Calendar) _lastUpdate.clone());
    }

    boolean getIsFighting() {
        return IsFighting;
    }

    public void setIsFighting(boolean isFighting) {
        if (IsFighting != isFighting) {
            setLastUpdate(new GregorianCalendar());
        }
        IsFighting = isFighting;
    }

    public boolean getIsDeleted() {
        return _isDeleted;
    }

    @Deprecated
    public void setIsDeleted(boolean isDeleted) {
        if (_isDeleted != isDeleted) {
            setLastUpdate(new GregorianCalendar());
        }
        _isDeleted = isDeleted;
    }

    public Calendar getLastUpdate() {
        return _lastUpdate;
    }

    @Deprecated
    public void setLastUpdate(Calendar lastUpdate) {
        _lastUpdate = lastUpdate;
    }
}
