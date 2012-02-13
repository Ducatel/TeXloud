<?php 
	if($var->flash)
		echo '<p id="flash">' . $var->flash . '</p>';
?>
<article>
	<section>
		<div id='module_connexion' class="boxConnexion">
			<div id="module_connexion_menubar" class="menubar">
				Authentification
			</div>
			<div id="design_search_contents" class="contents">
				<form action="/user/processLogin" method="post">
					<table>
						<tr>
							<td><label for="identifiant">Identifiant :</label></td>
							<td><input type="text" name="identifiant" id="identifiant" /></td>
						</tr>
						<tr>
							<td><label for="pass">Mot de passe : </label></td>
							<td><input type="password" name="motpasse" id="motpasse" /></td>
						</tr>
						<tr>
							<td></td>
							<td><input type="submit" name="register" value="Connexion" class="submit" /></td>
						</tr>
					</table>
					
					
				</form>
				<p class="form_options">
					<a href="/signup">S'inscrire</a>
					<a href="#">Mot de passe oublié</a>
				</p>
			</div>
		</div>
	</section>

	<section id="paragraph">


			<img src='/images/dessin.png' alt='texloud explication' style='width:450px;margin:0;padding:0;position:fixed;top:70px;' />


	</section>
</article>
