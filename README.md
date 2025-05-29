This API is part of the backend for my Technician Career which featured a 700 pages of documentation(document is in private, pm me if you want to see it).
Refere here for the [web-app](https://github.com/DeltaWasHere/web-app), [desktop-app](https://github.com/DeltaWasHere/desktop-app), [api](https://github.com/DeltaWasHere/api) and [discord-bot](https://github.com/DeltaWasHere/discord-bot)

This mobile app serves as the mobile view of the web app but its code entirely on Java using the android SDK to run natively.

## File uploads
File upload was something that I tought was gonna be used since I  had been developing mobile apps for almost 2 years at this point(school mainly) but ended up in the rabbit hole of how this represented a big vulnerability for the android system and was changed from only being the file picker that returned the file path to having to use a cursor to loop trough the file path and get the file. 

## Web view manipulation
Sine the authentication is a redirection into the Steam or Xbox OAuth I manipulated the Web view to handle the redirections of the url done by the page and extracting the authentication data from the webview into the application, permitting a way way better flow.
