# Usa l'immagine con Gradle 8.13 e JDK 23 (Amazon Corretto)
FROM gradle:8.13-jdk23-corretto-al2023 AS build

# Copia l'intero progetto nella directory /app del contenitore
COPY . /app

# Imposta la directory di lavoro nel contenitore
WORKDIR /app

# Converte i caratteri di fine riga di gradlew da CRLF (Windows) a LF (Unix) utilizzando sed
RUN sed -i 's/\r$//' gradlew

# Imposta i permessi di esecuzione per gradlew
RUN chmod +x gradlew

# Espone le porte di Spring Boot
EXPOSE 8080
EXPOSE 5005

# Aggiungi variabile di ambiente per configurare se attivare il debug remoto
ENV DEBUG_MODE=false

# Esegui gradlew bootRun per avviare l'applicazione Spring Boot con supporto per il debug remoto se DEBUG_MODE è true
CMD ["sh", "-c", "if [ \"$DEBUG_MODE\" = \"true\" ]; then ./gradlew bootRun --no-daemon --debug-jvm; else ./gradlew bootRun; fi"]
