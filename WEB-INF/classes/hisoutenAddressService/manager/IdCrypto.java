package hisoutenAddressService.manager;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author bngper
 */
public class IdCrypto {

    private final int[] _keyValues;
    private final int _shiftCount;

    public IdCrypto(int[] keyValues, int shiftCount) {
        _keyValues = keyValues;
        _shiftCount = shiftCount;
    }

    public String encrypt(String ip) {
        String id;
        try {
            String[] ipStrings = ip.split("\\.");
            int[] intValues = new int[]{Integer.parseInt(ipStrings[0]) + _keyValues[0] + _keyValues[1], Integer.parseInt(ipStrings[1]) + _keyValues[2] + _keyValues[3],
                Integer.parseInt(ipStrings[2]) + _keyValues[0] + _keyValues[2], Integer.parseInt(ipStrings[3]) + _keyValues[1] + _keyValues[3]};
            for (int i = 0; i < intValues.length; i++) {
                while (256 <= intValues[i]) {
                    intValues[i] -= 256;
                }
            }

            List<String> idTexts = new LinkedList<String>();
            for (int value : intValues) {
                String hexString = Integer.toHexString(value);
                if (hexString.length() == 1) {
                    hexString = "0" + hexString;
                }
                idTexts.add(hexString);
            }
            String baseId = "";
            for (String idText : idTexts) {
                baseId += idText;
            }

            // 最後に文字列ずらし
            char[] idChars = baseId.toCharArray();
            List<Character> chars = new LinkedList<Character>();
            for (char c : idChars) {
                chars.add(c);
            }
            for (int i = 0; i < _shiftCount; i++) {
                int shiftPoint = i;
                if (chars.size() <= shiftPoint) {
                    shiftPoint -= chars.size();
                }
                chars.add(chars.remove(shiftPoint));
                chars.add(chars.remove(0));
            }
            String shiftedId = "";
            for (char c : chars) {
                shiftedId = shiftedId.concat(Character.toString(c));
            }
            id = shiftedId;
        } catch (Exception ex) {
            id = "ID_ERROR";
        }

        return id;
    }

    public String decrypt(String id) {
        try {
            // ずらされている文字列を元通りに
            char[] idChars = id.toCharArray();
            List<Character> chars = new LinkedList<Character>();
            for (char c : idChars) {
                chars.add(c);
            }
            for (int i = _shiftCount - 1; 0 <= i; i--) {
                int shiftPoint = i;
                if (chars.size() <= shiftPoint) {
                    shiftPoint -= chars.size();
                }
                chars.add(0, chars.remove(chars.size() - 1));
                chars.add(shiftPoint, chars.remove(chars.size() - 1));
            }
            String baseId = "";
            for (char c : chars) {
                baseId = baseId.concat(Character.toString(c));
            }

            // 復元
            String[] hexParts = new String[]{baseId.substring(0, 2), baseId.substring(2, 4), baseId.substring(4, 6), baseId.substring(6, 8)};
            int[] parts = new int[]{Integer.parseInt(hexParts[0], 16), Integer.parseInt(hexParts[1], 16), Integer.parseInt(hexParts[2], 16), Integer.parseInt(hexParts[3], 16)};

            parts[0] = parts[0] - _keyValues[0] - _keyValues[1];
            parts[1] = parts[1] - _keyValues[2] - _keyValues[3];
            parts[2] = parts[2] - _keyValues[0] - _keyValues[2];
            parts[3] = parts[3] - _keyValues[1] - _keyValues[3];

            for (int i = 0; i < parts.length; i++) {
                while (parts[i] < 0) {
                    parts[i] += 256;
                }
            }

            return Integer.toString(parts[0]) + "." + Integer.toString(parts[1]) + "." + Integer.toString(parts[2]) + "." + Integer.toString(parts[3]);
        } catch (Exception ex) {
            return "";
        }
    }
}
