# GPML Homology Mapper

Local homology mapper converts all GPML files in a directory to new pathways with identifiers from another species. The user needs to provide the homology mapping (e.g. from BioMart) and a file with new gene names for the new species (e.g. from BioMart).

----

### 1. Download latest release
Download the latest HomologyMapper-xxx.zip file from https://github.com/PathVisio/homology.mapper/releases

----

### 2. Homology mapping and new gene names
To use the HomologyMapper you need to provide two files:
* Homology mapping - file with two columns - separated by tab (easiest to retrieve from Ensembl Biomart - http://www.ensembl.org/biomart)
  * first column = identifier in original species
  * second column = identifier in new species
  
* New gene names - file with two columns - separated by tab (easiest to retrieve from Ensembl Biomart - http://www.ensembl.org/biomart)
  * first column = identifier in new species
  * second column = gene name for new species

----

### 3. 
  
