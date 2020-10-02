<g:if test="${ name == 'no-data' }">
    <h3>⌛ Données en cours de récupération</h3>
    <p>Vos données de consommation sont en cours de récupération auprès d’Enedis. Cette opération peut prendre quelques minutes…</p>
</g:if>
<g:else>
    <h3>Vos données de consommation sont disponibles</h3>
    <p>
        Voyons voir
        <g:if test="${linky}">
            <g:link controller="device" action="deviceChart" params="['device.id': linky.id]">
                🔎
            </g:link>
        </g:if>
        <g:else>🔎</g:else>
    </p>
</g:else>
