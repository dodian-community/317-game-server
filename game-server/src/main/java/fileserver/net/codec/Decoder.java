package fileserver.net.codec;

import java.util.List;

import fileserver.FileServer;
import fileserver.FileServerConstants;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * A {@link ByteToMessageDecoder} used for decoding incoming data.
 * @author Professor Oak
 */
public class Decoder extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> arg2) throws Exception {
		
		//Make sure there's data to read.
		if(in.isReadable()) {
			
			//Read the request opcode..
			int opcode = in.readByte();
			
			//Handle the request..
			switch(opcode) {
			case FileServerConstants.JAGGRAB_REQUEST_OPCODE:
				decodeJagGrabRequest(ctx, in);
				break;
			case FileServerConstants.ONDEMAND_REQUEST_OPCODE:
				decodeOnDemandRequest(ctx, in);
				break;
			}
		}
	}

	/**
	 * Decodes an incoming JagGrab request.
	 * @param ctx		The channel.
	 * @param in		The incoming buffer.
	 */
	private void decodeJagGrabRequest(ChannelHandlerContext ctx, ByteBuf in) {
		//Make sure we have proper amount of bytes in the buffer
		//For the data we'll be reading..
		if(in.isReadable()) {

			//Read incoming path
			byte[] pathBuffer = new byte[in.readableBytes()];
			in.readBytes(pathBuffer);
			final String path = new String(pathBuffer, FileServerConstants.JAGGRAB_CHARSET);

			//Attempt to get the file that's been requested..
			final ByteBuf file = FileServer.getCacheLoader().request(path);

			//If we loaded the file, send it.
			//Otherwise close the channel.
			if (file != null) {

				//Create the outgoing buffer
				ByteBuf buffer = Unpooled.buffer(Integer.BYTES + file.readableBytes());

				//Write file size
				buffer.writeInt(file.readableBytes());

				//Write the file
				buffer.writeBytes(file);

				//Send the outgoing buffer and then close the channel
				ctx.writeAndFlush(buffer).addListener(ChannelFutureListener.CLOSE);
			} else {
				ctx.close();
			}
		}
	}
	
	/**
	 * Decodes an incoming OnDemand request.
	 * @param ctx		The channel.
	 * @param in		The incoming buffer.
	 */
	private void decodeOnDemandRequest(ChannelHandlerContext ctx, ByteBuf in) {
		//Make sure we have proper amount of bytes in the buffer
		//For the data we'll be reading..
		if (in.readableBytes() >= Byte.BYTES + Integer.BYTES) {

			//Read request attributes..

			//Read file type
			int fileType = in.readUnsignedByte() + 1;
			
			//Read file id
			int fileId = in.readInt();
			
			//Attempt to load the requested file..
			ByteBuf file = null;
			try {
				file = FileServer.getCacheLoader().getFile(fileType, fileId);
			} catch (Exception e) {
				e.printStackTrace();
			}

			//If we loaded the file, send it (in chunks).
			//Otherwise close the channel.
			if(file != null) {
				final int length = file.readableBytes();

				//Go through the file..
				for (int chunk = 0; file.readableBytes() > 0; chunk++) {
					int chunkSize = file.readableBytes();

					//Make sure each chunk is max {@link #FileServerConstants.MAX_ONDEMAND_CHUNK_LENGTH_BYTES} bytes.
					if (chunkSize > FileServerConstants.MAX_ONDEMAND_CHUNK_LENGTH_BYTES) {
						chunkSize = FileServerConstants.MAX_ONDEMAND_CHUNK_LENGTH_BYTES;
					}

					//Create a buffer containing the file chunk
					byte[] tmp = new byte[chunkSize];
					file.readBytes(tmp, 0, tmp.length);
					ByteBuf chunkData = Unpooled.wrappedBuffer(tmp, 0, chunkSize);

					//Create the outgoing buffer
					ByteBuf buffer = Unpooled.buffer();

					//Write file type
					buffer.writeByte(fileType - 1);

					//Write file id
					buffer.writeMedium(fileId);

					//Write file length
					buffer.writeInt(length);

					//Write file chunk id
					buffer.writeShort(chunk);

					//Write the actual file chunk
					buffer.writeBytes(chunkData);

					//Send the outgoing buffer
					ctx.writeAndFlush(buffer);
				}
			} else {
				ctx.close();
			}
		}
	}
}
