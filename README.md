Publish custom metrics to AWS Cloudwatch using Micrometer library

This demo showcases two simple metrics - a **Counter** and a **Gauge**
<li>Counter gets incremented everytime any REST API is invoked.
<li>Gauge monitors the mock Connection Pool, it submits the reading to Cloudwatch after configured period