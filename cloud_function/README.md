# Log ingestion demo using Cloud Functions in Java

This folder demonstrates a very basic Cloud Functions instance written in Java that ingests logs using Google [Logging client library][1] (v3.14).
It synchronously calls Cloud Logging API to ingest text and structured logs.
To run it you will need an access to a Google Cloud project with enabled Cloud Functions, Cloud Build and Artifact Registry APIs.
Also you will need permissions to deploy Cloud Functions instances.

## Step-by-step instructions

Follow the below steps to deploy and run the Cloud Functions instance and then to check the results:

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
   export FUNC_NAME="logging-test-function"
   ```

   Consider selecting a region closer to your geographic location if you are not in US.

1. Deploy the code to Cloud Functions:

   ```bash
   gcloud functions deploy $FUNC_NAME --gen2 \
    --source=./serverless_logging_demo/cloud_function \
    --runtime=java11 --trigger-http \
    --entry-point="com.google.example.helloworld.HelloWorldFunction" \
    --project=$PROJECT_ID --region=$REGION
   ```

   * If you are prompted to enable the Artifact Registry API, respond by pressing `y`.
   * You will be prompted to **allow unauthenticated invocations**: respond `y`.

   Wait for deployment to complete.

1. Run `curl` command to trigger the function execution and log ingestion:

   ```bash
   FUNC_URL=$(gcloud functions describe $FUNC_NAME --project=$PROJECT_ID --format="value(serviceConfig.uri)")
   curl $FUNC_URL 
   ```

1. Open the following URL to see logs in your project:

   ```terminal
   https://console.cloud.google.com/logs/query?project=PROJECT_ID
   ```

   Mind to use the same `PROJECT_ID` as you used in the setup step.

1. In the [Query pane][2] query for the service logs. Type:

   ```terminal
   log_id("helloworld-function")
   ```

   Press "Run Query" button (at top right above the pane).
   You will see entries like below with the `resource` field
   populated with Cloud Functions metadata: ![Cloud Functions log entry][logs]

## Clean up

To remove after-run charges you might want to delete the following resources:

* Cloud Functions instance
* Artifact registry repository
* Service logs

[1]: https://cloud.google.com/logging/docs/reference/libraries
[2]: https://cloud.google.com/logging/docs/view/logs-explorer-interface#query-builder
[logs]: ../images/cloud_functions_log.png
