# BITTORRENT

# Build
`gradle build`

# Usage
`gradle run`

`./client -<debug|info> <torrent> <destination>`

torrents and files are in `dl`, example use :

`./client -info dl/torrents/iceberg.jpg.torrent dl/files`

log verbosity level : debug > info > error (default)
[etc.](https://logging.apache.org/log4j/2.x/log4j-api/apidocs/index.html)

To enable shell completion : `complete -A file ./client`

## Commands
```
./opentracker.debug -i 127.0.0.1 -p 6969
ps -aux | grep idea
killall java
```
