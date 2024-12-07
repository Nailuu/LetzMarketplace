FROM jenkins/agent:bookworm-jdk21
LABEL description="Docker image for LetzMarketplaceAPI CI/CD Jenkins agents"
USER root
RUN apt-get update -y && apt-get upgrade -y
RUN apt-get install maven -y
RUN apt-get install wget -y
RUN wget -qO- https://download.red-gate.com/maven/release/com/redgate/flyway/flyway-commandline/11.0.1/flyway-commandline-11.0.1-linux-x64.tar.gz | tar -xvz && ln -s `pwd`/flyway-11.0.1/flyway /usr/local/bin
USER jenkins