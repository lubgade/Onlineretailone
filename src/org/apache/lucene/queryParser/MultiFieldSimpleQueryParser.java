package org.apache.lucene.queryParser;

/**
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
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
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;
import org.apache.lucene.analysis.tokenattributes.OffsetAttribute;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.MultiPhraseQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TermQuery;

/**
 * A QueryParser which constructs queries to search multiple fields.
 *
 * @author <a href="mailto:kelvin@relevanz.com">Kelvin Tan</a>
 * @version $Revision: 27 $
 */
public class MultiFieldSimpleQueryParser extends SimpleQueryParser
{

    private String[] fields;
    private Map boosts;

    public MultiFieldSimpleQueryParser(String[] fields, Analyzer a, Map boosts)
    {
        super("", a);
        this.fields = fields;
        this.boosts = boosts;
        
       	setValidFields(fields);
    }

    /** Parses a query string, returning a {@link org.apache.lucene.search.Query}.
     *  @param query  the query string to be parsed.
     *  @throws ParseException if the parsing fails
     */
    public Query parse(String query) throws ParseException {
      ReInit(new FastCharStream(new StringReader(query)));
      try {
        return Query("");
      }
      catch (ParseException tme) {
        // rethrow to include the original query:
        throw new ParseException("Cannot parse '" +query+ "': " + tme.getMessage());
      }
      catch (TokenMgrError tme) {
        throw new ParseException("Cannot parse '" +query+ "': " + tme.getMessage());
      }
      catch (BooleanQuery.TooManyClauses tmc) {
        throw new ParseException("Cannot parse '" +query+ "': too many boolean clauses");
      }
    }

    /**
     * @exception ParseException throw in overridden method to disallow
     */
    protected Query getFieldQuery(String field, String queryText, boolean isPhrase)  throws ParseException {
      // Use the analyzer to get all the tokens, and then build a TermQuery,
      // PhraseQuery, or nothing based on the term count

      if ((!"".equals(field) && validFieldSet.contains(field)) || this.fields.length == 1) {
        if ("".equals(field) || !(validFieldSet.contains(field))) {
          field = this.fields[0];
        }
        return super.getFieldQuery(field, queryText, isPhrase);
      }

      Vector v = new Vector();
      org.apache.lucene.analysis.Token t;
      int positionCount = 0;
      boolean severalTokensAtSamePosition = false;

      // If the specified field is not a valid one, treat it as a common query text
      //changed-------------------------------------------------------------------
      //if(! validFieldSet.contains(field)) {
      //changed-------------------------------------------------------------------
      if(!"".equals(field) && ! validFieldSet.contains(field)) {
        TokenStream source = analyzer.tokenStream(this.field, new StringReader(field));
        OffsetAttribute offsetAttribute = source.getAttribute(OffsetAttribute.class);
        CharTermAttribute termAttribute = source.getAttribute(CharTermAttribute.class);
        try {
  		while (source.incrementToken()) {
  	    	int startOffset = offsetAttribute.startOffset();
  	    	int endOffset = offsetAttribute.endOffset();
  		    String s = termAttribute.toString();
  		    	t = new org.apache.lucene.analysis.Token(s,startOffset,endOffset );
  		   if(! s.equals("")) {
  		      v.addElement(t);
  		    }
  		  }
  	} catch (IOException e1) {
  		// TODO Auto-generated catch block
  		e1.printStackTrace();
  	}
        try {
          source.close();
        }
        catch (IOException e) {
          // ignore
        }
        //begin---------------------------------------------------------------
        // reset the invalid field name
        //field = this.field;
        field = "";
        //end-----------------------------------------------------------------
      }

//System.out.println("MultiFieldSimpleQueryParser.getFieldQuery(" + field + "," + queryText + ")");
      TokenStream source = analyzer.tokenStream(field, new StringReader(queryText));
      OffsetAttribute offsetAttribute = source.getAttribute(OffsetAttribute.class);
      CharTermAttribute termAttribute = source.getAttribute(CharTermAttribute.class);
    
      try {
  		while (source.incrementToken()) {
  		 
  		 	int startOffset = offsetAttribute.startOffset();
  	    	int endOffset = offsetAttribute.endOffset();
  		    String s = termAttribute.toString();
  		    t = new org.apache.lucene.analysis.Token(s,startOffset,endOffset );
  			v.addElement(t);
  //System.out.println("SimpleQueryParser.getFieldQuery() - t[" + t + "], t.getPositionIncrement:" + t.getPositionIncrement());
  		  if (t.getPositionIncrement() != 0)
  		    positionCount += t.getPositionIncrement();
  		  else
  		    severalTokensAtSamePosition = true;
  		}
  	} catch (IOException e1) {
  		// TODO Auto-generated catch block
  		e1.printStackTrace();
  	}
      try {
        source.close();
      }
      catch (IOException e) {
        // ignore
      }

      if (v.size() == 0) {
        return null;
      }
      if (v.size() == 1) {
        BooleanQuery q = new BooleanQuery(true);
        for (int m = 0; m < this.fields.length; m++) {
          t = (org.apache.lucene.analysis.Token) v.elementAt(0);
          TermQuery tq = new TermQuery(new Term(this.fields[m], t.term()));
          if (this.boosts.get(this.fields[m]) != null) {
            float boost = ((Float) this.boosts.get(this.fields[m])).floatValue();
            tq.setBoost(boost);
          }
          q.add(tq, BooleanClause.Occur.SHOULD);
        }
        return q;
      }
      
      // 含同义词的多个Token
      if (severalTokensAtSamePosition) {
        if (positionCount == 1) {
          // no phrase query:
          // 同义词检索 (synonym)
          // 如:"MS" -> {"ms", "microsoft", "微软"}
          BooleanQuery q = new BooleanQuery(true);
          for (int i = 0; i < v.size(); i++) {
            t = (org.apache.lucene.analysis.Token) v.elementAt(i);
//System.out.println("MultiFieldSimpleQueryParser.getFieldQuery() - t[" + i + "][" + t + "]");
            BooleanQuery sq = new BooleanQuery(true);
            for (int m=0; m<this.fields.length; m++) {
              TermQuery tq = new TermQuery(
                new Term(this.fields[m], t.term()));
              float boost = this.boosts.get(this.fields[m]) == null ? 1.0f : ((Float) this.boosts.get(this.fields[m])).floatValue();
              tq.setBoost(boost);
              sq.add(tq, BooleanClause.Occur.SHOULD);
            }
            q.add(sq, BooleanClause.Occur.SHOULD);
          }
          return q;
        }
        else {
          if(isPhrase) {
            // phrase query:
            // 含同义词的短语
            // 如:"ms appstore" -> {{"ms", "microsoft", "微软"} appstore} 
            BooleanQuery q = new BooleanQuery(true);
            for (int m=0; m<this.fields.length; m++) {
              MultiPhraseQuery mpq = new MultiPhraseQuery();
              mpq.setSlop(phraseSlop);
              List multiTerms = new ArrayList();
              for (int i = 0; i < v.size(); i++) {
                t = (org.apache.lucene.analysis.Token) v.elementAt(i);
                if (t.getPositionIncrement() == 1 && multiTerms.size() > 0) {
                  mpq.add((Term[])multiTerms.toArray(new Term[0]));
                  multiTerms.clear();
                }
                multiTerms.add(new Term(field, t.term()));
              }
              mpq.add((Term[])multiTerms.toArray(new Term[0]));
              q.add(mpq, BooleanClause.Occur.SHOULD);
            }
            return q;
          }
          else {
            //含同义词的多个Token
            BooleanQuery q = new BooleanQuery(true);
            List multiTerms = new ArrayList();
            for (int i = 0; i < v.size(); i++) {
              t = (org.apache.lucene.analysis.Token) v.elementAt(i);
              if (t.getPositionIncrement() == 1 && multiTerms.size() > 0) {
                BooleanQuery sq = new BooleanQuery(true);
                for (int m=0; m<this.fields.length; m++) {
                BooleanQuery ssq = new BooleanQuery(true);
                  for (int n=0; n<multiTerms.size(); n++) {
                    org.apache.lucene.analysis.Token oneToken = 
                     (org.apache.lucene.analysis.Token) multiTerms.get(n);
                    TermQuery tq = new TermQuery(
                      new Term(this.fields[m], oneToken.term()));
                    float boost = this.boosts.get(this.fields[m]) == null ? 1.0f : ((Float) this.boosts.get(this.fields[m])).floatValue();
                    tq.setBoost(boost);
                    ssq.add(tq, BooleanClause.Occur.SHOULD);
                  }
                  sq.add(ssq, BooleanClause.Occur.SHOULD);
                }
                q.add(sq, BooleanClause.Occur.MUST);
                multiTerms.clear();
              }
              multiTerms.add(new Term(field, t.term()));
            }
            if (multiTerms.size() > 0) {
              BooleanQuery sq = new BooleanQuery(true);
              for (int m=0; m<this.fields.length; m++) {
                BooleanQuery ssq = new BooleanQuery(true);
                for (int n=0; n<multiTerms.size(); n++) {
                  org.apache.lucene.analysis.Token oneToken = 
                   (org.apache.lucene.analysis.Token) multiTerms.get(n);
                  TermQuery tq = new TermQuery(
                    new Term(this.fields[m], oneToken.term()));
                  float boost = this.boosts.get(this.fields[m]) == null ? 1.0f : ((Float) this.boosts.get(this.fields[m])).floatValue();
                  tq.setBoost(boost);
                  ssq.add(tq, BooleanClause.Occur.SHOULD);
                }
                sq.add(ssq, BooleanClause.Occur.SHOULD);
              }
              q.add(sq, BooleanClause.Occur.MUST);
            }
            return q;
          }
        }
      }
      // 不含同义词的多个Token
      else {
   	    if(isPhrase) {
          BooleanQuery q = new BooleanQuery(true);
          for (int m=0; m<this.fields.length; m++) {
            PhraseQuery pq = new PhraseQuery();
            pq.setSlop(phraseSlop);
            for (int i = 0; i < v.size(); i++) {
              pq.add(new Term(this.fields[m], ((org.apache.lucene.analysis.Token) 
                v.elementAt(i)).term()));
            }
            q.add(pq, BooleanClause.Occur.SHOULD);
          }
          return q;
        }
        else {
          BooleanQuery q = new BooleanQuery(true);
          for (int i = 0; i < v.size(); i++) {
            t = (org.apache.lucene.analysis.Token) v.elementAt(i);
//System.out.println("MultiFieldSimpleQueryParser.getFieldQuery() - t[" + i + "][" + t + "]");
            BooleanQuery sq = new BooleanQuery(true);
            for (int m=0; m<this.fields.length; m++) {
              TermQuery tq = new TermQuery(
                new Term(this.fields[m], t.term()));
              float boost = this.boosts.get(this.fields[m]) == null ? 1.0f : ((Float) this.boosts.get(this.fields[m])).floatValue();
              tq.setBoost(boost);
              sq.add(tq, BooleanClause.Occur.SHOULD);
            }
            q.add(sq, BooleanClause.Occur.MUST);
          }
          return q;
        }
      }
    }
}
