@echo off
set homepath=%~dp0
set modelpath=%~dp0model
set datapath=%~dp0data

rem search for newer versions
for /f "tokens=*" %%G in ('dir /s /b zimpl*.exe') do call :Loop %%G
goto :EndLoop
:Loop
%1 -V > nul
if %ERRORLEVEL% == 0 (
	%zimplpath% -V > vers
	set /p v1= < vers
	del vers
	%1 -V > vers
	set /p v2= < vers
	del vers
	if "%v1%" LSS "%v2%" (
		set zimplpath=%1
	)
 )
goto :eof
:EndLoop
%zimplpath% -V >nul
if %ERRORLEVEL% == 0 goto :dataFile
echo.
echo no zimple found exit program
goto :end

:dataFile
echo.
echo use "%zimplpath%"
echo Zimple verion:
%zimplpath% -V
echo.
cd %datapath%
echo Files in /data:
dir /b
echo.
echo enter Data file
set /P d=""
cd %homepath%
if "%d%" == "" goto :dataFile
if not exist %datapath%\%d% (
	echo File not found 
	goto :dataFile
)
%zimplpath% -V > vers
set /p version= < vers
del vers
if "%version%" LSS "2.06" goto :noPrint

:choice
echo print sets,parameters and variables?[Y/N]
set /P c=""
if /I "%c%" == "Y" (
	%zimplpath% -o LP %datapath%\%d% %modelpath%\LP.zpl %modelpath%\LP_Print.zpl
	echo.
	goto :end
)
if /I "%c%" == "N" (
	:noPrint
	%zimplpath% -o LP %datapath%\%d% %modelpath%\LP.zpl
	echo.
	goto :end
)
goto :choice


:end
echo press key to quit
pause >nul