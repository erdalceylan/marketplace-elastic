# Build aşaması
FROM gradle:8.7-jdk21-jammy AS build

WORKDIR /app

# Gradle Wrapper ve Build dosyalarını kopyala
COPY gradlew .
COPY gradle gradle/
COPY build.gradle .
COPY settings.gradle .

# Bağımlılıkları önbelleğe almak için boş bir derleme yap
# Bu, sadece bağımlılıkların indirilmesini sağlar, daha sonraki katmanlarda hız kazandırır.
RUN ./gradlew dependencies

# Projenin kaynak kodunu kopyala
COPY src src/

# Uygulamayı derle ve JAR dosyasını oluştur
RUN ./gradlew build -x test

# Runtime aşaması
FROM eclipse-temurin:21-jre-jammy

# Sadece JAR dosyasını kopyala
COPY --from=build /app/build/libs/*.jar app.jar

# Uygulamayı çalıştır
ENTRYPOINT ["java", "-jar", "app.jar"]