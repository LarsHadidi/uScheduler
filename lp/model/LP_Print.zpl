# Ausgaben zur Kontrolle
do print "Kurse:";
do print Kurs;
do print "";

do print "Gruppen:";
do print Gruppe;
do print "";

do print "Studenten:";
do print Student;
do print "";

do print "Alle Konflikte:";
do print BereinigteKonfliktPaar;
do print "";

do print "Bereinigte Konflikte:";
do print KonfliktPaar;
do print "";

do print "Gruppen der Kurse:";
do forall <k> in Kurs: print KursGruppen[k];
do print "";

do print "Kursanmeldungen von Studenten:";
do forall <s> in Student: print StudentKursAnmeldung[s];
do print "";

do print "Spez. Gruppenanmeldungen von Studenten";
do forall <s> in Student: print StudentGruppenAnmeldung[s];
do print "";

#do print "Alle Gruppenanmeldungen von Studenten:";
#do forall <s> in Student: print AllGrpFor(s);
#do print "";

do print "Alle moeglichen Gruppenkonflikte von Studenten:";
do forall <s> in Student: print KonflikteFor(s);
do print "";

do print "Parameter:";
do forall <g> in Gruppe: print g,": Min ",grpMinSize[g]," Max ",grpMaxSize[g];
#do forall <g> in Gruppe: print g,": KursMax ",maxSizeInKursOf[g];
#do print "totalMaxGrpSize", maxGrpSize;
do print "";

do print "Variablen:";
do print y;
do print z;