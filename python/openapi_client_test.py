import timeit

import requests

_times = 7
_number = 100

def test():
    requests.post("http://localhost:8080/v1/greetings", json={"name": "Bryan"})


if __name__ == "__main__":
    total_seconds = min(timeit.Timer(stmt="test()", setup="from __main__ import test").repeat(_times, _number))
    milliseconds = total_seconds / _number * 1000.0
    print("Milliseconds per call: {:,.2f}".format(milliseconds))
