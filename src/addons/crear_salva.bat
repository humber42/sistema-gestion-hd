@echo off

goto :init

:header
    echo %__NAME% v%__VERSION%
    echo Este programa es propiedad de HASH,
    echo el mismo ejecuta una funcionalidad que permite
    echo hacer salvas o recuperar bases de datos de postgres

:usage
    echo USAGE:
    echo    %__BAT_NAME% [flags] "required argument" "optional argument"
    echo.
    echo.   /?, --help show this helper
    echo.   /v, --version show the version
    echo.   -f, --flag value specified a named parameter value

:version
    if "%~1"=="full" call :header & goto :eof
    echo %__VERSION%
    goto :eof

:missing_argument
    call :header
    call :usage
    echo.
    echo ***                           ***
    echo *** MISSING REQUIRED ARGUMENT ***
    echo ***                           ***
    echo.
    goto :eof

:init
    set "__NAME=%~n0"
    set "__VERSION=1.0"
    set "__YEAR=2022"

    set "__BAT_FILE=%~0"
    set "__BAT_PATH=%~dp0"
    set "__BAT_NAME=%~nx0"
    set "__RUTA_BASE=%~d0%~p0%"

    set "OptHelp="
    set "OptVersion="
    set "OptVerbose="
    set "Salva="

    set "Route="
    set "Mode="
    set "Format="
    set "Host="
    set "Port="
    set "User="
    set "Encoding="
    set "Fichero="
    set "Database="
    set "Password="

:parse
    if "%~1"=="" goto :validate
    if /i "%~1"=="/?"       call :header & call :usage "%~2" & goto :end
    if /i "%~1"=="-?"       call :header & call :usage "%~2" & goto :end
    if /i "%~1"=="--help"   call :header & call :usage "%~2" & goto :end

    if /i "%~1"=="/v"              call :version & goto :end
    if /i "%~1"=="-v"              call :version & goto :end
    if /i "%~1"=="--version"       call :version & goto :end

    if /i "%~1"=="/e"         set "OptVerbose=yes" & shift & goto :parse
    if /i "%~1"=="-e"         set "OptVerbose=yes" & shift & goto :parse
    REM if /i "%~1"=="--verbose"  set "OptVerbose=yes" & shift & goto :parse

    if /i "%~1"=="-r"         set "Route=%~2" & shift & shift & goto :parse
    if /i "%~1"=="-m"         set "Mode=%~2" & shift & shift & goto :parse
    if /i "%~1"=="-F"         set "Format=%~2" & shift & shift & goto :parse
    if /i "%~1"=="-h"         set "Host=%~2" & shift & shift & goto :parse
    if /i "%~1"=="-p"         set "Port=%~2" & shift & shift & goto :parse
    if /i "%~1"=="-u"         set "User=%~2" & shift & shift &  goto :parse
    if /i "%~1"=="-enc"         set "Encoding=%~2" & shift & shift &  goto :parse
    if /i "%~1"=="-archivo"         set "Fichero=%~2" & shift & shift & goto :parse
    if /i "%~1"=="-db"         set "Database=%~2" &  shift & shift & goto :parse
    if /i "%~1"=="-pass" set "Password=%~2" & shift & shift & goto :parse

    if not defined Salva     set "Salva=%~1"     & shift & goto :parse

    shift
    goto :parse

:validate
    if not defined Salva call :missing_argument & goto :end

:main
    if defined OptVerbose (
            echo **** DEBUG IS ON
        )

    if %Salva% == "save" goto :execute_save
    if %Salva% == "restore" goto :execute_restore
    
:execute_save
    echo operacion: "%Salva%"
    echo ruta: "%Route%"
    echo modo: "%Mode%"
    echo formato: "%Format%"
    echo host: "%Host%"
    echo port: "%Port%"
    echo user: "%User%"
    echo encoding: "%Encoding%"
    echo file: "%Fichero%"
    echo database: "%Database%"

    start  %__RUTA_BASE%%Route% %Mode% -F %Format% -h %Host% -p %Port% -U %User% -E %Encoding% -f %Fichero% %Database%

:execute_restore
    echo operacion: "%Salva%"
    echo ruta: "%Route%"
    echo modo: "%Mode%"
    echo formato: "%Format%"
    echo host: "%Host%"
    echo port: "%Port%"
    echo user: "%User%"
    echo encoding: "%Encoding%"
    echo file: "%Fichero%"
    echo database: "%Database%"
    echo password: "%Password%"

    @echo "Wait..."    
    SETLOCAL
    set PGPASSWORD = %Password%
    start  %__RUTA_BASE%%Route% --host %Host% --port %Port% --username %User% --dbname %Database% %Mode% %Fichero%

:end
    call :cleanup
    exit /B

:cleanup
    REM The cleanup function is only really necessary if you
    REM are _not_ using SETLOCAL.
    set "__NAME="
    set "__VERSION="
    set "__YEAR="

    set "__BAT_FILE="
    set "__BAT_PATH="
    set "__BAT_NAME="

    set "OptHelp="
        set "OptVersion="
        set "OptVerbose="
        set "Salva="

        set "Route="
        set "Mode="
        set "Format="
        set "Host="
        set "Port="
        set "User="
        set "Enc="
        set "File="
        set "Database="


    goto :eof
