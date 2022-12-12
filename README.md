# BITTORRENT

# Build
`gradle build`

# Usage

`./client -<debug|info|all> <torrent> <destination>`

torrents and files are in `dl`, example use :

`./client -all dl/torrents/iceberg.jpg.torrent dl/files`

log verbosity level : debug > info > error (default)
[etc.](https://logging.apache.org/log4j/2.x/log4j-api/apidocs/org/apache/logging/log4j/Level.html)

To enable shell completion : `complete -A file ./client`

## Commands
```sh
./opentracker.debug -i 127.0.0.1 -p 6969
bittorrent-tracker -p 6969 --http -4
aria2c -T dl/torrents/iceberg.jpg.torrent -d dl/target
ps -aux | grep idea
killall java
```
