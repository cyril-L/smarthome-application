<%@ page import="smarthome.automation.ChartViewEnum" %>

<g:set var="compteur" value="${ command.device.newDeviceImpl() }"/>

<p class="separator" style="text-align:center; font-weight:bold; font-size: medium;">Synthèse
	<g:if test="${ command.viewMode == ChartViewEnum.day }">
		<g:formatDate date="${ command.dateChart }" format="dd/MM/yyyy"/>
	</g:if>
	<g:elseif test="${ command.viewMode == ChartViewEnum.month }">
		<g:formatDate date="${ command.dateChart }" format="MMMM yyyy"/>
	</g:elseif>
	<g:else>
		<g:formatDate date="${ command.dateChart }" format="yyyy"/>
	</g:else>
</p>

<g:set var="totalWh" value="${0.0}"/>

<div style="overflow-x:auto;">
	<table class="aui app-datatable" style="margin-top:20px;" data-client-pagination="true">
		<thead>
			<tr>
				<th>Période</th>
				<th style="text-align:center">Consommation</th>
			</tr>
		</thead>
		<tbody>
			<g:each var="data" in="${ chart?.values?.sort { it.key } }">
				<tr>
					<td>
						<g:if test="${ command.viewMode == ChartViewEnum.day }">
							<g:formatDate date="${ data.key }" format="HH:mm"/>
						</g:if>
						<g:elseif test="${ command.viewMode == ChartViewEnum.month }">
							<g:formatDate date="${ data.key }" format="EEEE dd/MM/yyyy"/>
						</g:elseif>
						<g:else>
							<g:formatDate date="${ data.key }" format="MMMM yyyy"/>
						</g:else>	
					</td>
				<td style="text-align:center; font-weight:bold;"><span class="link">
					<g:set var="wh" value="${data.value.find{ it.name?.startsWith("base") }?.value}"/>
					<g:if test="${wh}">
						<g:set var="totalWh" value="${totalWh + wh}"/>
						<g:if test="${command.viewMode == ChartViewEnum.day}">
							<g:formatNumber number="${wh}" maxFractionDigits="1"/>  Wh
						</g:if>
						<g:else>
							<g:formatNumber number="${wh / 1000}" maxFractionDigits="1"/>  kWh
						</g:else>
					</g:if>
				</span></td>
				</tr>
			</g:each>
		</tbody>
		
		<g:set var="googleChartTarifValues" value="${ googleChartTarif?.values?.values() }"/>
		
		<tfoot>
			<td><strong>TOTAL</strong></td>
			<td style="text-align:center; font-weight:bold;">
				<g:formatNumber number="${totalWh / 1000}" maxFractionDigits="1"/> kWh
			</td>
		</tfoot>
	</table>
</div>    

