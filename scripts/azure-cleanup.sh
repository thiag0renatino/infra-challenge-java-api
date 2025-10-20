#!/usr/bin/env bash
set -euo pipefail
RG="rg-challenge-mottu"
echo ">> [CLEANUP] Deletando Resource Group $RG ..."
az group delete -n "$RG" --yes --no-wait
