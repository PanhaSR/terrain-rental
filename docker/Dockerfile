# JDK 23+ as required
FROM eclipse-temurin:21-jdk

ARG REPO_URL="https://github.com/CHANGE_ME/terrain-rental.git"
ARG BRANCH="main"

RUN apt-get update && apt-get install -y --no-install-recommends \
        nginx openssh-server git maven php-cli curl ca-certificates \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app
RUN git clone --branch ${BRANCH} ${REPO_URL} . \
    && mvn -q -DskipTests clean package

COPY nginx.conf /etc/nginx/sites-available/default

RUN mkdir -p /var/run/sshd \
    && echo 'root:Hello@123' | chpasswd \
    && sed -i 's/#\?PermitRootLogin.*/PermitRootLogin yes/' /etc/ssh/sshd_config \
    && sed -i 's/#\?PasswordAuthentication.*/PasswordAuthentication yes/' /etc/ssh/sshd_config

COPY start.sh /start.sh
RUN chmod +x /start.sh

EXPOSE 22 80 8080
CMD ["/start.sh"]
