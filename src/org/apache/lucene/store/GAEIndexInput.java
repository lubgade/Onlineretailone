package org.apache.lucene.store;

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

import java.io.IOException;

/**
 * A memory-resident {@link IndexInput} implementation like the {@link RAMInputStream}.
 * 
 * $Id: GAEIndexInput.java 25 2009-09-14 02:10:51Z lhelper $
 */
public class GAEIndexInput extends IndexInput implements Cloneable {
  static final int BUFFER_SIZE = GAEFile.SEGMENT_LENGTH;

  private GAEFile file;
  private long length;

  private byte[] currentBuffer;
  private int currentBufferIndex;

  private int bufferPosition;
  private long bufferStart;
  private int bufferLength;

  GAEIndexInput(GAEFile f) throws IOException {
    file = f;
    if ( file != null){
    length = file.getLength();
    if (length / BUFFER_SIZE >= Integer.MAX_VALUE) {
      throw new IOException("Too large RAMFile! " + length);
    }
    // make sure that we switch to the
    // first needed buffer lazily
    currentBufferIndex = -1;
    currentBuffer = new byte[BUFFER_SIZE];
    }	

  }

  public void close() {
    // nothing to do here
  }

  public long length() {
    return length;
  }

  public byte readByte() throws IOException {
	 
	 if (bufferPosition >= bufferLength) {
      currentBufferIndex++;
      switchCurrentBuffer();
    }
   
    return currentBuffer[bufferPosition++];
   }

  public void readBytes(byte[] b, int offset, int len) throws IOException {
    while (len > 0) {
      if (bufferPosition >= bufferLength) {
        currentBufferIndex++;
        switchCurrentBuffer();
      }

      int remainInBuffer = bufferLength - bufferPosition;
      int bytesToCopy = len < remainInBuffer ? len : remainInBuffer;
      System.arraycopy(currentBuffer, bufferPosition, b, offset, bytesToCopy);
      offset += bytesToCopy;
      len -= bytesToCopy;
      bufferPosition += bytesToCopy;
    }
  }

  private final void switchCurrentBuffer() throws IOException {
	if ( file != null){  
    if (currentBufferIndex >= file.getSegmentCount()) {
      // end of file reached, no more buffers left
      throw new IOException("Read past EOF");
    } else {
      currentBuffer = file.getSegment(currentBufferIndex);
      bufferPosition = 0;
      bufferStart = (long) BUFFER_SIZE * (long) currentBufferIndex;
      long buflen = length - bufferStart;
      bufferLength = buflen > BUFFER_SIZE ? BUFFER_SIZE : (int) buflen;
    }
	}
  }

  public long getFilePointer() {
    return currentBufferIndex < 0 ? 0 : bufferStart + bufferPosition;
  }

  public void seek(long pos) throws IOException {
    if (currentBuffer == null || pos < bufferStart || pos >= bufferStart + BUFFER_SIZE) {
      currentBufferIndex = (int) (pos / BUFFER_SIZE);
      switchCurrentBuffer();
    }
    bufferPosition = (int) (pos % BUFFER_SIZE);
  }
}
