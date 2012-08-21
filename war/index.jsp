<%@page contentType="text/html; charset=UTF-8"%><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<title>GAELucene - Run Lucene on Google AppEngine</title>
</head>

<body>
<table border="0" width="100%" cellpadding="0" cellspacing="0">
<tr>
<td align="left" valign="top"><a href="http://code.google.com/p/gaelucene/"><h1>GAELucene</h1></a></td>
<td align="right" valign="top"><%=(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()))%><br>
<a href="<%=org.apache.gaelucene.config.GAELuceneConfig.getUrlPattern()%>/index">Dashboard</a> | <a href="http://so.3gmatrix.cn/">Demo</a>
</td>
</tr>
</table>

<hr>
<p><i>Run Lucene applications on Google AppEngine!</i></p>
<a href="<%=org.apache.gaelucene.config.GAELuceneConfig.getUrlPattern()%>/index">Go to Dashboard</a>

<div style="clear:both;"></div>
<hr>
<p><a href="http://code.google.com/" target="_blank"><img src="http://code.google.com/appengine/images/appengine-silver-120x30.gif" alt="Powered by Google App Engine" border="0"/></a> & <a href="http://lucene.apache.org/" target="_blank"><img src="/images/lucene-green-120x18.gif" alt="Powered by Lucene" border="0"/></a> & <a href="http://code.google.com/p/gaelucene/">GAELucene</a><br></p>
</body>
</html>