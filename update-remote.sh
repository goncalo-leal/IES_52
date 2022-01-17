#!/bin/bash

jar_local=./api/ShopAholytics/target/ShopAholytics-0.0.1-SNAPSHOT.jar
frontend_local=./FrontEnd

jar_remote=
frontend_remote=

remote=deti-engsoft-15

echo Username:
read user


printf "[+] Copy jar..."
scp ${jar_local} ${user}@${remote}:${jar_remote}
printf "[+] Copy frontend..."
scp ${frontend_local}*.html ${frontend_local}/js ${user}@${remote}:${frontend_remote}
printf "[+] run.sh" 
ssh ${user}@${remote} "cd ies_52; sudo ./run.sh"

