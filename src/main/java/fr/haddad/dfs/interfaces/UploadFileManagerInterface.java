package fr.haddad.dfs.interfaces;

/**
 * for the test of this API, I used 2 NameNodes and three datanodes
 * that are placed on 5 machines, the flows are sent to the active NameNode that is chosen automatically  
 * and it manages communication with datanodes blocks in terms 
 * of management ..... .,user should indicate the address of the cluster in core-site.xml,and hdfs-core.xml.
 * if client don't like to use high availability cluster ,we should indicate the hdfs url in config.properties file
 * it is tested locally 
 * if you want to send a stream remotely, you just use a 
 * web service which receives a file and in use this API.
 */
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.hadoop.fs.Path;

import fr.haddad.dfs.impl.FileFuture;
import fr.riadh.control.FileInHdfsAlreadyExistException;
import fr.riadh.control.FileInHdfsNotFoundException;
import fr.riadh.control.FileInLocalNotFoundException;
import fr.riadh.control.IsNoStorageSpaceException;
import fr.riadh.control.NeedToUseLargeFileMethodeException;
import fr.riadh.control.UserPermissionHdfsException;

public interface UploadFileManagerInterface {
	/**
	 * When size of small file over <code>LARGE_FILE_THRESHOLD</code>, large
	 * file Mechanism must be preferred.
	 */
	public static long LARGE_FILE_THRESHOLD = 50000;

	/**
	 * A method for large files asynchronously.
	 *
	 * @param localpath
	 *            like file:///C:/Users/Riadh/test.txt
	 * @param RemoteFileURL
	 *            like hdfs://192.168.20.8:9000/riadh/
	 * @return FileFuture represents the result of an asynchronous
	 *         computation. Methods are provided to check if the computation is
	 *         complete, to wait for its completion, and to retrieve the result
	 *         of the computation.
	 * @throws FileAlreadyExistException
	 * @throws IOException
	 */
	public FileFuture uploadLargeFile(final Path localpath,
			final Path RemoteFilePath) throws FileInHdfsAlreadyExistException,
			FileInLocalNotFoundException, IsNoStorageSpaceException,
			UserPermissionHdfsException,FileInHdfsNotFoundException, Exception;

	/**
	 * method for small files (less than LARGE_FILE_THRESHOLD) in synchronous
	 * mode.
	 *
	 * @param localpath
	 *            like file:///C:/Users/Riadh/test.txt
	 * @param RemoteFilePath
	 *            like hdfs://192.168.20.8:9000/riadh/
	 * @throws Exception
	 *             tu use the second methode)
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public void uploadtSmallFile(final Path localpath, final Path RemoteFilePath)
			throws NeedToUseLargeFileMethodeException,
			FileInHdfsAlreadyExistException, FileInLocalNotFoundException,
			IsNoStorageSpaceException, UserPermissionHdfsException, IOException,FileInHdfsNotFoundException;

	/**
	 * @param dir
	 *            name of directory url of directory that will be created like
	 *            hdfs://192.168.20.8:9000/user/folder1
	 * @throws FileInHdfsAlreadyExistException 
	 * @throws IOException
	 */
	// Create a new directory in HDFS
	public boolean mkdir(Path dir) throws FileInHdfsAlreadyExistException 
									 , IOException;

	/**
	 * @param dir
	 *            name of directory url of directory that will be deleted like
	 *            hdfs://192.168.20.8:9000/user/folder1
	 * @throws FileInHdfsNotFoundException 
	 * @throws IOException
	 * @throws FileInHdfsNotFoundExecption
	 * @throws FileInHdfsNotFoundExeception
	 */
	// delete dir named directory in HDFS
	public boolean delete(Path dir) throws IOException, FileInHdfsAlreadyExistException /*
									 * throws IOException,fr.riadh.control.
									 * FileNotFoundException
									 */;

	/**
	 * Future<FilesStatus> The result type returned by this Future's get
	 * method for an asynchronous computation.
	 */
	/**
	 * this methode test if @param hdfsfile exist in hdfs
	 * 
	 * @param hdfsfile
	 * @return
	 * @throws UserPermissionHdfsException
	 * @throws IOException
	 * @throws FileInHdfsNotFoundException
	 */
	public boolean ifExistsInLocal(Path hdfsfile) throws IOException,
			UserPermissionHdfsException /*
										 * throws
										 * IOException,UserPermissionHdfsException
										 */;

	/**
	 * this methode return the list of files
	 * 
	 * @param path
	 *            of hdfs file
	 * @return
	 * @throws IOException
	 * @throws URISyntaxException
	 */
	public List<Path> getListFile() throws IOException, URISyntaxException;

	// test if path is exist in hdfs
	/*
	 * public boolean ifExistsInHDFS(Path distination) throws IOException,
	 * UserPermissionHdfsException; }
	 */
	public boolean writeFile(byte[] content, Path hdfs)
			throws UserPermissionHdfsException, IOException;
}
