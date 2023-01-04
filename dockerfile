FROM tomcat:8.5.69-jdk8-openjdk
COPY *.war /usr/local/tomcat/webapps
WORKDIR .
