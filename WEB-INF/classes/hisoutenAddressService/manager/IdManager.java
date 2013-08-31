package hisoutenAddressService.manager;

import hisoutenAddressService.model.Id;
import javax.servlet.ServletContext;
import javax.xml.ws.handler.MessageContext;

/**
 *
 * @author bngper
 */
public class IdManager {

    private static final String SETTING_NAME = "IdKey";
    private IdCrypto _idCrypto;

    /**
     *
     * @param messageContext
     */
    public IdManager(MessageContext messageContext) {
        ServletContext servletContext = (ServletContext) messageContext.get(MessageContext.SERVLET_CONTEXT);
        String idKeyString = servletContext.getInitParameter(SETTING_NAME);

        String[] keyStrings = idKeyString.split(",");
        int[] keyValues = new int[]{Integer.parseInt(keyStrings[0]), Integer.parseInt(keyStrings[1]), Integer.parseInt(keyStrings[2]), Integer.parseInt(keyStrings[3])};

        for (int i = 0; i < keyValues.length; i++) {
            if (keyValues[i] < 0) {
                keyValues[i] = Math.abs(i);
            }
        }

        int shiftCount = Math.abs(idKeyString.hashCode()) % 10 + 5;

        this._idCrypto = new IdCrypto(keyValues, shiftCount);
    }

    /**
     *
     * @param context
     * @return
     */
    public Id create(String Ip) {
        return new Id(_idCrypto.encrypt(Ip));
    }

    public String decrypt(String id) {
        return _idCrypto.decrypt(id);
    }
}
