cd ./talk-wave-server
mvn clean package
java -jar target/talk-wave-client-1.0-SNAPSHOT.jar

cd ./../talk-wave-client
mvn clean package
java -jar target/talk-wave-client-1.0-SNAPSHOT.jar