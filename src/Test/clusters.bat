@echo off

java -jar mojo.jar clustered.rsf gaAlgorithmCluster.rsf -fm
java -jar mojo.jar clustered.rsf kModesOutput.rsf -fm
java -jar mojo.jar kModesOutput.rsf gaAlgorithmCluster.rsf -fm

rem Remove temporary file
del output.txt
del gaAlgorithmCluster.rsf
del relationshipOutput.rsf
del clustered.rsf
del kModesOutput.rsf

