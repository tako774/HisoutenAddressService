package hisoutenAddressService.manager;

import hisoutenAddressService.model.Chat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author bngper
 */
public class ChatManager {

    private final int _chatMaxCount;
    private final int _chatMaxMinutes;
    private final List<Chat> _chats;

    public ChatManager(int chatMaxCount, int chatMaxMinutes) {
        _chatMaxCount = chatMaxCount;
        _chatMaxMinutes = chatMaxMinutes;
        _chats = new ArrayList<Chat>();
    }

    public synchronized List<Chat> getChats(Calendar time) {
        reflesh();

        List<Chat> chatData = new ArrayList<Chat>();

        if (time == null) {
            for (Chat chat : _chats) {
                chatData.add(chat.copy());
            }
        } else {
            for (Chat chat : _chats) {
                if (chat.Time.after(time)) {
                    chatData.add(chat.copy());
                }
            }
        }

        return chatData;
    }

    public synchronized List<Chat> getChatsOld(Calendar time) {
        reflesh();

        List<Chat> chatData = new ArrayList<Chat>();

        if (time == null) {
            for (Chat chat : _chats) {
                chatData.add(chat.copyOld());
            }
        } else {
            for (Chat chat : _chats) {
                if (chat.Time.after(time)) {
                    chatData.add(chat.copyOld());
                }
            }
        }

        return chatData;
    }

    public synchronized void AddChat(Chat chat) {
        _chats.add(chat);

        while (_chatMaxCount < _chats.size()) {
            _chats.remove(0);
        }
    }

    public synchronized void clear() {
        _chats.clear();
    }

    public synchronized void reflesh() {
        Calendar now = new GregorianCalendar();

        while (0 < _chats.size()) {
            Chat oldestChat = _chats.get(0);
            Calendar deleteTime = (Calendar) oldestChat.Time.clone();
            deleteTime.add(Calendar.MINUTE, _chatMaxMinutes);
            if (now.after(deleteTime)) {
                _chats.remove(0);
            } else {
                break;
            }
        }
    }
}
