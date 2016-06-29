<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript">
        var lastEventTimestamp = 0;
        var evtSource;
        var retry = 0;

        function appendEventMsg(msg) {
            var newElem = document.createElement("li");
            newElem.innerHTML = msg;
            var eventList = document.getElementById("eventList");
            eventList.appendChild(newElem);
        }

        function onmessage(e) {
            retry = 0;
            var obj = JSON.parse(e.data);
            lastEventTimestamp = obj.time;
            appendEventMsg("message: " + e.data);
        }

        function onclose(e) {
            evtSource.close();
            appendEventMsg("closed");
        }

        function onerror(e) {
            appendEventMsg("errors happened, retry #" + retry + " ...");
            evtSource.close();

            retry++;
            if (retry <= 10) {
                setTimeout(start, 1000);
            } else {
                appendEventMsg("won't retry again")
            }
        }

        function start() {
            appendEventMsg("start a new EventSource");
            evtSource = new EventSource("messageServlet?t=" + lastEventTimestamp);
            evtSource.onerror = onerror;
            evtSource.addEventListener("msg", onmessage);
            evtSource.addEventListener("close", onclose);
        }

        window.onload = start;

    </script>
</head>
<body>
<h2>Hello text/event-stream!</h2>
<ul id="eventList">
</ul>
</body>
</html>
