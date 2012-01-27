<?php
include_once("./PHPsrc/Tree.php");
echo '<link rel="stylesheet" href="./css/arbre.css" />';
echo '<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1/jquery.min.js"></script>';
echo '<script type="text/javascript" src="./js/arbre.js"></script>';


$tree=new Tree("Workspace");
$tree->addNode("Un Fichier_1",$tree->getRoot()->getId(),false);


$id=$tree->addNode("Un dossier_1",$tree->getRoot()->getId(),true);
$tree->addNode("Un Fichier_1_1",$id,false);
$fichier20=$tree->addNode("Un Fichier_1_2 ",$id,false);


$id2=$tree->addNode("Un dossier_2",$tree->getRoot()->getId(),true);
$tree->addNode("Un Fichier_2_1",$id2,false);
$tree->addNode("Un Fichier_2_2 ",$id2,false);
$id3=$tree->addNode("Un dossier_2_1",$id2,true);
$tree->addNode("Un Fichier_2_1_1",$id3,false);
$tree->addNode("Un Fichier_2_1_2 ",$id3,false);

/*
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
echo "<hr/>";*/

echo $tree->toStringHTML();
?>