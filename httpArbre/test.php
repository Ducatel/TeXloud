<?php
include_once("./PHPsrc/Tree.php");

$tree=new Tree("racine");
$tree->addNode("Un Fichier",$tree->getRoot()->getId(),false);


$id=$tree->addNode("Un dossier",$tree->getRoot()->getId(),true);
$tree->addNode("Un Fichier",$id,false);
$fichier20=$tree->addNode("Un Fichier2 ",$id,false);


$id2=$tree->addNode("Un dossier2",$tree->getRoot()->getId(),true);
$tree->addNode("Un Fichier",$id2,false);
$tree->addNode("Un Fichier2 ",$id2,false);
$id3=$tree->addNode("Un dossier21",$id2,true);
$tree->addNode("Un Fichier",$id3,false);
$tree->addNode("Un Fichier2 ",$id3,false);

echo $tree->toString();
echo "<hr/>";

echo "suppression Fichier:<br/><br/>";
$tree->removeNode($fichier20);

echo $tree->toString();
echo "<hr/>";

echo "suppression dossier sans sous dossier:<br/><br/>";
$tree->removeNode($id);

echo $tree->toString();
echo "<hr/>";

echo "suppression dossier avec sous dossier:<br/><br/>";
$tree->removeNode($id2);

echo $tree->toString();
echo "<hr/>";

echo $tree->toStringHTML();
?>