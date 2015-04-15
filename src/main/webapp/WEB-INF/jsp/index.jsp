<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<c:set var="req" value="${pageContext.request}" />
<c:set var="uri" value="${req.requestURI}" />
<c:set var="url">${req.requestURL}</c:set>

<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1"> 
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    
<title>Top Posts</title>

<base href="${fn:substring(url, 0, fn:length(url) - fn:length(uri))}${req.contextPath}/" />
<link rel='stylesheet' href='webjars/bootstrap/3.3.4/css/bootstrap.min.css'>
<script src="webjars/jquery/2.1.3/jquery.min.js"></script>
<script src="webjars/bootstrap/3.3.4/js/bootstrap.min.js"></script>
<link rel='stylesheet' href='resources/styles/main.css'>

</head>
<body>

    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="#">Top Posts</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <ul class="nav navbar-nav">
            <li class="active"><a href="#">Home</a></li>
            <li><a href="#about">About</a></li>
            <li><a href="#contact">Contact</a></li>
          </ul>
        </div><!--/.nav-collapse -->
      </div>
    </nav>

	<div class='container'>
		<div class='starter-template'>
			<h4 class='text-left'>Check out the posts for today</h4>
			<div class='docks-container'>
				<div class='row'>
					<c:forEach items="${posts}" var="post">
						<div class='col-xs-12 single-dock-container'>
							<div class='col-xs-12'>
								<h3><a href="${post.url}">${post.title}</a></h3>
							</div>
							<div class='col-xs-12'>
								<span>via <a href="${post.feed.url}">${post.feed.name}</a></span>
							</div>
							<div class='col-xs-12'>
								<div>${post.html}</div>
							</div>
						</div>
					</c:forEach>
				</div>
			</div>
		
		</div>
	</div><!-- /.container -->
    
</body>
</html>