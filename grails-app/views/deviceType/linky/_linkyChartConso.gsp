<%@ page import="smarthome.automation.ChartViewEnum" %>


<div id="chartDivConso" data-chart-type="${ command.deviceImpl.defaultChartType().factory }">
	<h6 class="h6">Chargement...</h6>
	<div class="aui-progress-indicator">
	    <span class="aui-progress-indicator-value"></span>
	</div>
	<g:render template="${ command.deviceImpl.chartDataTemplate() }" model="[command: command,
		datas: chart.values, chart: chart, compareChart: compareChart]"/>
</div>
