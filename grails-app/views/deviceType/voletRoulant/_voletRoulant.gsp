<div>
	<g:set var="url" value="${ g.createLink(controller: 'device', action: 'invokeAction') }"/>
	<g:set var="formId" value="form-device-${ device.id }"/>
	
	<g:form name="${ formId }">
		<g:hiddenField name="actionName" value="push"/>
		<g:hiddenField name="id" value="${ device.id }"/>
		<g:hiddenField name="value" value="${ device.value }"/>

		<div style="display:table; margin-bottom:15px">
			<div class="slider volet-roulant" style="height:90px; width:50px; display:table-cell" data-min="0" data-max="100" data-step="25" 
				data-range="max" data-orientation="vertical" data-element-id="slider-value-${device.id }"
				data-action-name="level" data-form-id="${ formId }" data-url="${ url }" data-value="${ device.value }">
			</div>
			
			<div style="display:table-cell; padding-left:15px">
				<div class="aui-buttons" >
					<a class="aui-button ajax-invoke-action-button" title="Fermer" data-action-name="close" data-url="${ url }" data-form-id="${ formId }">
						<span class="aui-icon aui-icon-small aui-iconfont-arrows-down aui-icon-column-patch"></span></a>
					<a class="aui-button ajax-invoke-action-button" title="Ouvrir" data-action-name="open" data-url="${ url }" data-form-id="${ formId }">
						<span class="aui-icon aui-icon-small aui-iconfont-arrows-up aui-icon-column-patch"></span></a>
					<a class="aui-button ajax-invoke-action-button" title="Stop" data-action-name="stop" data-url="${ url }" data-form-id="${ formId }">
						<span class="aui-icon aui-icon-small aui-iconfont-pause aui-icon-column-patch"></span></a>
				</div>
				
				<div style="display:table; padding-top:15px">
					<p style="font-weight:bold; font-size:18pt; display:table-cell; padding-right:10px; vertical-align:top"><span id="slider-value-${device.id }">${device.value ? device.value.toDouble().intValue() : '-' }</span>%</p>
					<div style="display:table-cell; padding-left:10px; vertical-align:top;" class="separator-left">
						<p style="font-size:8pt">
						<strong>Conso :</strong> ${ device.metavalueByLabel('energy')?.value }kWh
						<br/>
						<strong>Puiss :</strong> ${ device.metavalueByLabel('power')?.value }W
						</p>
					</div>
				</div>
			</div>
		</div>
	</g:form>
</div>