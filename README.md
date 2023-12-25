# Overview

Web serivce to handle notification messages in varying forms (sms, email and push)

# Stack

- Java SpringBoot
- AWS SNS, SES
- Docker

# Configuration

### Docker

- Create a ```.env``` file at the project root (use the provided ```.env.example``` as guidance).
- A docker compose file is provided. Run the application using ```docker compose up -d```
- By default the application is accessible on port ```12013```