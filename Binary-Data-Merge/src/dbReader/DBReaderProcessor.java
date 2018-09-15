package dbReader;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import dbReaderFramework.*;
import taqDBReaders.GZFileUtils;

/**
 * DBProcessor that merges data from a list of DBReaders
 * @author Penghao Chen
 *
 */
public class DBReaderProcessor implements I_DBProcessor {

	private final DataOutputStream _outStream;
	
	// constructor
	/**
	 * @param outFilePath	output file path and name
	 * @throws IOException
	 */
	public DBReaderProcessor(String outFilePath) throws IOException {
		_outStream = GZFileUtils.getGZippedFileOutputStream(outFilePath);
	}
	
	/**
	 * merge together the data of several readers
	 */
	@Override
	public boolean processReaders(
			long sequenceNumber,
			int numReadersWithNewData,
			LinkedList<I_DBReader> readers) {
		
		for (I_DBReader reader: readers) {
			
			if (!(reader instanceof DBReader)) {return false;}
			// we are primarily dealing with DBReader
			// but this forced casting is really, really really bad design
			// We need to modify the interface for more elegant design
			
			DBReader dbReader = (DBReader) reader;
			
			if (reader.getLastSequenceNumberRead() == sequenceNumber) {
				int nRecs = dbReader.getNRecsRead();
				for (int i=0; i<nRecs; i++) {
					try {
						_outStream.writeLong(dbReader.getSequenceNum(i));
						_outStream.writeShort(dbReader.getStockId(i));
						_outStream.writeInt(dbReader.getSize(i));
						_outStream.writeFloat(dbReader.getPrice(i));
					} catch (IOException e) {
						return false;
					}
				}
			}
		}		
		return true;
	}

	/**
	 * create file & close the OutputStream
	 */
	@Override
	public void stop() throws Exception {
		_outStream.flush();
		_outStream.close();
	}
}
