package org.wp.homology;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.bridgedb.DataSource;
import org.bridgedb.IDMapper;
import org.bridgedb.IDMapperException;
import org.bridgedb.Xref;
import org.bridgedb.bio.Organism;
import org.pathvisio.core.model.ConverterException;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.core.view.MIMShapes;

public class HomologyMapper {

	public static void main(String[] args) {
		
		try {
			Properties props = readConfigFile(new File("mapper.config"));
			HomologyMapper mapper = new HomologyMapper(props);
			mapper.convertPathways();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IDMapperException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private static Properties readConfigFile(File file) throws FileNotFoundException, IOException {
		Properties props = new Properties();
		props.load(new FileReader(file));
		
		return props;	
	}

	private Organism source;
	private Organism target;
	private DataSource dsSource;
	private DataSource dsTarget;
	private File outputDir;
	private File mappingFile;
	private File geneNameFile;
	private IDMapper geneMapper;
	private String webserviceUrl;
	private File logFile;
	
	public HomologyMapper(Properties props) throws ClassNotFoundException, IDMapperException {
		source = Organism.fromLatinName(props.getProperty("source.organism"));
		target = Organism.fromLatinName(props.getProperty("target.organism"));

		outputDir = new File(props.getProperty("output.dir"));
		mappingFile = new File(props.getProperty("mapping.file"));
		geneNameFile = new File(props.getProperty("new.gene.names"));
		
		File bridgedb = new File(props.getProperty("source.gene.mapping.db"));
		geneMapper = Utils.setUpIDMapper(bridgedb);
		
		dsSource = DataSource.getExistingBySystemCode(props.getProperty("ds.source"));
		dsTarget = DataSource.getExistingBySystemCode(props.getProperty("ds.target"));

		webserviceUrl = props.getProperty("webservice.url");
		logFile = new File(props.getProperty("log.file"));
		
		MIMShapes.registerShapes();
	}
	
	public void convertPathways() {
		try {
			Map<Xref,Xref> map = MappingFileReader.readMappingFile(mappingFile, dsSource, dsTarget);
			Map<String, String> geneNames = MappingFileReader.readGeneNameFile(geneNameFile);
			Map<String, Pathway> pathways = PathwayReader.readPathways(source, webserviceUrl);
			
			List<Report> reports = new ArrayList<Report>();
			
			for(String p : pathways.keySet()) {
				Pathway pathway = pathways.get(p);
				String [] buffer = p.split(":");
				Report r = PathwayConverter.convertPathway(pathway, buffer[0], buffer[1], map, target, geneMapper, dsSource, geneNames);
				if(r != null) reports.add(r);
			}
			
			List<Report> pathwaysWithErrors = new ArrayList<Report>();
			for(Report r : reports) {
				
				try {
					File outputFile = new File(outputDir, r.getFileName());
					r.getNewPathway().writeToXml(outputFile, true);
					
				} catch(Exception e) {
					pathwaysWithErrors.add(r);
				}
			}
			
			
			
			for(Report r : pathwaysWithErrors) {
				System.out.println("Remove: " + r.getName());
				reports.remove(r);
			}
			
			BufferedWriter writer = new BufferedWriter(new FileWriter(logFile));
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
				writer.write(r.getName() + " (" + r.getWpId() + ":" + r.getRevision() + ")" + "\t" + 
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: " + e.getMessage());
//			e.printStackTrace();
		} catch (ConverterException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR: " + e.getMessage());
//			e.printStackTrace();
		}
	}
}
