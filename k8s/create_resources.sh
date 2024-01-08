#!/bin/sh

create_resource() {
    local file="$1"
    local resource="$2"

    if [ -f "$file" ]; then
        echo "Criando $resource..."
        kubectl create -f "$file"
    else
        echo "Arquivo $file não encontrado no diretório atual para $resource."
        echo "Por favor, verifique e certifique-se de que o arquivo existe para prosseguir."
    fi
}

create_resource "./namespace.yml" "o namespace"

create_resource "./ingress.yml" "o ingress"

create_resource "./endpoint.yml" "o ConfigMap dos endpoints"

create_resource "./postgres-secret.yml" "os secrets do postgres"

create_resource "./postgres-deployment.yml" "o deployment do postgres"

create_resource "./postgres-service.yml" "o service do postgres"

create_resource "./user-deployment.yml" "o deployment do User-API"

create_resource "./user-service.yml" "o service do User-API"

create_resource "./product-deployment.yml" "o deployment do Product-API"

create_resource "./product-service.yml" "o service do Product-API"

create_resource "./shopping-deployment.yml" "o deployment do Shopping-API"

create_resource "./shopping-service.yml" "o service do Shopping-API"
