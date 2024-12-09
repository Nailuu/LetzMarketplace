# Instructions

For Docker to work inside Jenkins agent, follow these steps.

1. Inside Jenkins Web Interface, go to `Docker Cloud -> Container settings -> Extra Groups`
2. Run the command `getent group docker` on the host machine
3. Set the `Extra Groups` value to the group GUID of the step 2 (e.g. `docker:x:124:ubuntu`, GUID = 124)
4. Now set the `Mounts` value to `type=bind,source=/var/run/docker.sock,destination=/var/run/docker.sock`