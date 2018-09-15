package dbReader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import dbReaderFramework.*;

public class MergeAll{

	private File _folder; 
	private LinkedList<File> _fileList;
	private String _extension;
	private int _wrapSize;
	
	// getter & setter
	public LinkedList<File> getFileList() {return _fileList;}
	public String getExtension() {return _extension;}
	public int getWrapSize() {return _wrapSize;}
	
	// constructor
	/**
	 * @param folderPath Input folder path. Output folder is the same folder.
	 * @param extension	Files with which extension to be read.
	 * @param wrapSize	Merge how many files together per step.
	 */
	public MergeAll(String folderPath, String extension, int wrapSize) {
		
		_folder = new File(folderPath);
		_extension = extension;
		this.readExtension(_extension);
		_wrapSize = wrapSize;

	}
	
	/**
	 * Read files in the sepcified folder with certain extension name.
	 * Made protected for easy test.
	 * @param extension
	 * @return number of files with specified extension
	 */
	protected int readExtension(String extension) {
		
		int counter = 0;
		
		LinkedList<File> fileList = new LinkedList<>();		
		for (File f: _folder.listFiles()) {
			
			try {
				String[] nameComponents = f.getName().split("\\.");
				int l = nameComponents.length;
				if (nameComponents[l-1].equals(extension)) {
					fileList.add(f);
					counter ++;
				}
			} catch (Exception e) {
				continue;
			}
		}
		_fileList = fileList;
		
		return counter;
	}
	
	/**
	 * Read files in the sepcified folder with certain extension name.
	 * Made protected for easy test.
	 * @return number of files with specified extension
	 */
	protected int readExtension() {
		return this.readExtension(_extension);
	}
	
	/**
	 * read a certain generation of merged files.
	 * This is usually called only when there is a need to keep all the merged files
	 * in the intermediate steps.  
	 * Made protected for easy test.
	 * @param generation	file generation to be read
	 * @return
	 */
	protected int readGeneration(int generation) {
		
		int counter = 0;
		Iterator<File> fIter = _fileList.iterator();
		while (fIter.hasNext()) {
			counter ++;
			File f = fIter.next();
			String[] fname = f.getName().split("_|\\.");
			int l = fname.length;
			
			if (!fname[l-2].equals(""+generation)) {
				fIter.remove();
				counter --;
			}
		}
		
		return counter;
	}

	/**
	 * Delete the current files in the file list field.
	 */
	protected void clearCurrentGeneration() {
		for (File f : _fileList) {
			f.delete();
		}
	}
	
	/**
	 * Allocate reader lists to the saved file list, grouped by wrapSize
	 * @return
	 * @throws IOException
	 */
	private LinkedList<LinkedList<I_DBReader>> allocateReader() throws IOException {
		
		LinkedList<LinkedList<I_DBReader>> lst_readerlst = new LinkedList<>();
		Iterator<File> fIter = _fileList.iterator();
		while (fIter.hasNext()) {
			LinkedList<I_DBReader> readerlst = new LinkedList<>();
			for (int i=0; i<_wrapSize; i++) {
				if (fIter.hasNext()) {
					File f = fIter.next();
					DBReader dbReader = new DBReader(f.getPath());
					readerlst.add(dbReader);
				}
			}
			lst_readerlst.add(readerlst);
		}
		return lst_readerlst;
	}
	
	/**
	 * Allocate processors to the list of reader lists
	 * @param generation	Generation of the output files.
	 * @param lst_readerlst
	 * @return
	 * @throws IOException
	 */
	private LinkedList<LinkedList<I_DBProcessor>> allocateProcessor(int generation,
			LinkedList<LinkedList<I_DBReader>> lst_readerlst) throws IOException {
		
		LinkedList<LinkedList<I_DBProcessor>> lst_processorlst = new LinkedList<>();
		int fileCode = 0;
		for (LinkedList<I_DBReader> _: lst_readerlst) {
			String outputFile = _folder.getPath() + "\\" + fileCode + "_" + generation + ".dat";
			DBReaderProcessor processor = new DBReaderProcessor(outputFile);
			LinkedList<I_DBProcessor> processorlst = new LinkedList<>();
			processorlst.add(processor);
			lst_processorlst.add(processorlst);
			fileCode++;
		}
		
		return lst_processorlst;
	}
	
	/**
	 * Iteratively merge files until there is only one file left
	 * @param generation Merge starting from which generation.
	 * @throws Exception
	 */
	public void run(int generation) throws Exception {
		
		if (_wrapSize <= 1) {return;}
		
		while (this.readGeneration(generation)>1) {
			LinkedList<LinkedList<I_DBReader>> lst_readerlst = this.allocateReader();
			LinkedList<LinkedList<I_DBProcessor>> lst_processorlst = 
					this.allocateProcessor(generation+1, lst_readerlst);
			
			int l = lst_readerlst.size();
			
			for (int i=0; i<l; i++) {
				LinkedList<I_DBReader> readerlst = lst_readerlst.poll();
				LinkedList<I_DBProcessor> processorlst = lst_processorlst.poll();
				MergeClock clock = new MergeClock(readerlst, processorlst);
				DBManager dbm = new DBManager(readerlst, processorlst, clock);
				dbm.launch();
			}
			generation++;
			this.clearCurrentGeneration();	// not necessary if need to save results of intermediate steps
			this.readExtension();
		}
	}
	
	public static void main(String[] args) throws Exception {
		
		String INPUT_DIR = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing in Finance\\MERGE HW\\sampleTAQ\\trades\\20070620";
		String OUTPUT_DIR = "C:\\Users\\Penghao Chen\\Desktop\\NYU\\Computing in Finance\\MERGE HW\\outputFile";
		
		PreProcessor preP = new PreProcessor(INPUT_DIR, OUTPUT_DIR);
		preP.run();
		
		int wrapSize = 2;
		String extension = "dat";
		int startGen = 1;	// starting generation
		MergeAll merge = new MergeAll(OUTPUT_DIR, extension, wrapSize);
		merge.run(startGen);
	}
}
