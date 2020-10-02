<header id="header" role="banner">
<!-- App Header goes inside #header -->

  <nav class="aui-header aui-dropdown2-trigger-group" role="navigation" >
  	<div class="aui-header-inner">
  		
  	  <!--div class="aui-header-before">
  	  	<g:if test="${ mobileAgent }">
	  	  	<ul class="aui-nav">
	  	  		<li><a href="#dropdown2-app-switch" aria-owns="dropdown2-app-switch" aria-haspopup="true" class="aui-dropdown2-trigger aui-dropdown2-trigger-arrowless" >
	  	  			<span class="aui-icon aui-icon-small aui-iconfont-appswitcher"></span></a>
	  	  			<div class="aui-dropdown2 aui-style-default aui-dropdown2-in-header" id="dropdown2-app-switch" aria-hidden="true">
	                    <div class="aui-dropdown2-section">
	                        <ul>
	                        	<g:render template="/templates/dropDownItem" model="[items: app.navigationItems(category: 'navbarPrimary')?.subitems, category: 'navbarPrimary' ]"/>
	                        </ul>
	                    </div>
	                </div>
	  	  		</li>      	 	
	      	 </ul>
      	 </g:if>
  	  </div-->
  	
  	
      <div class="aui-header-primary">
      
          <!--div class="aui-header-logo logoParentHeader">
  	  		<g:link uri="/">
  	  			<asset:image src="home_w.png"/>
	        	<span class="logoTextHeader "><g:meta name="app.code"/></span>
        	</g:link>
      	 </div-->
      	 
      	 <ul class="aui-nav">
			<li><g:link controller="dataChallenge" action="dashboard" class="aui-button aui-button-primary">Tableau de bord</g:link></li>
			<li><g:link controller="dataChallenge" action="personalData" class="aui-button aui-button-primary">Mes consentements</g:link></li>
      	 </ul>
      	 
      </div>
      
      
      <div class="aui-header-secondary">
          <ul class="aui-nav">
              <!-- Menu user (dynamique) -->
              <li>
			  	<a href="#dropdown2-header9" aria-owns="dropdown2-header9" aria-haspopup="true" class="aui-dropdown2-trigger" >
					<sec:username/>
                  </a>
                <div class="aui-dropdown2 aui-style-default aui-dropdown2-in-header" id="dropdown2-header9" style="display: none; top: 40px; min-width: 160px; left: 1213px;" aria-hidden="true">

                    <sec:ifSwitched>
                    	<div class="aui-dropdown2-section">
	                        <ul>
	                            <li><g:link action="exitSwitchUser" controller="user">Revenir à votre session</g:link></li>
	                        </ul>
	                    </div>
					</sec:ifSwitched>
                    
                    <div class="aui-dropdown2-section">
                        <ul>
                            <li><g:link controller="logout">Déconnexion</g:link></li>
                        </ul>
                    </div>
                </div>
              </li>
          </ul>
      </div>
  	 </div>
  </nav>

<!-- App Header goes inside #header -->
</header>