## FTP file base

Client sends the file name to the server
- The server, if that file exists, sends the contents of the file to the client line by line. Otherwise it sends an error message to the client
- The client shows on the screen the file lines received from the server and create a file or, in case of an error, shows the user the error message

server/file.txt is the file to transfer from server/ to client/

## Folder Structure

The workspace contains two oflders, where:

- `client`: containt the client files
- `server`: containt the server files

