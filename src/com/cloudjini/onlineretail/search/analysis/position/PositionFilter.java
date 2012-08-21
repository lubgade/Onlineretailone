package com.cloudjini.onlineretail.search.analysis.position;


import java.io.IOException;

import org.apache.lucene.analysis.TokenFilter;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.tokenattributes.PositionIncrementAttribute;

/** Set the positionIncrement of all tokens to the "positionIncrement",
 * except the first return token which retains its original positionIncrement value.
 * The default positionIncrement value is zero.
 */
public final class PositionFilter extends TokenFilter {

  /** Position increment to assign to all but the first token - default = 0 */
  private int positionIncrement = 0;
  

  
  private PositionIncrementAttribute posIncrAtt;

  /**
   * Constructs a PositionFilter that assigns a position increment of zero to
   * all but the first token from the given input stream.
   * 
   * @param input the input stream
   */
  public PositionFilter(final TokenStream input) {
    super(input);
    posIncrAtt = addAttribute(PositionIncrementAttribute.class);
  }

  /**
   * Constructs a PositionFilter that assigns the given position increment to
   * all but the first token from the given input stream.
   * 
   * @param input the input stream
   * @param positionIncrement position increment to assign to all but the first
   *  token from the input stream
   */
  public PositionFilter(final TokenStream input, final int positionIncrement) {
    this(input);
    this.positionIncrement = positionIncrement;
  }

  @Override
  public final boolean incrementToken() throws IOException {
    if (input.incrementToken()) {
        posIncrAtt.setPositionIncrement(positionIncrement);
        return true;
    } else {
      return false;
    }
  }

  @Override
  public void reset() throws IOException {
    super.reset();
   
  }
}