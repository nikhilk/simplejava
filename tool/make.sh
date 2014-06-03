#!/bin/sh

mvn clean package

cat stub.sh app/bin/jist-app-0.1.jar > app/bin/jist

chmod +x app/bin/jist
ls -l app/bin/jist

