package bittorrent.client;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.Arrays;

public class Utils {

	private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
	
    /**
     * Convert a byte array to a string containing its hexadecimal
     * representation (as expressed in the 
     * <a href="https://www.wireshark.org/docs/wsug_html_chunked/ChUseMainWindowSection.html">
     * "packet details" and "packet bytes" panes</a> 
     * of wireshark
     * @param bytes the bytes to convert
     * @return the hexadecimal string of the byte array
     * @see <a href="https://stackoverflow.com/q/9655181">https://stackoverflow.com/q/9655181</a>
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    //convert hexString into a bytearray
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
	
    //function for URL encoding info_hash and peer_id
    //like https://wiki.theory.org/BitTorrentSpecification#Tracker_HTTP.2FHTTPS_Protocol
    //info_hash and peer_id must be given as bytearray
    public static String byteArrayToURLString(byte in[]) {
    	
    	String resultat= "";
    	String BYTE_ENCODING="ISO-8859-1";
    	try {
			resultat = URLEncoder.encode(new String(in, BYTE_ENCODING), BYTE_ENCODING).replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return resultat;
    	
    }

    // TODO : add tests to this function
    public static String ipByteToString(byte[] ipByte) {
        assert(ipByte.length == 4);
        String ipString = "";
        for (byte b : ipByte) {
            int unsignedByte = b & 0xff; // java stores it's bytes as signed, ranging from -127 to 128. we use a mask to change the value to an unsigned byte
            //byte[] singleByte = {b};
            //int unsignedByte = (new BigInteger(singleByte)).intValueExact();
            ipString += Integer.toString(unsignedByte) + ".";
        }
        return ipString.substring(0,ipString.length()-1);
    }

    //TODO: add tests to this function
    public static int portByteToInt(byte[] portByte){
        assert(portByte.length == 2);
        return byteArrayToUnsignedInt(portByte);
    }

    //TODO: add tests to this function
    public static int byteArrayToUnsignedInt(byte[] byteArray){
        int decimal = 0;
        for (byte b : byteArray) {
            decimal = (decimal << 8) + (b & 0xff);
        }
        return decimal;
    }

    //TODO: add tests to this function
    public static byte[][] deepenByteArray (byte[] byteArray,int sliceLengh){
        assert(byteArray.length % sliceLengh == 0);
        byte[][] deeperByteArray = new byte[byteArray.length/sliceLengh][sliceLengh];
        for ( int i = 0 ; i < byteArray.length; i += 6 ){
            deeperByteArray[i/sliceLengh] = Arrays.copyOfRange(byteArray, i, i+sliceLengh);
        }
        return deeperByteArray;
    }
}
