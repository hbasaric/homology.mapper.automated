package org.wp.homology;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.bridgedb.DataSource;
import org.bridgedb.IDMapper;
import org.bridgedb.IDMapperException;
import org.bridgedb.bio.Organism;

public class Configurations {

	// homology mapper config file
	public static String SOURCE_ORGANISM = "source.organism";
	public static String TARGET_ORGANISM = "target.organism";
	public static String OUTPUT_DIR = "output.dir";
	public static String INPUT_DIR = "input.dir";
	public static String SOURCE_GENE_MAPPING_DB = "source.gene.mapping.db";
	public static String MAPPING_FILE = "mapping.file";
	public static String NEW_GENE_NAMES = "new.gene.names";
			
	public static String SYSTEM_CODE_SOURCE = "system.code.source";
	public static String SYSTEM_CODE_TARGET = "system.code.target";
	
	public static String LOG_FILE = "log.file";
	
	private Organism sourceOrganism;
	private Organism targetOrganism;
	private File outputDir;
	private File inputDir;
	private IDMapper sourceGeneMapper;
	private File mappingFile;
	private File newGeneNames;
			
	private DataSource systemCodeSource;
	private DataSource systemCodeTarget;
	
	private File logFile;
		
	public void readConfigFile(File f) throws FileNotFoundException, IOException, ClassNotFoundException, IDMapperException {
		Properties props = new Properties();
		props.load(new FileReader(f));
		
		sourceOrganism = Organism.fromLatinName(props.getProperty(SOURCE_ORGANISM));
		targetOrganism = Organism.fromLatinName(props.getProperty(TARGET_ORGANISM));
		
		outputDir = new File(props.getProperty(OUTPUT_DIR));
		inputDir = new File(props.getProperty(INPUT_DIR));
		mappingFile = new File(props.getProperty(MAPPING_FILE));
		newGeneNames = new File(props.getProperty(NEW_GENE_NAMES));
		
		File bridgedb = new File(props.getProperty(SOURCE_GENE_MAPPING_DB));
		sourceGeneMapper = Utils.setUpIDMapper(bridgedb);
		
		systemCodeSource = DataSource.getExistingBySystemCode(props.getProperty(SYSTEM_CODE_SOURCE));
		systemCodeTarget = DataSource.getExistingBySystemCode(props.getProperty(SYSTEM_CODE_TARGET));

		logFile = new File(props.getProperty(LOG_FILE));
	}

	public Organism getSourceOrganism() {
		return sourceOrganism;
	}

	public void setSourceOrganism(Organism sourceOrganism) {
		this.sourceOrganism = sourceOrganism;
	}

	public Organism getTargetOrganism() {
		return targetOrganism;
	}

	public void setTargetOrganism(Organism targetOrganism) {
		this.targetOrganism = targetOrganism;
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
}
