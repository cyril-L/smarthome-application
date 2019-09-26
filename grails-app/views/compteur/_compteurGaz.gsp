<div class="card">

	<div class="card-header">
		<div class="card-actions float-right">
			<div class="dropdown show">
				<a href="#" data-toggle="dropdown" data-display="static">
					<app:icon name="more-horizontal"/>
				</a>
				<div class="dropdown-menu dropdown-menu-right">
					<g:if test="${ adict }">
						<g:link class="dropdown-item" action="edit" controller="notificationAccount" id="${ adict.id }">Service ADICT</g:link>
					</g:if>
					<g:if test="${ house?.compteurGaz }">
						<g:link class="dropdown-item" action="edit" controller="device" id="${ house.compteurGaz.id }">Compteur</g:link>
					</g:if>
					<g:link class="dropdown-item" action="resetCompteurGaz">Réinitialiser</g:link>
				</div>
			</div>
		</div>
		<h4 class="card-title"><app:icon name="fire" lib="awesome"/> Gaz</h4>
	</div> <!-- div.card-header -->
	
	<div class="card-body">
		<div>
			<g:if test="${ house?.compteurGaz && house.compteurGaz.id == adictDevice?.id }">
				<div class="row mb-4 text-center">
					<div class="col">
						<g:render template="/compteur/modeleGaz" model="[compteur: house?.compteurGaz]"/>
					</div>
					<div class="col">
						<div class="btn-group-vertical">
							<g:link class="btn btn-primary mb-2" action="adict" controller="adict"><app:icon name="user-check"/> ADICT</g:link>
							<g:link class="btn btn-primary mb-2 ${ !adictDevice ? 'disabled' : ''}" action="deviceChart" controller="device" params="['device.id': adictDevice?.id]" disabled="${ adictDevice ? 'false' : 'true'}"><app:icon name="bar-chart"/> Consommations</g:link>
						</div>
					</div>
				</div>
				
				<g:render template="/templates/messageInfo" model="[message: 'Gazpar with GRDF ADICT', icon: 'check-circle']"/>
			</g:if>
			
			<g:elseif test="${ house?.compteurGaz }">
				<div class="row mb-4 text-center">
					<div class="col">
						<g:render template="/compteur/modeleGaz" model="[compteur: house?.compteurGaz]"/>
					</div>
					<div class="col">
						<div class="btn-group-vertical">
							<g:link class="btn btn-primary mb-2" action="saisieIndex" id="${ house.compteurGaz.id }"><app:icon name="edit"/> Saisie index</g:link>
							<g:link class="btn btn-primary mb-2" action="deviceChart" controller="device" params="['device.id': house.compteurGaz.id]"><app:icon name="bar-chart"/> Consommations</g:link>
						</div>
					</div>
				</div>
				
				<g:render template="/templates/messageInfo" model="[message: house.compteurGaz.label, icon: 'check-circle']"/>
			</g:elseif>
			
			<g:else>
				
				<h5 class="mb-4 font-weight-bold">Votre compteur gaz n'est pas encore configuré. Veuillez choisir le type de compteur installé chez vous et cliquez sur suivant :</h5>
				
				<g:form action="registerCompteur">
					<g:hiddenField name="compteurType" value="gaz"/>
					
					<label class="custom-control custom-radio">
						<g:radio name="compteurModel" value="Gazpar" class="custom-control-input" checked="true"/>
						<span class="custom-control-label">Gazpar - GRDF ADICT</span>
					</label>
					<div class="row">
						<div class="col">
							<asset:image src="gazpar.png" class="ml-4" style="height:75px;"/>
						</div>
						<div class="col-8">
							<small class="font-text text-muted">Les consommations seront téléchargées automatiquement par le service GRDF ADICT</small>
						</div>
					</div>
					
					<label class="custom-control custom-radio mt-4">
						<g:radio name="compteurModel" value="Gaz" class="custom-control-input"/>
						<span class="custom-control-label">Compteur non communicant ou existant</span>
					</label>
					<div class="row">
						<div class="col">
							<asset:image src="compteur-gaz.png" class="ml-4" style="height:75px;"/>
						</div>
						<div class="col-8">
							<small class="font-text text-muted">Les index de consommation seront saisis manuellement ou envoyés automatiquement par impulsions</small>
							
							<g:select class="form-control" name="compteurGaz.id" from="${ compteurGazs }"
         								optionKey="id" optionValue="label" placeholder="Select compteur" noSelection="[null: '']"/>
						</div>
					</div>
					
					<div class="mt-4 text-right">
						<button class="btn btn-primary"><app:icon name="chevron-right"/> Suivant</button>
					</div>
				</g:form>
			</g:else>
			
			<g:if test="${ house?.compteurGaz }">
				<h6 class="text-center">Dernière valeur : il y a <app:formatTimeAgo date="${ house.compteurGaz.dateValue }"/></h6>
			</g:if>
		</div>
	</div> <!-- div.card-body -->
	
</div> <!-- div.card -->