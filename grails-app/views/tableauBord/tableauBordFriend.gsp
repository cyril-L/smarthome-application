<html>
<head>
<meta name='layout' content='authenticated-chart' />
</head>

<body>

	<g:applyLayout name="applicationContent" params="[panelContentClass: 'panelContentGrey']">
		<div class="aui-group">
			<div class="aui-item responsive" style="width:350px">
				<div class="filActualite" style="padding:15px;">
					<g:render template="/profil/profilPublic"></g:render>
				</div>
			</div>
			<div class="aui-item responsive">
				<div class="filActualite" style="padding:15px;">
					<div id="divHouseSyntheseConfort" async-url="${ g.createLink(controller: 'house', action: 'syntheseConfort', id: house?.id, params: [compare: true]) }"></div>
				</div>
				<br/>
				<div class="filActualite" style="padding:15px;">
					<div id="divHouseSyntheseConsommation" async-url="${ g.createLink(controller: 'house', action: 'syntheseConsommation', id: house?.id, params: [compare: true]) }"></div>
				</div>
				<br/>
				<div class="filActualite" style="padding:15px;">
					<div id="divDeviceShareGrid" async-url="${ g.createLink(controller: 'device', action: 'deviceShareGrid', id: user.id) }"></div>
				</div>
			</div>
		</div>
	
	</g:applyLayout>
	
</body>
</html>