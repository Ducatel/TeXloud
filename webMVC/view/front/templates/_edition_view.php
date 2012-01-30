<section>
	<div id="projet" class='coteprojet'>

		<div class="contextMenu" id="myMenu1">
			<li class="addFolder"><img
				src="js/jquery/plugins/simpleTree/images/folder_add.png" />
			</li>
			<li class="addDoc"><img
				src="js/jquery/plugins/simpleTree/images/page_add.png" />
			</li>
			<li class="edit"><img
				src="js/jquery/plugins/simpleTree/images/folder_edit.png" />
			</li>
			<li class="delete"><img
				src="js/jquery/plugins/simpleTree/images/folder_delete.png" />
			</li>
			<li class="expandAll"><img
				src="js/jquery/plugins/simpleTree/images/expand.png" />
			</li>
			<li class="collapseAll"><img
				src="js/jquery/plugins/simpleTree/images/collapse.png" />
			</li>
		</div>
		<div class="contextMenu" id="myMenu2">
			<li class="edit"><img
				src="js/jquery/plugins/simpleTree/images/page_edit.png" />
			</li>
			<li class="delete"><img
				src="js/jquery/plugins/simpleTree/images/page_delete.png" />
			</li>
		</div>

		<div id="wrap">
			<div id="annualWizard">
				<ul class="simpleTree" id='pdfTree'>
					<li class="root" id='<?php echo $var->treeManager->getRootId();  ?>'><span><?php echo $var->rootName; ?>
					</span>
						<ul>
							
						<?php echo $var->treeElements; ?></ul>
					</li>
				</ul>
			</div>

		</div>
		<div id='processing'></div>

	</div>


	<div id="editeur">


		<textarea id="codelatex" class="lined">
		 
		  </textarea>

	</div>

</section>
