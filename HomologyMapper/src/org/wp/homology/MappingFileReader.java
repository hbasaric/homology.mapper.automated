package org.wp.homology;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bridgedb.DataSource;
import org.bridgedb.Xref;

public class MappingFileReader {

	public static Map<Xref, Xref> readMappingFile(File f, DataSource input, DataSource output) throws IOException {
		Map<Xref, Xref> xrefs = new HashMap<Xref, Xref>();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		
		String line;
		while((line = reader.readLine()) != null) {
			String [] buffer = line.split("\t");
			if(buffer.length == 2) {
				if(!buffer[0].equals("") && !buffer[1].equals("")) {
					Xref sXref = new Xref(buffer[0], input);
					Xref tXref = new Xref(buffer[1], output);
					
					if(!xrefs.containsKey(sXref)) {
						xrefs.put(sXref, tXref);
					} else {
						System.out.println("multiple homologues " + buffer[0]);
					}
				}
			}
		}
		reader.close();
		return xrefs;
	}

	public static Map<String, String> readGeneNameFile(File file) throws IOException {
		Map<String, String> names = new HashMap<String, String>();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		
		String line;
		while((line = reader.readLine()) != null) {
			String [] buffer = line.split("\t");
			if(buffer.length == 2) {
				String id = buffer[0];
				String name = buffer[1];
				
				names.put(id, name);
			}
		}
		reader.close();
		return names;
	}
}
