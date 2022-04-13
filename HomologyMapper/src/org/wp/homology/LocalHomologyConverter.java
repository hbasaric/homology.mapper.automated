package org.wp.homology;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bridgedb.IDMapperException;
import org.bridgedb.Xref;
import org.bridgedb.bio.Organism;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.core.view.MIMShapes;

public class LocalHomologyConverter {

	public static void main(String[] args) {
		if(args.length ==1) {
			try {
				Configurations conf = new Configurations();
				conf.readConfigFile(new File(args[0]));
				MIMShapes.registerShapes();
				LocalHomologyConverter converter = new LocalHomologyConverter();
				// get target organism list from properties file, iterate through all organisms
				List<Organism> orgList = new ArrayList<Organism>();
				orgList = conf.getTargetOrganisms();
				Integer i = 0;
				for (Organism org : orgList) {
					converter.convertPathways(conf, i);
					i = i+1;
				}
					
			} catch (IOException e) {
				System.out.println("Cannot open config file " + args[0]);
			} catch (ClassNotFoundException e) {
				System.out.println("Cannot set up ID mapper");
			} catch (IDMapperException e) {
				System.out.println("Cannot set up ID mapper, IDMapper Exception");
			}
		}
	}
		
	public void convertPathways(Configurations conf, Integer i) throws IOException {
		
		List<File> mappingFileList = new ArrayList<File>();
		List<File> geneNamesList = new ArrayList<File>();
		List<File> logFilesList = new ArrayList<File>();
		List<File> outputFoldersList = new ArrayList<File>();
		List<Organism> targetOrganismList = new ArrayList<Organism>();
		
		targetOrganismList = conf.getTargetOrganisms();
		mappingFileList = conf.getMappingFileList();
		geneNamesList = conf.getGeneNamesList();
		logFilesList = conf.getLogFilesList();
		outputFoldersList = conf.getOutputFoldersList();
				
		Map<Xref,Xref> map = MappingFileReader.readMappingFile(mappingFileList.get(i), conf.getSystemCodeSource(), conf.getSystemCodeTarget());
		Map<String, String> geneNames = MappingFileReader.readGeneNameFile(geneNamesList.get(i));
		Map<String, Pathway> pathways = PathwayReader.readPathways(conf.getSourceOrganism(), conf.getInputDir());

		List<Report> reports = new ArrayList<Report>();
		
		for(String p : pathways.keySet()) {
			Pathway pathway = pathways.get(p);
			Report r = PathwayConverter.convertPathway(pathway, p, map, targetOrganismList.get(i), conf.getSourceGeneMapper(), 
					conf.getSystemCodeSource(), geneNames);
			if(r != null) reports.add(r);
		}
		
		List<Report> pathwaysWithErrors = new ArrayList<Report>();
		List<Report> pathwaysWithLowScore = new ArrayList<Report>();
		for(Report r : reports) {
			if (r.getHomologyScore() > 80) {
				try {
				   File outputPath = new File(outputFoldersList.get(i)+"/"+r.getSource());
				    outputPath.mkdir();
					File outputFile = new File(outputPath, r.getFileName());
					r.getNewPathway().writeToXml(outputFile, true);
					
				} catch(Exception e) {
					System.out.println(e.getMessage());
					pathwaysWithErrors.add(r);
				}
			}
			else {
				pathwaysWithLowScore.add(r);
			}
		}
		for(Report r : pathwaysWithErrors) {
			System.out.println("Remove: " + r.getName());
			reports.remove(r);
		}
		for(Report r : pathwaysWithLowScore) {
			System.out.println("Remove: " + r.getName());
			reports.remove(r);
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(logFilesList.get(i)));
		writer.write("Pathway\t"
				+ "Homology Score\t"
				+ "Total genes\t"
				+ "Mapped genes\t"
				+ "Multi-mapped genes\t"
				+ "No homology mapping genes\t"
				+ "No mapping genes\t"
				+ "No xref genes\t"
				+ "mapped genes\t"
				+ "multi-mapped genes\t"
				+ "no homology mapping genes\t"
				+ "no mapping genes\t"
				+ "no xref genes\n");
		for(Report r : reports) {
			writer.write(r.getName() + " (" + r.getSource() + ")" + "\t" + 
					r.getHomologyScore() + "\t" + 
					r.getTotalGenes().size() + "\t" + 
					r.getMappedGenes().size() + "\t"  + 
					r.getMultiMappingGenes().size() + "\t" + 
					r.getNoHomologyMappingGenes().size() + "\t" +
					r.getNoMappingGenes().size() + "\t" + 
					r.getNoXrefGenes().size() + "\t" +
					r.getMappedGenes() + "\t" +
					r.getMultiMappingGenes() + "\t" +
					r.getNoHomologyMappingGenes() + "\t" + 
					r.getNoMappingGenes() + "\t" +
					r.getNoXrefGenes()+"\n");
		}
		writer.close();
	}

}
