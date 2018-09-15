package dbReader;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;

import dbReaderFramework.*;

public class DBReader implements I_DBReader {

	private final DataInputStream _dataStream;
	private int _nRecsRead = 0;    // Number of records read in last read
	private boolean _isFinished = false;
	private long _lastSequenceNumberRead;
	private long _currSequenceNumber;

	private ArrayList<Record> _records = new ArrayList<>();
	
	// getter & setter
	public int getNRecsRead() {return _nRecsRead;}
	@Override public boolean isFinished() {return _isFinished;}
	@Override public long getLastSequenceNumberRead()  {return _lastSequenceNumberRead;}
	@Override public long getSequenceNumber() {return _currSequenceNumber;}	// get current sequence number
	public ArrayList<Record> getRecords() {return _records;}
	
	public long getSequenceNum(int i) {return _records.get(i).getSecFromEpoch();}	// get the sequence number of the i-th entry read
	public int getStockId(int i) {return _records.get(i).getStockId();}
	public int getSize(int i) {return _records.get(i).getSize();}
	public float getPrice(int i) {return _records.get(i).getPrice();}
	
	@Override public void stop() {
		_isFinished = true; 
		try {
			_dataStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// constructor
	/**
	 * @param filePath
	 * @throws IOException
	 */
	public DBReader(String filePath) throws IOException {
		
		FileInputStream fileInputStream = new FileInputStream(filePath);
		InputStream in = new GZIPInputStream(fileInputStream);
		_dataStream = new DataInputStream(in);
		
		_currSequenceNumber = _dataStream.readLong();
	}
	
	/**
	 * read file up to a certain time sequence
	 */
	@Override
	public int readChunk(long sequenceNum) {
		
		// clear up data from the last reading
		_nRecsRead = 0;
		_records.clear();
		
		while (!_isFinished && _currSequenceNumber <= sequenceNum) {
			// keep reading while not end of the file
			// and keep reading while the time is still within the frame
			
			try {
				_nRecsRead ++;

				Record rec = new Record(_currSequenceNumber,
						_dataStream.readUnsignedShort(),
						_dataStream.readInt(),
						_dataStream.readFloat());
				_records.add(rec);
				_currSequenceNumber = _dataStream.readLong();
			} catch (EOFException e) {
				// if we reach end of file, close file
				_currSequenceNumber = Long.MAX_VALUE;
				_isFinished = true;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			if (_isFinished) {_dataStream.close();}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		_lastSequenceNumberRead = sequenceNum;
		
		return _nRecsRead;	// number of records read
	}
	
	private class Record {

		private final long _secondsFromTheEpoch;
		private final int _stockId;
		private final int _size;
		private final float _price;
		
		// getter & setter
		public long getSecFromEpoch() {return _secondsFromTheEpoch;}
		public int getStockId() {return _stockId;}
		public int getSize() {return _size;}
		public float getPrice() {return _price;}
		
		// constructor
		public Record(long secondsFromTheEpoch, int stockId, int size, float price) {
			_secondsFromTheEpoch = secondsFromTheEpoch;
			_stockId = stockId;
			_size = size;
			_price = price;
		}
	}
}
