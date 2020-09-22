<%@ page import="smarthome.automation.ChartViewEnum" %>


<div class="aui-tabs horizontal-tabs">
    <div id="tabs-chartconso">
     	<g:render template="/deviceType/linky/linkyChartConso"/>
    </div>
    
    <div id="tabs-synthese" style="margin-top:15px">
    	<g:render template="/deviceType/linky/linkyChartSynthese"/>
    </div>
</div>

