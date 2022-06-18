#!/bin/bash

kubectl apply -f backend-deployment.yml
kubectl apply -f backend-svc.yml
kubectl apply -f db-secret.yaml
kubectl apply -f mysql-pvc.yml
kubectl apply -f mysql-deployment.yml
kubectl apply -f mysql-svc.yml
