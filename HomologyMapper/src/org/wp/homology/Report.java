package org.wp.homology;

import java.util.ArrayList;
import java.util.List;

import org.pathvisio.core.model.Pathway;

public class Report {

	private Pathway newPathway;
	private String name;
	private String wpId;
	private String revision;
	public Integer countTotalGenes = 0;
	private List<String> totalGenes;
	public Integer countMappedGenes = 0;
	private List<String> mappedGenes;
	public Integer countMultiMapping = 0;
	private List<String> multiMappingGenes;
	public Integer countNoMapping = 0;
	private List<String> noHomologyMappingGenes;
	public Integer countNoHomologyMapping = 0;
	private List<String> noMappingGenes;
	public Integer countNoXref = 0;
	private List<String> noXrefGenes;
	private String fileName;
	
	public Report(String name, String fileName) {
		this.name = name;
		this.fileName = fileName;
		totalGenes = new ArrayList<String>();
		mappedGenes= new ArrayList<String>();
		multiMappingGenes = new ArrayList<String>();
		noMappingGenes = new ArrayList<String>();
		noHomologyMappingGenes = new ArrayList<String>();
		noXrefGenes = new ArrayList<String>();
	}
	
	public String getFileName() {
		return fileName;
	}

	public Float getHomologyScore() {
		if(getTotalGenes().size() == 0) {
			return 0.0f;
		}
		
		float percentage = (getMappedGenes().size() * 100) / getTotalGenes().size();
		return percentage;
	}

	public List<String> getNoHomologyMappingGenes() {
		return noHomologyMappingGenes;
	}

	public String getName() {
		return name;
	}

	public List<String> getTotalGenes() {
		return totalGenes;
	}

	public List<String> getMappedGenes() {
		return mappedGenes;
	}

	public List<String> getMultiMappingGenes() {
		return multiMappingGenes;
	}

	public List<String> getNoMappingGenes() {
		return noMappingGenes;
	}

	public List<String> getNoXrefGenes() {
		return noXrefGenes;
	}	
	public Pathway getNewPathway() {
		return newPathway;
	}

	public void setNewPathway(Pathway newPathway) {
		this.newPathway = newPathway;
	}

	public String getWpId() {
		return wpId;
	}

	public void setWpId(String wpId) {
		this.wpId = wpId;
	}

	public String getRevision() {
		return revision;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}
}
