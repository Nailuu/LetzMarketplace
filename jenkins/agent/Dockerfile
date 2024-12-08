FROM jenkins/agent:bookworm-jdk21

USER root

RUN mkdir -p -m 755 /etc/apt/keyrings

# Install Dependencies
RUN apt-get update && apt-get upgrade
RUN apt-get install -y \
    maven \
    wget \
    ca-certificates \
    curl

# Install Docker
RUN curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc
RUN chmod a+r /etc/apt/keyrings/docker.asc
RUN echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/debian $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
RUN apt-get update && apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
RUN groupadd docker
RUN usermod -aG docker jenkins

# Install Flyway CLI
RUN wget -qO- https://download.red-gate.com/maven/release/com/redgate/flyway/flyway-commandline/11.0.1/flyway-commandline-11.0.1-linux-x64.tar.gz | tar -xvz && ln -s `pwd`/flyway-11.0.1/flyway /usr/local/bin
RUN chown -R jenkins:jenkins `pwd`/flyway-11.0.1 /usr/local/bin/flyway

# Install GitHub CLI
RUN wget -qO- https://cli.github.com/packages/githubcli-archive-keyring.gpg | tee /etc/apt/keyrings/githubcli-archive-keyring.gpg > /dev/null
RUN chmod go+r /etc/apt/keyrings/githubcli-archive-keyring.gpg
RUN echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/githubcli-archive-keyring.gpg] https://cli.github.com/packages stable main" | tee /etc/apt/sources.list.d/github-cli.list > /dev/null
RUN apt-get update && apt-get install -y gh

USER jenkins