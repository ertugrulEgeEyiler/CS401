@echo off
setlocal enabledelayedexpansion

REM Process each .txt file with ACDC and then compare using MOJO
for %%i in (file1.txt file2.txt file3.txt) do (
    REM Generate .rsf file from each .txt file
    java -jar acdc.jar %%i %%i.rsf

    REM Check if base .rsf exists; if not, set current as base
    if not exist base.rsf (
        copy %%i.rsf base.rsf
    ) else (
        REM Compare current .rsf with base.rsf
        java -jar mojo.jar base.rsf %%i.rsf -fm

        REM Optionally, update base .rsf or handle results here
    )

    REM Remove temporary .rsf file
    del %%i.rsf
)

REM Remove the base .rsf file
del base.rsf

endlocal
@echo done