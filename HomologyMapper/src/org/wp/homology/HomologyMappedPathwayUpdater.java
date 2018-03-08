package org.wp.homology;

import java.io.File;
import java.net.URL;
import java.util.Map;

import org.bridgedb.DataSource;
import org.bridgedb.IDMapper;
import org.bridgedb.Xref;
import org.bridgedb.bio.Organism;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.wikipathways.webservice.WSPathway;
import org.pathvisio.wikipathways.webservice.WSPathwayInfo;
import org.wikipathways.client.WikiPathwaysClient;

public class HomologyMappedPathwayUpdater {

	public static void main(String[] args) throws Exception {
		// original pathway
		String pathwayIdSource = "WP176";
		// pathway to overwrite
		String pathwayIdTarget = "WP1075";
		
		Organism orgTarget = Organism.BosTaurus;
		
		File mappingFile = new File("C:/Users/MSK/Downloads/homology-mapper/mart_export_hsa2bta.txt");
		File geneNameFile = new File("C:/Users/MSK/Downloads/homology-mapper/mart_export_bta_gene_names.txt");
		
		File bridgedb = new File("C:/Users/MSK/Data/BridgeDb/Hs_Derby_Ensembl_89.bridge");
		IDMapper sourceGeneMapper = Utils.setUpIDMapper(bridgedb);
		
		DataSource dsSource = DataSource.getExistingBySystemCode("En");
		DataSource dsTarget = DataSource.getExistingBySystemCode("En");
		
		Map<Xref,Xref> map = MappingFileReader.readMappingFile(mappingFile, dsSource, dsTarget);
		Map<String, String> geneNames = MappingFileReader.readGeneNameFile(geneNameFile);

		WikiPathwaysClient client = new WikiPathwaysClient(new URL("https://webservice.wikipathways.org"));
		WSPathway wsPathway = client.getPathway(pathwayIdSource);
		Pathway pathway = WikiPathwaysClient.toPathway(wsPathway);
		Report r = PathwayConverter.convertPathway(pathway, wsPathway.getId(), wsPathway.getRevision(), map, orgTarget, sourceGeneMapper, dsSource, geneNames);
		
		// Initial check --> check local file before updating pathway on WP
		System.out.println(r.getHomologyScore());
		System.out.println(r.getNoMappingGenes().size());
		r.getNewPathway().writeToXml(new File("output.gpml"), false);
		
		// After initial check --> run again and update pathway on WP
//		client.login("", "");
//		WSPathwayInfo i = client.getPathwayInfo(pathwayIdTarget);
//		client.updatePathway(pathwayIdTarget, r.getNewPathway(), "Updated homology conversion", Integer.parseInt(i.getRevision()));
	}

}
