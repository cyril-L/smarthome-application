<table class="table mt-2 app-datatable">
	<thead>
		<tr>
			<th></th>
			<th class="text-right">Référence (kWh)</th>
			<th class="text-right">Action (kWh)</th>
			<th class="text-right">Economie (%)</th>
			<th class="text-right">Classement</th>
			<th class="column-2-buttons"></th>
		</tr>
	</thead>
	
	<tbody>
	
		<!-- Niveau 1 : données du défi -->
		
		<tr class="">
			<td><h4 class="font-weight-bold">${ defi.libelle }</h4></td>
			<td class="text-right">${ defi.reference_global() }</td>
			<td class="text-right">${ defi.action_global() }</td>
			<td class="text-right">${ defi.economie_global }</td>
			<td class="text-right">${ defi.classement_global }</td>
			<td class="column-2-buttons">
				<a id="calculer-resultat-defi-button" data-url="${ g.createLink(action: 'calculerResultat', params: ['defi.id': defi.id]) }" class="btn btn-light" title="Calculer résultat"><app:icon name="cpu"/></a>
				<a id="defi-participant-button" class="btn btn-light" data-url="${ g.createLink(action: 'dialogDefiParticipant', params:['defi.id': defi.id]) }" title="Ajouter participant"><app:icon name="user"/></a>
			</td>
		</tr>
	
		<!-- Niveau 2: données des équipes -->
		
		<g:set var="equipes" value="${ participants.groupBy { it.defiEquipe }.sort { it.key.libelle } }"/>

		<g:each var="equipe" in="${ equipes }">
		
			<tr class="bg-primary text-white">
				<td>
					<h5 class="font-weight-bold text-white">
						<a id="defi-equipe-button" data-url="${ g.createLink(action: 'dialogDefiEquipe', id: equipe.key.id) }"><app:icon name="users"/> ${ equipe.key.libelle }</a>
					</h5>
				</td>
				<td class="text-right">${ equipe.key.reference_global() }</td>
				<td class="text-right">${ equipe.key.action_global() }</td>
				<td class="text-right">${ equipe.key.economie_global }</td>
				<td class="text-right">${ equipe.key.classement_global }</td>
				<td class="column-2-buttons">
					<a id="effacer-resultat-defi-button" data-url="${ g.createLink(action: 'effacerResultat', params:['defiEquipe.id': equipe.key.id]) }" class="btn btn-light ml-1" title="Effacer résultat"><app:icon name="delete"/></a>
				</td>
			</tr>
			
			<g:set var="profils" value="${ equipe.value.groupBy { it.user.profil }.sort { it.key.libelle } }"/>
			
			<g:each var="profil" in="${ profils }">
			
				<g:set var="equipeProfil" value="${ equipeProfils.find{ it.defiEquipe == equipe.key && it.profil == profil.key } }"/>
			
				<!-- Niveau 3 : données par profil de l'équipe -->
				
				<tr class="bg-light">
					<td><asset:image src="${profil.key.icon }" style="width:30px"/> ${ profil.key.libelle }</td>
					<td class="text-right">${ equipeProfil?.reference_global() }</td>
					<td class="text-right">${ equipeProfil?.action_global() }</td>
					<td class="text-right">${ equipeProfil?.economie_global }</td>
					<td class="text-right">${ equipeProfil?.classement_global }</td>
					<td class="column-2-buttons">
					</td>
				</tr>
			
				<g:each var="participant" in="${ profil.value }">
				
					<!-- Niveau 4 : données par participants -->
				
					<tr>
						<td class="pl-6"><app:icon name="user"/> ${ participant.user.prenomNom }</td>
						<td class="text-right">${ participant.reference_global() }</td>
						<td class="text-right">${ participant.action_global() }</td>
						<td class="text-right">${ participant.economie_global }</td>
						<td class="text-right">${ participant.classement_global }</td>
						<td class="column-2-buttons">
							<a id="effacer-resultat-defi-button" data-url="${ g.createLink(action: 'effacerResultat', params: ['defiEquipeParticipant.id': participant.id]) }" class="btn btn-light ml-1" title="Effacer résultat"><app:icon name="delete"/></a>
							<a id="delete-participant-button" data-url="${ g.createLink(action: 'deleteParticipant', id: participant.id) }" class="btn btn-light ml-1" title="Supprimer"><app:icon name="trash"/></a>
						</td>
					</tr>
				</g:each>
			</g:each>
		</g:each>
		
	</tbody>
</table>
