#!/bin/bash

echo "Buildando imagem Docker..."
docker build -t challenge-api .

echo "Executando container da API..."
docker run -d -p 8080:8080 --name challenge-container challenge-api