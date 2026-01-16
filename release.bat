@echo off
setlocal

rem 1. Kompilacja
javac -cp "jar/log4j-api-2.25.3.jar;jar/log4j-core-2.25.3.jar" --release 21 --enable-preview -d build src\net\hawrylak\photoorganizer\*.java

rem 2. Zwykły jar z Twoimi klasami
jar cfm PhotoOrganizer.jar manifest.txt -C build . log4j2.properties

rem 3. Złożenie fat-jara w tmp-fat
rmdir /S /Q tmp-fat 2>nul
mkdir tmp-fat

rem Twoje klasy + manifest
cd tmp-fat
jar xf ..\PhotoOrganizer.jar

rem DO TEGO SAMEGO KATALOGU rozpakuj log4j-api i log4j-core
jar xf ..\jar\log4j-api-2.25.3.jar
jar xf ..\jar\log4j-core-2.25.3.jar

rem Usuń obcy manifest, zostaw tylko swój
del META-INF\MANIFEST.MF 2>nul

cd ..

rem 4. Zbuduj finalny fat-jar z Twoim manifestem
jar cfm PhotoOrganizer.jar manifest.txt -C tmp-fat .

rem 5. Sprzątanie
rmdir /S /Q build
rmdir /S /Q tmp-fat

endlocal
