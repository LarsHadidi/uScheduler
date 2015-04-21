set Kurs := {<"k1"> ,<"k2"> ,<"k3">};
set Gruppe := {<"g1">,<"g2">,<"g3">,<"g4">,<"g5">,<"g6">};
set Student := {<"s1">,<"s2">,<"s3">,<"s4">,<"s5">,<"s6">,<"s7">};
set KonfliktPaar := {<"g1","g4"> ,<"g1","g5"> ,<"g1","g6">,<"g2","g4">,<"g2","g5">, <"g3","g6">, <"g4","g6">,<"g6","g4">};
set StudentKursAnmeldung[Student] := <"s1"> {<"k1">,<"k2">,<"k3">}, <"s2"> {<"k1">,<"k2">}, <"s3"> {<"k1">,<"k3">}, <"s4"> {<"k2">,<"k3">}, <"s5"> {<"k1">}, <"s6"> {<"k2">}, <"s7"> {<"k3">};				
set KursGruppen[Kurs] := <"k1">{<"g1">,<"g2">,<"g3">},<"k2">{<"g4">,<"g5">},<"k3">{<"g6">};
param grpMaxSize[<g> in Gruppe] := <"g1"> 3,<"g2"> 2 default 2;
param grpMinSize[Gruppe] := <"g3"> 0 default 1;
set StudentGruppenAnmeldung[Student] := <"s1"> {<"g1">,<"g2">,<"g3">,<"g4">,<"g5">,<"g6">}, <"s2"> {<"g1">,<"g2">,<"g3">,<"g4">,<"g5">}, <"s3"> {<"g1">,<"g2">,<"g3">,<"g6">}, <"s4"> {<"g4">,<"g5">,<"g6">}, <"s5"> {<"g1">,<"g2">}, <"s6"> {<"g4">,<"g5">}, <"s7"> {<"g6">};