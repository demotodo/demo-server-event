# EventSource

### References
* <https://developer.mozilla.org/zh-CN/docs/Server-sent_events/Using_server-sent_events>
* <https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events/Using_server-sent_events>
* <https://developer.mozilla.org/en-US/docs/Web/API/EventSource>
* <https://developer.mozilla.org/zh-CN/docs/Server-sent_events>

### Comments
* Used to send event messages from server side to client side.
* Client will reconnect the server automatically.


### TODO
* How to use `id` and `retry` properties of `text/event-stream` format ?
* reconnect mechanism ? retry count / timeout ?
* concurrent performance


# Async Servlet

* add the `asyncSupported` attribute, e.g. `@WebServlet(name = "asyncServlet", urlPatterns = "/asyncServlet", asyncSupported = true)`
* start async context, `AsyncContext asyncContext = req.startAsync()`
* get the associated response object, `PrintWriter out = asyncContext.getResponse().getWriter()`

### Comments
* using `asyncContext.getResponse().getWriter().println()` or `asyncContext.getResponse().getOutputStream()` can't detect the client disconnect well, so may lost some events after reconnect
