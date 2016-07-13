package org.wp.homology;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

import org.bridgedb.bio.Organism;
import org.pathvisio.core.model.ConverterException;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.wikipathways.webservice.WSCurationTag;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.pathvisio.wikipathways.webservice.WSPathwayInfo;
import org.wikipathways.client.WikiPathwaysClient;

public class PathwayReader {

	public static Map<String, Pathway> readPathways(Organism organism, String webserviceUrl) throws MalformedURLException, RemoteException, ConverterException {
		
		Map<String, Pathway> pathways = new HashMap<String, Pathway>();
		WikiPathwaysClient client = new WikiPathwaysClient(new URL(webserviceUrl));
		
		WSPathwayInfo[] wsPahways = client.listPathways(organism);
		
		for(WSPathwayInfo info : wsPahways) {
			WSCurationTag [] tags = client.getCurationTags(info.getId());
			for(WSCurationTag tag : tags) {
				if(tag.getName().equals("Curation:AnalysisCollection")) {
					WSPathway p = client.getPathway(info.getId());
					Pathway pathway = WikiPathwaysClient.toPathway(p);
					pathways.put(info.getId() + ":" + info.getRevision(), pathway);
				}
			}
		}
		
		return pathways;
	}
	
	public static Map<String, Pathway> readPathways(Organism organism, File inputDir) {
		Map<String, Pathway> pathways = new HashMap<String, Pathway>();		
		
		for(File f : inputDir.listFiles()) {
			if(f.getName().endsWith(".gpml")) {
				Pathway pathway = new Pathway();
				try {
					pathway.readFromXml(f, true);
				} catch (ConverterException e) {
					System.out.println("Could not parse pathway: " + f.getName());
				}
				pathways.put(f.getName(), pathway);
			}
		}
		
		return pathways;
	}
}
