package org.apache.gaelucene.tools;

/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.GAEFile;
import org.apache.lucene.store.GAEFileContentJDO;
import org.apache.lucene.store.GAEFileJDO;
import org.apache.lucene.store.GAEIndexCategoryJDO;
import org.apache.lucene.store.SimpleFSDirectory;

public class LuceneIndexImportHandler {

  // global logger
  private static Logger log = Logger.getLogger(LuceneIndexImportHandler.class.getName());
  public static int importToGAEDataStore(File indxDir, String category) throws IOException {
  return importToGAEDataStore(new SimpleFSDirectory(indxDir),category);
  }
  
  private static int importToGAEDataStore(FSDirectory indxDir, String category) throws IOException {
    long version = IndexReader.getCurrentVersion(indxDir); 
    File[] files = indxDir.getDirectory().listFiles();
    for (int i = 0; i < files.length; i++) {
      File file = files[i];
      log.info("transfering file '" + file.getName() + "'...");

      String fileName = file.getName();
      long fileLength = file.length();
      long lastModified = file.lastModified();
      int segmentCount = (int) (fileLength + 1) / GAEFile.SEGMENT_LENGTH + 1;

      Long fileId = GAEFileJDO.saveOrUpdate(category, new Long(version), fileName, new Long(fileLength),
          new Long(lastModified), new Integer(segmentCount));

      FileInputStream fis = new FileInputStream(file);
      for (int sn = 0; sn < segmentCount; sn++) {
        log.info("transfering file '" + fileName + "[" + sn + "]' ...");

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[GAEFile.SEGMENT_LENGTH];
        int bytesNeedRead = GAEFile.SEGMENT_LENGTH;
        while (bytesNeedRead > 0) {
          int bytes = fis.read(buffer, 0, bytesNeedRead);
          if (bytes < 0) {
            break;
          }
          os.write(buffer, 0, bytes);
          bytesNeedRead -= bytes;
        }
        byte[] data = os.toByteArray();
        GAEFileContentJDO.saveOrUpdate(fileId, new Integer(sn), new Long(data.length), data);
      }
      fis.close();
    }

    GAEIndexCategoryJDO.saveOrUpdate(category, version, new Long(System.currentTimeMillis()));
    return files.length;
  }
}
