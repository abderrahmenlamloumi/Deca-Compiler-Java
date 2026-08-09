FROM debian:latest

ARG UID
ARG GID


RUN apt update -y
RUN apt install -y python3 python3-flask git python3-pip
RUN pip3 install flask-cors --break-system-packages
RUN apt install -y default-jre default-jdk maven
RUN apt install -y coreutils

RUN groupadd -g ${GID} dockeruser && \
    useradd -m -u ${UID} -g dockeruser -s /bin/bash dockeruser

USER dockeruser

WORKDIR /opt/gl

ENTRYPOINT ["flask", "run", "--host=0.0.0.0"]

