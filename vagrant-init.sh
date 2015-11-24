#!/usr/bin/env bash

export DEBIAN_FRONTEND=noninteractive

cat > /etc/default/locale <<EOF
LANG="en_US.UTF-8"
LC_ALL="en_US.UTF-8"
EOF

cat > /etc/apt/sources.list <<EOF
deb mirror://mirrors.ubuntu.com/mirrors.txt trusty main restricted universe multiverse
deb mirror://mirrors.ubuntu.com/mirrors.txt trusty-updates main restricted universe multiverse
deb mirror://mirrors.ubuntu.com/mirrors.txt trusty-backports main restricted universe multiverse
deb mirror://mirrors.ubuntu.com/mirrors.txt trusty-security main restricted universe multiverse
EOF

apt-get update
add-apt-repository -y ppa:webupd8team/java
apt-get update
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections
apt-get -y install oracle-java8-installer oracle-java8-set-default authbind

wget https://bintray.com/artifact/download/vertx/downloads/vert.x-3.1.0-full.tar.gz -O /tmp/vertx.tar.gz
tar -vxzf /tmp/vertx.tar.gz -C /opt/
rm -v /tmp/vertx.tar.gz
ln -s /opt/vert.x-3.1.0/bin/vertx /usr/local/bin/vertx

cat > /etc/init/vertx.conf <<EOF
start on runlevel [2345]
stop on runlevel [016]

env JAVA_OPTS="-Djava.net.preferIPv4Stack=true"
exec /usr/local/bin/vertx bare
EOF

start vertx
