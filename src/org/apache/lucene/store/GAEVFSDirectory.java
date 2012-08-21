package org.apache.lucene.store;

import java.io.IOException;
import java.util.logging.Logger;

import org.apache.commons.vfs.AllFileSelector;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import com.newatlanta.commons.vfs.provider.gae.GaeVFS;

public class GAEVFSDirectory extends Directory {
	FileObject indexFolder = null;
	private static Logger log = Logger.getLogger(GAEVFSDirectory.class.getName());


	public GAEVFSDirectory(String name) throws IOException {
		 FileSystemManager fsManager = GaeVFS.getManager();
		 FileObject mainIndex = fsManager.resolveFile("gae://index");
		 if (!mainIndex.exists()){
			 mainIndex.createFolder();
		 }
		 indexFolder = fsManager.resolveFile("gae://index/"+name);
		 if ( !indexFolder.exists() ) {
			 indexFolder.createFolder();
		  }
	}
	  @Override
	  public Lock makeLock(String lockName){
		 return NoLockFactory.getNoLockFactory().makeLock(lockName);
	  }

	@Override
	public String[] listAll() throws IOException {
		// TODO Auto-generated method stub
		
		FileObject[] files = indexFolder.getChildren();
		String[] fileNames = new String[files.length];
		for (int i =0 ; i < files.length; i++){
			fileNames[i] = files[i].getName().getBaseName();
			log.info("FileNames:"+fileNames[i] );
		}
		return fileNames;
	}

	@Override
	public boolean fileExists(String name) throws IOException {
		// TODO Auto-generated method stub
		FileObject file = indexFolder.resolveFile(name);
		log.info("File exists:"+ file.getName().getPath());

		return file.exists();
	}

	@Override
	public long fileModified(String name) throws IOException {
		// TODO Auto-generated method stub
		FileObject file = indexFolder.resolveFile(name);
		return file.getContent().getLastModifiedTime();
		
	}

	@Override
	public void touchFile(String name) throws IOException {
		// TODO Auto-generated method stub
		FileObject file = indexFolder.resolveFile(name);
		if ( !file.exists()){
		  file.createFile();
		}
	}

	@Override
	public void deleteFile(String name) throws IOException {
		// TODO Auto-generated method stub
		FileObject file = indexFolder.resolveFile(name);
		if ( file.exists()){
			file.delete();
		}
	}

	@Override
	public long fileLength(String name) throws IOException {
		// TODO Auto-generated method stub
		FileObject file =indexFolder.resolveFile(name);
		return file.getContent().getSize();
	}

	@Override
	public IndexOutput createOutput(String name) throws IOException {
		// TODO Auto-generated method stub
		GAEVFSIndexOutput output=  new GAEVFSIndexOutput(indexFolder, name);
		return output;
	}

	@Override
	public IndexInput openInput(String name) throws IOException {
		// TODO Auto-generated method stub
		GAEVFSIndexInput input  = new GAEVFSIndexInput(indexFolder, name);
		return input;
	}

	@Override
	public void close() throws IOException {
		// TODO Auto-generated method stub
		indexFolder.close();
		
	}

}
