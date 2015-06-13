package fr.haddad.file.manager.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.apache.hadoop.fs.Path;
import org.junit.Assert;
import org.junit.Test;

import fr.haddad.dfs.impl.FilesStatus;
import fr.haddad.dfs.impl.UploadFileManagerImpl;
import fr.haddad.dfs.impl.FileFuture;
import fr.riadh.control.FileInHdfsNotFoundException;
import fr.riadh.control.FileInLocalNotFoundException;
import fr.riadh.control.IsNoStorageSpaceException;
import fr.riadh.control.NeedToUseLargeFileMethodeException;
import fr.riadh.control.UserPermissionHdfsException;

public class testSaveFileManager {
	UploadFileManagerImpl fileManagerImpl;

	
	
	
	/*// test get list of directories
	@Test
	public void testgetListFiles() throws IOException, URISyntaxException,
			UserPermissionHdfsException {

		fileManagerImpl = UploadFileManagerImpl.getInstance();
		List<Path> listPath = fileManagerImpl.getListFile();
		for (Path path : listPath) {
			System.out.println("path" + path);

		}
		Assert.assertEquals(listPath.size(), 2);

	}

	// test folder creation
	@Test
	public void testMkdir() throws IOException, URISyntaxException,
			UserPermissionHdfsException {
		Path dir = new Path("/user/hadoopmaster/felfel");
		fileManagerImpl = UploadFileManagerImpl.getInstance();
		boolean result = fileManagerImpl.mkdir(dir);
		System.out.println(result + "mkdir");
		Assert.assertEquals(result, true);

	}

	// test folder delete
	@Test
	public void testdelete() throws IOException, FileInHdfsNotFoundException,
			FileNotFoundException {
		fileManagerImpl = UploadFileManagerImpl.getInstance();
		Path dir = new Path("/TestFacture");
		boolean result = fileManagerImpl.delete(dir);
		System.out.println(result + "delete");
		Assert.assertEquals(result, true);

	}

	// test upload small file
	@Test
	public void testSaveSmallFile() throws IOException,
			NeedToUseLargeFileMethodeException, URISyntaxException,
			UserPermissionHdfsException, FileInLocalNotFoundException,
			IsNoStorageSpaceException {
		fileManagerImpl = UploadFileManagerImpl.getInstance();
		Path RemoteFilePath = new Path("/user/hadoopmaster");
		Path RemoteTestFilePath = new Path("/user/hadoopmaster/primefaces.zip");
		Path localpath = new Path(
				"/home/hadoopmaster/Téléchargements/primefaces.zip");
		fileManagerImpl.uploadtSmallFile(localpath, RemoteFilePath);
		Assert.assertEquals(fileManagerImpl.ifExistsInHDFS(RemoteTestFilePath),
				true);
	}

	// test synchronous test
	@Test
	public void testwriteToFile() throws IOException,
			NeedToUseLargeFileMethodeException, URISyntaxException,
			UserPermissionHdfsException {
		String myText = "my test synchronous";
		fileManagerImpl = UploadFileManagerImpl.getInstance();
		Path RemoteFilePath = new Path("/hadoop.txt");
		boolean writed = fileManagerImpl.writeFile(myText.getBytes(),
				RemoteFilePath);
		Assert.assertEquals(writed, true);
	}

	// test uplaod large file
	@Test
	public void testUploadLargeFile() throws Exception {
		fileManagerImpl = UploadFileManagerImpl.getInstance();
		Path RemoteFilePath = new Path("/Fathel");
		Path localpath = new Path(
				"/home/hadoopmaster/Téléchargements/eclipse-jee-luna-SR2-linux-gtk-x86_64.tar.gz");
		fileManagerImpl.uploadLargeFile(localpath, RemoteFilePath);
		Assert.assertEquals(fileManagerImpl.ifExistsInHDFS(RemoteFilePath),
				true);
	}
//test cancel large file upload
	@Test
	public void testUploadLargeFileFutureCancelMethode() throws Exception {
		fileManagerImpl = UploadFileManagerImpl.getInstance();
		Path RemoteFilePath = new Path(
				"/Ali/eclipse-jee-luna-SR2-linux-gtk-x86_64.tar.gz");
		Path localpath = new Path(
				"/home/hadoopmaster/Téléchargements/eclipse-jee-luna-SR2-linux-gtk-x86_64.tar.gz");
		FileFuture variable = fileManagerImpl.uploadLargeFile(localpath,
				RemoteFilePath);
		System.out.println(variable.cancel(true));
		Assert.assertEquals(fileManagerImpl.ifExistsInHDFS(RemoteFilePath),
				false);
	}
	//test isCanceled large file upload
	@Test
	public void testUploadLargeFileFutureIsCanceledMethode() throws Exception {
		fileManagerImpl = UploadFileManagerImpl.getInstance();
		Path RemoteFilePath = new Path(
				"/Ali/eclipse-jee-luna-SR2-linux-gtk-x86_64.tar.gz");
		Path localpath = new Path(
				"/home/hadoopmaster/Téléchargements/eclipse-jee-luna-SR2-linux-gtk-x86_64.tar.gz");
		FileFuture variable = fileManagerImpl.uploadLargeFile(localpath,
				RemoteFilePath);
		System.out.println("------canceled---------" + variable.isCancelled());
		Assert.assertEquals(variable.isCancelled(), false);
	}
	//test isDonelarge file upload
	@Test
	public void testUploadLargeFileFutureIsDoneMethode() throws Exception {
		fileManagerImpl = UploadFileManagerImpl.getInstance();
		Path RemoteFilePath = new Path(
				"/Ali/eclipse-jee-luna-SR2-linux-gtk-x86_64.tar.gz");
		Path localpath = new Path(
				"/home/hadoopmaster/Téléchargements/eclipse-jee-luna-SR2-linux-gtk-x86_64.tar.gz");
		FileFuture variable = fileManagerImpl.uploadLargeFile(localpath,
				RemoteFilePath);

		System.out.println("--------done--------" + variable.isDone());
		Assert.assertEquals(variable.isDone(), true);
	}
	//test future get large file upload
*/	@Test
	public void testUploadLargeFileFutureGetMethode() throws Exception {
		fileManagerImpl = UploadFileManagerImpl.getInstance();
		Path RemoteFilePath = new Path(
				"/Clients/eclipse-jee-luna-SR2-linux-gtk-x86_64.tar.gz");
		Path localpath = new Path(
				"/home/hadoopmaster/Téléchargements/eclipse-jee-luna-SR2-linux-gtk-x86_64.tar.gz");
		FileFuture variable = fileManagerImpl.uploadLargeFile(localpath,
				RemoteFilePath);
		/*while (!variable.isDone()) {
			System.out.println("pas encore==========================");
			}*/
		System.out.println(variable.get());
		System.out.println("--------done--------" + variable.isDone());
		Assert.assertEquals(variable.get().getCompletion(), 264916992);
		Assert.assertEquals(variable.get().getFile().toString(),
				"/Clients/eclipse-jee-luna-SR2-linux-gtk-x86_64.tar.gz");
		Assert.assertEquals(variable.get().getUnit().toString(), "MILLISECONDS");
	}
}



