<h3>Tendance électricité</h3>

<g:if test="${meanPreviousMonth && meanThisMonth}">
    <g:set var="trend" value="${100 * (meanThisMonth - meanPreviousMonth) / meanPreviousMonth}"/>
</g:if>
<g:else>
    <g:set var="trend" value="${null}"/>
</g:else>

<g:if test="${trend && trend > 0}">
    <g:set var="trendIcon" value="↗"/>
    <g:set var="trendClass" value="bad"/>
</g:if>
<g:elseif test="${trend && trend < 0}">
    <g:set var="trendIcon" value="↘"/>
    <g:set var="trendClass" value="good"/>
</g:elseif>
<g:else>
    <g:set var="trendIcon" value="="/>
    <g:set var="trendClass" value="neutral"/>
</g:else>

<div class="aui-group" style="margin:10px 0px; padding:10px 0px">
    <div class="aui-item">
        <div class="trend-${trendClass}" style="text-align:center;">
            <svg viewBox="0 0 16 20" style="width: 100px; height: 100px;">
                <use href="${assetPath(src: 'noun_Electricity_1142300.svg')}#electricity"></use>
            </svg>
            <g:if test="${trend}">
                <h2>
                    ${trendIcon} <g:formatNumber number="${trend}" maxFractionDigits="0"/> %
                </h2>
            </g:if>
        </div>
    </div>
    <div class="aui-item separator-left">
        <h5 style="margin-top:10px">Consommation moyenne</h5>
        <ul class="label" style="padding-inline-start:5px;">
            <li>
                <g:formatDate date="${previousMonth}" format="MMMM"/> :
                <g:if test="${meanPreviousMonth}">
                    <g:formatNumber number="${meanPreviousMonth / 1000}" maxFractionDigits="0"/>  kWh / jour
                </g:if>
                <g:else>
                    non disponible
                </g:else>
            </li>
            <li>
                du 1er au <g:formatDate date="${today}" format="d MMMM"/> :
                <g:if test="${meanThisMonth}">
                    <g:formatNumber number="${meanThisMonth / 1000}" maxFractionDigits="0"/>  kWh / jour
                </g:if>
                <g:else>
                    non disponible
                </g:else>
            </li>
        </ul>
    </div>
</div>

<div style="text-align:right; font-weight:bold;">
    <g:link controller="device" action="deviceChart" params="['device.id': device.id]">
        <span class="aui-icon aui-icon-small aui-iconfont-arrows-right"></span> Voir ma conso
    </g:link>
</div>
