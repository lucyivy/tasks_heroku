call C:\Users\lucyna.bluszcz\IdeaProjects\tasks\runcrud.bat
if "%ERRORLEVEL%" == "0" goto openURL
echo.
echo RUNCRUD has errors - breaking work
goto fail

:openURL
start chrome http://localhost:8080/crud/v1/task/getTasks
goto end

:fail
echo.
echo There were errors

:end
echo.
echo Work is finished.