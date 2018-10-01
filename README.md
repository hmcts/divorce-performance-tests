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

###  License
```The MIT License (MIT)

Copyright (c) 2018 HMCTS (HM Courts & Tribunals Service)

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
```
