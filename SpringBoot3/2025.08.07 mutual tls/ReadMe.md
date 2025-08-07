```bash
$ scripts/create-certs.sh 2>&1 | tee scripts/create-certs.01.txt


./gradlew :books-server:bootRun
./gradlew :books-server:bootRun --args="--spring.profiles.active=ssl"

./gradlew :books-client:bootRun --args='--spring.profiles.active=ssl'
```