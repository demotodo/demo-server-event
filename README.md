# EventSource

## References
* <https://developer.mozilla.org/zh-CN/docs/Server-sent_events/Using_server-sent_events>
* <https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events/Using_server-sent_events>
* <https://developer.mozilla.org/en-US/docs/Web/API/EventSource>
* <https://developer.mozilla.org/zh-CN/docs/Server-sent_events>

## Comments
* Used to send event messages from server side to client side

## TODO
* How to use `id` and `retry` properties of `text/event-stream` format ?
* Will resend or receive previous event message after reconnect ?
* reconnect mechanism ? retry count / timeout ?
* concurrent performance
* how to distinguish by session ?


# Async Servlet

* add the `asyncSupported` attribute, e.g. `@WebServlet(name = "asyncServlet", urlPatterns = "/asyncServlet", asyncSupported = true)`
* start async context, `AsyncContext asyncContext = req.startAsync()`
* get the associated response object, `PrintWriter out = asyncContext.getResponse().getWriter()`
