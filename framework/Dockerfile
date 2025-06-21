FROM selenium/standalone-chrome:4.11.0

USER root

# Install Maven and Git
RUN apt-get update && \
    apt-get install -y maven git && \
    rm -rf /var/lib/apt/lists/*

# Set workdir and copy project
WORKDIR /opt/automation
COPY framework/ /opt/automation/framework

# Set workdir to framework directory for Maven
WORKDIR /opt/automation/framework

# Set environment variables for Maven
ENV MAVEN_OPTS="-Xmx1024m"

# Default command: run the CDP test
CMD ["mvn", "test", "-Dtest=CDPNetworkInterceptTest"]