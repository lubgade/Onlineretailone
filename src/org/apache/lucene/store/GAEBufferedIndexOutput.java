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



/**
 * 
 *
 * @author Rushikesh Ubgade
 */
public abstract class GAEBufferedIndexOutput extends ConfigurableBufferedIndexOutput  {

    /**
     * The buffer size setting name. See {@link JdbcFileEntrySettings#setIntSetting(String,int)}.
     * Should be set in bytes.
     */
    public static final String BUFFER_SIZE_SETTING = "indexOutput.bufferSize";
    /**
     * Implement  configuration as when time permits.
     * @param name
     * @throws IOException
     */
    public void configure(int buffersize) throws IOException {
      initBuffer(buffersize);
    }
}
