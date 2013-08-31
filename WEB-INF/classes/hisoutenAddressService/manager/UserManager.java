package hisoutenAddressService.manager;

import hisoutenAddressService.model.Id;
import hisoutenAddressService.model.User;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author bngper
 */
public class UserManager {

    private final List<User> _users;
    private final int _enterMaxMinutes;
    private final int _enterMaxSeconds;

    public UserManager(int enterMaxMinutes, int enterMaxSeconds) {
        _users = new LinkedList<User>();
        _enterMaxMinutes = enterMaxMinutes;
        _enterMaxSeconds = enterMaxSeconds;
    }

    public int getUserCount() {
        removeOld();

        return _users.size();
    }

    synchronized public void enter(Id id) {
        User user = new User(id);

        _users.remove(user);
        _users.add(user);
    }

    synchronized public void leave(Id id) {
        User user = new User(id);
        _users.remove(user);
    }

    synchronized private void removeOld() {
        Calendar now = new GregorianCalendar();

        while (0 < _users.size()) {
            User first = _users.get(0);

            Calendar deleteTime = (Calendar) first.Time.clone();
            deleteTime.add(Calendar.MINUTE, _enterMaxMinutes);
            deleteTime.add(Calendar.SECOND, _enterMaxSeconds);

            if (now.after(deleteTime)) {
                _users.remove(0);
            } else {
                break;
            }
        }
    }
}
