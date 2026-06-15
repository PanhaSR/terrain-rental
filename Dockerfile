FROM eclipse-temurin:23-jdk-noble

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y --no-install-recommends \
    nginx \
    openssh-server \
    php \
    php-cli \
    php-mbstring \
    php-xml \
    php-curl \
    php-mysql \
    php-zip \
    php-gd \
    php-intl \
    php-bcmath \
    git \
    curl \
    maven \
    && apt-get clean && rm -rf /var/lib/apt/lists/*

RUN java -version

RUN mkdir -p /var/run/sshd \
    && echo 'root:Hello@123' | chpasswd \
    && sed -i 's/#PermitRootLogin prohibit-password/PermitRootLogin yes/' /etc/ssh/sshd_config \
    && sed -i 's/PasswordAuthentication no/PasswordAuthentication yes/' /etc/ssh/sshd_config \
    && sed -i 's/#PasswordAuthentication yes/PasswordAuthentication yes/' /etc/ssh/sshd_config

WORKDIR /app
RUN git clone https://github.com/PanhaSR/terrain-rental.git .

RUN mvn package -DskipTests -q

RUN rm -f /etc/nginx/sites-enabled/default
COPY nginx.conf /etc/nginx/conf.d/springboot.conf

COPY start.sh /start.sh
RUN chmod +x /start.sh

EXPOSE 22 80 8080

ENTRYPOINT ["/start.sh"]
