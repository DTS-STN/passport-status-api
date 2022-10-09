# Kustomize manifests for deploying to local cluster

This folder contains Kubernetes Kustomize manifests that can be used to deploy
the Passport Status API to a local Kuberntes cluster.

The examples provided here assume you have a registry running inside your
cluster and exposed via ingress on `https://registry.localtest.me/`.

## Requirements

- A local Kubernetes cluster (ie: k3d, k3s, minikube, kind, etc).
- A recent version of `kubectl` and a `kubeconfig` file that can connect to your cluster.
- A container registry to publish and fetch the application's container image to/from.

## Building and pushing a container image

You can build a container image using the following commands:

``` sh
mvn clean spring-boot:build-image --define image.registry=registry.localtest.me
docker push registry.localtest.me/passport-status-api:latest
```

## Deploying the application using `kubectl`

You can deploy the application using the following commands:

``` sh
kubectl create namespace passport-status
kubectl apply --kubeconfig {path-to-your-kubeconfig} --kustomize kustomize/environments/local/ --namespace passport-status
```

This will deploy an pod and ingress that listens for traffic on `https://passport-status-api.localtest.me/`.

## Appendix

## Deploying a registry

See <https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/docs/examples/docker-registry/deployment.yaml> for
information about how to deploy a registry inside of kubernetes using an nginx ingress.
