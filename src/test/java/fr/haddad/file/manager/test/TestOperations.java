package fr.haddad.file.manager.test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.util.Progressable;
import org.junit.Assert;
import org.junit.Test;
import fr.haddad.dfs.control.ProgressDelegate;

public class TestOperations {
	public static String hdfsUrl = "hdfs://hadoopmaster:9000";
	public static FSDataOutputStream out;
	public static InputStream in;
	public static long lengh;
	Configuration conf = new Configuration();
	FileSystem fs;
	ProgressDelegate progressdel;
	@Test
	public void testHDFSMkdir() throws IOException {
		fs = FileSystem.get(URI.create(hdfsUrl), conf);
		Path path = new Path("/Clients");
		fs.mkdirs(path);
		Assert.assertEquals(fs.exists(path), true);
	}
	/*//test delete
	@Test
	public void testHDFSdelete() throws IOException,FileNotFoundException {
		fs = FileSystem.get(URI.create(hdfsUrl), conf);
		Path path = new Path("/TestFacture");
		fs.delete(path,true);
		Assert.assertEquals(fs.exists(path), true);
	}
	// upload a local file to HDFS
	

	// test create a file
	@Test
	public void testCreateFile() throws IOException {

		fs = FileSystem.get(URI.create(hdfsUrl), conf);
		Path path = new Path("/a.txt");
		FSDataOutputStream out = fs.create(path);
		out.write("hello hadoop".getBytes());
		Assert.assertEquals(fs.exists(path), true);
	}

	// rename a file
	@Test
	public void testRenameFile() throws IOException {

		fs = FileSystem.get(URI.create(hdfsUrl), conf);
		Path path = new Path("/test/a.txt");
		Path newpath = new Path("/test/b.txt");
		System.out.println(fs.rename(path, newpath));
		Assert.assertEquals(fs.exists(newpath), true);
		Assert.assertEquals(fs.exists(path), true);
	}
//test upload file with methode 1
	@Test
	public void testUploadFile1() throws IOException {

		fs = FileSystem.get(URI.create(hdfsUrl), conf);
		Path src = new Path(
				"/home/hadoopmaster/Téléchargements/eclipse-jee-luna-SR2-linux-gtk-x86_64.tar.gz");
		Path dst = new Path("/test/eclipse-------.gz");
		fs.delete(dst, true);
		fs.create(dst);
		File file = new File(src.toString());

		System.out.println(file.length());
		// System.out.println("avant"+f);

		fs.copyFromLocalFile(src, dst);
		Assert.assertEquals(fs.exists(dst), true);

	}
	//test upload file with methode 2
	@Test
	public void testUploadFile2() throws IOException {
		fs = FileSystem.get(URI.create(hdfsUrl), conf);
		in = new BufferedInputStream(
				new FileInputStream(
						new File(
								"/home/hadoopmaster/Téléchargements/eclipse-jee-luna-SR2-linux-gtk-x86_64.tar.gz")));
		FSDataOutputStream out = fs.create(new Path("/test/rcc1"));
		IOUtils.copyBytes(in, out, 4096);
		Assert.assertEquals(fs.exists(new Path("/test/rcc1")), true);
	}
	//test upload file with methode 3 with progress bar
	@Test
	public void testUploadFile3() throws IOException {
		fs = FileSystem.get(URI.create(hdfsUrl), conf);
		File file = new File(
				"/home/hadoopmaster/Téléchargements/eclipse-jee-luna-SR2-linux-gtk-x86_64.tar.gz");
		lengh = file.length();
		in = new BufferedInputStream(new FileInputStream(file));
		out = fs.create(new Path("/test/rcc9/x86_64.tar.gz"),
				new Progressable() {
					@Override
					public void progress() {
						try {
							System.out.println(((float) out.getPos() / lengh) * 100);
							if (progressdel != null) {
								progressdel.setCurrentProgress(((float) out
										.getPos() / lengh) * 100);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}

					}
				});
		Assert.assertEquals(fs.exists(new Path("/test/rcc9/x86_64.tar.gz")), true);
		IOUtils.copyBytes(in, out, 4096);
	}

	*/
	
}
