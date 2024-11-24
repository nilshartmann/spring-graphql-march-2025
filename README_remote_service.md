# Der "Remote Service"

* Das Modul `remote-service` enthält eine eigenständige Spring Boot-Anwendung, die den "Publisher Service" (HTTP API) und den "Media GraphQl Service" (GraphQL API) zur Verfügung stellt.
* Die beiden Dienste stehen exemplarisch für externe APIs, die wir in unserer eigenen Anwendung konsumieren wollen.
* Im "echten Leben" wären sie nicht Teil unseres Workspaces und wir hätten auch keinen Zugriff auf deren Source-Code.
  * Wahrscheinlich wären es auch zwei getrennte Dienste
  

## Starten der RemoteServiceApplication

* Wir brauchen den Remote-Service erst im Laufe des Workshops. 
    - Ich gebe dir Bescheid, sobald du ihn starten musst

* **Zum Starten:**
  * Führe die Klasse `nh.springgraphql.remoteservice.RemoteServiceApplication` aus
  * Die Anwendung läuft auf Port `8090`.
  * Wenn dieser Port bei dir belegt ist, kannst du ihn anpassen:
      * In der Datei `remote-service/src/main/resources/application.properties` musst du das Property `server.port` auf den gewünschten Port setzen
      * In der GraphQl-Anwendung musst du in `graphql-service/src/main/resources/application.properties` die Url mit dem Property `publisher.service.base-url` anpassen
  * Wenn der Service läuft kannst du zum **ausprobieren** einen Publisher abfragen, z.B. in dem du im Browser (oder per curl, wget, ...) http://localhost:8090/api/publishers/1 aufrufst
