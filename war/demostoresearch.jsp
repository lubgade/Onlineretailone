<%@ page
contentType="text/html;charset=UTF-8"
import="java.net.URLEncoder"
import="java.text.SimpleDateFormat"
import="java.util.Collection"
import="java.util.Date"
import="java.util.Iterator"

import="org.apache.lucene.analysis.standard.StandardAnalyzer"
import="org.apache.lucene.document.Document"
import="org.apache.lucene.index.GAEIndexReader"
import="org.apache.lucene.index.GAEIndexReaderPool"
import="org.apache.lucene.queryParser.MultiFieldQueryParser"
import="org.apache.lucene.search.BooleanClause"
import="org.apache.lucene.search.highlight.SimpleHTMLFormatter"
import="org.apache.lucene.search.highlight.Highlighter"
import="org.apache.lucene.search.highlight.QueryScorer"
import="org.apache.lucene.search.IndexSearcher"
import="org.apache.lucene.search.Query"
import="org.apache.lucene.search.TopDocs"
import="org.apache.lucene.search.ScoreDoc"
import="org.apache.lucene.search.TopScoreDocCollector"
import="com.liferay.util.*"
%><%!
private static StandardAnalyzer analyzer=new StandardAnalyzer(org.apache.lucene.util.Version.LUCENE_34);

public void jspInit() {
}

static String[] matchFields=new String[] {"storeName", "storeDesc", "managerName", "address"};
static BooleanClause.Occur[] matchFlags=new BooleanClause.Occur[] {BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD, BooleanClause.Occur.SHOULD};
%><%
String queryString=ParamUtil.getString(request, "q", "").trim();
if ("".equals(queryString)) {
  queryString="Spencers OR Chikkalasandra OR Padmanabhanagar";
}
Query queryObject=MultiFieldQueryParser.parse(org.apache.lucene.util.Version.LUCENE_34,queryString, matchFields, matchFlags, analyzer);
GAEIndexReaderPool readerPool = GAEIndexReaderPool.getInstance();
GAEIndexReader indexReader = readerPool.borrowReader("store");
if (indexReader == null) {
  out.println("null");
  return;
}
int maxDoc = indexReader.maxDoc();
int numDocs = indexReader.numDocs();
int hitsPerPage = 10;
IndexSearcher searcher = new IndexSearcher(indexReader);
TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);

searcher.search(queryObject, collector);
ScoreDoc[] hits = collector.topDocs().scoreDocs;
int resultCount=hits.length;
readerPool.returnReader(indexReader);
%>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>GAELucene Search Demo</title>
<style>
</style>
</head>

<body marginheight="3" topmargin="3">

<table border="0" width="100%" cellpadding="0" cellspacing="0">
<tr>
<td align="left" valign="top"><a href="http://code.google.com/p/gaelucene/"><h1>GAELucene</h1></a></td>
<form action="?" method="get" id="form">
<td align="right" valign="top">
<input type="text" class="input" title="GAELucene Search" value="<%=Html.escape(queryString, true)%>" maxlength="60" size="60" name="q" autocomplete="off"/>
<input type="submit" name="btnG" value="GAELucene Search"/>
</td>
</form>
</tr>
</table>

<hr>
maxDoc:<%=maxDoc%><br>
numDocs:<%=numDocs%><br>
queryObject:<%=queryObject%><br>

<table border="0" width="100%" cellpadding="0" cellspacing="0" style="background:#e3eaf4;">
<tr>
<td><b>Web</b></div></td>
<td align="right"><p>Match <b><%=queryString%></b> total <b><%=resultCount%></b> Result</p></td>
</tr>
</table>

<h2 class="hd">Search Result</h2>
<div>
<ol>
<%
	if(resultCount > 0) { 
	  QueryScorer queryScorer=new QueryScorer(queryObject);
	  SimpleHTMLFormatter htmlFormater=new SimpleHTMLFormatter("<font color=#ff0000>", "</font>");
	 Highlighter highlighter = new Highlighter(htmlFormater,queryScorer);
	  for (int i=0; i < resultCount; i++) {
	    int docid = hits[i].doc;
		Document doc=searcher.doc(docid);
	    float score=hits[i].score;
	    String title=(doc.getFieldable("storeName") != null) ? doc.getFieldable("storeName").stringValue() : "";
	    String content=(doc.getFieldable("storeDesc") != null) ? doc.getFieldable("storeDesc").stringValue() : "";
	    String author=(doc.getFieldable("managerName") != null) ? doc.getFieldable("managerName").stringValue() : "";
	    String pubtime=(doc.getFieldable("address") != null) ? doc.getFieldable("address").stringValue() : "";
	    String id=(doc.getFieldable("id") != null) ? doc.getFieldable("id").stringValue() : "";
		
	    String titleshighlighted = highlighter.getBestFragment(analyzer, "storeName", title);
		if (titleshighlighted == null ) titleshighlighted =title;
		String contenthighlighted = highlighter.getBestFragment(analyzer, "address", content);

%>
<li>
<h3>
<%=titleshighlighted%>
</h3>
<div>
<%=contenthighlighted%>....<br>
<span class=gl><%=pubtime%> - <%=author%> -<%=id %>></span>
</div>
</li><br>
<%
  }
}%>
</ol>
</div>

<hr>
<p><a href="http://code.google.com/" target="_blank"><img src="http://code.google.com/appengine/images/appengine-silver-120x30.gif" alt="Powered by Google App Engine" border="0"/></a> & <a href="http://lucene.apache.org/" target="_blank"><img src="/images/lucene-green-120x18.gif" alt="Powered by Lucene" border="0"/></a><br></p>

</body>
</html>