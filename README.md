# ClientServeurHTTP
A simple client-server based on HTTP 1.1

*Features:*
* Minimalist HTTP server -> only handles GET requests
* Minimalist HTTP navigateur -> only handles GET requests

*Usage:*

1. Run Server File
2. Run Navigateur File :
 * Possible hosts : localhost, localhost:3000
 * Possible resource paths : index.txt, README.html, chat.jpg, resources/panda.jpg

*TODO:*
- [x] Minimalist server :
 - [x] Handles concurrent requests
 - [x] Test on existent browsers : Firefox, Internet Explorer
 - [ ] Use Scanner to ask for server port
- [ ] Minimalist client :
 - [ ] Read response body correctly : fix content length of 1 when response doesn't have content
 - [x] Save response body on a file in local directory (ex: Browser) ~~(then display the file)~~
 - [ ] Create "Browser" directory if doesn't exist
 - [ ] ~~Improve interface (Swing -> JavaFX)~~ => Remove interface
 - [ ] Client error status
 - [ ] Clean code
- [ ] Supprimer franglais de partout
- [x] Move all resources into resources folder
