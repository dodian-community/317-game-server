package fileserver;

import java.nio.charset.Charset;

/**
 * Attributes for configuring the FileServer.
 * @author Professor Oak
 */
public class FileServerConstants {

	/**
	 * The port for the file-server service.
	 */
	public static final int FILE_SERVER_PORT = 43596;
	
	/**
	 * The opcode for a JagGrab request.
	 */
	public static final byte JAGGRAB_REQUEST_OPCODE = 1;
	
	/**
	 * The opcode for an OnDemand request.
	 */
	public static final byte ONDEMAND_REQUEST_OPCODE = 2;	
	
	/**
	 * The character set used in a JagGrab request.
	 */
	public static final Charset JAGGRAB_CHARSET = Charset.forName("US-ASCII");
	
	/**
	 * The maximum length of an ondemand file chunk, in bytes.
	 */
	public static final int MAX_ONDEMAND_CHUNK_LENGTH_BYTES = 500;
}
