This mobile app is one of the clients for my Technician Career which featured a 700 pages of documentation(document is in private, pm me if you want to see it).
Refere here for the [web-app](https://github.com/DeltaWasHere/web-app), [desktop-app](https://github.com/DeltaWasHere/desktop-app), [api](https://github.com/DeltaWasHere/api) and [discord-bot](https://github.com/DeltaWasHere/discord-bot)

This mobile app serves as the mobile view of the web app, but it's coded entirely in Java using the Android SDK to run natively.

## File uploads
I initially thought file upload would be needed, since I had been developing mobile apps for nearly two years at that point (mainly for school), but I ended up going down a rabbit hole regarding how this posed a significant vulnerability for the Android system. As a result, I changed the implementation from simply using a file picker that returned the file path to using a cursor to loop through the file path and retrieve the file securely. 

## Web view manipulation
Since authentication involves redirection to Steam or Xbox OAuth, I manipulated the WebView to handle URL redirections made by the page and extract the authentication data into the app, resulting in a much smoother flow.
