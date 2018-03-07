# This repo contains the tests for verifying the performance the Divorce Reform project.

## Getting Started

### Prerequisites

Dependencies
1. Docker
2. Gatling 

Environment Variables to be defined:
* E2E_FRONTEND_URL - The base url of Divorce Frontend Application
* IDAM_URL - The base url of IDAM server


### Running Gatling tests in Gatling container

1. Start docker daemon 
2. Run below to build your local docker container from where dockerfile exists.  If 'reformgatling' image exists in your local you can skip this step

    ```
    > docker build -t reformgatling:local  .

    ```


3. Run below to run gatling scripts against docker images
    ```
   > docker run --net=host --rm -it  -e E2E_FRONTEND_URL -v `pwd`/src/test/resources:/opt/gatling/conf -v `pwd`/src/test/scala/simulations:/opt/gatling/user-files/simulations -v `pwd`/results:/opt/gatling/results -v `pwd`/data:/opt/gatling/data reformgatling:local -s simulations.Divorce

   or

   > Execute ./runloadtest.sh

   ```
    
4. Reports folder will be created once tests successfully ran


### Running Gatling tests using maven without container

1. Pull latest code from this repo.

2. Execute below command to run tests

    ```
      > mvn gatling:execute

    ```

### Debugging:
To read a value from the gatling virtual session use something like:
```
.exec(session => {
        println(">>>>>>>>>>>>>>>>>>>> " + session("state").as[String])
        session
      })
```
