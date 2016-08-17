# GPML Homology Mapper

Local homology mapper converts all GPML files in a directory to new pathways with identifiers from another species. The user needs to provide the homology mapping (e.g. from BioMart) and a file with new gene names for the new species (e.g. from BioMart).

----

### 1. Download latest release
Download and unzip the latest HomologyMapper-xxx.zip file from https://github.com/PathVisio/homology.mapper/releases

----

### 2. Homology mapping and new gene names
To use the HomologyMapper you need to provide two files:
* Homology mapping - file with two columns - separated by tab (easiest to retrieve from Ensembl Biomart - http://www.ensembl.org/biomart, see [screenshot](https://github.com/PathVisio/homology.mapper/blob/master/test/biomart-homology.png))
  * first column = identifier in original species
  * second column = identifier in new species
  * check hsa-mmu.txt test file (only an example!): https://github.com/PathVisio/homology.mapper/tree/master/test
  
* New gene names - file with two columns - separated by tab (easiest to retrieve from Ensembl Biomart - http://www.ensembl.org/biomart, see [screenshot](https://github.com/PathVisio/homology.mapper/blob/master/test/biomart-gene-names.png))
  * first column = identifier in new species
  * second column = gene name for new species
  * check mmu-genes.txt test file (only an example!): https://github.com/PathVisio/homology.mapper/tree/master/test

----

### 3. Create config file
The easiest is to use the example config file and adapt it for your use case:
https://github.com/PathVisio/homology.mapper/blob/master/HomologyMapper/mapper.config

----

### 4. Create config file
In step 1 you downloaded and unzipped the HomologyMapper-xxx.zip file. Use the command line and go into the created directory (on the same level as HomologyMapper.jar is located). Then run the following command:

```
java -jar -Dencoding.file=UTF-8 HomologyMapper.jar /path-to-config-file/my.config
```
