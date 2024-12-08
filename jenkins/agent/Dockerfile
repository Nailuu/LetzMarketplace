FROM jenkins/agent:bookworm-jdk21
LABEL description="Docker image for LetzMarketplaceAPI CI/CD Jenkins agents"
USER root
RUN apt-get update -y && apt-get upgrade -y
RUN apt-get install maven -y
RUN apt-get install wget -y
RUN wget -qO- https://download.red-gate.com/maven/release/com/redgate/flyway/flyway-commandline/11.0.1/flyway-commandline-11.0.1-linux-x64.tar.gz | tar -xvz && ln -s `pwd`/flyway-11.0.1/flyway /usr/local/bin
RUN chown -R jenkins:jenkins `pwd`/flyway-11.0.1 /usr/local/bin/flyway
RUN mkdir -p -m 755 /etc/apt/keyrings
RUN wget -qO- https://cli.github.com/packages/githubcli-archive-keyring.gpg | tee /etc/apt/keyrings/githubcli-archive-keyring.gpg > /dev/null
RUN chmod go+r /etc/apt/keyrings/githubcli-archive-keyring.gpg
RUN echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/githubcli-archive-keyring.gpg] https://cli.github.com/packages stable main" | tee /etc/apt/sources.list.d/github-cli.list > /dev/null
RUN apt-get update
RUN apt-get install -y gh
USER jenkins