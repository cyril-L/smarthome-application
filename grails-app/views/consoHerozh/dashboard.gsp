<html>
<head>
    <meta name='layout' content='consoherozh-app' />
</head>
<body>
<h1>Tableau de bord</h1>

<div class="dashboard">

<counter-connected class="draggable" title="Linky" <g:if test="${linky}">connected</g:if>>
    <ion-icon slot="icon" name="flash-outline"></ion-icon>
    <ion-icon slot="handle" name="reorder-three" class="handle"></ion-icon>
    <g:if test="${linky}">
        <g:link controller="device" action="deviceChart" params="['device.id': linky.id]" role="button" slot="view-entries-btn" class="text-secondary">
            <ion-icon name="bar-chart-outline" style="font-size: 32px;"></ion-icon>
        </g:link>
    </g:if>
    <g:link controller="dataChallenge" action="personalData" role="button" slot="connect-btn">
        <button type="button" class="btn btn-outline-secondary" id="connect-btn">Connecter</button>
    </g:link>
</counter-connected>

<g:if test="${linky}">
<script type="text/javascript">
    querySelectorLast(document, 'counter-connected').entries = new CounterEntries("kWh", "kWh", counterEntriesFromDailyConsumption(
        <%= linkyDays %>
    ));
</script>
</g:if>

<counter-manual class="draggable" title="Eau">
    <ion-icon slot="icon" name="water-outline"></ion-icon>
    <ion-icon slot="handle" name="reorder-three" class="handle"></ion-icon>
</counter-manual>

<script type="text/javascript">
    querySelectorLast(document, 'counter-manual').entries = new CounterEntries("m³", "L", [
        { date: '2020-10-17', value: 25955/1000 },
        { date: '2020-11-17', value: 26500/1000 },
        { date: '2020-12-15', value: 27425/1000 },
        { date: '2021-01-07', value: 28251/1000 }
    ]);
</script>

<counter-manual class="draggable" title="Gaz">
    <ion-icon slot="icon" name="flame-outline"></ion-icon>
    <ion-icon slot="handle" name="reorder-three" class="handle"></ion-icon>
</counter-manual>

<script type="text/javascript">
    querySelectorLast(document, 'counter-manual').entries = new CounterEntries("m³", "L", []);
</script>

<div class="card draggable">
    <asset:image src="join-group.jpg" class="card-img-top handle" alt="Bannière ConsoHerozh"/>
    <div class="card-body">
        <p class="card-text">Les ConsoHerozh : Consommateurs d’énergie responsables et informés</p>
    </div>
    <div class="card-footer" style="text-align: right;">
        <a href="https://www.facebook.com/groups/422285815424568/" target="_blank" rel="noopener noreferrer" class="btn btn-outline-secondary" role="button" >
            <ion-icon name="logo-facebook"></ion-icon>
            Rejoignez-nous !
        </a>
    </div>
</div>

</div>

</body>
</html>