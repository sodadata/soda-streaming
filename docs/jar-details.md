# content of jar
jar tf jar-file | grep scan.yml

#details on jar
https://docs.oracle.com/javase/tutorial/deployment/jar/view.html

#create new jar
jar cf poc-tooling/new-jar poc-tooling/streaming-monitor.jar streaming-monitor/src/main/resources/scan3.yml -> does not give what we want

#create jar from existing jar
https://stackoverflow.com/questions/40212285/how-to-create-jar-file-from-existing-jar-file

#simply overwrite some files to create updated jar
jar uf ../../../../poc-tooling/streaming-monitor.jar scan3.yml (the -c option does not work)
#scan files are only loaded in the /scans directory.
#start of from empty /scans directory
#copy default jar to other location and start demo from this copy. 
#inside the resources create a scan.template file to showcase how it should work
#datasource/warehouse file -> datasource-local / datasource-cluster