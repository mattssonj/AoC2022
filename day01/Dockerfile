FROM azul/zulu-openjdk-alpine:17 AS BUILD
COPY Solution.kt code/Solution.kt
ADD https://github.com/JetBrains/kotlin/releases/download/v1.7.21/kotlin-compiler-1.7.21.zip .
RUN apk add -U unzip && rm -rf /var/cache/apk/*
RUN apk add --no-cache bash
RUN unzip kotlin-compiler-1.7.21.zip && rm -rf kotlin-compiler-1.7.21.zip
RUN kotlinc/bin/kotlinc code -include-runtime -d aoc.jar

FROM azul/zulu-openjdk-alpine:17
COPY --from=BUILD aoc.jar /app.jar
COPY input.txt ./day05/input.txt
CMD java -jar /app.jar