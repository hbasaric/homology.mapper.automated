package org.wp.homology;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.bridgedb.DataSource;
import org.bridgedb.IDMapper;
import org.bridgedb.IDMapperException;
import org.bridgedb.bio.Organism;

public class Configurations {

	// homology mapper config file
	public static String SOURCE_ORGANISM = "source.organism";
	public static String TARGET_ORGANISMS = "target.organisms";
	public static String OUTPUT_DIR = "output.dir";
	public static String OUTPUT_FOLDERS = "output.folders";
	public static String INPUT_DIR = "input.dir";
	public static String SOURCE_GENE_MAPPING_DB = "source.gene.mapping.db";
	public static String MAPPING_FILE_DIRECTORY = "mapping.file.directory";
	public static String MAPPING_FILES = "mapping.files";
	public static String NEW_GENE_NAMES_DIRECTORY = "new.gene.names.directory";
	public static String NEW_GENE_NAMES = "new.gene.names";
			
	public static String SYSTEM_CODE_SOURCE = "system.code.source";
	public static String SYSTEM_CODE_TARGET = "system.code.target";
	
	public static String LOG_FILES_DIRECTORY = "log.files.directory";
	public static String LOG_FILES = "log.files";
	
	private Organism sourceOrganism;
	private List<Organism> targetOrganisms = new ArrayList<Organism>();
	private File outputDir;
	private List<File> outputFoldersList = new ArrayList<File>();
	private File inputDir;
	private IDMapper sourceGeneMapper;
	private File mappingFile;
	private List<File> mappingFilesList = new ArrayList<File>();
	private List<File> geneNamesList = new ArrayList<File>();
	private File newGeneNames;
			
	private DataSource systemCodeSource;
	private DataSource systemCodeTarget;
	
	private File logFile;
	private List<File> logFilesList = new ArrayList<File>();
		
	public void readConfigFile(File f) throws FileNotFoundException, IOException, ClassNotFoundException, IDMapperException {
		Properties props = new Properties();
		props.load(new FileReader(f));
		
		sourceOrganism = Organism.fromLatinName(props.getProperty(SOURCE_ORGANISM));
		List<String> targetOrganismsToList = Arrays.asList(props.getProperty(TARGET_ORGANISMS).split("\\s*,\\s*"));
		for (String orgString : targetOrganismsToList) {
			targetOrganisms.add(Organism.fromLatinName(orgString));
		}
				
		outputDir = new File(props.getProperty(OUTPUT_DIR));
		inputDir = new File(props.getProperty(INPUT_DIR));
		
		List<String> mappingFilesToList = Arrays.asList(props.getProperty(MAPPING_FILES).split("\\s*,\\s*"));
		for (String mappingFile : mappingFilesToList) {
			File file = new File(props.getProperty(MAPPING_FILE_DIRECTORY) + mappingFile);
			mappingFilesList.add(file);
		}
		
		List<String> geneNamesToList = Arrays.asList(props.getProperty(NEW_GENE_NAMES).split("\\s*,\\s*"));
		for (String geneNamesFile : geneNamesToList) {
			File file = new File(props.getProperty(NEW_GENE_NAMES_DIRECTORY) + geneNamesFile);
			System.out.println(geneNamesFile);
			geneNamesList.add(file);
		}
		
		List<String> outputFoldersToList = Arrays.asList(props.getProperty(OUTPUT_FOLDERS).split("\\s*,\\s*"));
		for (String outputFolder : outputFoldersToList) {
			File file = new File(props.getProperty(OUTPUT_DIR) + outputFolder);
			outputFoldersList.add(file);
		}

		
		File bridgedb = new File(props.getProperty(SOURCE_GENE_MAPPING_DB));
		sourceGeneMapper = Utils.setUpIDMapper(bridgedb);
		
		systemCodeSource = DataSource.getExistingBySystemCode(props.getProperty(SYSTEM_CODE_SOURCE));
		systemCodeTarget = DataSource.getExistingBySystemCode(props.getProperty(SYSTEM_CODE_TARGET));

		List<String> logFilesToList = Arrays.asList(props.getProperty(LOG_FILES).split("\\s*,\\s*"));
		for (String logFile : logFilesToList) {
			File file = new File(props.getProperty(LOG_FILES_DIRECTORY) + logFile);
			logFilesList.add(file);
		}
	}

	public Organism getSourceOrganism() {
		return sourceOrganism;
	}

	public void setSourceOrganism(Organism sourceOrganism) {
		this.sourceOrganism = sourceOrganism;
	}

	public File getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(File outputDir) {
		this.outputDir = outputDir;
	}

	public File getInputDir() {
		return inputDir;
	}

	public void setInputDir(File inputDir) {
		this.inputDir = inputDir;
	}

	public IDMapper getSourceGeneMapper() {
		return sourceGeneMapper;
	}

	public void setSourceGeneMapper(IDMapper sourceGeneMapper) {
		this.sourceGeneMapper = sourceGeneMapper;
	}

	public File getMappingFile() {
		return mappingFile;
	}

	public void setMappingFile(File mappingFile) {
		this.mappingFile = mappingFile;
	}

	public File getNewGeneNames() {
		return newGeneNames;
	}

	public void setNewGeneNames(File newGeneNames) {
		this.newGeneNames = newGeneNames;
	}

	public DataSource getSystemCodeSource() {
		return systemCodeSource;
	}

	public void setSystemCodeSource(DataSource systemCodeSource) {
		this.systemCodeSource = systemCodeSource;
	}

	public DataSource getSystemCodeTarget() {
		return systemCodeTarget;
	}

	public void setSystemCodeTarget(DataSource systemCodeTarget) {
		this.systemCodeTarget = systemCodeTarget;
	}

	public File getLogFile() {
		return logFile;
	}

	public void setLogFile(File logFile) {
		this.logFile = logFile;
	}

	public void setTargetOrganisms(List<Organism> targetOrganisms) {
		this.targetOrganisms = targetOrganisms;
	}
	
	public List<Organism> getTargetOrganisms() {		
		return targetOrganisms;
	}

	public List<File> getMappingFileList() {
		return mappingFilesList;
	}

	public List<File> getGeneNamesList() {
		return geneNamesList;
	}

	public List<File> getLogFilesList() {
		return logFilesList;
	}
	
	public List<File> getOutputFoldersList() {
		return outputFoldersList;
	}
}
