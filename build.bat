@ECHO off
IF EXIST "minecraft-recipe-editor.jar" (
	DEL minecraft-recipe-editor.jar
)
ECHO Building a jar archive...
CD bin
jar -cfe ..\minecraft-recipe-editor.jar editor.Application .\*
CD ..
ECHO Finished
