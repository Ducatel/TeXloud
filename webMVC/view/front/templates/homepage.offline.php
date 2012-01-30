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
		<p>LaTeX est un langage et un système de composition de documents créé
			par Leslie Lamport en 198312. Plus exactement, il s'agit d'une
			collection de macro-commandes destinées à faciliter l'utilisation du
			« processeur de texte » TeX de Donald Knuth. Depuis 1993, il est
			maintenu par le LATEX3 Project team. La première version utilisée
			largement, appelée LaTeX2.09, est sortie en 1984. Une révision
			majeure, appelée LaTeX2ε est sortie en 1991. Le nom est l'abréviation
			de Lamport TeX. On écrit souvent LATEX, le logiciel permettant les
			mises en forme correspondant au logo. Du fait de sa relative
			simplicité, il est devenu la méthode privilégiée d'écriture de
			documents scientifiques employant TeX. Il est particulièrement
			utilisé dans les domaines techniques et scientifiques pour la
			production de documents de taille moyenne ou importante (thèse ou
			livre, par exemple). Néanmoins, il peut aussi être employé pour
			générer des documents de types variés (par exemple,</p>
	</section>
</article>
