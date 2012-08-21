/*
 * Copyright 2004-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.lucene.store;

import java.io.IOException;
import java.io.InputStream;

import org.apache.lucene.index.IndexFileNames;
import org.apache.lucene.index.SegmentInfos;



/**
 * @author Rushikesh Ubgade
 */
public abstract class AbstractGAEIndexOutput extends GAEBufferedIndexOutput {

    protected String fileName;

    protected GAEDirectory gaeDirectory;
    long fileid;

    public void configure(GAEDirectory directory,String fileName) throws IOException {
        this.fileName = fileName;
        this.gaeDirectory = directory;
    }
    

    public void close() throws IOException {
        super.close();
        final long length = length();
        doBeforeClose();
        InputStream is = openInputStream();
        byte[] bytes = org.apache.commons.io.IOUtils.toByteArray(is);
 	    GAEIndexCategoryJDO.saveOrUpdate(gaeDirectory.getCategory(),gaeDirectory.getVersion() , new Long(System.currentTimeMillis()));
 	    int segmentCount = (int) (length + 1) / GAEFile.SEGMENT_LENGTH + 1;
 	   
 	/*   if (fileName.startsWith(IndexFileNames.SEGMENTS) && !fileName.equals(IndexFileNames.SEGMENTS_GEN)) {
 		 long  gen = SegmentInfos.generationFromSegmentsFileName(fileName);
 		 if ( gen != 0){
 			 gen = gen*-1;
 		 }
 		 fileName = IndexFileNames.SEGMENTS + "_" + gen; 
 	   }*/
 	   
 		   
 	    this.fileid = GAEFileJDO.saveOrUpdate(gaeDirectory.getCategory(),gaeDirectory.getVersion(), this.fileName,length ,
	   	          new Long(System.currentTimeMillis()),segmentCount );
        
		GAEFileContentJDO.saveOrUpdate(fileid, new Integer(0),length, bytes);

        doAfterClose();
    }

    protected abstract InputStream openInputStream() throws IOException;

    protected void doAfterClose() throws IOException {

    }

    protected void doBeforeClose() throws IOException {

    }
}
