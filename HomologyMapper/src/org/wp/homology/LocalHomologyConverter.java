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
				converter.convertPathways(conf);
			} catch (IOException e) {
				System.out.println("Cannot open config file " + args[0]);
			} catch (ClassNotFoundException e) {
				System.out.println("Cannot set up ID mapper");
			} catch (IDMapperException e) {
				System.out.println("Cannot set up ID mapper, IDMapper Exception");
			}
		}
	}
		
	public void convertPathways(Configurations conf) throws IOException {
		Map<Xref,Xref> map = MappingFileReader.readMappingFile(conf.getMappingFile(), conf.getSystemCodeSource(), conf.getSystemCodeTarget());
		Map<String, String> geneNames = MappingFileReader.readGeneNameFile(conf.getNewGeneNames());
		Map<String, Pathway> pathways = PathwayReader.readPathways(conf.getSourceOrganism(), conf.getInputDir());

		List<Report> reports = new ArrayList<Report>();
		
		for(String p : pathways.keySet()) {
			Pathway pathway = pathways.get(p);
			Report r = PathwayConverter.convertPathway(pathway, p, map, conf.getTargetOrganism(), conf.getSourceGeneMapper(), 
					conf.getSystemCodeSource(), geneNames);
			if(r != null) reports.add(r);
		}
		
		List<Report> pathwaysWithErrors = new ArrayList<Report>();
		for(Report r : reports) {
			
			try {
				File outputFile = new File(conf.getOutputDir(), r.getFileName());
				r.getNewPathway().writeToXml(outputFile, true);
				
			} catch(Exception e) {
				System.out.println(e.getMessage());
				pathwaysWithErrors.add(r);
			}
		}
	
		for(Report r : pathwaysWithErrors) {
			System.out.println("Remove: " + r.getName());
			reports.remove(r);
		}
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(conf.getLogFile()));
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
