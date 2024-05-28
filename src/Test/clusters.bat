@echo off


java -jar txt2rsf.jar clustered.txt clustered.rsf
java -jar txt2rsf.jar gaAlgorithmCluster.txt gaAlgorithmCluster.rsf
java -jar txt2rsf.jar kModesOutput.txt kModesOutput.rsf
java -jar txt2rsf.jar relationshipOutput.txt relationshipOutput.rsf

java -jar mojo.jar clustered.rsf %2 -fm
java -jar mojo.jar gaAlgorithmCluster.rsf %2 -fm
java -jar mojo.jar kModesOutput.rsf %2 -fm
java -jar mojo.jar relationshipOutput.rsf %2 -fm

rem Remove temporary files
del clustered.txt
del gaAlgorithmCluster.txt
del kModesOutput.txt
del relationshipOutput.txt
del clustered.rsf
del gaAlgorithmCluster.rsf
del kModesOutput.rsf
del relationshipOutput.rsf
