package hisoutenAddressService.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * ユーザー
 *
 * @author bngper
 */
public class User {

    public final Id Id;
    public final Calendar Time;

    @SuppressWarnings("unused")
    @Deprecated
    private User() {
        this(null);
    }

    public User(Id id) {
        this(id, new GregorianCalendar());
    }

    public User(Id id, Calendar time) {
        Id = id;
        Time = time;
    }

    public User copy() {
        return new User(Id.copy(), (Calendar) Time.clone());
    }

    @Override
    public boolean equals(Object other) {
        if (other == null || !(other instanceof User)) {
            return false;
        }
        if (other == this) {
            return true;
        }

        User user = (User) other;

        return Id.equals(user.Id);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.Id != null ? this.Id.hashCode() : 0);
        return hash;
    }
}
