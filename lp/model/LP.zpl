#LP-Problem:
#Mengen:
# gegeben:
#	S: Students												//alle Studenten
#	G: Gruppen												//alle Gruppen
#	K: Kurse												//alle Kurse
#	GK(k): alle Gruppen von Kurs k in K						//alle Gruppen die zu Kurs k gehoeren	
#	KS(s): alle Kursanmeldungen von Student s in S			//alle Kurse in denen Student s angemeldet ist
#	GS(s): alle Gruppenanmeldungen von Student s in S		//alle Gruppen in denen Student s angemeldet ist
#	GG: (g1,g2)| g1 in G, g2 in G with g1 Konflikt g2		//Paare der Gruppenkonflikte
# generierbar aus gegebenen Mengen:
#	SG: (s,g)|s in S, g in GS(s)							//Paare (Student s, Angemeldete Gruppe des Students s)
#
#Val:
#	cMin[g] : g in G 										//Minimale Gruppengroesse der Gruppe g
#	cMax[g] : g in G										//Maximale Gruppengroesse der Gruppe g
#
#Var: 
#	y[s,g] binary : (s,g) in SG								//Entscheidungsvariable ob Student in Gruppe eingeordnet wird, nur fuer Gruppen in denen ein Student auch angemeldet ist
#	z[g] binary : g in G									//Entscheidungsvariable ob eine Gruppe auch gefuellt wird/existiert
#
#LP:
# max: 
#	Sum y[s,g] : (s,g) in SG								//Zielfunktion: Summe aller Einordungen y 
# bounds:
#//zu b1: Gruppengroesse als Summe aller ueber alle moeglichen Studenten einer Gruppe, Schranken an Gruppengroesse mit cMin,cMax und z[g]
#//zu b2: Ein Student darf max nur in einer Gruppe pro Vorlesung sein, kann auf <= 1 gesetzt werden, da Bedingungen mit == 0 existieren nicht, da wir nur y[s,g] haben fuer Gruppen (und damit Kursen) in denen der Studen angemeldet ist
#//zu b3: Moegliche Gruppenkonflikte pro Student, Einordung nur in einer Gruppe erlaubt
# b1: g in G
#	z[g]*cMing[g] <= Sum y[s,g] : s in S with (s,g) in SG <= z[g]*cMax[g]	
# b2: s in S, k in KS(s)
#	Sum y[s,g] : g in GK(k) <= 1							
# b3: s in S, (g1,g2) in GG with g1 in GS(s) and g2 in GS(s)
#	y[s,g1] + y[s,g2] <= 1									

#BereinigteKonfliktPaar contains only 1 Paar of Symmetric Couples, meaning it transfroms {<a1,a2>,<a2,a1>} -> {<a1,a2>}
set BereinigteKonfliktPaar :=  KonfliktPaar without {<e1,e2> in KonfliktPaar with <e2,e1> in KonfliktPaar and e1>e2};

#returns alle moeglichen Konflikte fuer ein student s
defset KonflikteFor(s) := StudentGruppenAnmeldung[s]*StudentGruppenAnmeldung[s] inter BereinigteKonfliktPaar;
#generats maxGrpSize per Kurs (momentan nicht benoetig)
#param maxSizeInKursOf[<g> in Gruppe] := max <k,g1> in Kurs*Gruppe with <g> in KursGruppen[k] and <g1> in KursGruppen[k] : grpMaxSize[g1];
#generats maxGrpSize total (momentan nicht benoetig)
#param maxGrpSize := max <g> in Gruppe : grpMaxSize[g];


#gernerate only needed var y[s,g] witch controls if student s in grp g and save Indexset as Iy
set Iy :={<s,g> in Student*Gruppe with <g> in StudentGruppenAnmeldung[s]};
var y[<s,g> in Iy] binary;
#generate var witch controls grp existents 
var z[<g> in Gruppe] binary;

#Zielfunktionen
maximize zuweisungen: sum <s,g> in Iy : y[s,g];

#Schranken an Gruppengroesse
#subto grpSizeMin: forall <g> in Gruppe: z[g] * grpMinSize[g] <= sum <s> in Student with <s,g> in Iy: y[s,g] and  z[g] * grpMaxSize[g] >= sum <s> in Student with <s,g> in Iy: y[s,g];
subto grpSizeMin: forall <g> in Gruppe: z[g] * grpMinSize[g] <= sum <s> in Student with <s,g> in Iy: y[s,g];
subto grpSizeMax: forall <g> in Gruppe: z[g] * grpMaxSize[g] >= sum <s> in Student with <s,g> in Iy: y[s,g];

#Schranken an Kurse bzw Gruppenbedinungen/konflikte inerhalb eines Kurses
#kurse mit nur einer Gruppe sind schon durch (y[s,g] : binary) erfuellt, alle generierten y[s,g] sind anmeldungen daher immer <= 1
subto perKurs: forall <s,k> in Student*Kurs with <k> in StudentKursAnmeldung[s] and card(KursGruppen[k]) > 1: sum <g> in KursGruppen[k] with <s,g> in Iy: y[s,g] <= 1;

#Schranken fuer Konflikte
subto grpKonfl: forall <s> in Student: forall <g1,g2> in KonflikteFor(s): y[s,g1] + y[s,g2] <= 1;

#Schranken fuer Garantie das mind eine Gruppe pro Kurs existiert (momentan nicht benoetig)
#subto minOneGrp: forall <k> in Kurs: sum <g> in KursGruppen[k]: z[g] >= 1;

##Ab hier nicht mehr LP-Problem
#zusaetzliche Checks fuer Gruppengroessenparameter (Programmabbruch wenn nicht bestanden)
do forall <g> in Gruppe: check 0 <= grpMinSize[g];
do forall <g> in Gruppe: check 0 <= grpMaxSize[g];
do forall <g> in Gruppe: check grpMinSize[g] <= grpMaxSize[g];