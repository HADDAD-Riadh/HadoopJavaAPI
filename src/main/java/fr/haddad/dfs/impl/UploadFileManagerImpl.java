package fr.haddad.dfs.impl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.PrivilegedExceptionAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.security.AccessControlException;
import org.apache.hadoop.security.UserGroupInformation;
import org.apache.hadoop.util.Progressable;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import fr.haddad.dfs.control.ProgressDelegate;
import fr.haddad.dfs.interfaces.UploadFileManagerInterface;
import fr.riadh.control.Control;
import fr.riadh.control.FileInHdfsAlreadyExistException;
import fr.riadh.control.FileInHdfsNotFoundException;
import fr.riadh.control.FileInLocalNotFoundException;
import fr.riadh.control.IsNoStorageSpaceException;
import fr.riadh.control.NeedToUseLargeFileMethodeException;
import fr.riadh.control.UserPermissionHdfsException;

public class UploadFileManagerImpl extends Observable implements
		UploadFileManagerInterface {
	// hadoop config
	private static Configuration conf;
	// user authorized to do hdfs operations
	private static String user;
	// authentification
	private static UserGroupInformation ugi;
	// hdfs url
	private static String HDFS_URL;
	// used for native functionality like mkdir ,write,delete....
	private static FileSystem fileSystem;
	// hdfs config
	private static String nodeConfig = "fs.default.name";
	private static FilesStatus gettingFileStatus;
	private static long start, duration;

	public static String getERROR_MESSAGE() {
		return ERROR_MESSAGE;
	}

	public static void setERROR_MESSAGE(String eRROR_MESSAGE) {
		ERROR_MESSAGE = eRROR_MESSAGE;
	}

	// hdfs config
	private static String jobConfig = "hadoop.job.ugi";
	// for singleton
	private static UploadFileManagerImpl uniqueInstance;
	// used to write
	private static FSDataOutputStream out;
	// used to read
	private static InputStream in;
	// used to get all files paths
	private static FileStatus[] fileStatus;
	FileStatus[] fileStatus2 = null;
	private static List<Path> list;
	// for progress changes
	public ProgressDelegate delegate;
	// used to read configurations from properties file.
	Properties properties;
	private static FileFuture future;
	private static String ERROR_MESSAGE;
	// used for tracability
	public static Logger logger = Logger.getLogger(UploadFileManagerImpl.class);
	private static ExecutorService executor;

	// singleton
	public static synchronized UploadFileManagerImpl getInstance()
			throws IOException {
		if (uniqueInstance == null) {
			uniqueInstance = new UploadFileManagerImpl();
		}
		return uniqueInstance;
	}

	// constructor
	public UploadFileManagerImpl() throws IOException {
		super();

		properties = new Properties();
		URL myUrl = UploadFileManagerImpl.class.getClassLoader().getResource(
				"hdfs.properties");
		try {
			// open connection
			InputStream myInput = myUrl.openConnection().getInputStream();
			// load properties key value
			properties.load(myInput);
		} catch (IOException e1) {
			logger.error("hdfs.properties is missing or not exist");
		}
		// get hdfs url
		HDFS_URL = properties.getProperty("hdfsURL");
		user = properties.getProperty("user");
		// DFS_URL = properties.getProperty("hdfsURL");
		ugi = UserGroupInformation.createRemoteUser(user);
		conf = new Configuration();
		conf.set(nodeConfig, HDFS_URL);
		conf.set(jobConfig, user);
		fileSystem = FileSystem.get(conf);
		delegate = ProgressDelegate.getInstance();
		list = new ArrayList<Path>();
	}

	// upload Large file to hdfs
	public FileFuture uploadLargeFile(final Path localpath,
			final Path RemoteFilePath) throws Exception {

		{

			String folderPath = RemoteFilePath.toString();
			Path pathFolder = new Path(folderPath.substring(0,
					folderPath.lastIndexOf("/")));

			if (!fileSystem.exists(pathFolder)) {
				logger.log(Level.ERROR, "Folder  not existed in hdfs....");
				throw new FileInHdfsNotFoundException(
						"Folder  not existed in hdfs....");
			}

			if (fileSystem.exists(RemoteFilePath)) {
				logger.log(Level.ERROR, "File Already existed in hdfs....");
				throw new FileInHdfsAlreadyExistException(
						"File Already existed in hdfs...");
			}

			gettingFileStatus = new FilesStatus();
			executor = Executors.newCachedThreadPool();
			future = new FileFuture(new Callable<FilesStatus>() {
				public FilesStatus call()
						throws UserPermissionHdfsException {
					start = System.currentTimeMillis();
					try {

						try {
							ugi.doAs(new PrivilegedExceptionAction<Void>() {

								public Void run() throws Exception {

									File file = new File(localpath.toString());
									final long lengh = file.length();
									in = new BufferedInputStream(
											new FileInputStream(file));

									//
									// fileSystem.delete(RemoteFilePath,true);
									// delete(RemoteFilePath);
									/**
									 * create file with progress
									 */
									out = fileSystem.create(RemoteFilePath,
											new Progressable() {

												@Override
												public void progress() {
													try {

														/**
														 * show the calculation
														 * of the value of %
														 * using file length and
														 * bytes that are copied
														 */
														System.out
																.println(((float) out
																		.getPos() / lengh) * 100);
														/**
														 * delegate is used for
														 * the external acces of
														 * progress value
														 */
														delegate.setCurrentProgress(((float) out
																.getPos() / lengh) * 100);
														/**
														 * design pattern
														 * observer to send
														 * current progress in
														 * real time
														 */
														duration = System
																.currentTimeMillis()
																- start;
														setChanged();
														notifyObservers(delegate
																.getCurrentProgress());
														gettingFileStatus
																.setCompletion(out
																		.getPos());

														gettingFileStatus
																.setFile(RemoteFilePath);
														gettingFileStatus
																.setDuration(duration);

													} catch (IOException e) {
														e.printStackTrace();
													}

												}
											});

									IOUtils.copyBytes(in, out, 4096);
									/*
									 * if (future.isDone()) {
									 * 
									 * 
									 * }
									 */
									// /out.close();

									return null;
								}

							});

						} catch (IOException e) {
							e.printStackTrace();
						}

					} catch (InterruptedException e) {
						logger.error("InterruptedException" + e);
						System.out.println("Epic fail.");
					}

					return gettingFileStatus;
				}
			}, gettingFileStatus, fileSystem, RemoteFilePath, out);
			future.run();
			// new Thread(future).start();
			// executor.execute(future);
		}

		try {
			try {
				System.out.println(future.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			} // use future
		} catch (ExecutionException ex) {
			ex.printStackTrace();
		}

		return (FileFuture) future;

	}

	// upload small file to hdfs
	public void uploadtSmallFile(final Path localpath, final Path RemoteFilePath)
			throws FileInLocalNotFoundException, IsNoStorageSpaceException,
			UserPermissionHdfsException, IOException,
			FileInHdfsNotFoundException {
		{

			String folderPath = RemoteFilePath.toString();
			Path pathFolder = new Path(folderPath.substring(0,
					folderPath.lastIndexOf("/")));

			if (!fileSystem.exists(pathFolder)) {
				logger.log(Level.ERROR, "Folder  not existed in hdfs....");
				throw new FileInHdfsNotFoundException(
						"Folder  not existed in hdfs....");
			}

			if (fileSystem.exists(RemoteFilePath)) {
				logger.log(Level.ERROR, "File Already existed in hdfs....");
				throw new FileInHdfsAlreadyExistException(
						"File Already existed in hdfs...");
			}
			File file = new File(localpath.toString());
			final long length = file.length();

			/*
			 * try { if (fileSystem.exists(RemoteFilePath)) {
			 * logger.log(Level.ERROR, "File Already existed in hdfs....");
			 * throw new FileInHdfsAlreadyExistException(
			 * "File Already existed in hdfs..."); } } catch (IOException e2) {
			 * e2.printStackTrace(); }
			 */
			try {
				if (Control.getInstance().FileSizeControl(length)) {
					try {

						ugi.doAs(new PrivilegedExceptionAction<Void>() {

							public Void run() throws Exception {

								fileSystem.copyFromLocalFile(localpath,
										RemoteFilePath);
								return null;

							}
						});
						throw new UserPermissionHdfsException(
								"User Permission exception...");
					} catch (Exception e) {

					}

				}
			} catch (NeedToUseLargeFileMethodeException e1) {
				setERROR_MESSAGE("your file length " + length
						+ "is biger than Theshold"
						+ UploadFileManagerInterface.LARGE_FILE_THRESHOLD);
				e1.printStackTrace();
			}

		}

	}

	// create folder
	public boolean mkdir(final Path dir)
			throws FileInHdfsAlreadyExistException, IOException {

		String folderPath = dir.toString();
		Path pathFolder = new Path(folderPath.substring(0,
				folderPath.lastIndexOf("/")));
		if (fileSystem.exists(pathFolder)) {
			logger.log(Level.ERROR, "Folder   existed in hdfs....");
			throw new FileInHdfsAlreadyExistException(
					"Folder Already existed in hdfs....");
		}

		try {

			ugi.doAs(new PrivilegedExceptionAction<Void>() {

				public Void run() throws Exception {
					fileSystem.mkdirs(dir);
					return null;
				}
			});
			throw new UserPermissionHdfsException(
					"User Permission exception...");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;
	}

	// delete folder
	public boolean delete(final Path dir) throws IOException,
			FileInHdfsAlreadyExistException {
		if (fileSystem.exists(dir)) {
			logger.log(Level.ERROR, "File  Already existed in hdfs....");
			throw new FileInHdfsAlreadyExistException(
					"File  Already existed in hdfs....");
		}
		fileSystem.delete(dir, true);

		return false;
	}

	// test if file exist in local
	public boolean ifExistsInLocal(Path source) throws IOException,
			UserPermissionHdfsException {
		boolean isExists = false;
		try {
			isExists = fileSystem.exists(source);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return isExists;
	}

	// test if file exist in hdfs
	public boolean ifExistsInHDFS(Path distination) throws IOException,
			FileInHdfsAlreadyExistException {

		boolean isExists = fileSystem.exists(distination);
		return isExists;
	}

	// get all files in hdfs (list of directory)
	public List<Path> getListFile() throws IOException, URISyntaxException {
		fileStatus = fileSystem.listStatus(new Path(HDFS_URL + "/"));
		return getFileRecursive(fileStatus);
	}

	// recursive is used by function getListFile()
	public List<Path> getFileRecursive(FileStatus[] fileStatus)
			throws IOException, URISyntaxException {

		for (FileStatus fs : fileStatus) {
			if (fs.isDirectory() || fs.isFile()) {
				list.add(fs.getPath());
			}
		}
		return list;

	}

	// write
	public boolean writeFile(byte[] content, Path hdfs)
			throws UserPermissionHdfsException, IOException {
		OutputStream outputStream = null;
		try {
			outputStream = fileSystem.create(hdfs, false);
			outputStream.write(content);
		} catch (AccessControlException e) {
			throw new UserPermissionHdfsException("User Permission exception");
		} finally {
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException e) {
				logger.log(Level.ERROR, "" + e.getMessage());
			}
		}
		return true;
	}
}
