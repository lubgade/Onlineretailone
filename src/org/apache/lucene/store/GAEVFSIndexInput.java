package org.apache.lucene.store;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemException;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.RandomAccessContent;
import org.apache.commons.vfs.util.RandomAccessMode;

import com.newatlanta.commons.vfs.provider.gae.GaeFileObject;
import com.newatlanta.commons.vfs.provider.gae.GaeRandomAccessContent;

public class GAEVFSIndexInput  extends IndexInput implements Cloneable{
	FileObject gaeDirectory;
	FileObject currentFile;
	RandomAccessContent content;
	  private static Logger log = Logger.getLogger(GAEVFSIndexInput.class.getName());

	public GAEVFSIndexInput(FileObject file , String name) throws IOException{
		this.gaeDirectory = file;
		currentFile= file.resolveFile(name);
		if ( currentFile.getType() == FileType.FOLDER){
			log.info("currentFile is Folder:"+ currentFile.getName().getBaseName());
		}
		log.info("currentFile is File:"+ currentFile.getName().getBaseName());
		
			
		content = currentFile.getContent().getRandomAccessContent(RandomAccessMode.READ);

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
		} catch (FileSystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public void seek(long pos) throws IOException {
		// TODO Auto-generated method stub
		log.info("File Name for seek:" + currentFile.getName().getPath());
		//currentFile.getContent().getInputStream().
		content.seek(pos); 
	}

	@Override
	public long length() {
		// TODO Auto-generated method stub
		try {
			return content.length();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public byte readByte() throws IOException {
		// TODO Auto-generated method stub
		return content.readByte();
		
	}

	@Override
	public void readBytes(byte[] b, int offset, int len) throws IOException {
		// TODO Auto-generated method stub
		content.readFully(b, offset, len);
	}

}
