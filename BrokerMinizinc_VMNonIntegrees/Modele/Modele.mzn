% Les données
int : NComp;
int : NMachine;
int : NLocalisations;
int : NOS;
int : NAlone;
int : NFlow;
int : NFourn;
int: NMoisLocation;
int: NConcurrence;

array [1..NLocalisations] of string : Localisations;
array [1..NOS] of string : OS;
array [1..NFourn] of string : Fournisseurs;

array[1..NComp, 1..NOS] of int : CompOs;
array[1..NComp, 1..NLocalisations] of int : CompLoc;
array[1..NComp] of int : CompMem;
array[1..NComp] of int : CompProc;


array[1..NAlone] of int : CompAlone;
array[1..NFlow,1..2] of int : CompFlow;
array[1..NConcurrence,1..2] of int : CompConcurrence;

array[1..NMachine] of int : VMOs;
array[1..NMachine] of int : VMLoc;
array[1..NMachine] of int : VMMem;
array[1..NMachine] of int : VMProc;

array[1..NMachine] of int : VMCout;
array[1..NMachine] of int : VMFourn;
array[1..NFourn,1..NFourn] of int : VMPrixLien;

% Les variables
array[1..NComp] of var 1..NMachine : Placement;
array[1..NMachine] of var 0..NComp : NbCompParMachine;
array[1..NConcurrence] of var 0..1 : verificationModele; 
var 0..NMachine : coutLien;

% Les Contraintes
%Remplie le tableau de 1 si un lien existe entre 2 VM Concurrentes et 0 sinon
constraint forall(conc in 1.. NConcurrence,flow in 1..NFlow)(if (CompConcurrence[conc,1]==CompFlow[flow,1] /\ CompConcurrence[conc,2]==CompFlow[flow,2]) \/ 
                  (CompConcurrence[conc,1]==CompFlow[flow,2] /\ CompConcurrence[conc,2]==CompFlow[flow,1]) then verificationModele[conc] = 1 else verificationModele[conc] = 0 endif);

%Regarde si il n'y a pas de conflit dans le modèle
constraint sum(verificationModele)==0;

%Si L'Os de la machine correspond à la VM
%constraint forall(c in 1..NComp)(CompOs[c]=VMOs[Placement[c]]);
constraint forall(c in 1..NComp)(CompOs[c,VMOs[Placement[c]]] = 1);

%Si La localisation de la machine correspond à la VM
%constraint forall(c in 1..NComp)(CompLoc[c]=VMLoc[Placement[c]]);
constraint forall(c in 1..NComp)(CompLoc[c,VMLoc[Placement[c]]] = 1);

%La somme de la taille de la mémoire des Composants sur une machine est inférieur ou égale à la taille de la mémoire de la VM
constraint forall(v in 1..NMachine)(sum( [(Placement[c]==v)*CompMem[c] | c in 1..NComp])<=VMMem[v]);

%La somme du nombre de processeurs des Composants sur une machine est inférieur ou égale au nombre de processeurs de la VM
constraint forall(v in 1..NMachine)(sum( [(Placement[c]==v)*CompProc[c] | c in 1..NComp])<=VMProc[v]);

%Le nombre de composant sur chaque machine
constraint forall(v in 1..NMachine)( count(Placement, v, NbCompParMachine[v]) );

%La contrainte Alone
constraint forall(c in 1..NAlone) ( NbCompParMachine[Placement[CompAlone[c]]] = 1);

%La contrainte Flow
constraint coutLien = sum( [(Placement[CompFlow[x,1]] != Placement[CompFlow[x,2]])* VMPrixLien[VMFourn[Placement[CompFlow[x,1]]],VMFourn[Placement[CompFlow[x,2]]]] | x in 1..NFlow]);

%On cherche à minimiser le cout
solve minimize (NMoisLocation*sum([ (NbCompParMachine[v]>0)*VMCout[v]| v in 1..NMachine]))+coutLien;

output[
"La machine \(machine) (Pays: \(Localisations[VMLoc[machine]]), OS: \(OS[VMOs[machine]]), Fournisseur: \(Fournisseurs[VMFourn[machine]])) doit héberger le(s) composant(s) : {" 
++ concat(["\(composant)," | composant in 1..NComp where fix(Placement[composant])=machine]) 
++ "}\n" | machine in 1..NMachine where fix(NbCompParMachine[machine])>0
];

output["\nLe coût de location des machines est de " ++ show (NMoisLocation * sum([ (NbCompParMachine[v]>0)*VMCout[v]| v in 1..NMachine])) ++ " € pour "++show(NMoisLocation)++" mois de location\n" ];
output["le cout des lien "++show(coutLien)++" €\n"];