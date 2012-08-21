package org.apache.lucene.store;

import java.io.IOException;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.RandomAccessContent;
import org.apache.commons.vfs.util.RandomAccessMode;

public class GAEVFSIndexOutput extends IndexOutput{
	FileObject gaeDirectory;
	FileObject currentFile;
	RandomAccessContent content;
	FileContent fileContent;
	public GAEVFSIndexOutput(FileObject directory,String name) throws IOException{
		this.gaeDirectory = directory;
		currentFile= directory.resolveFile(name);
		if(!currentFile.exists()){
			currentFile.createFile();
		}
		fileContent = currentFile.getContent();
		content = currentFile.getContent().getRandomAccessContent(RandomAccessMode.READWRITE);
		
	}
	@Override
	public void flush() throws IOException {
		// TODO Auto-generated method stub
		fileContent.getOutputStream().flush();
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		currentFile.close();
	}

	@Override
	public long getFilePointer() {
		// TODO Auto-generated method stub
		try {
			return content.getFilePointer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
		
	}

	@Override
	public void seek(long pos) throws IOException {
		// TODO Auto-generated method stub
		content.seek(pos);
	}

	@Override
	public long length() throws IOException {
		// TODO Auto-generated method stub
		return content.length();
	}

	@Override
	public void writeByte(byte b) throws IOException {
		// TODO Auto-generated method stub
			content.write(b);
	}

	@Override
	public void writeBytes(byte[] b, int offset, int length) throws IOException {
		// TODO Auto-generated method stub
		content.write(b, offset, length);
	}

}
