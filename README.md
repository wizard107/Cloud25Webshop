## Setup

### 1. Install Google Cloud CLI

#### For Windows:
1. Download the Google Cloud SDK installer.
2. Run the installer and follow the prompts.
3. Restart your terminal or command prompt after installation.

#### For MacOS:
`brew install --cask google-cloud-sdk`


### 2. Configure Google Cloud CLI
#### Configure Google Cloud CLI

`gcloud auth login`

#### Set your project ID:

`gcloud config set project loyal-polymer-447814-a4`

### 3. Clone the Repository
`git clone https://github.com/wizard107/Cloud25Webshop`


# Building and Deploying

## Using Google Cloud Build

#### Build and push the image to Google Container Registry:
`gcloud builds submit --tag gcr.io/loyal-polymer-447814-a4/cloud`

#### Deploy the image to Cloud Run:
`gcloud run deploy cloud --image gcr.io/loyal-polymer-447814-a4/cloud:latest --platform managed --region europe-west3`

## Local Build and Deploy (alternative)

#### Build the Docker image locally:
`docker build -t gcr.io/loyal-polymer-447814-a4/cloud .
`
#### Push the image to Google Container Registry:
`docker push gcr.io/loyal-polymer-447814-a4/cloud
`
