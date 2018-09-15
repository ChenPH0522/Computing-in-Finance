package dbReader;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

import org.junit.Test;

import dbReaderFramework.*;
import taqDBReaders.*;

public class Test_DBReaderProcessor {
	
	private static final String INPUT_DIR = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing in Finance\\MERGE HW\\sampleTAQ\\trades\\20070620";
	private static final String OUTPUT_DIR = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing in Finance\\MERGE HW\\outputFile";
	private static final HashMap<String, String> FILES = new HashMap<>();
	static {
		FILES.put("IBM", INPUT_DIR + "\\IBM_trades.binRT");
		FILES.put("MSFT", INPUT_DIR + "\\MSFT_trades.binRT");
		FILES.put("TICKA", INPUT_DIR + "\\TICKA_trades.binRT");
		FILES.put("TICKB", INPUT_DIR + "\\TICKB_trades.binRT");
		FILES.put("TICKC", INPUT_DIR + "\\TICKC_trades.binRT");
	}
	
	@Test
	public void test_DBReaderProcessor() throws Exception {
		
		// read the original IBM & MSFT files
		ReadGZippedTAQTradesFile ibm_original = new ReadGZippedTAQTradesFile(FILES.get("IBM"));
		ReadGZippedTAQTradesFile msft_original = new ReadGZippedTAQTradesFile(FILES.get("MSFT"));
		// get number of records
		int nRecs_IBM = ibm_original.getNRecs();
		int nRecs_MSFT = msft_original.getNRecs();
		// get last times
		long ibm_lastTime = ibm_original.getSecsFromEpoch() * 1000L + ibm_original.getMillisecondsFromMidnight(nRecs_IBM-1);
		long msft_lastTime = msft_original.getSecsFromEpoch() * 1000L + msft_original.getMillisecondsFromMidnight(nRecs_MSFT-1);
		long lastTime = Math.max(ibm_lastTime, msft_lastTime);
		
		// pre-process - generate new IBM & MSFT files
		PreProcessor preP = new PreProcessor(INPUT_DIR, OUTPUT_DIR);
		preP.run();

		// re-read the newly generated files
		String ibm_newPath = preP.getOutputMap().get("IBM");
		String msft_newPath = preP.getOutputMap().get("MSFT");
		DBReader ibm_reader = new DBReader(ibm_newPath);
		DBReader msft_reader = new DBReader(msft_newPath);
		ibm_reader.readChunk(ibm_lastTime);		// read entire file
		msft_reader.readChunk(msft_lastTime);	// read entire file
		
		// merge newly generated IBM & MSFT
		LinkedList<I_DBReader> readers = new LinkedList<>();
		readers.add(ibm_reader);
		readers.add(msft_reader);
		int n = 2;	// number of readers with new data
		String outPath = OUTPUT_DIR + "\\output.binRT";
		DBReaderProcessor merger = new DBReaderProcessor(outPath);
		merger.processReaders(lastTime, n, readers);
		merger.stop();
		
		// re-read the entire merged files
		DBReader merged = new DBReader(outPath);
		merged.readChunk(lastTime);

		// check if the number of entries match the sum of IBM & MSFT entries
		assertEquals(nRecs_IBM + nRecs_MSFT, merged.getRecords().size());
		
		this.clearFolder();
	}
	
	public void clearFolder() {
		File folder = new File(OUTPUT_DIR);
		File[] fLst = folder.listFiles();
		for (File f: fLst) {
			f.delete();
		}
	}
}
