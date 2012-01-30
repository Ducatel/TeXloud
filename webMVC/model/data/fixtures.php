<?php
require_once('../Query.class.php');
require_once('../object.class.php'); 
require_once('../ObjectAssoc.class.php');
require_once('../WorkingPlace.class.php');
require_once('../Status.class.php');
require_once('../StaticPage.class.php');
require_once('../Survey.class.php');
require_once('../Choice.class.php');
require_once('../Civility.class.php');
require_once('../Speciality.class.php');
require_once('../Article.class.php');
require_once('../User.class.php');
require_once('../Functions.class.php');
require_once('../Address.class.php');
require_once('../MailPreference.class.php');
require_once('../PainInterest.class.php');

<<<<<<< .mine
$labels_speciality = array('cancerologue', 'rhumatologue', 'sexologue', 'psychiatre', 'pédiatre');
=======
>>>>>>> .r56

$labels_speciality = array('cancerologue', 'rhumatologue', 'psychiatre', 'pédiatre');

foreach($labels_speciality as $l){
	$w = new Speciality();
	$w->label = $l;
	$w->save();
}

$labels_civility = array('Mr', 'Mme', 'Mlle');

foreach($labels_civility as $l){
	$w = new Civility();
	$w->label = $l;
	$w->save();
}

$labels_working_places = array('en ville', "à l'hôpital", 'mixte', 'en cabinet');

foreach($labels_working_places as $l){
	$w = new WorkingPlace();
	$w->label = $l;
	$w->save();
}

$labels_status = array('médecin', 'pharmacien', 'étudiant en médecine', 'autre');

foreach($labels_status as $ls){
	$s = new Status();
	$s->label = $ls;
	$s->save();
}

$page = new StaticPage();

$page->content = '
		<div class="intro">
			Patients with severe chronic pain are often insufficiently treated resulting in a high burden for society. Especially in non-cancer pain indications there is still limited access to potent substances. Despite not being intended for long term use, the majority of chronic pain patients are treated with non-opioid analgesics for lengthy periods and NSAIDs are used most frequently<sup>1</sup>.
			<br /><br />
			In a pan-European survey, 40% were not satisfied with the management of their pain, and 12% said their physicians never determined how much pain they were experiencing<sup>1</sup>.
			<br /><br />
			Pharmacological treatment is often limited by side effects. This is especially true for strong opioids where side effects limit the effective analgesic dose that can be achieved. Hence, patients and physicians struggle to find the balance between sufficient pain relief and acceptable tolerability - and therefore are trapped in a Vicious Circle often leading to treatment discontinuations<sup>2</sup>.
			<br /><br /> 
			This becomes even more important for indications like chronic low back pain where a neuropathic component is often involved. In this case the combination of a classical opioid with co-analgesics is commonly used but there is evidence for an increased risk of side effects under combination therapy<sup>3</sup>.
			<br /><br />
			Therefore, improving physicians' . "\'" . ' knowledge about the physiological difference between neuropathic and nociceptive pain and the specific pharmacological options as well as the individualisation of treatment is crucial to make better treatment decisions and to offer patients throughout Europe a more effective pain treatment at an early stage<sup>2</sup>.
		</div>
		
		<div class="references">
			<h2>References</h2>
			
			<ul>
				<li><sup>1</sup> Breivik H et al.: Survey of chronic pain in Europe: prevalence, impact on daily life, and treatment. Eur J Pain. 2006; 10:287-333.</li>
				<li><sup>2</sup> Varrassi G et al. Pharmacological treatment of chronic pain &#8212; the need for CHANGE. Cur Med Res Opin, 2010, 26(5): 1231-1245.</li>
				<li><sup>3</sup> Hanna M et al. Prolonged-release oxycodone enhances the effects of existing gabapentin therapy in painful diabetic neuropathy patients. Eur J Pain, 2008, Vol 12: 804-813.</li>
			</ul>
		</div>';
$page->type = 'formation';
$page->title = 'Limite des traitements actuels';
$page->slug = 'current_cures_limits';
$page->save();


$survey = new Survey();
$survey->title = 'La prise en charge de la douleur chronique est-elle aujourd\'hui satisfaisante ?';
$survey->is_active = 1;
$survey->save();

$choices = array('Oui', 'Non');

foreach($choices as $lc){
	$c = new Choice();
	$c->survey_id = $survey->id;
	$c->label = $lc;
	$c->save();
}

$user = new User();

$user->username = 'john.doe';
$user->civility_id = 1;
$user->lastname = 'Doe';
$user->firstname = 'John';
$user->speciality_id = 1;
$user->cnom = '123456789';
$user->status_id = 1;
$user->working_place_id = 1;

$user->date_of_birth = '1970-12-23';

$user->email = 'john.doe@yopmail.com';
$user->salt = md5(microtime());

$user->password = sha1($user->salt.'test');

$addr = new Address();
$addr->residence = '62 rue Edouard Dumont';
$addr->city = 'Paris';
$addr->zip = '75000';
$addr->save();


$user->address_id = $addr->id;
$user->save();

for($i=0; $i<10; $i++){
	$article = new Article();
	$article->image = '/images/examples/entry_image.jpg';
	$article->user_id = $user->id;
	$article->title = 'Lorem ipsum dolor sit amet';
	$article->content = '<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi sodales quam vitae massa gravida feugiat elementum eros aliquam. Maecenas varius sollicitudin dictum. Proin viverra molestie mauris purus.</p><br /><br /><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi sodales quam vitae massa gravida feugiat elementum eros aliquam. Maecenas varius sollicitudin dictum. Proin viverra molestie mauris purus.</p><br /><p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi sodales quam vitae massa gravida feugiat elementum eros aliquam. Maecenas varius sollicitudin dictum. Proin viverra molestie mauris purus.</p>';
	$article->created_at = date('Y-m-d h:i:s', time()-($i*24*60*60));
	$article->slug = Functions::slugify($article->title, 'Article');
	$article->save();
}

$label_mail_prefs = array('mailing', 'meeting');

foreach($label_mail_prefs as $l){
	$mp = new MailPreference();
	$mp->type = $l;
	$mp->save();
}

// Pages statiques restantes

$loremipsum =  '
		<div class="intro">
Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi et sodales ipsum. Fusce quis nibh eu quam gravida eleifend. Sed risus velit, semper scelerisque porttitor hendrerit, ultrices sit amet odio. Phasellus ullamcorper mattis libero, in dignissim massa fermentum sit amet. In a urna purus, vel dapibus mi. Aenean sollicitudin dictum risus, ut condimentum lectus mollis eget. Donec nec facilisis eros. Sed dolor turpis, sagittis adipiscing luctus commodo, commodo in sapien. Mauris mauris tellus, malesuada vitae luctus et, venenatis ac lacus. Mauris quis turpis tellus, in elementum diam. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Maecenas eget dignissim lectus. Suspendisse potenti.

Nam at enim ut augue auctor imperdiet ut at enim. Duis vitae magna eleifend arcu placerat suscipit. Sed vehicula convallis turpis, et bibendum augue congue at. Aenean quis sapien eros, at aliquam arcu. Donec adipiscing rutrum venenatis. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Cras velit ante, dignissim quis consectetur eu, lobortis eu neque. Praesent volutpat varius quam, sit amet malesuada purus condimentum ac. Sed consequat ipsum fringilla odio feugiat vel mattis elit rhoncus. Mauris dui nunc, pulvinar et feugiat fermentum, tincidunt vel dui. Pellentesque sagittis lacus nec magna tincidunt molestie. Phasellus erat quam, porttitor eu sollicitudin at, rutrum sit amet sapien. Donec nibh dolor, auctor vel gravida eu, volutpat et libero. Vestibulum mi nisi, imperdiet commodo porttitor placerat, ullamcorper sed augue.

Cras sapien elit, sollicitudin vel condimentum non, venenatis non nunc. Suspendisse sagittis, eros at iaculis tincidunt, enim tellus condimentum leo, at varius erat odio eu mauris. Etiam tincidunt quam eget justo vestibulum a vulputate nisl rutrum. Suspendisse commodo hendrerit pellentesque. Ut quis mauris ultricies sem rhoncus bibendum. Cras in nulla a metus ornare laoreet. Sed fringilla luctus interdum. Sed quis diam ut enim semper consequat mattis id ligula. Duis ullamcorper gravida porta. Sed nulla sem, fermentum ac aliquet vitae, facilisis ut mi. Integer id scelerisque mi.

Nam mollis pharetra lectus id bibendum. Maecenas eget tortor quis velit condimentum hendrerit id eget quam. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia Curae; Duis venenatis lobortis malesuada. Nullam pharetra aliquet ipsum in aliquam. Fusce eu justo nec augue tristique feugiat. Vivamus egestas, mi sed tempor facilisis, turpis ante consequat lorem, sed gravida nibh nisl non sem. Praesent mi quam, cursus non molestie ut, sagittis ut neque. Nulla eget tortor vel sem posuere pharetra. Nam porta, lorem eu imperdiet viverra, lacus odio rutrum lectus, imperdiet adipiscing urna est eget augue. Mauris malesuada est id metus ullamcorper aliquet. Vivamus ornare, sapien ut porta elementum, justo quam accumsan sapien, sed aliquet nulla urna in magna. Donec quis nibh tortor. Ut pellentesque libero eget urna sollicitudin quis faucibus arcu venenatis. Nullam ornare augue sit amet mauris bibendum ac auctor lacus suscipit.

Vivamus lobortis, nunc rhoncus euismod semper, eros dui consequat dolor, vitae facilisis felis enim sit amet arcu. Nunc vehicula enim nec risus facilisis egestas. Suspendisse consectetur porta erat non dictum. Donec vel sapien risus, vel adipiscing tortor. Nullam aliquet varius ante non iaculis. Maecenas eros enim, pharetra non tempor sed, tincidunt quis ipsum. Donec ac pharetra nibh. In nec enim dui, sed fermentum enim. Ut id pharetra velit.

		</div>';

$page = new StaticPage();
$page->content = $loremipsum;
$page->type = 'page';
$page->title = 'Objectifs Du Programme';
$page->slug = 'programObjectives';
$page->save();

$page = new StaticPage();
$page->content = $loremipsum;
$page->type = 'page';
$page->title = 'Comité Scientifique';
$page->slug = 'scientificComity';
$page->save();

$page = new StaticPage();
$page->content = $loremipsum;
$page->type = 'page';
$page->title = 'Réalité Et Enjeux De La Douleur';
$page->slug = 'realityAndIssuesOfPain';
$page->save();

$page = new StaticPage();
$page->content = $loremipsum;
$page->type = 'page';
$page->title = 'Programme Change Pain';
$page->slug = 'changePainProgram';
$page->save();

$page = new StaticPage();
$page->content = $loremipsum;
$page->type = 'page';
$page->title = 'Informer Les Patients';
$page->slug = 'informPatients';
$page->save();


$pain_iterest_label = array('évaluation', 'implications sociales', 'tolérence');

foreach($pain_iterest_label as $l){
	$pi = new PainInterest();
	$pi->label = $l;
	$pi->save();
}
?>
