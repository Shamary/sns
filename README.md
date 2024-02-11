# Overview

Web service to handle notification messages in varying forms (sms, email and push)

# Stack

- Java SpringBoot 3
- Gradle 8.5
- AWS SNS, SES, Lambda, S3
- Docker

# Requirements

- [AWS Account](https://portal.aws.amazon.com/billing/signup#/start/email)
- [AWS CLI](https://aws.amazon.com/cli/)
- [AWS SAM CLI](https://docs.aws.amazon.com/serverless-application-model/latest/developerguide/install-sam-cli.html)

# Serverless

The application was designed to be deployed in a serverless manner using [AWS Lambda](https://aws.amazon.com/lambda/).
The ```template.yml``` is located at the root of the project and contains the necessary definitions 
for serverless deployment to AWS.

### serverless deployment

- In a terminal run ```sam build```
- ```sam deploy --guided --capabilities CAPABILITY_NAMED_IAM```

### local deployment
- In a terminal run ```sam build```
- ```sam local start-api```

# Docker

- Create a ```.env``` file at the project root (use the provided ```.env.example``` as guidance).
- A docker compose file is provided. Run the application using ```docker compose up -d```
- By default, the application is accessible on port ```12013```
