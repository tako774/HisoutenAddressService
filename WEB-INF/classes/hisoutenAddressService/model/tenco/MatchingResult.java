package hisoutenAddressService.model.tenco;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * 
 * @author bngper
 * 
 */
public class MatchingResult {

	public final TencoUser Host;
	public final TencoUser Client;
	private boolean _hostPrepared;
	private boolean _clientPrepared;
	private Calendar _lastUpdate;

	@SuppressWarnings("unused")
	@Deprecated
	private MatchingResult() {
		this(null, null);
	}

	public MatchingResult(TencoUser host, TencoUser client) {
		Host = host;
		Client = client;
		_lastUpdate = new GregorianCalendar();
	}

	/**
	 * @param hostPrepared the hostPrepared to set
	 */
	public void setHostPrepared(boolean hostPrepared) {
		_hostPrepared = hostPrepared;
		_lastUpdate = new GregorianCalendar();
	}

	/**
	 * @return the hostPrepared
	 */
	public boolean isHostPrepared() {
		return _hostPrepared;
	}

	/**
	 * @param clientPrepared the clientPrepared to set
	 */
	public void setClientPrepared(boolean clientPrepared) {
		_clientPrepared = clientPrepared;
		_lastUpdate = new GregorianCalendar();
	}

	/**
	 * @return the clientPrepared
	 */
	public boolean isClientPrepared() {
		return _clientPrepared;
	}

	/**
	 * @return the lastUpdate
	 */
	public Calendar getLastUpdate() {
		return _lastUpdate;
	}

	@Override
	public String toString() {
		return "host: " + Host.Id.getValue() + "\r\n" + "client: " + Client.Id.getValue() + "\r\n" + "host prepared: " + Boolean.toString(_hostPrepared) + "\r\n" + "client prepared: "
				+ Boolean.toString(_clientPrepared) + "\r\n" + "host unregistered: " + Boolean.toString(Host.isUnregistered()) + "\r\n" + "client unregistered: "
				+ Boolean.toString(Client.isUnregistered()) + "\r\n";
	}
}
