# BITTORRENT

gradle build

gradle run

## Tracker

./opentracker.debug -i 127.0.0.1 -p 6969
./opentracker.debug -i <adresse publique|interface loopback> -p 6969

## Testing

To test the client further than unit tests, you can locally run a tracker through docker

Pull the docker image using:
`docker pull lednerb/opentracker-docker`

You can then run it with:
`docker run -d --name opentracker -p 6969:6969/udp -p 6969:6969 lednerb/opentracker-docker`

To verify that the tracker is runing, you can look at the stats at:
`http://localhost:6969/stats`
