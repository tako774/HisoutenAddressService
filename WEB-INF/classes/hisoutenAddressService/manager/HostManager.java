package hisoutenAddressService.manager;

import hisoutenAddressService.model.Host;
import hisoutenAddressService.model.Id;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 *
 * @author bngper
 */
public class HostManager {

    private int _no = 0;
    private final int _hostMaxCount;
    private final int _hostMaxMinutes;
    private final List<Host> _hosts;

    public HostManager(int hostMaxCount, int hostMaxMinutes) {
        _hostMaxCount = hostMaxCount;
        _hostMaxMinutes = hostMaxMinutes;
        _hosts = new ArrayList<Host>();
    }

    public synchronized List<Host> getHosts(Calendar time) {
        refresh();

        List<Host> hostData = new ArrayList<Host>();

        if (time == null) {
            for (Host host : _hosts) {
                if (!host.getIsDeleted()) {
                    hostData.add(host.copy());
                }
            }
        } else {
            for (Host host : _hosts) {
                if (host.getLastUpdate().after(time)) {
                    hostData.add(host.copy());
                }
            }
        }

        return hostData;
    }

    public synchronized void addHost(Id id, String ip, int port, String rank, String comment) {

        for (int i = _hosts.size() - 1; 0 <= i; i--) {
            if (_hosts.get(i).Id.equals(id) && _hosts.get(i).Ip.equals(ip)) {
                _hosts.get(i).delete();
                break;
            }
        }

        Host host = new Host(getNextNo(), id, ip, port, rank, comment);
        _hosts.add(host);

        deleteOverSizeHosts();
    }

    public synchronized void setFighting(Id id, String ip, boolean fighting) {
        for (int i = _hosts.size() - 1; 0 <= i; i--) {
            if (_hosts.get(i).Id.equals(id) && _hosts.get(i).Ip.equals(ip)) {
                _hosts.get(i).setIsFighting(fighting);
                break;
            }
        }
    }

    public synchronized void deleteHost(Id id, String ip) {
        for (int i = _hosts.size() - 1; 0 <= i; i--) {
            if (_hosts.get(i).Id.equals(id) && _hosts.get(i).Ip.equals(ip)) {
                _hosts.get(i).delete();
                break;
            }
        }
    }

    public synchronized void clear() {
        _no = 0;

        for (Host host : _hosts) {
            host.delete();
        }
    }

    public synchronized void forceDeleteHost(int no, String ip) {
        for (int i = _hosts.size() - 1; 0 <= i; i--) {
            if (_hosts.get(i).No == no && _hosts.get(i).Ip.equals(ip)) {
                _hosts.get(i).delete();
                break;
            }
        }
    }

    public synchronized void forceSetFighting(int no, String ip, boolean fighting) {
        for (int i = _hosts.size() - 1; 0 <= i; i--) {
            if (_hosts.get(i).No == no && _hosts.get(i).Ip.equals(ip)) {
                _hosts.get(i).setIsFighting(fighting);
                break;
            }
        }
    }

    private synchronized int getNextNo() {
        _no += 1;
        if (999 < _no) {
            _no = 1;
        }
        return _no;
    }

    private synchronized void deleteOverSizeHosts() {
        int notDeletedHostCount = 0;
        for (Host host : _hosts) {
            if (!host.getIsDeleted()) {
                notDeletedHostCount += 1;
            }
        }

        while (_hostMaxCount < notDeletedHostCount) {
            for (int i = 0; i < _hosts.size(); i++) {
                if (!_hosts.get(i).getIsDeleted()) {
                    _hosts.get(i).delete();
                    notDeletedHostCount -= 1;
                    break;
                }
            }
        }
    }

    private synchronized void refresh() {
        Calendar now = new GregorianCalendar();

        List<Host> removeHosts = new ArrayList<Host>();
        for (Host host : _hosts) {
            if (!host.getIsDeleted()) {
                // まだ削除されていないホストの場合
                // 削除する時間を計算（登録から _hostMaxMinutes 分後）
                Calendar deleteTime = (Calendar) host.Time.clone();
                deleteTime.add(Calendar.MINUTE, _hostMaxMinutes);

                // 削除する時間がきていたら削除
                if (now.after(deleteTime)) {
                    host.delete();
                }
            } else {
                // 既に削除されているホストの場合
                // 除去する時間を計算（削除された時間から10分後）
                Calendar removeTime = (Calendar) host.getLastUpdate().clone();
                removeTime.add(Calendar.MINUTE, 10);

                // 除去する時間がきていたら除去（するリストに追加）
                if (now.after(removeTime)) {
                    removeHosts.add(host);
                }
            }
        }

        if (0 < removeHosts.size()) {
            _hosts.removeAll(removeHosts);
        }
    }
}
