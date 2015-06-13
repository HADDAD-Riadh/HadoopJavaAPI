package fr.haddad.dfs.control;

import java.io.IOException;


/**
 * @author HADDAD Riadh
 *this class is used to update the % value used by progress bar
 */
public  class ProgressDelegate {
	//value of progress
   public float progress;
   //for singleton
   private static ProgressDelegate uniqueInstance;
   //setter
	public  void setCurrentProgress(float progress){
		this.progress=progress;
			
		
	}
	//getter
	public  float getCurrentProgress(){
		return progress;
		
	}
	//singleton
	public static synchronized ProgressDelegate getInstance()
			throws IOException {
		if (uniqueInstance == null) {
			uniqueInstance = new ProgressDelegate();
		}
		return uniqueInstance;
	}

}
