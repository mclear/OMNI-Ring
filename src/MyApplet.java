package ExampleProject;

import javacard.framework.APDU;
import javacard.framework.Applet;
import javacard.framework.ISOException;
import javacard.security.CryptoException;
import javacard.framework.ISO7816;
import javacard.framework.Util;



public class MyApplet extends Applet {
	private final static byte[] helloworld = {0x48, 0x65, 0x6c, 0x6c, 0x6f, 0x20, 0x57, 0x6f, 0x72, 0x6c, 0x64, 0x21};


	private MyApplet(byte[] parameters, short offset, byte length) {
		register(parameters, (short) (offset + 1), parameters[offset]);
	}

	public static void install(byte[] parameters, short offset, byte length) {
		new MyApplet(parameters, offset, length);
	}

	public void process(APDU apdu) throws ISOException {
		// Good practice: Return 9000 on SELECT
		if (selectingApplet()) {
			return;
		}
		
		byte[] buf = apdu.getBuffer();
		
		switch (buf[ISO7816.OFFSET_INS]) {
			case (byte) 0x00:
				Util.arrayCopy(helloworld, (byte) 0, buf, ISO7816.OFFSET_CDATA, (byte) helloworld.length);
            	apdu.setOutgoingAndSend(ISO7816.OFFSET_CDATA, (byte) helloworld.length);
            	break;
			default:
				// good practice: If you don't know the INStruction, say so:
				ISOException.throwIt(ISO7816.SW_INS_NOT_SUPPORTED);
		}
	}
}
