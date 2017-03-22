FROM java:latest

RUN apt-get -y update
RUN apt-get -y install maven

ADD build.sh /app/build.sh
ADD run.sh /app/run.sh
ADD deck/pom.xml /app/deck/pom.xml
ADD deck/src /app/deck/src
WORKDIR /app

RUN sh build.sh
