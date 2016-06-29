<!DOCTYPE html>
<html>
<head>
    <script type="text/javascript">
        var evtSource = new EventSource("sourceServlet");

        function appendEventList(elem) {
            var eventList = document.getElementById("eventList");
            eventList.appendChild(elem);
        }

        evtSource.onmessage = function (e) {
            var newElem = document.createElement("li");
            newElem.innerHTML = "message: " + e.data;
            appendEventList(newElem);
        };

        evtSource.addEventListener("ping", function (e) {
            var newElem = document.createElement("li");
            var obj = JSON.parse(e.data);
            newElem.innerHTML = "ping at: " + obj.time;
            appendEventList(newElem);
        }, false);

    </script>
</head>
<body>
<h2>Hello text/event-stream!</h2>
<ul id="eventList">
</ul>
</body>
</html>
