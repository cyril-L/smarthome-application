<g:if test="${ notificationAccount?.jsonConfig?.usage_point_id }">

    <h3>Vous avez autorisé le transfert de vos données Enedis</h3>

    <p>Le transfert de vos consommations électrique vers ALOEN est activé.Ce site est autorisé à collecter les données Enedis pour que vous puissiez disposer d’informations sur votre consommation d’électricité.</p>
    <p>Vous pouvez à tout moment <a href="#">révoquer votre consentement ici</a>, ou sur votre espace client Enedis.</p>
    <br/>TODO PRM, données, finalité, politique de confidentialité ?

</g:if>
<g:else>
    <h3>Autoriser le transfert de vos données Enedis</h3>

    <h5>Enedis gère le réseau d’électricité jusqu’au compteur d’électricité.
    Pour consulter votre consommation, autorisez Enedis à nous transmettre vos données Linky.</h5>

    <div class="aui-group aui-group-split">
        <div class="aui-item">
            <p>En cliquant sur ce bouton, vous allez accéder à votre compte personnel
            Enedis où vous pourrez donner votre accord pour qu’Enedis nous transmette vos données.</p>
        </div>
        <div class="aui-item">
            <g:link action="authorize" controller="dataConnect">
                <asset:image src="vert-enedis.png" width="200px"/>
            </g:link>
        </div>
    </div>

    <h6>Pour donner votre autorisation, vous devez créer un compte personnel Enedis. Il vous permet également de suivre et gérer vos données de consommation d’électricité.
    Munissez-vous de votre facture d’électricité pour créer votre espace.</h6>

    <h6>Enedis est le gestionnaire du réseau public de distribution d’électricité sur 95% du territoire français continental.</h6>
</g:else>
