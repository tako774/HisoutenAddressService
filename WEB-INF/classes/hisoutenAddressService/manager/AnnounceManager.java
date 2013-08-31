package hisoutenAddressService.manager;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author bngper
 */
public class AnnounceManager {

    private final List<String> _announces;

    public AnnounceManager() {
        _announces = new ArrayList<String>();
    }

    public synchronized List<String> getAnnounces() {
        List<String> announceData = new ArrayList<String>();
        for (String announce : _announces) {
            announceData.add(new String(announce));
        }
        return announceData;
    }

    public synchronized void setAnnounces(List<String> announces) {
        _announces.clear();
        if (announces == null) {
            return;
        }
        for (String announce : announces) {
            _announces.add(announce);
        }
    }

    public synchronized void addAnnounce(String announce) {
        _announces.add(announce);
    }

    public synchronized void removeAnnounce(int index) {
        _announces.remove(index);
    }

    public synchronized void clear() {
        _announces.clear();
    }
}
