package com.adjointweb.onlineretail.Dao;
import java.io.IOException;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.shingle.ShingleAnalyzerWrapper;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenizer;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenizer.Side;

import com.adjointweb.onlineretail.search.analysis.position.PositionFilter;
import com.adjointweb.onlineretailone.entities.Shingle;


import java.util.Arrays;

public class SearchUtils {
        
        
        private static final Logger log = Logger.getLogger(SearchUtils.class.getName());
        
   

        		
        
        /**
         * Uses english stemming (snowball + lucene) + stopwords for getting the words.
         * 
         * @param index
         * @return
         */
        public static Set<String> getTokensForIndexingOrQuery(
                        String index_raw,
                        int maximumNumberOfTokensToReturn) {
        		
            Set<String> returnSet = new HashSet<String>();

        		if ( index_raw == null){ 
        			log.warning("Token is null");
        			return returnSet;
        		}
        				
                String indexCleanedOfHTMLTags = index_raw.replaceAll("\\<.*?>"," ");
                
                
                
                try {
                        
                      
                        	
                        Analyzer shingleAnalyzer = new StandardAnalyzer( org.apache.lucene.util.Version.LUCENE_34);
                        
                        
                        TokenStream source = shingleAnalyzer.tokenStream(
                                        "content", 
                                        new StringReader(indexCleanedOfHTMLTags));
                		StopFilter stopFilter= new StopFilter(org.apache.lucene.util.Version.LUCENE_34,source,EnglishAnalyzer.getDefaultStopSet(),true);
                		stopFilter.setEnablePositionIncrements(false);
                		PositionFilter pFilter = new PositionFilter(stopFilter);
                			
                		
                		ShingleFilter filter = new ShingleFilter(pFilter);
                	    filter.setMaxShingleSize(8);
                	    filter.setOutputUnigrams(true);

                        
                        OffsetAttribute offsetAttribute = filter.getAttribute(OffsetAttribute.class);
                        CharTermAttribute termAttribute = filter.getAttribute(CharTermAttribute.class);
                        
                    while ((filter.incrementToken()) 
                                && (returnSet.size() < maximumNumberOfTokensToReturn)) {
                	
                	int startOffset = offsetAttribute.startOffset();
        	    	int endOffset = offsetAttribute.endOffset();			
        	    	
        	    	String s = termAttribute.toString();
        	    	s = removeDuplicate(s);
        	    	returnSet.add(s.trim());
                        
                    }
                        
                } catch (IOException e) {
                        log.severe(e.getMessage());	
                }
                
                //returnSet.add(indexCleanedOfHTMLTags.trim().toLowerCase());
                return returnSet;
                 
                
        }
        public static Set<String> getNgramsForQuery(String query, int maximumNumberOfTokensToReturn){
        	Set<String> ngrams = new LinkedHashSet<String>();

        	Analyzer shingleAnalyzer = new StandardAnalyzer(org.apache.lucene.util.Version.LUCENE_34);
            
            
            String indexCleanedOfHTMLTags = query.replaceAll("\\<.*?>"," ");
    		TokenStream source = shingleAnalyzer.tokenStream("content", new StringReader(indexCleanedOfHTMLTags));
    		StopFilter stopFilter= new StopFilter(org.apache.lucene.util.Version.LUCENE_34,source,EnglishAnalyzer.getDefaultStopSet(),true);
    		stopFilter.setEnablePositionIncrements(false);
    		PositionFilter pFilter = new PositionFilter(stopFilter);
    			
    		
    		ShingleFilter filter = new ShingleFilter(pFilter);
    	    filter.setMaxShingleSize(8);
    	    filter.setOutputUnigrams(true);


    		
    		CharTermAttribute termAttribute = filter.getAttribute(CharTermAttribute.class);
    		
			try {
    	        while (filter.incrementToken())  {
    	        	String token = termAttribute.toString();
    	        
    	        	EdgeNGramTokenizer ngramToken = new EdgeNGramTokenizer(new StringReader(token) , Side.FRONT, 1, 5);
    				EdgeNGramTokenFilter nFilter = new EdgeNGramTokenFilter(ngramToken, EdgeNGramTokenFilter.Side.FRONT , 1, 5);
    				CharTermAttribute ntermAttribute = nFilter.getAttribute(CharTermAttribute.class);
	
					while((ngramToken.incrementToken()) && (ngrams.size() < maximumNumberOfTokensToReturn)){
					 StringBuffer buffer =	new StringBuffer();
					 buffer.append(ntermAttribute.buffer());
					 String  ngram = buffer.toString();
					 log.info(" Ngrams :" + ngram);
					 ngrams.add(ngram.trim());
					}
    	        }
			}
    	        catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
            	log.warning("Error in creating ngrams"+e.getMessage());

			}
			return ngrams;
        }
        
        
        public static Set<Shingle> createShilgles(String query){
        	
        	Map<String,Shingle> shingles = new LinkedHashMap<String,Shingle> () ;       	
        	Analyzer shingleAnalyzer = new StandardAnalyzer(org.apache.lucene.util.Version.LUCENE_34);
        	String indexCleanedOfHTMLTags = query.replaceAll("\\<.*?>"," ");

    		
    		TokenStream source = shingleAnalyzer.tokenStream("content", new StringReader(indexCleanedOfHTMLTags));

    		StopFilter stopFilter= new StopFilter(org.apache.lucene.util.Version.LUCENE_34,source,EnglishAnalyzer.getDefaultStopSet(),true);
    		stopFilter.setEnablePositionIncrements(false);
    		PositionFilter pFilter = new PositionFilter(stopFilter);
    			
    		
    		ShingleFilter filter = new ShingleFilter(pFilter);
    	    filter.setMaxShingleSize(8);
    	    filter.setOutputUnigrams(true);

    		
    		OffsetAttribute offsetAttribute = filter.getAttribute(OffsetAttribute.class);
    	    CharTermAttribute termAttribute = filter.getAttribute(CharTermAttribute.class);
    	    
            try{
    	        while (filter.incrementToken())  {
    				int startOffset = offsetAttribute.startOffset();
    				int endOffset = offsetAttribute.endOffset();			
    				String words= termAttribute.toString();
    				words = removeDuplicate(words);
    				    				
    				log.info(words +" start:"+ startOffset + " end:"+endOffset);  
    				EdgeNGramTokenizer ngramToken = new EdgeNGramTokenizer(new StringReader(words) , Side.FRONT, 1, 5);
    				EdgeNGramTokenFilter nFilter = new EdgeNGramTokenFilter(ngramToken, EdgeNGramTokenFilter.Side.FRONT , 1, 5);
    				CharTermAttribute ntermAttribute = nFilter.getAttribute(CharTermAttribute.class);

    				while(nFilter.incrementToken()){
    				 StringBuffer buffer =	new StringBuffer();
    				 buffer.append(ntermAttribute.buffer());
    				 String  ngram = buffer.toString();
       				 Shingle sShingle = shingles.get(ngram);
       				 if (sShingle == null ){ 
       					 sShingle = new Shingle();
       					 sShingle.setShingle(ngram.trim());
       				 }
     				 sShingle.add(words.trim());
     				 shingles.put(ngram, sShingle); 
    				 log.info(words+" Ngrams :" + ngram);
    				}
 
    		 }
            
            }
            catch (IOException e) {
            	e.printStackTrace();
            	log.warning("Error in creating ngrams"+e.getMessage());
            	}
            Set<Shingle> shingleSet = new LinkedHashSet<Shingle>();
            shingleSet.addAll(shingles.values());
            return shingleSet;
        }
  
        public static String removeDuplicate(String s){
    		String[] sWords = s.split(" ");
    		Set<String> set = new LinkedHashSet<String>();
    		StringBuffer buffer = new StringBuffer();
    		for (int i=0; i < sWords.length; i++){
    			set.add(sWords[i]);
    		}
    		
    	    Iterator it = set.iterator();
    	    while(it.hasNext()){
    	    	buffer.append(it.next()).append(" " );
    	    }
    	    return buffer.toString();
    	}
        
        
}