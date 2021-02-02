<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <meta charset="UTF-8" />

    <title><g:layoutTitle default="${g.meta(name: 'app.code') }"/></title>
    <link rel="shortcut icon" href="${assetPath(src: 'favicon.ico')}" type="image/x-icon">
    <link rel="apple-touch-icon" href="${assetPath(src: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${assetPath(src: 'apple-touch-icon-retina.png')}">

    <script type="module" src="https://unpkg.com/ionicons@5.2.3/dist/ionicons/ionicons.esm.js"></script>
    <script nomodule="" src="https://unpkg.com/ionicons@5.2.3/dist/ionicons/ionicons.js"></script>

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-giJF6kkoqNQ00vy+HMDP7azOuL0xtbfIcaT9wjKHr8RbDVddVHyTfAAsrekwKmP1" crossorigin="anonymous">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.0-beta1/dist/js/bootstrap.bundle.min.js" integrity="sha384-ygbV9kiqUc6oa4msXn9868pTtWMgiQaeYH7/t7LECLbyPA2x65Kgf80OJFdroafW" crossorigin="anonymous"></script>

    <script src="https://cdn.jsdelivr.net/npm/chart.js@2.9.4/dist/Chart.min.js" integrity="sha256-t9UJPrESBeG2ojKTIcFLPGF7nHi2vEc7f5A2KpH/UBU=" crossorigin="anonymous"></script>

    <!--Let browser know website is optimized for mobile-->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <script src="https://unpkg.com/packery@2/dist/packery.pkgd.min.js"></script>
    <script src="https://unpkg.com/draggabilly@2/dist/draggabilly.pkgd.min.js"></script>


    <asset:stylesheet src="application.css"/>
    <asset:stylesheet src="consoherozh.css"/>

    <asset:javascript src="consoherozh.js"/>
    <asset:javascript src="utils.js"/>
    <asset:javascript src="bootstrap-components.js"/>
    <asset:javascript src="components.js"/>
    <asset:javascript src="consoherozh.js"/>
    <g:layoutHead/>
</head>
<body style="background: white;">

<g:include view="/layouts/headerAuthenticated.gsp"/>

<section id="content" role="main" <%= app.stateInsertAttr()  %>>
    <div class="container">
        <g:layoutBody/>
    </div>
</section>

<div id="ajaxDialog"></div>

<g:include view="/layouts/footer.gsp"/>

<asset:deferredScripts/>

</body>
</html>