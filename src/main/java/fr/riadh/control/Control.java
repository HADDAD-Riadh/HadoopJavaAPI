package fr.riadh.control;

import java.net.URI;
import java.net.URISyntaxException;


import org.apache.log4j.Logger;

import fr.haddad.dfs.interfaces.UploadFileManagerInterface;

public class Control {
	//used for singleton
	  private static Control uniqueInstance;
	  public static Logger logger = Logger.getLogger(Control.class);
      private Control()
      {
      }
      //singleton
      public static synchronized Control getInstance()
      {
              if(uniqueInstance==null)
              {
                      uniqueInstance = new Control();
              }
              return uniqueInstance;
      }
      
	
	
	//test if the hdfs url is correct 
	public boolean hdfsPathisCorrect(String HDFS_URL) {
		URI uri = null;

		try {
			uri = new URI(HDFS_URL);
		} catch (URISyntaxException e) {
			return false;
		}

		if((!uri.getScheme().equals("hdfs"))||(uri.getHost() == null) || (uri.getPort()==-1) ){
		
			return false;
		}

		return true;

	}
	public boolean FileSizeControl(long length) throws NeedToUseLargeFileMethodeException{
	
	if (length> UploadFileManagerInterface.LARGE_FILE_THRESHOLD ) {
		
		
		logger.error("your file length "+length+"is biger than Theshold" +  UploadFileManagerInterface.LARGE_FILE_THRESHOLD );
		throw new NeedToUseLargeFileMethodeException("your file length is biger than Theshold");
		
		}
	return true;
	}
	
	
	
	
	
	
	
	
	
	
}
