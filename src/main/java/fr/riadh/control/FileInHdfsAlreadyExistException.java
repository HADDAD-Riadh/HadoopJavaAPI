package fr.riadh.control;

import org.apache.hadoop.fs.FileAlreadyExistsException;


public class FileInHdfsAlreadyExistException extends FileAlreadyExistsException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public FileInHdfsAlreadyExistException (String message) {

		super(message);
		}
}
