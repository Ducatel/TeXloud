<div id="formulaire">
	<article id="monForm">
		<form action="/signup/register" method="post">
			<fieldset>
				<legend>Compte</legend>
				<p>
					<label for="username">Identifiant : </label> <input type="text"
						id="username" name="register[username]" required />
				</p>
				<p>
					<label for="pwd">Mot de passe : </label> <input type="password"
						id="pwd" name="register[pwd]" required />
				</p>
				<p>
					<label for="pwd2">Confirmer le mot de passe : </label> <input
						type="password" id="pwd2" name="register[pwd_check]" required />
				</p>
				<p>
					</br>
					<label for="mail">Adresse email : </label> <input type="email"
						id="mail" name="register[mail]" required />
				</p>
			</fieldset>

			<p>
				<input type="submit" name="submit" value="Envoyer" /> 
				<input type="reset" value="Reset" name="del" />
				<a class="back" href="/">Précédent</a>
			</p>
		</form>
	</article>
</div>

