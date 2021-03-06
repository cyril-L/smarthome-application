<%@page import="smarthome.core.DateUtils" %>

<html>
<head>
<meta name='layout' content='authenticated' />
</head>

<body>

	<nav class="aui-navgroup aui-navgroup-horizontal">
	    <div class="aui-navgroup-inner">
	        <div class="aui-navgroup-primary">
	            <ul class="aui-nav">
	                <li><g:link action="devicesGrid" controller="device" params="[favori: true]">Favoris</g:link></li>
	                <g:each var="tableauBord" in="${ tableauBords }">
						<li>
							<g:link action="devicesGrid" controller="device" params="[tableauBord: tableauBord]">${ tableauBord }</g:link>
						</li>	                
	                </g:each>
	            </ul>
	        </div><!-- .aui-navgroup-primary -->
	    </div><!-- .aui-navgroup-inner -->
	</nav>


	<g:applyLayout name="applicationHeader">
		<div class="aui-group aui-group-split">
			<div class="aui-item responsive">
				<h3>Prévisions à 48h, ${ house.location }</h3>
			</div>
			<div class="aui-item responsive">
				<div class="aui-buttons" style="margin-top:0px;">
					<g:link class="aui-button" action="dailyForecast" controller="houseWeather" id="${ house.id }">Prévisions à 7j</g:link>
				</div>
			</div>
		</div>
	</g:applyLayout>


	<g:applyLayout name="applicationContent">
		<div style="text-align:right;">
			<a style="font-size:x-small; font-weight:bold;" href="https://darksky.net/poweredby/" target="darksky">Powered by Dark Sky</a>
		</div>
		
		<div style="overflow-x:auto; margin-top:10px;">
		<table class="aui datatable">
		    <tbody>
		    	<g:set var="currentHourDate" value="${ DateUtils.truncHour(new Date()) }"/>
		    	<g:set var="dayOfYear" value="${ -1 }"/>
		    
		    	<g:each var="forecast" in="${ hourlyForecasts }">
		    		<g:if test="${ forecast.date[Calendar.DAY_OF_YEAR] != dayOfYear }">
		    			<g:set var="dayOfYear" value="${ forecast.date[Calendar.DAY_OF_YEAR] }"/>
		    			<tr style="background:#0747a6;">
				            <td colspan="6"><h4 style="color:#deebff;"><g:formatDate date="${ forecast.date }" format="EEEE dd/MM/yyyy"/></h4></td>
				        </tr>
		    		</g:if>
		    		
		    		<g:set var="rowStyle" value="${ currentHourDate == forecast.date ? 'font-weight:bold; color:#0747a6;' : '' }"/>
		    		
			        <tr style="${ rowStyle }">
			            <td><g:formatDate date="${ forecast.date }" format="H'h'"/></td>
			            <td>
			            	<span style="font-size:18px;"><i class="wi ${ forecast.style }"></i></span> ${ forecast.text }
			            	<span class="h6-nomargin" title="Couverture nuageuse">${ forecast.cloudCover }% <i class="wi wi-cloud"></i></span>
			            </td>
			            <td style="text-align:center; min-width:75px;">
			            	${ forecast.temperature }° <i style="font-size:14pt;" class="wi wi-thermometer"></i>
			            	<span class="h6-nomargin" title="Ressentie">${ forecast.apparentTemperature }°</span>
			            </td>
			            <td style="text-align:center;">
			            	${ forecast.precipProbability }% <i style="font-size:14pt;" class="wi wi-rain"></i>
			            	<span class="h6-nomargin">${ forecast.precipType }</span>
			            </td>
			            <td style="text-align:center;">
			            	${ forecast.windSpeed }km/h <i style="font-size:18pt;" class="wi wi-wind from-${ forecast.windBearing }-deg"></i>
			            	<span class="h6-nomargin" title="Rafale">${ forecast.windGust }km/h</span>
			            </td>
			            <td style="min-width:75px;">
			            	${ forecast.pressure }hPa <i style="font-size:14pt;" class="wi wi-barometer"></i>
			            	<g:if test="${ mobileAgent }"><br/></g:if><g:else>&nbsp;</g:else>
			            	${ forecast.humidity }% <i style="font-size:14pt;" class="wi wi-raindrop"></i>
			            	<g:if test="${ mobileAgent }"><br/></g:if><g:else>&nbsp;</g:else>
			            	${ forecast.uvIndex }uv <i style="font-size:14pt;" class="wi wi-hot"></i>
			            </td>
			        </tr>
		        </g:each>
		    </tbody>
		</table>
		</div>
	</g:applyLayout>
	
</body>
</html>