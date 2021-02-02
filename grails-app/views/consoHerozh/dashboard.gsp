<html>
<head>
    <meta name='layout' content='consoherozh-app' />
</head>
<body>
<h1 class="mb-4 mt-2">Tableau de bord</h1>

<div class="dashboard">

<counter-connected
  class="draggable"
  title="Linky"
  connect-url="${createLink(controller: "dataChallenge", action: "personalData")}"
  <g:if test="${linky}">
      connected
      view-entries-url="${createLink(controller: "device", action: "deviceChart", params:['device.id': linky.id])}"
  </g:if>>
    <ion-icon slot="icon" name="flash-outline"></ion-icon>
    <ion-icon slot="handle" name="reorder-three" class="handle"></ion-icon>
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