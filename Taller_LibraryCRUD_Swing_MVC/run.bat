@echo off
setlocal
if not exist out mkdir out
dir /s /b src\*.java > sources.txt
javac -encoding UTF-8 -d out @sources.txt
if not exist out\META-INF\services mkdir out\META-INF\services
xcopy /E /I /Y resources\META-INF\services out\META-INF\services >nul
java -cp out co.unicauca.biblioteca.app.Main
endlocal
