<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<title><g:layoutTitle default="${g.meta(name: 'app.code') }"/></title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		
		<link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}" type="image/x-icon">		
		<link rel="apple-touch-icon" href="${assetPath(src: 'apple-touch-icon.png')}">
		<link rel="apple-touch-icon" sizes="114x114" href="${assetPath(src: 'apple-touch-icon-retina.png')}">
		
		<asset:stylesheet src="application.css"/>
		<asset:javascript src="application.js"/>
		<script type="text/javascript" src="https://www.google.com/jsapi"></script>
		<script type="text/javascript" src="https://html2canvas.hertzen.com/dist/html2canvas.min.js"></script>
		<g:layoutHead/>
	</head>
	<body class="${pageProperty(name:'body.class')}" onload="${pageProperty(name: 'body.onload')}">

	<div id="capture" style="padding: 10px; background: #f5da55; display: none;">
		<h4 style="color: #000; ">Hello world!</h4>
	</div>

		<g:include view="/layouts/headerAuthenticated.gsp"/>
		
		<section id="content" role="main" <%= app.stateInsertAttr()  %>>
			<g:layoutBody/>
		</section>
		
		<div id="ajaxDialog"></div>
		
		<g:include view="/layouts/footer.gsp"/>
		
		<asset:script type="text/javascript">
			onLoadGoogleChart()
		</asset:script>
		
		<asset:deferredScripts/>
	</body>
</html>
