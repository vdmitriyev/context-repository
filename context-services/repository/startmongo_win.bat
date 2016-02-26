REM @author Viktor Dmitriyev

REM setting params
set MONGODB_HOME=c:\Soft\MongoDB\Server\3.2\
set MONGODB_DATA_PATH=%MONGODB_HOME%\data
set CONTEXT_DB=%MONGODB_DATA_PATH%\dbcontext

REM create folders
if not exist %MONGODB_DATA_PATH% mkdir %MONGODB_DATA_PATH%
if not exist %CONTEXT_DB% mkdir %CONTEXT_DB%

REM start
%MONGODB_HOME%\bin\mongod.exe --dbpath %CONTEXT_DB%