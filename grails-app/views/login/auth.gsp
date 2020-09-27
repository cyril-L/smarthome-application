<html>
<head>
	<meta name='layout' content='anonymous'/>
</head>

<body>
	<g:applyLayout name="applicationContent">
    
    <g:if test="${ mobileAgent }">
    	<h1><g:meta name="app.code"/></h1>
    </g:if>
    <g:else>
    	<h1>Bienvenue sur <g:meta name="app.code"/></h1>
    </g:else>

		<div class="aui-group">
			<div class="aui-item">

			<h3 class="separator">1) Visualiser ma conso</h3>

			<form action="${postUrl}" method="post" id="d" class="aui login ${ mobileAgent ? 'top-label' : '' }" autocomplete='off'>
				<fieldset>
					<div class="field-group">
						<input class="text" type="email" id="username" name="j_username" placeholder="Adresse mail" autofocus="true">
					</div>
					<div class="field-group">
						<input class="password" type="password" id="password" name="j_password" placeholder="Mot de passe" >
					</div>
				 </fieldset>
				 <fieldset class="group">
					<div class="checkbox">
						<input type="hidden" name="${rememberMeParameter}" id="remember_me" value='true'>
					</div>
				</fieldset>
				<div class="buttons-container">
					<div class="buttons">
						<input class="aui-button aui-button-primary" type="submit" value="Me connecter" id="connexion">
						<br/>
						<g:link class="cancel" controller="register" action="forgotPassword">J'ai oublié mon mot de passe</g:link>
					</div>
				</div>
			</form>
				<g:form controller="register" action="account" class="aui login">
					<div class="buttons-container">
					<button class="aui-button aui-button-primary highlight">Créer un compte</button>
					</div>
				</g:form>
			</div>
			<div class="aui-item">
				<h3 class="separator">2) Rejoindre notre communauté</h3>
				<p>Accroche et petite description du groupe</p>
				<ul>
				<li>🌳 Éco gestes</li>
				<li>👍 Défis</li>
				<li>😀 Communauté</li>
				</ul>
				<p>
				<strong>Notre communauté n'attend plus que VOUS,<br/>
					<a href="#">rejoinez-nous sur Facebook</a> !</strong>
				</p>
			</div>
		</div>
	</g:applyLayout>
	
</body>
</html>