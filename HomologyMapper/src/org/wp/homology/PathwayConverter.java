package org.wp.homology;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bridgedb.DataSource;
import org.bridgedb.IDMapper;
import org.bridgedb.IDMapperException;
import org.bridgedb.Xref;
import org.bridgedb.bio.Organism;
import org.pathvisio.core.model.ObjectType;
import org.pathvisio.core.model.Pathway;
import org.pathvisio.core.model.PathwayElement;

public class PathwayConverter {

	public static Report convertPathway(Pathway p, String wpId, String revision, Map<Xref,Xref> mapping, Organism newOrg, IDMapper mapper, DataSource dsSource, Map<String, String> geneNames) {
	
		String oldOrg = p.getMappInfo().getOrganism();
		try {
			Pathway newP = p.clone();
			newP.getMappInfo().setOrganism(newOrg.latinName());
			
			Report report = new Report(p.getMappInfo().getMapInfoName(), p.getMappInfo().getMapInfoName().replace(" ", "_").replace("/", "_")+".gpml");
			report.setNewPathway(newP);
			report.setWpId(wpId);
			report.setRevision(revision);
			
			for(PathwayElement e : newP.getDataObjects()) {
				if(e.getObjectType().equals(ObjectType.DATANODE) && 
						(e.getDataNodeType().equals("GeneProduct") ||
						e.getDataNodeType().equals("Protein") ||
						e.getDataNodeType().equals("Rna") ||
						e.getDataNodeType().equals("Other"))) {
					report.countTotalGenes++;
					report.getTotalGenes().add(printGene(e));
					
					if(e.getXref() != null && !e.getXref().getId().equals("") && e.getXref().getDataSource() != null) {
						if(mapping.containsKey(e.getXref())) {
							e.addComment("Homology Mapping from " + oldOrg + " to " + newOrg.latinName() + ": Original ID = " + e.getXref(), "HomologyMapper");
							Xref newX = mapping.get(e.getXref());
							e.setElementID(newX.getId());
							e.setDataSource(newX.getDataSource());
							if(geneNames.containsKey(newX.getId())) {
								e.setTextLabel(geneNames.get(newX.getId()));
							}
							report.countMappedGenes++;
							report.getMappedGenes().add(printGene(e));
						} else {
							try {
								Set<Xref> res = mapper.mapID(e.getXref(), dsSource);
								
								if(res.size() == 0) {
									report.getNoMappingGenes().add(printGene(e));
									report.countNoMapping++;
									e.setElementID("");
									e.setDataSource(null);
								} else {
									List<Xref> mappedXrefs = new ArrayList<Xref>();
									for(Xref x : res) {
										if(mapping.containsKey(x)) {
											if(!mappedXrefs.contains(x)) mappedXrefs.add(x);
										}
									}
		
									if(mappedXrefs.size() == 1) {
										e.addComment("Homology Mapping from " + oldOrg + " to " + newOrg.latinName() + ": Original ID = " + e.getXref(), "HomologyMapper");
										Xref newX = mapping.get(mappedXrefs.get(0));
										e.setElementID(newX.getId());
										e.setDataSource(newX.getDataSource());
										if(geneNames.containsKey(newX.getId())) {
											e.setTextLabel(geneNames.get(newX.getId()));
										}
										report.countMappedGenes++;
										report.getMappedGenes().add(printGene(e));
									} else if(mappedXrefs.size() > 1) {
										String comment = "Multiple homologues found: ";
										for(Xref x : mappedXrefs) {
											comment = comment + mapping.get(x) + ";";
										}
										e.addComment(comment, "HomologyMapper");
										report.getMultiMappingGenes().add(printGene(e));
										report.countMultiMapping++;
										e.setElementID("");
										e.setDataSource(null);
									} else {
										report.getNoHomologyMappingGenes().add(printGene(e));
										report.countNoHomologyMapping++;
										String comment = "No homologues found for original id " + e.getXref();
										e.addComment(comment, "HomologyMapper");
										e.setElementID("");
										e.setDataSource(null);
									}
								}
							} catch (IDMapperException e1) {
								// TODO Auto-generated catch block
								System.out.println("ERROR\t" + e1.getMessage());
							}
							
						}
					} else {
						report.getNoXrefGenes().add(printGene(e));
						report.countNoXref++;
					}
				}
			}
			newP.getMappInfo().addComment("This pathway was inferred from " + oldOrg + " pathway [http://wikipathways.org/instance/"+ wpId + "_r" + revision + " " + wpId + "_" + revision +"] with a " + report.getHomologyScore() + "% conversion rate.","HomologyMapper");
			return report;
		} catch(Exception e) {
			
		}
		
		
//		System.out.println("\n===================================\n");
//		System.out.println(newP.getMappInfo().getMapInfoName() + "\t" + report.getHomologyScore());
//		System.out.println("IDs mapped: " + report.countMappedGenes + "\n" + report.getMappedGenes() + "\n");
//		System.out.println("IDs with multi mapping: " + report.countMultiMapping + "\n" + report.getMultiMappingGenes() + "\n");
//		System.out.println("IDs with no homology mapping: " + report.countNoHomologyMapping + "\n" + report.getNoHomologyMappingGenes() + "\n");
//
//		System.out.println("IDs with no mapping: " + report.countNoMapping + "\n" + report.getNoMappingGenes() + "\n");
//		System.out.println("Datanodes without Xref: " + report.countNoXref + "\n" + report.getNoXrefGenes() + "\n");
		
		return null;
	}
	
	private static String printGene(PathwayElement e) {
		String gene = e.getGraphId() + " : ";
		gene = gene + e.getTextLabel().replace("\n", " ").replace("\t", "") + " : ";
		gene = gene + e.getXref();
		return gene;
	}
	
	public static Report convertPathway(Pathway p, String source, Map<Xref,Xref> mapping, Organism newOrg, IDMapper mapper, DataSource dsSource, Map<String, String> geneNames) {
		
		String oldOrg = p.getMappInfo().getOrganism();
		try {
			Pathway newP = p.clone();
			newP.getMappInfo().setOrganism(newOrg.latinName());
			
			Report report = new Report(p.getMappInfo().getMapInfoName(), p.getMappInfo().getMapInfoName().replace(" ", "_").replace("/", "_")+".gpml");
			report.setNewPathway(newP);
			report.setSource(source);
			
			for(PathwayElement e : newP.getDataObjects()) {
				if(e.getObjectType().equals(ObjectType.DATANODE) && 
						(e.getDataNodeType().equals("GeneProduct") ||
						e.getDataNodeType().equals("Protein") ||
						e.getDataNodeType().equals("Rna") ||
						e.getDataNodeType().equals("Other"))) {
					report.countTotalGenes++;
					report.getTotalGenes().add(printGene(e));
					
					if(e.getXref() != null && !e.getXref().getId().equals("") && e.getXref().getDataSource() != null) {
						if(mapping.containsKey(e.getXref())) {
							e.addComment("Homology Mapping from " + oldOrg + " to " + newOrg.latinName() + ": Original ID = " + e.getXref(), "HomologyMapper");
							Xref newX = mapping.get(e.getXref());
							e.setElementID(newX.getId());
							e.setDataSource(newX.getDataSource());
							if(geneNames.containsKey(newX.getId())) {
								e.setTextLabel(geneNames.get(newX.getId()));
							}
							report.countMappedGenes++;
							report.getMappedGenes().add(printGene(e));
						} else {
							try {
								Set<Xref> res = mapper.mapID(e.getXref(), dsSource);
								
								if(res.size() == 0) {
									report.getNoMappingGenes().add(printGene(e));
									report.countNoMapping++;
									e.setElementID("");
									e.setDataSource(null);
								} else {
									List<Xref> mappedXrefs = new ArrayList<Xref>();
									for(Xref x : res) {
										if(mapping.containsKey(x)) {
											if(!mappedXrefs.contains(x)) mappedXrefs.add(x);
										}
									}
		
									if(mappedXrefs.size() == 1) {
										e.addComment("Homology Mapping from " + oldOrg + " to " + newOrg.latinName() + ": Original ID = " + e.getXref(), "HomologyMapper");
										Xref newX = mapping.get(mappedXrefs.get(0));
										e.setElementID(newX.getId());
										e.setDataSource(newX.getDataSource());
										if(geneNames.containsKey(newX.getId())) {
											e.setTextLabel(geneNames.get(newX.getId()));
										}
										report.countMappedGenes++;
										report.getMappedGenes().add(printGene(e));
									} else if(mappedXrefs.size() > 1) {
										String comment = "Multiple homologues found: ";
										for(Xref x : mappedXrefs) {
											comment = comment + mapping.get(x) + ";";
										}
										e.addComment(comment, "HomologyMapper");
										report.getMultiMappingGenes().add(printGene(e));
										report.countMultiMapping++;
										e.setElementID("");
										e.setDataSource(null);
									} else {
										report.getNoHomologyMappingGenes().add(printGene(e));
										report.countNoHomologyMapping++;
										String comment = "No homologues found for original id " + e.getXref();
										e.addComment(comment, "HomologyMapper");
										e.setElementID("");
										e.setDataSource(null);
										
									}
								}
							} catch (IDMapperException e1) {
								// TODO Auto-generated catch block
								System.out.println("ERROR\t" + e1.getMessage());
							}
							
						}
					} else {
						report.getNoXrefGenes().add(printGene(e));
						report.countNoXref++;
					}
				}
			}
			newP.getMappInfo().addComment("This pathway was inferred from " + oldOrg + " pathway ["+ source + "] with a " + report.getHomologyScore() + "% conversion rate.","HomologyMapper");
			return report;
		} catch(Exception e) {
			
		}
		
		
//		System.out.println("\n===================================\n");
//		System.out.println(newP.getMappInfo().getMapInfoName() + "\t" + report.getHomologyScore());
//		System.out.println("IDs mapped: " + report.countMappedGenes + "\n" + report.getMappedGenes() + "\n");
//		System.out.println("IDs with multi mapping: " + report.countMultiMapping + "\n" + report.getMultiMappingGenes() + "\n");
//		System.out.println("IDs with no homology mapping: " + report.countNoHomologyMapping + "\n" + report.getNoHomologyMappingGenes() + "\n");
//
//		System.out.println("IDs with no mapping: " + report.countNoMapping + "\n" + report.getNoMappingGenes() + "\n");
//		System.out.println("Datanodes without Xref: " + report.countNoXref + "\n" + report.getNoXrefGenes() + "\n");
		
		return null;
	}
}
