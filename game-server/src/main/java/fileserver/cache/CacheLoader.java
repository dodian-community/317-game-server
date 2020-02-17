package fileserver.cache;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.CRC32;

import net.dodian.old.util.CompressionUtil;
import com.google.common.base.Preconditions;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Represents a file system of caches.
 * @author Ryley Kimmel <ryley.kimmel@live.com>
 * @author Artem Batutin <artembatutin@gmail.com>
 * @editor Swiffy96
 */
public class CacheLoader {

	/**
	 * The cache file.
	 */
	private RandomAccessFile cacheFile;

	/**
	 * The index files.
	 */
	private RandomAccessFile[] indices = new RandomAccessFile[256];

	/**
	 * The cached archive hashes.
	 */
	public ByteBuf CRC_TABLE;


	/**
	 * The preloadable-files table.
	 */
	public ByteBuf[] PRELOAD_FILES = new ByteBuf[CacheConstants.PRELOAD_FILES.length];	

	/**
	 * Constructs and initializes a {@link CacheLoader} from the specified
	 * {@code directory}.
	 * @return The constructed {@link CacheLoader} instance.
	 * @throws Exception 
	 */
	public void init() throws Exception {
		Path root = Paths.get(CacheConstants.CACHE_BASE_DIR);
		Preconditions.checkArgument(Files.isDirectory(root), "Supplied path must be a directory!");

		Path data = root.resolve(CacheConstants.DATA_PREFIX);
		Preconditions.checkArgument(Files.exists(data), "No data file found in the specified path!");

		int indexCount = 0;

		//Load cache index files (indices)
		for (int index = 0; index < indices.length; index++) {
			File f = new File(root + "/"+CacheConstants.INDEX_PREFIX+"" + index);
			if (f.exists() && !f.isDirectory()) {
				indexCount++;
				indices[index] = new RandomAccessFile(f, "r");
			}
		}

		if (indexCount <= 0) {
			throw new Exception("No index file(s) present");
		}

		//Load actual main cache..
		File cache = new File(root + "/" + CacheConstants.DATA_PREFIX);
		if (cache.exists() && !cache.isDirectory()) {
			cacheFile = new RandomAccessFile(cache, "r");
		} else {
			throw new Exception("No data file present!");
		}

		//Load preload files...
		for(int i = 0; i < CacheConstants.PRELOAD_FILES.length; i++) {
			String fileName = CacheConstants.PRELOAD_FILES[i];
			File file = new File(CacheConstants.CACHE_BASE_DIR + fileName);
			if (!file.exists() || file.isDirectory()) {
				throw new Exception("Preload file "+fileName+" is not present!");
			}
			getPreloadFile(i);
		}
	}

	/**
	 * Attempts to return the file with the {@link path} requested.
	 * 
	 * @param path		The path of the file requested.
	 * @return			The file requested.
	 * @throws IOException
	 */
	public ByteBuf request(String path) {
		try {

			if (path.startsWith("crc")) {
				return getCrcTable();
			} else if (path.startsWith("title")) {
				return getFile(0, 1);
			} else if (path.startsWith("config")) {
				return getFile(0, 2);
			} else if (path.startsWith("interface")) {
				return getFile(0, 3);
			} else if (path.startsWith("media")) {
				return getFile(0, 4);
			} else if (path.startsWith("versionlist")) {
				return getFile(0, 5);
			} else if (path.startsWith("textures")) {
				return getFile(0, 6);
			} else if (path.startsWith("wordenc")) {
				return getFile(0, 7);
			} else if (path.startsWith("sounds")) {
				return getFile(0, 8);
			} else if (path.startsWith("sounds")) {
				return getFile(0, 8);
			}

			/**
			 * Preloadable files
			 */
			if (path.startsWith("preload")) {
				String file = path.substring(path.lastIndexOf("/") + 1);
				for(int i = 0; i < CacheConstants.PRELOAD_FILES.length; i++) {
					if(CacheConstants.PRELOAD_FILES[i].equals(file)) {
						return getPreloadFile(i);
					}
				}
			}

		} catch(IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * Gets the index of a file.
	 * @return The {@link Index}.
	 * @throws IOException if an I/O error occurs.
	 */
	private CacheIndex getIndex(int type, int file) throws IOException {
		int index = type;
		if (index < 0 || index >= indices.length) {
			throw new IndexOutOfBoundsException();
		}

		byte[] buffer = new byte[CacheConstants.INDEX_SIZE];
		RandomAccessFile indexFile = indices[index];
		synchronized (indexFile) {
			long ptr = (long) file * (long) CacheConstants.INDEX_SIZE;
			if (ptr >= 0 && indexFile.length() >= (ptr + CacheConstants.INDEX_SIZE)) {
				indexFile.seek(ptr);
				indexFile.readFully(buffer);
			} else {
				throw new FileNotFoundException("Can't find: Type: "+type+", file: "+file);
			}
		}

		return CacheIndex.decode(buffer);
	}

	/**
	 * Gets the number of files with the specified type.
	 * @param type The type.
	 * @return The number of files.
	 * @throws IOException if an I/O error occurs.
	 */
	private int getFileCount(int type) throws IOException {
		if (type < 0 || type >= indices.length) {
			throw new IndexOutOfBoundsException();
		}

		RandomAccessFile indexFile = indices[type];
		synchronized (indexFile) {
			return (int) (indexFile.length() / CacheConstants.INDEX_SIZE);
		}
	}

	/**
	 * Gets a file.
	 * @return A {@link ByteBuffer} which contains the contents of the file.
	 * @throws IOException if an I/O error occurs.
	 */
	public ByteBuf getFile(int type, int file) throws IOException {
		CacheIndex index = getIndex(type, file);
		ByteBuf buffer = Unpooled.buffer(index.getSize());

		// calculate some initial values
		long ptr = (long) index.getBlock() * (long) CacheConstants.BLOCK_SIZE;
		int read = 0;
		int size = index.getSize();
		int blocks = size / CacheConstants.CHUNK_SIZE;
		if (size % CacheConstants.CHUNK_SIZE != 0) {
			blocks++;
		}

		for (int i = 0; i < blocks; i++) {

			// read header
			byte[] header = new byte[CacheConstants.HEADER_SIZE];
			synchronized (cacheFile) {
				cacheFile.seek(ptr);
				cacheFile.readFully(header);
			}

			// increment pointers
			ptr += CacheConstants.HEADER_SIZE;

			// parse header
			int nextFile = ((header[0] & 0xFF) << 8) | (header[1] & 0xFF);
			int curChunk = ((header[2] & 0xFF) << 8) | (header[3] & 0xFF);
			int nextBlock = ((header[4] & 0xFF) << 16) | ((header[5] & 0xFF) << 8) | (header[6] & 0xFF);
			int nextType = header[7] & 0xFF;

			// check expected chunk id is correct
			if (i != curChunk) {
				throw new IOException("Chunk id mismatch.");
			}

			// calculate how much we can read
			int chunkSize = size - read;
			if (chunkSize > CacheConstants.CHUNK_SIZE) {
				chunkSize = CacheConstants.CHUNK_SIZE;
			}

			// read the next chunk and put it in the buffer
			byte[] chunk = new byte[chunkSize];
			synchronized (cacheFile) {
				cacheFile.seek(ptr);
				cacheFile.readFully(chunk);
			}			
			buffer.writeBytes(chunk);

			// increment pointers
			read += chunkSize;
			ptr = (long) nextBlock * (long) CacheConstants.BLOCK_SIZE;

			// if we still have more data to read, check the validity of the
			// header
			if (size > read) {				
				if (nextType != (type + 1)) {
					throw new IOException("File type mismatch.");
				}

				if (nextFile != file) {
					throw new IOException("File id mismatch.");
				}
			}
		}

		return buffer;
	}

	public byte[] getDecompressedFile(int cacheId, int indexId) throws IOException {
		ByteBuf compressed = getFile(cacheId, indexId);
		ByteBuf decompressed = Unpooled.wrappedBuffer(CompressionUtil.gunzip(compressed.array()));
		return decompressed.array();
	}

	/**
	 * Gets a file.
	 * @param file The file to get.
	 * @return A byte array which contains the contents of the file.
	 */
	public byte[] getFile(File file) {
		try  {
			int i = (int)file.length();
			byte data[] = new byte[i];
			DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
			datainputstream.readFully(data, 0, i);
			datainputstream.close();
			return data;			
		} catch(Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	/**
	 * Returns the ByteBuf of a preloaded file.
	 * @param index		The index of the preload file to return.
	 * @return A {@link ByteBuffer} which contains the contents of the sprite file.
	 */
	public ByteBuf getPreloadFile(int index) {
		synchronized (this) {
			if (PRELOAD_FILES[index] != null) {
				return PRELOAD_FILES[index].slice();
			}
		}

		//Load the file data...
		byte[] data = getFile(new File(CacheConstants.CACHE_BASE_DIR + CacheConstants.PRELOAD_FILES[index]));

		//Create a copied buffer from the file data..
		ByteBuf buf = Unpooled.copiedBuffer(data);

		synchronized (this) {
			PRELOAD_FILES[index] = buf.asReadOnly();
			return PRELOAD_FILES[index].slice();
		}
	}

	/**
	 * Returns the cached {@link #CRC_TABLE} if they exist, otherwise they
	 * are calculated and cached for future use.
	 * @return The hashes of each {@link CacheArchive}.
	 * @throws IOException If some I/O exception occurs.
	 */
	public ByteBuf getCrcTable() throws IOException {
		synchronized (this) {
			if(CRC_TABLE != null) {
				return CRC_TABLE.slice();
			}
		}

		// the number of archives
		int archives = getFileCount(0);
		int[] crcs = new int[archives + PRELOAD_FILES.length];

		CRC32 crc32 = new CRC32();
		for(int file = 1; file < crcs.length; file++) {

			ByteBuf buffer;

			//Should we fetch the crc for a preloadable file or a cache file?
			if(file < archives) {
				buffer = getFile(CacheConstants.CONFIG_INDEX, file);
			} else {
				buffer = getPreloadFile(file - archives);
			}

			crc32.reset();
			byte[] bytes = new byte[buffer.readableBytes()];
			buffer.readBytes(bytes, 0, bytes.length);
			crc32.update(bytes, 0, bytes.length);
			crcs[file] = (int) crc32.getValue();
		}

		ByteBuf buffer = Unpooled.buffer(crcs.length * Integer.BYTES + 4);

		int hash = 1234;
		for(int crc : crcs) {
			hash = (hash << 1) + crc;
			buffer.writeInt(crc);
		}

		buffer.writeInt(hash);

		synchronized (this) {
			CRC_TABLE = buffer.asReadOnly();
			return CRC_TABLE.slice();
		}
	}
}