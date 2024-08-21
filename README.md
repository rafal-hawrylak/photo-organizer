## Building jar
`javac -d ./build src/net/hawrylak/photoorganizer/*.java`

`cd ./build`

`jar cvf PhotoOrganizer.jar *`

`mv PhotoOrganizer.jar ../jar`

`cd ..`

`rm -Recurse -Force build`

