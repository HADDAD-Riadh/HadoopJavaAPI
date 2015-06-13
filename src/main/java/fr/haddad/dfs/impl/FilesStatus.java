package fr.haddad.dfs.impl;
import java.util.concurrent.TimeUnit;

import org.apache.hadoop.fs.Path;

public class FilesStatus {
private  Path file;
//bytes that are copied 
private  long completion;
//duration to complet copy
private  long duration;
private  TimeUnit unit=TimeUnit.MILLISECONDS;
public Path getFile() {
	return file;
}
@Override
public String toString() {
	return "FilesStatus [file=" + file + ", completion=" + completion
			+ ", duration=" + duration + ", unit=" + unit + "]";
}
public void setFile(Path file) {
	this.file = file;
}

public long getCompletion() {
	return completion;
}

public void setCompletion(long completion) {
	this.completion = completion;
}

public long getDuration() {
	return duration;
}

public void setDuration(long duration) {
	this.duration = duration;
}

public TimeUnit getUnit() {
	return unit;
}

public void setUnit(TimeUnit unit) {
	this.unit = unit;
}


}