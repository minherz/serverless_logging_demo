# Log ingestion demo using Cloud Run service in Java

This folder demonstrates a very basic Spring Boot service that ingests logs using Google [Logging client library][1] (v3.14).
The library's write() API is used to ingest text and structured logs to Google Cloud Logging backend.
To run it you will need an access to a Google Cloud project with enabled Cloud Run, Cloud Build and Artifact Registry APIs.
Also you will need permissions to deploy Cloud Run services.

## Step-by-step instructions

Follow the below steps to deploy and run the service on Cloud Run and then to check the results:

1. Clone this repo:

   ```bash
   git clone https://github.com/minherz/serverless_logging_demo
   ```

1. Login to Google Cloud:

   ```bash
   gcloud auth login
   ```

   Follow instructions to authenticate.

1. Setup your project and deployment location:

   ```bash
   export PROJECT_ID="place your project id here"
   export REGION="us-central1"
   export SVC_NAME="logging-test-svc"
   ```

   Consider selecting a region closer to your geographic location if you are not in US.

1. Deploy the service to Cloud Run:

   ```bash
   gcloud run deploy $SVC_NAME \
    --source=./serverless_logging_demo/cloud_run \
    --project=$PROJECT_ID --region=$REGION
   ```

   * If you are prompted to enable the Artifact Registry API, respond by pressing `y`.
   * You will be prompted to **allow unauthenticated invocations**: respond `y`.

   Wait for deployment to complete.

1. Run `curl` command to trigger the handler execution and log ingestion:

   ```bash
   SERVICE_URL=$(gcloud run services describe $SVC_NAME --project=$PROJECT_ID --region=$REGION --format="value(status.url)")
   curl -H "traceparent: 00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01" $SERVICE_URL 
   ```

   If you want to test it with Google trace context run:

   ```bash
   SERVICE_URL=$(gcloud run services describe $SVC_NAME --project=$PROJECT_ID --region=$REGION --format="value(status.url)")
   curl -H "X-Cloud-Trace-Context: 105445aa7843bc8bf206b12000100000/1;o=1" $SERVICE_URL 
   ```

1. Open the following URL to see logs in your project:

   ```terminal
   https://console.cloud.google.com/logs/query?project=PROJECT_ID
   ```

   Mind to use the same `PROJECT_ID` as you used in the setup step.

1. In the [Query pane][2] query for the service logs. Type:

   ```terminal
   log_id("helloworld-service")
   ```

   Press "Run Query" button (at top right above the pane).

## Clean up

To remove after-run charges you might want to delete the following resources:

* Cloud Run service
* Artifact registry repository
* Service logs

[1]: https://cloud.google.com/logging/docs/reference/libraries
[2]: https://cloud.google.com/logging/docs/view/logs-explorer-interface#query-builder
