@echo off


java -jar txt2rsf.jar clustered.txt clustered.rsf
java -jar txt2rsf.jar gaAlgorithmCluster.txt gaAlgorithmCluster.rsf
java -jar txt2rsf.jar kModesOutput.txt kModesOutput.rsf
java -jar txt2rsf.jar relationshipOutput.txt relationshipOutput.rsf

java -jar mojo.jar clustered.rsf gaAlgorithmCluster.rsf -fm
java -jar mojo.jar clustered.rsf kModesOutput.rsf -fm
java -jar mojo.jar clustered.rsf relationshipOutput.rsf -fm
java -jar mojo.jar relationshipOutput.rsf gaAlgorithmCluster.rsf -fm
java -jar mojo.jar kModesOutput.rsf relationshipOutput.rsf -fm

rem Remove temporary file


