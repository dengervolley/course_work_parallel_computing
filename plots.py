import csv
import matplotlib.pyplot as plt
measurements = {}
with(open('coursework/benchmarks/run.csv', newline='') as benchmarks):
    reader = csv.reader(benchmarks, delimiter=',')
    next(reader)
    for row in reader:
        if row[1] in measurements:
            measurements[row[1]]["threads"].append(row[0])
            measurements[row[1]]["time"].append(row[2])
        else:
            measurements[row[1]] = dict()
            measurements[row[1]]["threads"] = [row[0]]
            measurements[row[1]]["time"] = [row[2]]

for key in measurements:
    threads = [int(x) for x in measurements[key]["threads"]]
    timings = [int(y) for y in measurements[key]["time"]]
    plt.plot(threads, timings, label=f"NFiles: {key}")

plt.xlabel('Threads count')
plt.ylabel('Execution time')
plt.legend(loc='best')
plt.show()