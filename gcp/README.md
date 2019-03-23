# GCP Configuration

Some docs/helpers/tools to build and deploy the simulator on GCP.

## Cloud Build

[cloudbuild](cloudbuild) contains an example to build the dependencies, mocks
and Docker image using Cloud Build.

    cd cloudbuild
    gcloud builds submit --config=cloudbuild.yaml  

## Kubernetes

[k8s](k8s) contains an example of deployment using 3 distinct simulators. They are
available at `/sim1`, `/sim2`, `/sim3`.

**Requirements:**

* **VPC-native** Kubernetes cluster
* A domain name
* SSL certificate for the domain
* Service account for the application
* Reserved IP (used to point the domain)

### VPC-native

...

### SSL Cert

For instance with [Let’s Encrypt](https://letsencrypt.org/):

* Create the certificate

      certbot certonly \
          --preferred-challenges dns \
          --manual \
          -d <YOUR_DNS_DOMAIN>

* Import into GCP

      gcloud compute ssl-certificates create repbus-simulator \
          --certificate=<PATH_TO>/cert.pem
          --private-key=<PATH_TO>/privkey.pem

### Service Account

* Create a new key for the SA used by the application
* Import the key into a Kubernetes secret:
 
      kubectl create secret generic app-key-$(date +%s) \
          --from-file=app-key.json \
          --dry-run -o yaml | kubectl apply -f

### Deployment

* For each `k8s/mock-simX.tpl.yaml` files:
  * update `<YOUR_K8S_SECRET>` with previously generated secret
  * update `<YOUR_IMAGE>` with the GCR image used 
  * update the `SPRING_APPLICATION_JSON` value to add/update more Spring/Application related parameters
* Edit `k8s/ingress.tpl.yaml`:
  * Update `<YOUR_DOMAIN>` with the appropriate DNS
* Apply the full config:

      kubectl apply -f k8s/
* Wait...
* Simulators will be available at `<YOUR_DOMAIN>/sim1`,...

