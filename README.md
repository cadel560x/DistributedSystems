# DistributedSystems
This is the RMI Database Server for 2018 Distributed Systems project.
This application runs on SQL in-memory database called h2. So there is no need to have a DBMS running like MySQL or Oracle.
## Execution
It is necessary to include the h2 jar file into the class path. The h2 jar file can be found at the root of this repository:
* Unix/Linux/Mac OS
`java -cp ./database-service.jar:./h2-1.4.197.jar ie.gmit.sw.ServiceSetup`
* Windows
`java -cp ./database-service.jar;./h2-1.4.197.jar ie.gmit.sw.ServiceSetup`  

This application serves RMI requests on default RMI tcp port 1099