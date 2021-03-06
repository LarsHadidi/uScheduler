# uScheduler 3.0

Software to assign students into the exercises for each of their selected courses, without time intersections.
The system is designed to work on any tabular input. The semantics of that will be defined in a specific layout-file.

<img src="https://cdn.rawgit.com/LarsHadidi/uScheduler/master/UML/Architecture.svg" width="600" alt="Software Structure"/>


The following linear program models the problem:

<img src="https://raw.githubusercontent.com/LarsHadidi/ResourcesRepository/master/LP_Input.png" width="600" alt="LP Input"/>
<img src="https://raw.githubusercontent.com/LarsHadidi/ResourcesRepository/master/LP_Output.png" width="600" alt="LP Output"/>
<img src="https://raw.githubusercontent.com/LarsHadidi/ResourcesRepository/master/LP_ContrAndObj.png" width="600" alt="LP Contraints and objective"/>

The linear program is written in ZIMPL, a free algebraic modeling language.

**The server:**

- <img src="https://raw.githubusercontent.com/LarsHadidi/ResourcesRepository/master/Scalatra_Logo.png" width="64" alt="Scalatra Logo"/> Built on [Scalatra](http://scalatra.org/) 
-  <img src="https://raw.githubusercontent.com/LarsHadidi/ResourcesRepository/master/Apache%20Shiro%20Large.jpg" width="64" alt="Shiro Logo"/>Uses [Apache Shiro](http://shiro.apache.org/) for user authentication, authorization and session management.
