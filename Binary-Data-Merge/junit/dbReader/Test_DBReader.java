package dbReader;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Test;

import taqDBReaders.ReadGZippedTAQTradesFile;

public class Test_DBReader {

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
	public void test_ReadChunk() throws IOException {
		
		// pre-process
		PreProcessor preP = new PreProcessor(INPUT_DIR, OUTPUT_DIR);
		preP.run();
		
		// read file using ReadGZippedTAQTradesFile
		ReadGZippedTAQTradesFile ibm_origin = new ReadGZippedTAQTradesFile(FILES.get("IBM"));
		int nRec = ibm_origin.getNRecs();
		// get the maximum time sequence
		long maxSequence = 
				ibm_origin.getSecsFromEpoch() * 1000L + ibm_origin.getMillisecondsFromMidnight(nRec-1);
		
		// read transformed file using DBReader
		DBReader ibm_new = new DBReader(preP.getOutputMap().get("IBM"));
		ibm_new.readChunk(maxSequence);
		
		// entry-by-entry comparison
		for (int i=0; i<nRec; i++) {
			assertEquals(ibm_origin.getSize(i), ibm_new.getSize(i));
		}
		
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
