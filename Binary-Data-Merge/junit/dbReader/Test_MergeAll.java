package dbReader;

import static org.junit.Assert.*;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.junit.Test;

import dbReaderFramework.*;
import taqDBReaders.ReadGZippedTAQTradesFile;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Test_MergeAll {

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
	public void test_a_UtilMethods() throws Exception {
		
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
		
		// pre-process: generate several *.binRT files
		PreProcessor preP = new PreProcessor(INPUT_DIR, OUTPUT_DIR);
		preP.run();
		
		// transform the output file array to set
		File outfolder = new File(OUTPUT_DIR);
		File[] flst_outside = outfolder.listFiles();
		HashSet<File> fset_outside = new HashSet<>();
		for (File f: flst_outside) {
			if (f.getName().split("\\.")[1].equals("dat")) {
				fset_outside.add(f);
			}
		}
		
		// create a random txt file
		PrintWriter writer = new PrintWriter(OUTPUT_DIR + "\\somefile.txt", "UTF-8");
		writer.write("Something");
		writer.close();
		
		// create MergeAll
		String extension = "dat";
		int wrapSize = 2;
		MergeAll mergeAll = new MergeAll(OUTPUT_DIR, extension, wrapSize);
		
		// transform the File ArrayList under MergeAll to set		
		LinkedList<File> flst_merge = mergeAll.getFileList();
		HashSet<File> fset_merge = new HashSet<>();
		for (File f: flst_merge) {
			fset_merge.add(f);
		}

		// cross check:
		// every element in the first map should be in the second map
		// every element in the second map should be in the first map
		for (File f: fset_outside) {
			assertTrue(fset_merge.contains(f));
		}
		
		for (File f: fset_merge) {
			assertTrue(fset_outside.contains(f));
		}

		// Read 1st generation. File list length should be 5
		mergeAll.readGeneration(1);
		assertEquals(5, mergeAll.getFileList().size());
		
		// generate 2nd generation files:

		// get list of reader lists
		LinkedList<LinkedList<I_DBReader>> lst_readerlst = new LinkedList<>();
		Iterator<File> fIter = mergeAll.getFileList().iterator();
		while (fIter.hasNext()) {
			LinkedList<I_DBReader> readerlst = new LinkedList<>();
			for (int i=0; i<wrapSize; i++) {
				if (fIter.hasNext()) {
					File f = fIter.next();
					DBReader dbReader = new DBReader(f.getPath());
					readerlst.add(dbReader);
				}
			}
			lst_readerlst.add(readerlst);
		}
		assertEquals(3, lst_readerlst.size());	// currently we have 3 grouped DBReader list

		// get list of processor lists
		LinkedList<LinkedList<I_DBProcessor>> lst_processorlst = new LinkedList<>();
		int fileCode = 0;
		int generation = 2;
		for (LinkedList<I_DBReader> _ : lst_readerlst) {
			String outputFile = OUTPUT_DIR + "\\" + fileCode + "_" + generation + ".dat";
			DBReaderProcessor processor = new DBReaderProcessor(outputFile);
			LinkedList<I_DBProcessor> processorlst = new LinkedList<>();
			processorlst.add(processor);
			lst_processorlst.add(processorlst);
			fileCode ++;
		}
		assertEquals(3, lst_processorlst.size());
		
		// merge files
		int l = lst_readerlst.size();
		for (int i=0; i<l; i++) {
			LinkedList<I_DBReader> readerlst = lst_readerlst.poll();
			LinkedList<I_DBProcessor> processorlst = lst_processorlst.poll();
			MergeClock clock = new MergeClock(readerlst, processorlst);
			DBManager dbm = new DBManager(readerlst, processorlst, clock);
			dbm.launch();
		}
		
		// clear 1st generation files
		mergeAll.clearCurrentGeneration();
		
		// re-read the folder. There should be three 2nd generation files left
		assertEquals(3, mergeAll.readExtension());
		
		// compare the total number of entries
		int entryCount = 0;
		for (File f: mergeAll.getFileList()) {
			DBReader reader = new DBReader(f.getPath());
			entryCount += reader.readChunk(lastTime);
		}
		assertEquals(nRecs_IBM*3 + nRecs_MSFT*2, entryCount);
		
		this.clearFolder();
	}

	@Test
	public void test_b_run() throws Exception {
		
		PreProcessor preP = new PreProcessor(INPUT_DIR, OUTPUT_DIR);
		preP.run();
		
		int wrapSize = 2;
		String extension = "dat";
		int startGen = 1;	// starting generation
		MergeAll merge = new MergeAll(OUTPUT_DIR, extension, wrapSize);
		merge.run(startGen);
		
		// read the original IBM & MSFT files
		ReadGZippedTAQTradesFile ibm_original = new ReadGZippedTAQTradesFile(FILES.get("IBM"));
		ReadGZippedTAQTradesFile msft_original = new ReadGZippedTAQTradesFile(FILES.get("MSFT"));
		// get number of records
		int nRecs_IBM = ibm_original.getNRecs();
		int nRecs_MSFT = msft_original.getNRecs();
		int total = nRecs_IBM*3 + nRecs_MSFT*2;
		// get last times
		long ibm_lastTime = ibm_original.getSecsFromEpoch() * 1000L + ibm_original.getMillisecondsFromMidnight(nRecs_IBM-1);
		long msft_lastTime = msft_original.getSecsFromEpoch() * 1000L + msft_original.getMillisecondsFromMidnight(nRecs_MSFT-1);
		long lastTime = Math.max(ibm_lastTime, msft_lastTime);
		
		// get the merged output file
		merge.readExtension();
		File f = merge.getFileList().peek();
		DBReader dbReader = new DBReader(f.getPath());
		dbReader.readChunk(lastTime);
		assertTrue(dbReader.isFinished());
		
		// check entry number
		assertEquals(total, dbReader.getNRecsRead());
		
		// check time stamp
		long lastTimeStamp = 0;
		for (int i=0; i<total; i++) {
			assertTrue(dbReader.getSequenceNum(i)>=lastTimeStamp);
			lastTimeStamp = dbReader.getSequenceNum(i);
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
