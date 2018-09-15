package dbReader;

import static org.junit.Assert.*;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.zip.GZIPInputStream;

import org.junit.Test;

public class Test_PreProcessor {

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
	public void test_constructor() {
		
		PreProcessor preP = new PreProcessor(INPUT_DIR, OUTPUT_DIR);
		assertEquals(INPUT_DIR, preP.getInputPath());
	}

	@Test
	public void test_makeMap() throws IOException {
		
		PreProcessor preP = new PreProcessor(INPUT_DIR, OUTPUT_DIR);
		preP.makeMap();
		/*
		for (String ticker: preP.getNameMap().keySet()) {
			System.out.println(preP.getNameMap().get(ticker));
			System.out.println(ticker);
		} */
		//  Test successful
	}
	
	@Test
	public void test_rewrite() throws IOException {
		
		PreProcessor preP = new PreProcessor(INPUT_DIR, OUTPUT_DIR);
		preP.run();
		
		// entry-by-entry comparison		
		HashMap<String, String> files_generated = preP.getOutputMap();
		
		for (String ticker: files_generated.keySet()) {
			
			// read original data
			FileInputStream FIS_original = new FileInputStream(FILES.get(ticker));
			InputStream in_original = new GZIPInputStream(FIS_original);
			DataInputStream DIS_original = new DataInputStream(in_original);
			DIS_original.readInt();				// skip epoch time
			int nRec = DIS_original.readInt();	// skip # of records
			for (int i=0; i<nRec; i++) {		// skip time
				DIS_original.readInt();
			}

			// read generated data
			FileInputStream FIS_generated = new FileInputStream(files_generated.get(ticker));
			InputStream in_generated = new GZIPInputStream(FIS_generated);
			DataInputStream DIS_generated = new DataInputStream(in_generated);
			
			// comparison
			for (int i=0; i<nRec; i++) {
				DIS_generated.readLong();			// skip time
				DIS_generated.readUnsignedShort();	// skip stockId
				assertEquals(DIS_original.readInt(), DIS_generated.readInt());
				DIS_generated.readFloat();			// skip price
			}

			DIS_original.close();
			DIS_generated.close();
		}
		
		//this.clearFolder();
	}
	
	public void clearFolder() {
		File folder = new File(OUTPUT_DIR);
		File[] fLst = folder.listFiles();
		for (File f: fLst) {
			f.delete();
		}
	}
}
