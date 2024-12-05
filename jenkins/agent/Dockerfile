FROM jenkins/agent:bookworm-jdk21
LABEL description="Docker image for LetzMarketplace CI/CD Jenkins agents"

RUN sudo apt-get update -y && sudo apt-get upgrade -y

# Install Flyway CLI
RUN wget -qO- https://download.red-gate.com/maven/release/com/redgate/flyway/flyway-commandline/11.0.1/flyway-commandline-11.0.1-linux-x64.tar.gz | tar -xvz && sudo ln -s `pwd`/flyway-11.0.1/flyway /usr/local/bin
