package dbReader;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import taqDBReaders.*;

public class PreProcessor {

	private final int _MaxInt = 32768;	// 2-byte or 16-bit number limit, unsigned (exclusive)	
	private HashMap<String, Integer> _nameMap = new HashMap<>();		// ticker: code
	private Set<Integer> _codeSet = new HashSet<Integer>();	// codes
	private HashMap<String, String> _outputMap = new HashMap<>();		// ticker: pathname
	private File _infolder;		// input folder
	private File _outFolder;	// output folder
	private String _filterExtension = "binRT";
	
	// getter & setter
	public HashMap<String, Integer> getNameMap() {return _nameMap;}
	public Set<Integer> getCodeSet() {return _codeSet;}
	public HashMap<String, String> getOutputMap() {return _outputMap;}
	public String getInputPath() {return _infolder.getPath();}
	public String getOutputPath() {return _outFolder.getPath();}
	public String getFilter() {return _filterExtension;}
	public void setInputPath(String inPath) {_infolder = new File(inPath);}
	public void setOutputPath(String outPath) {_outFolder = new File(outPath);}
	public void setFilter(String filter) {_filterExtension = filter;}
	
	// constructor
	/**
	 * @param inPath	input folder path
	 * @param outPath	output folder path
	 */
	public PreProcessor(String inPath, String outPath) {
		_infolder = new File(inPath);
		_outFolder = new File(outPath);
	}
	
	/**
	 * Make a String-Integer code map
	 */
	protected void makeMap() throws IOException {
		
		File[] fileList = _infolder.listFiles();
		Random r = new Random();
		
		for (File f: fileList) {
			
			// read only *.binRT files
			String[] filename = f.getName().split("_|\\.");
			
			if (filename[2].equals(_filterExtension)) {
				
				// get the ticker
				String ticker = filename[0];
				
				if (!_nameMap.containsKey(ticker)) {	// if ticker not in name map:
					
					int rnd = r.nextInt(_MaxInt);	// generate random two_byte number
					while (_codeSet.contains(rnd)) {
						// keep updating the two_byte number until it's not yet used
						rnd = r.nextInt(_MaxInt);
					}
					
					_nameMap.put(ticker, rnd);
					_codeSet.add(rnd);
				}
			}
		}
	}
	
	/**
	 * rewrite files to a new location, in a new format
	 * @throws IOException
	 */
	protected void rewrite() throws IOException {
		
		File[] fileList = _infolder.listFiles();
		
		for (File f: fileList) {
			
			String ticker = f.getName().split("_")[0];
			String filePath = 
					_outFolder.getPath() + "\\" + String.format("%05d", _nameMap.get(ticker)) + "_1.dat";
			_outputMap.put(ticker, filePath);
			DataOutputStream outputStream = GZFileUtils.getGZippedFileOutputStream(filePath);
			
			ReadGZippedTAQTradesFile taq = new ReadGZippedTAQTradesFile(f.getPath());
			
			for (int i=0; i<taq.getNRecs(); i++) {
				outputStream.writeLong(taq.getSecsFromEpoch() * 1000L + taq.getMillisecondsFromMidnight(i));
				outputStream.writeShort(_nameMap.get(f.getName().split("_")[0]));
				outputStream.writeInt(taq.getSize(i));
				outputStream.writeFloat(taq.getPrice(i));
			}
			
			outputStream.flush();
			outputStream.close();
		}
	}
	
	protected void saveStockCodes() throws FileNotFoundException, UnsupportedEncodingException {
		
		PrintWriter writer = new PrintWriter(_outFolder + "\\StockCodes.txt", "UTF-8");
		
		for (String ticker: _nameMap.keySet()) {
			writer.println(ticker + ": " + String.format("%05d", _nameMap.get(ticker)));
		}
		writer.close();
	}
	
	public void run() throws IOException {
		this.makeMap();
		this.rewrite();
		this.saveStockCodes();
	}
}
