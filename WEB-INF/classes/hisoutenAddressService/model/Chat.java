package hisoutenAddressService.model;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 *
 * @author bngper
 */
public class Chat {

    public final Calendar Time;
    public final Id Id;
    public final String Name;
    public final String Contents;

    @SuppressWarnings("unused")
    @Deprecated
    private Chat() {
        this(null, "");
    }

    public Chat(Id id, String contents) {
        this(new GregorianCalendar(), id, contents);
    }

    public Chat(Id id, String name, String contents) {
        this(new GregorianCalendar(), id, name, contents);
    }

    public Chat(Calendar time, Id id, String contents) {
        this(time, id, null, contents);
    }

    public Chat(Calendar time, Id id, String name, String contents) {
        Time = time;
        Id = id;
        Name = name;
        Contents = contents;
    }

    public Chat copy() {
        if (Name == null) {
            return new Chat((Calendar) Time.clone(), Id.copy(), null, new String(Contents));
        } else {
            return new Chat((Calendar) Time.clone(), Id.copy(), new String(Name), new String(Contents));
        }
    }

    public Chat copyOld() {
        if (Name == null || Name.equals("")) {
            return new Chat((Calendar) Time.clone(), Id.copy(), new String(Contents));
        } else {
            return new Chat((Calendar) Time.clone(), Id.copy(), new String(Name + Contents));
        }
    }
}
