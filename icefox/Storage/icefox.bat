
@echo off

REM set ICEFOXBASE=%CD%

REM set ICEFOXBASE=C:\Apps

REM ############# SET DERBY PARAMETERS
REM set DERBY_HOME=%ICEFOXBASE%\db-derby-10.14.2.0-bin
REM DERBY DEFAULT PORT IS 1527
set DERBY_PORT=9393
REM set PATH=%DERBY_HOME%\bin:%PATH%


rem For embedded connection
rem export JESDB="CONNECT 'jdbc:derby:$ICEFOXBASE/jesdb;create=false';"

rem ICEFOX db directory under $ICEFOXBASE where actual db files are located
set ICEFOXDBDIR=IceFox

rem For client-server connection on localhost, which is needed by IceFox web, if remote, use ip addrs
set ICEFOXDB=CONNECT 'jdbc:derby://localhost:%DERBY_PORT%/%ICEFOXDBDIR%;create=false';
set DERBY_OPTS="-Dij.maximumDisplayWidth=1000"





REM if %1 == START then

if -%1-==-- echo Argument one not provided, it must be START or STOP & exit /b

if -%1-==-START- echo Starting Derby Server & goto startdb

if -%1-==-STOP- echo Starting Derby Server & goto stopdb

REM in case it reaches this far, parameter provided is incorrect, so, exit.
echo Parameter incorrect, it must be START or STOP & exit /b

:startdb
echo Starting DB
rem Start Derby server for connections from any host
%DERBY_HOME%/bin/startNetworkServer.bat -p %DERBY_PORT% -h 0.0.0.0

exit /b

:stopdb
echo Stopping DB

rem Stop Derby server
%DERBY_HOME%/bin/stopNetworkServer.bat -p %DERBY_PORT%
