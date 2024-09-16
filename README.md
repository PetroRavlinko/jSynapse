# jSynapse
jSynapse is the Java implementation of matrix.org homeserver.
Full spec: http://matrix.org/docs/spec/


Steps to get it running:
- install MongoDB and intialize replica set
- git clone git@github.com:swarmcom/jSynapse.git
- mvn clean install
- java -jar target/jSynapse-1.0-SNAPSHOT.jar
(or import Maven project in IDE of choice and run JSynapseServer class)


Install and start from Docker image:
- sudo docker pull dizzy/mongodb
- sudo docker run --name mongo_swarm -p 27017:27017 -d dizzy/mongodb:latest --noprealloc --smallfiles --replSet=jsynapse
then intiate replicaset as: mongo --eval "rs.initiate()"

- sudo docker pull dizzy/jsynapse
- sudo docker run -d --name synapse1 -p 5555:5555 --link mongo_swarm:mongo_swarm dizzy/jsynapse:latest --spring.data.mongodb.uri=mongodb://mongo_swarm:27017/matrix

You can run a farm of jSynapse servers, pointing all to the same MongoDB instance:
- sudo docker run -d --name synapse2 -p 5556:5555 --link mongo_swarm:mongo_swarm dizzy/jsynapse:latest --spring.data.mongodb.uri=mongodb://mongo_swarm:27017/matrix
- sudo docker run -d --name synapse3 -p 5557:5555 --link mongo_swarm:mongo_swarm dizzy/jsynapse:latest --spring.data.mongodb.uri=mongodb://mongo_swarm:27017/matrix

By deafult jSynapse homeserver accepts requests on port 5555. You can change this by passing --port.server={PORT}
To start with a different domain (default swarmcom.org) use --domain=mydomain.org

To check server health point browser to http://localhost:5555/actuator/health

Examples to get/set user display name and avatar url

curl -X PUT -H "Content-Type: application/json" -d '{"displayname":"John Doe"}'  http://localhost:5555/_matrix/client/api/v1/profile/@mirceac:swarmcom.org/displayname?access_token=VEJbwzztYLhcIkKM

curl http://localhost:5555/_matrix/client/api/v1/profile/@mirceac:swarmcom.org/displayname?access_token=VEJbwzztYLhcIkKM
{"displayname":"John Doe"}



curl -X PUT -H "Content-Type: application/json" -d '{"avatar_url":"url"}'  http://localhost:5555/_matrix/client/api/v1/profile/@mirceac:swarmcom.org/avatar_url?access_token=VEJbwzztYLhcIkKM

curl http://localhost:5555/_matrix/client/api/v1/profile/@mirceac:swarmcom.org/avatar_url?access_token=VEJbwzztYLhcIkKM
{"avatar_url":"url"}


## Check

```shell
 mvn versions:display-plugin-updates
```

