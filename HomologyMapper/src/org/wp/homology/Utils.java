// WP2RDF
package org.wp.homology;

import java.io.File;

import org.bridgedb.BridgeDb;
import org.bridgedb.IDMapper;
import org.bridgedb.IDMapperException;
import org.bridgedb.bio.DataSourceTxt;

/**
 * 
 * @author mkutmon
 * @author ryanmiller
 *
 */
public class Utils {
	
	public static IDMapper setUpIDMapper(File file) throws IDMapperException, ClassNotFoundException {
		DataSourceTxt.init();
		Class.forName("org.bridgedb.rdb.IDMapperRdb");  
		IDMapper mapper = BridgeDb.connect("idmapper-pgdb:" + file.getAbsolutePath());
		return mapper;
	}
}