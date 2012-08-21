package snippets;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.LinkedHashSet;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenizer;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenizer.Side;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.ngram.EdgeNGramTokenFilter;
import com.cloudjini.onlineretail.search.analysis.position.PositionFilter;
import org.apache.lucene.analysis.shingle.ShingleFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopFilter;
import org.apache.lucene.analysis.TokenStream;


public class TestNGrams {

	public static void main(String[] args) {
		String query = "The cindy  crawford super the store with  an ultimate goodies";
		
		Analyzer analyzer = new StandardAnalyzer(org.apache.lucene.util.Version.LUCENE_34);
		System.out.println("Stop words" + EnglishAnalyzer.getDefaultStopSet().toString());
		
		TokenStream source = analyzer.tokenStream("content", new StringReader(query));
		StopFilter stopFilter= new StopFilter(org.apache.lucene.util.Version.LUCENE_34,source,EnglishAnalyzer.getDefaultStopSet(),true);
		stopFilter.setEnablePositionIncrements(false);
		PositionFilter pFilter = new PositionFilter(stopFilter);	
		ShingleFilter filter = new ShingleFilter(pFilter);
	    filter.setMaxShingleSize(8);
	    filter.setOutputUnigrams(true);

	    OffsetAttribute offsetAttribute = filter.getAttribute(OffsetAttribute.class);
	    CharTermAttribute termAttribute = filter.getAttribute(CharTermAttribute.class);
	    Set<String> set = new LinkedHashSet<String>();
        try{
        	
	        while (filter.incrementToken())  {
				int startOffset = offsetAttribute.startOffset();
				int endOffset = offsetAttribute.endOffset();	
				String words = termAttribute.toString();
				words  = removeDuplicate(words);
				set.add(words);
		 }
	        printNgrams(set);
        
        }
        catch (IOException e) {
			e.printStackTrace();

        	}
	}
	
	public static void printNgrams(Set<String> set){
		
		Iterator it = set.iterator();
		while(it.hasNext()){	
		
		String words = (String)it.next();
		System.out.println(words);
		EdgeNGramTokenizer ngramToken = new EdgeNGramTokenizer(new StringReader(words) , Side.FRONT, 1, 3);
		EdgeNGramTokenFilter nFilter = new EdgeNGramTokenFilter(ngramToken, EdgeNGramTokenFilter.Side.FRONT , 1, 3);
		CharTermAttribute ntermAttribute = nFilter.getAttribute(CharTermAttribute.class);

		
		try {
			while(ngramToken.incrementToken()){
			 StringBuffer buffer =	new StringBuffer();
			 buffer.append(ntermAttribute.buffer());
			 String  ngram = buffer.toString();
			 System.out.println(words+" Ngrams :" + ngram);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		}

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
