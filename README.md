# GPML Homology Mapper

This tool allows you to locally perform homology mapper conversion from GPML files in a directory to new pathways (with identifiers from another species). The user needs to provide the homology mapping (e.g. from BioMart, Wikidata), a file with new gene names for the new species (e.g. from BioMart, Wikidata), and the original species mapping file between databases (from BridgeDb).

----

### 1. Homology mapping and new gene names
To use the HomologyMapper, you need to provide three files:

1.1 Homology mapping - file with two columns - separated by tab (filename ends with .tsv or .txt) :
  * first column = identifier in original species
  * second column = identifier in new species
  * See [hsa-mmu.txt test file](https://github.com/PathVisio/homology.mapper/tree/master/test) for an example of required input.

     1. Ensembl Biomart - http://www.ensembl.org/biomart, see [screenshot](https://github.com/PathVisio/homology.mapper/blob/master/test/biomart-homology.png))  
     1. Wikidata - [example](https://w.wiki/AeL) ID mapping from Mouse to Human
  
1.2 New gene names - file with two columns - separated by tab (filename ends with .tsv or .txt) :
  * first column = identifier per gene for new species
  * second column = gene name per identifier for new species
  * check [mmu-genes.txt test file](https://github.com/PathVisio/homology.mapper/tree/master/test) for an example of required input.

    1. Ensembl Biomart - http://www.ensembl.org/biomart, see [screenshot](https://github.com/PathVisio/homology.mapper/blob/master/test/biomart-gene-names.png))
    1. Wikidata - [example](https://w.wiki/AeM) mapping Human ID to name

1.3 Identifier mapping for original species - (filename ends with .bridge file) :
  * Download these files from the [BridgeDb website](https://bridgedb.github.io/data/gene_database/).
  * Use the correct species (the original one).
  * This file is used to map the identifiers in the original GPML file to one database, which is later used to convert to a new species.
----

### 2. Update config file
Use the example config file and adapt it to your use case:
https://github.com/PathVisio/homology.mapper/blob/master/HomologyMapper/mapper.config
  * source.organism = Latin Name of Original Organism, as being [supported by PathVisio](https://www.bridgedb.org/mapping-databases/ensembl-gene-mappings/).
  * target.organism = Latin Name of New species.
  * output.dir = Local folder where results (new GPML files) should be stored (be aware that you have to create this folder yourself!)
  * input.dir = Local folder where original GPML can be retrieved
  * source.gene.mapping.db = Location of local identifier mapping database, see step 1.3
  * mapping.file = Location of mapping file from original to new species, see step 1.1
  * new.gene.names = Location of file with new species ID and name, see step 1.2
  * system.code.source = [BridgeDb System code](https://www.bridgedb.org/documentation/system-codes/) for Original Species Identifiers Database, as used in step 1.1
  * system.code.target = [BridgeDb System code](https://www.bridgedb.org/documentation/system-codes/) for New Species Identifiers Database, as used in step 1.2
  * log.file =  Local file where information on conversion is stored (in case of issues running the code).
----

### 3. Download latest release
Download and unzip the latest HomologyMapper-xxx.zip file from https://github.com/PathVisio/homology.mapper/releases

----

### 4. Run HomologyMapper file
Use the command line and go into the created directory of the unzipped HomologyMapper folder (on the same level as HomologyMapper.jar is located). Then run the following command:
```
java -jar -Dencoding.file=UTF-8 HomologyMapper.jar /path-to-config-file/my.config
```
